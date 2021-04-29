class IndexWebModel {

    #token = null
    #difficulty = {}
    #clazz = {}

    //information getter
    getToken(){
        return getCookie('token')
    }

    getQuestionList(page, offset, difficulty, clazz){
        //postAndWait queryQuestionList
        let ret = postAndWait('/queryQuestionList', {page:page,offset:offset,difficulty:difficulty,class:clazz})
        let list = []
        let problems = ret["problems"]
        for(let problem in problems){
            let pid = problems[problem]["problem"]["pid"]
            let pname =problems[problem]["problem"]["pname"]
            let pdiffi = this.#difficulty[problems[problem]["problem"]["pdifficulty"]]["name"]
            let prate = (Math.floor(parseInt(problems[problem]["problem"]["ppass"])/parseInt(problems[problem]["problem"]["psubmit"]) * 10000)/100).toString()+"%"
            let pstatus = problems[problem]["passed"] === 0?"未完成":"完成"
            list.push([pid, pname, pdiffi, prate, pstatus])
        }
        return list
    }

    getUIInfo(){
        return postAndWait("/getUIInfo",{})
    }

    //html operation
    initQuestions(){
        let questionTable = document.getElementById("question_table")
        let styles = ["text-align: center","text-align: left","text-align: center","text-align: center","text-align: center"]
        //pick ui info from server
        let UIInfo = this.getUIInfo()
        let diff_select = document.getElementById("diff_select")
        let class_select = document.getElementById("class_select")
        let status_select = document.getElementById("status_select")
        let diffs = UIInfo['difficulties']
        for(let i=0;i<diffs.length;i++){
            this.#difficulty[diffs[i]['value']] = diffs[i]
            diff_select.options.add(new Option(diffs[i]["name"],diffs[i]["value"],diffs[i]['value']===0))
        }
        let classes = UIInfo['classes']
        for(let i=0;i<classes.length;i++){
            this.#clazz[classes[i]["cid"]] = classes[i]
            class_select.options.add(new Option(classes[i]["cname"],classes[i]["cid"],classes[i]['cid']===0))
        }
        status_select.options.add(new Option("全部","-1",true))
        status_select.options.add(new Option("未完成","0",false))
        status_select.options.add(new Option("完成","1",false))
        //pick list from server
        let questionList = this.getQuestionList()
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
        //add question table listener
        let question_table = document.getElementById("question_table")
        question_table.addEventListener("click",function (event){
            let pid = event.target.parentElement.getAttribute("pid")
            window.location.href = "./problems-"+pid.toString()
        })
    }

    constructor() {
        this.#token = this.getToken()
        this.initQuestions()
    }
}

//instantiate model of index.ejs
new IndexWebModel()