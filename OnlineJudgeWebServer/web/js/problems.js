class ProblemsWebModel{

    #editor = null
    #lang2mode = {"java":"text/x-java", "c":"text/x-csrc", "c++":"text/x-c++src", "python2":"text/x-python", "python3":"text/x-python"}

    constructor() {
        this.initializeGUI()
    }

    initializeGUI(){
        let that = this
        //init size
        let width = document.body.clientWidth
        let height = window.innerHeight
        //get elements
        let page_header = document.getElementById("page_header")
        let gap_hr = document.getElementById("gap_hr")
        let question_container = document.getElementById("question_container")
        let question_detail = document.getElementById("question_detail")
        let comments_detail = document.getElementById("comments_detail")
        let code_frame = document.getElementById("code_frame")
        let code_container = document.getElementById("code_container")
        let main_panel = document.getElementById("main_panel")
        //get used height
        let style_ph = window.getComputedStyle(page_header)
        let style_gh = window.getComputedStyle(gap_hr)
        let style_cp = window.getComputedStyle(main_panel)
        let usedHeight = parseInt(style_gh.height) + parseInt(style_gh.marginTop) + parseInt(style_gh.marginBottom)
            + parseInt(style_ph.height) + parseInt(style_ph.marginTop) + parseInt(style_ph.marginBottom)
            + parseInt(style_cp.marginTop) + parseInt(style_cp.marginBottom)
        //resize panel
        main_panel.style.height = (height - usedHeight - 15).toString() + "px"
        //set tab
        document.getElementById("tab_panel_opt_question").addEventListener("click",function (){
            that.selectTab("question")
        })
        document.getElementById("tab_panel_opt_comments").addEventListener("click",function (){
            that.selectTab("comments")
        })
        this.selectTab("question")
        //load question
        /*
            get question from server
        */
        //
        let detail = "# abc\n* t1\n* t2\n\npara\n\n"
        for(let i=0;i<5;i++){
            detail+=detail
        }
        question_detail.innerHTML = markdown.toHTML(detail)
        //load code panel
        let language_selet = document.getElementById("language_select")
        let langs = Object.keys(this.#lang2mode)
        for(let i=0;i<langs.length;i++){
            language_selet.options.add(new Option(langs[i],i))
        }
        language_selet.onchange = function (ev){
            code_container.innerHTML = ""
            let lang = language_selet.options[language_selet.selectedIndex].text
            that.setLanguage(lang)
        }
        //load editor
        this.setLanguage("java")
        //register button listener
        let run_bnt = document.getElementById("run_bnt")
        let submit_bnt = document.getElementById("submit_bnt")
        run_bnt.addEventListener("click",function (){
            that.showAndWaitOutput()
            //run
            let result = "run result"
            that.showOutput(result)
        })
        submit_bnt.addEventListener("click",function (){
            that.showAndWaitOutput()
            //submit
            let result = "submit result"
            that.showOutput(result)
        })
    }

    //comment control
    insertComment(id, ){
        let coments_detail = document.getElementById("comments_detail")
        let comment = document.createElement("div")
        comment.id =
        coments_detail.appendChild(comment)
    }

    //editor control
    getCode(){
        console.log(this.#editor.getValue())
    }

    setLanguage(lang){
        let that = this
        let editorOpts= {
            mode: this.#lang2mode[lang],
            smartIndent: true,
            indentWithTabs: true,
            lineNumbers: true,
            matchBrackets: true,
            indentUnit: 4,
            autoCloseBrackets: true,
            hintOptions:{
                completeSingle: false
            }
        }
        this.#editor = CodeMirror(code_container, editorOpts)
        let style_ce = window.getComputedStyle(code_container)
        this.#editor.setSize(style_ce.width, style_ce.height)
        this.#editor.on('keyup', function (cm, event) {
            let key = event.keyCode
            if(key>=65 && key<=90){
                that.#editor.showHint()
            }
        })
    }

    //tab control
    selectTab(tabName){
        let tabs={
            question:{tab:document.getElementById("tab_panel_opt_question"),panel:document.getElementById("question_detail")},
            comments:{tab:document.getElementById("tab_panel_opt_comments"),panel:document.getElementById("comments_detail")}
        }
        let keys = Object.keys(tabs)
        for(let i=0;i<keys.length;i++){
            if(keys[i]==tabName){
                tabs[keys[i]]["tab"].style.background = "#ffffff"
                //show panel
                tabs[keys[i]]["panel"].style.display = "inline-block"
            }
            else{
                tabs[keys[i]]["tab"].style.background = "#f7f7f7"
                //hide panel
                tabs[keys[i]]["panel"].style.display = "none"
            }
        }
    }

    //output control
    #clickConfirmOfOutputDialog(el, ev){
        let output_dialog = document.getElementById("output_dialog")
        output_dialog.close()
    }

    showAndWaitOutput(){
        let output_dialog = document.getElementById("output_dialog")
        let output_msg = document.getElementById("output_msg")
        let output_bnt = document.getElementById("output_bnt")
        output_msg.value = "等待结果....."
        output_bnt.style.background = "#bbbbbb"
        output_bnt.innerText = ". . . . . ."
        output_bnt.removeEventListener("click", this.#clickConfirmOfOutputDialog)
        output_dialog.showModal()
    }

    showOutput(result){
        let output_dialog = document.getElementById("output_dialog")
        let output_msg = document.getElementById("output_msg")
        let output_bnt = document.getElementById("output_bnt")
        output_msg.value = result
        output_bnt.style.background = ""
        output_bnt.innerText = "确定"
        output_bnt.addEventListener("click", this.#clickConfirmOfOutputDialog)
    }
}

let model = new ProblemsWebModel()