class IndexWebModel {

    #token = null

    //information getter
    getToken(){
        return getCookie('token')
    }

    getQuestionList(){
        /*get question list from server*/
        return [
            [1, '最长上升子序列', '中等', "56.7%", '未完成'],
            [2, '最快的上学路径', '中等', "75.4%", '未完成']
        ]
    }

    getUIInfo(){

    }

    //html operation
    initQuestions(){
        let questionTable = document.getElementById("question_table")
        let styles = ["text-align: center","text-align: left","text-align: center","text-align: center","text-align: center"]
        //pick ui info from server
        let UIInfo = this.getUIInfo()
        //pick list from server
        let questionList = this.getQuestionList()
        //show list
        for(let i=0;i<questionList.length;i++){
            let row = questionTable.insertRow()
            if(i%2===0)
                row.style.background = "#fafafafa"
            for(let j=0;j<5;j++){
                let cell = row.insertCell()
                cell.style.cssText = styles[j]
                cell.innerText=questionList[i][j].toString()
            }
        }
    }

    constructor() {
        this.#token = this.getToken()
        this.initQuestions()
    }
}

//instantiate model of index.ejs
new IndexWebModel()