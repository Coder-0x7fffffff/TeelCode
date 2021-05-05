class IndexWebModel {

    #token = null
    #difficulty = {}
    #clazz = {}
    #page = 1
    #UIInfo = null

    //information getter
    getToken(){
        return getCookie('token')
    }

    getQuestionList(page, offset, difficulty, clazz, status){
        //postAndWait queryQuestionList
        let ret = postAndWait('/queryQuestionList', {page:page,offset:offset,difficulty:difficulty,class:clazz,status:status})
        let list = []
        let problems = ret["problems"]
        for(let problem in problems){
            let pid = problems[problem]["problem"]["pid"]
            let pname =problems[problem]["problem"]["pname"]
            let pdiffi = this.#difficulty[problems[problem]["problem"]["pdifficulty"]]["name"]
            let prate = (Math.floor(parseInt(problems[problem]["problem"]["ppass"])/parseInt(problems[problem]["problem"]["psubmit"]) * 10000)/100).toString()
            let pstatus = "未提交"
            switch(problems[problem]["passed"] === 0){
                 default:
                    pstatus = "未完成"
                    break
                case 1:
                    pstatus = "完成"
                    break
            }
            list.push([pid, pname, pdiffi, prate==="NaN"?"无提交":prate+"%", pstatus])
        }
        return list
    }

    moreQuestions(reset = false, default_search = true){
        let questionTable = document.getElementById("question_table")
        if(reset === true){
            while(questionTable.rows.length>1)questionTable.rows[questionTable.rows.length-1].remove()
            this.#page=1
        }
        let styles = ["text-align: center","text-align: left","text-align: center","text-align: center","text-align: center"]
        let diff = 0
        let clazz = 0
        let status = -1
        if(default_search === false){
            let diff_select = document.getElementById("diff_select")
            let class_select = document.getElementById("class_select")
            let status_select = document.getElementById("status_select")
            diff = diff_select.options[diff_select.selectedIndex].innerText
            clazz = class_select.options[class_select.selectedIndex].innerText
            status = status_select.options[status_select.selectedIndex].innerText
            let diffs = this.#UIInfo['difficulties']
            for(let i=0;i<diffs.length;i++){
                if(diffs[i]['name'] === diff)
                    diff = diffs[i]['value']
            }
            let classes = this.#UIInfo['classes']
            for(let i=0;i<classes.length;i++){
                if(classes[i]['cname'] === clazz)
                    clazz = classes[i]['cid']
            }
            switch(status){
                case "全部":
                    status = -1
                    break
                case "未完成":
                    status = 0
                    break
                case "完成":
                    status = 1
                    break
                default:
                    status = -1
                    break
            }
        }
        let questionList = this.getQuestionList(this.#page,10, diff, clazz,status)
        //show list
        for(let i=0;i<questionList.length;i++){
            let row = questionTable.insertRow()
            row.setAttribute("pid",questionList[i][0])
            if(i%2===0)
                row.style.background = "#fafafafa"
            for(let j=0;j<5;j++){
                let cell = row.insertCell()
                cell.style.cssText = styles[j]
                cell.innerText=questionList[i][j].toString()
            }
        }
        this.#page++
    }

    getUIInfo(){
        return postAndWait("/getUIInfo",{})
    }

    //html operation
    initUI(){
        let questionTable = document.getElementById("question_table")
        //pick ui info from server
        this.#UIInfo = this.getUIInfo()
        let diff_select = document.getElementById("diff_select")
        let class_select = document.getElementById("class_select")
        let status_select = document.getElementById("status_select")
        let diffs = this.#UIInfo['difficulties']
        for(let i=0;i<diffs.length;i++){
            this.#difficulty[diffs[i]['value']] = diffs[i]
            diff_select.options.add(new Option(diffs[i]["name"],diffs[i]["value"],diffs[i]['value']===0))
        }
        let classes = this.#UIInfo['classes']
        for(let i=0;i<classes.length;i++){
            this.#clazz[classes[i]["cid"]] = classes[i]
            class_select.options.add(new Option(classes[i]["cname"],classes[i]["cid"],classes[i]['cid']===0))
        }
        status_select.options.add(new Option("全部","-1",true))
        status_select.options.add(new Option("未完成","0",false))
        status_select.options.add(new Option("完成","1",false))
        //pick list from server
        this.moreQuestions()
        //add question table listener
        let question_table = document.getElementById("question_table")
        question_table.addEventListener("click",function (event){
            let pid = event.target.parentElement.getAttribute("pid")
            window.location.href = "./problems-"+pid.toString()
        })
        document.getElementById("table_more").addEventListener("click",function (event){
            model.moreQuestions()
        })
        //search listener
        document.getElementById("search_bnt").addEventListener("click", function (event){
            model.moreQuestions(true,false)
        })
    }

    constructor() {
        this.#token = this.getToken()
        this.initUI()
    }
}

//instantiate model of index.ejs
let model = new IndexWebModel()