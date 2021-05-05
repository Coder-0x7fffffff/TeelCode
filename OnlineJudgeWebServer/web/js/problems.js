class ProblemsWebModel{

    #editor = null
    #lang2mode = {"c":"text/x-csrc", "c++":"text/x-c++src","java":"text/x-java", "python2":"text/x-python", "python3":"text/x-python"}
    #lang = null
    #question_id = null
    replyContext = null

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
        let reply_confirm = document.getElementById("reply_confirm")
        let reply_cancel = document.getElementById("reply_cancel")
        let comments_panel_reply_bnt = document.getElementById("comments_panel_reply_bnt")
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
        let pathname = window.location.pathname
        this.#question_id = parseInt(pathname.split("-")[1])
        let ret = postAndWait("/queryQuestion",{problem_id:this.#question_id})
        if(ret['err']==null){
            let detail = ret['problem']['problem_detail']
            question_detail.innerHTML = markdown.toHTML(detail)
        }else{
            showError(ret['err'])
        }
        //load comments
        ret = postAndWait("/queryComments",{problem_id:this.#question_id,page:1,offset:10})
        if(ret['err']==null){
            let comments = ret['comments']
            for(let i=0;i<comments.length;i++){
                this.insertComment(comments[i])
            }
        }else{
            showError(ret['err'])
        }
        comments_detail.addEventListener("click", this.#clickOnCommentsPanel)//listener
        reply_confirm.addEventListener("click", this.#replyConfirm)
        reply_cancel.addEventListener("click", this.#replyCancel)
        comments_panel_reply_bnt.addEventListener("click", this.commentReply)
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
        let run_bnt = document.getElementById("run_bnt")//run
        run_bnt.addEventListener("click",function (){
            that.showRunDialog()
        })
        let submit_bnt = document.getElementById("submit_bnt")//submit
        submit_bnt.addEventListener("click",function (){
            that.showAndWaitOutput()
            /*submit*/
            let states = ["等待评测","正在评测","编译错误","答案正确","运行错误","格式错误","答案错误","运行超时","内存超限"]
            postNoWait("/submit",{time:timeToStr(new Date()),id:model.#question_id,platform:that.#lang,code:that.#editor.getValue()},function (ajax){
                if (ajax.readyState===4 && ajax.status===200){
                    let ret = JSON.parse(ajax.responseText)
                    if(ret['err']==null){
                        let output = ret['result']
                        let outputStr = ""
                        let count = output["count"]
                        for(let i=1;i<=count;i++){
                            let str = "----------测试"+i.toString()+"----------\n"
                            let state = output[i.toString()]["ResultCode"]
                            state = state<states.length?states[state]:state;
                            str +=
                                "状态："+state+"\n" +
                                "内存："+output[i.toString()]["MemoryUsed"]+"KB\n" +
                                "耗时："+output[i.toString()]["TimeUsed"]+"MS\n" +
                                ((state !== "答案正确")?("输出：\n"+output[i.toString()]["ResultInfo"]):"")+
                                "\n"
                            outputStr += str
                        }
                        that.showOutput(outputStr)
                    }else{
                        that.showOutput(ret['err'])
                    }
                }
            })
        })
    }

    //comment control
    insertComment(comment){
        let cid = comment["comments_id"].toString()
        let username = comment["username"]
        let img = comment["img"]
        let detail = comment["detail"]
        let time = timeToStr(new Date(comment["time"]))
        let replies = comment["replies"]
        let coments_detail = document.getElementById("comments_detail")
        //ids
        let comment_container_id_str = "comment_container_"+cid
        let comment_icon_id_str = "comment_icon_"+cid
        let comment_username_id_str = "comment_username_"+cid
        let comment_time_id_str = "comment_time_"+cid
        let comment_detail_id_str = "comment_detail_"+cid
        let comment_show_bnt_id_str = "comment_show_"+cid
        let comment_reply_bnt_id_str = "comment_reply_"+cid
        let comment_share_bnt_id_str = "comment_share_"+cid
        let comment_replies_id_str = "comment_replies_"+cid
        //create div
        let commentHtml = ''+
            '                <div cid="'+cid+'" id="'+comment_container_id_str+'" class="comment_container">\n' +
            '                    <img id="'+comment_icon_id_str+'" class="comment_icon" src="./img/defaut_img.jpg">\n' +
            '                    <a id="'+comment_username_id_str+'" class="comment_username" href="/userinfo-'+username+'">'+username+'</a>\n' +
            '                    <a id="'+comment_time_id_str+'" class="comment_time">'+time+'</a>\n' +
            '                    <p id="'+comment_detail_id_str+'" class="comment_detail">'+detail+'</p>\n' +
            '                    <button id="'+comment_show_bnt_id_str+'" class="comment_bnt">查看评论</button>\n' +
            '                    <button id="'+comment_reply_bnt_id_str+'" class="comment_bnt">回复</button>\n' +
            '                    <button id="'+comment_share_bnt_id_str+'" class="comment_bnt">分享</button>\n' +
            '                    <div id="'+comment_replies_id_str+'" class="comment_replies"></div>\n' +
            '                </div>'
        coments_detail.innerHTML = commentHtml + coments_detail.innerHTML
        let comment_replies = document.getElementById(comment_replies_id_str)
        comment_replies.style.display="none"
        if(replies.length>0){
            //insert replys
            for(let i=0;i<replies.length;i++) {
                let subcid = replies[i]["comments_id"].toString()
                let subusername = replies[i]['username']
                let subreply_username = replies[i]['reply_username']
                let subtime = replies[i]['time']
                let subimg = replies[i]['img']
                let subdetail = replies[i]['detail']
                //ids
                let sub_comment_reply_id_str = "comment_reply_container_" + subcid + "_to_" + cid
                let sub_comment_icon_id_str = "comment_icon_" + subcid + "_to_" + cid
                let sub_comment_username_id_str = "comment_username_" + subcid + "_to_" + cid
                let sub_comment_time_id_str = "comment_time_" + subcid + "_to_" + cid
                let sub_comment_detail_id_str = "comment_detail_" + subcid + "_to_" + cid
                let sub_comment_reply_bnt_id_str = "comment_reply_" + subcid + "_to_" + cid
                let sub_comment_share_bnt_id_str = "comment_share_" + subcid + "_to_" + cid
                let replyHtml = '' +
                    '                        <div cid="' + subcid + '" id="' + sub_comment_reply_id_str + '" class="comment_reply">\n' +
                    '                            <img id="' + sub_comment_icon_id_str + '" class="comment_icon" src="./img/defaut_img.jpg">\n' +
                    '                            <a id="' + sub_comment_username_id_str + '" class="comment_username" href="/userinfo-'+subusername+'">' + subusername + '</a>\n' +
                    '                            <a id="' + sub_comment_time_id_str + '" class="comment_time">' + subtime + '</a>\n' +
                    '                            <p id="' + sub_comment_detail_id_str + '" class="comment_detail"><a class="comment_reply_username" href="/userinfo-'+subreply_username+'">@' + subreply_username + '</a>' + subdetail + '</p>\n' +
                    '                            <button id="' + sub_comment_reply_bnt_id_str + '" class="comment_bnt">回复</button>\n' +
                    '                            <button id="' + sub_comment_share_bnt_id_str + '" class="comment_bnt">分享</button>\n' +
                    '                        </div>'
                comment_replies.innerHTML += replyHtml
            }
        }else{
            document.getElementById(comment_show_bnt_id_str).style.display="none"
        }
    }

    #clickOnCommentsPanel(event){
        let id=event.target.id
        let pat = '(comment_show_|comment_reply_|comment_share_)(\\d+)((_to_)(\\d+))*'
        let ret = id.match(pat)
        if(ret==null)
            return
        switch(ret[1]){
            case "comment_show_":{
                let cid = parseInt(ret[2])
                let replies_element = document.getElementById("comment_replies_"+cid.toString())
                if(replies_element.style.display=="none"){
                    event.target.innerText="收起评论"
                    replies_element.style.display="inline-block"
                }else{
                    event.target.innerText="查看评论"
                    replies_element.style.display="none"
                }
                break
            }
            case "comment_reply_": {
                //get reply cid
                let cid = -1
                if (ret[5] != undefined)
                    cid = parseInt(ret[5])
                else cid = parseInt(ret[2])
                //get @ person
                let reply_username = null
                reply_username = event.target.parentElement.getElementsByClassName("comment_username")[0].innerText
                let problem_id = model.#question_id
                let username = getCookie("username")
                //create comment
                //console.log(username+problem_id+cid+reply_username)
                let reply_dialog = document.getElementById("reply_dialog")
                let reply_text = document.getElementById("reply_text")
                model.replyContext = {
                    problem_id:problem_id,
                    username:username,
                    time:null,
                    parent_comment_id:cid,
                    reply_username:reply_username,
                    detail:null
                }
                reply_dialog.showModal()
                break
            }
            case "comment_share_":{
                alert("等待接入......")
            }
        }
    }

    commentReply(){
        model.replyContext = {
            problem_id:model.#question_id,
            username:getCookie("username"),
            time:null,
            parent_comment_id:-1,
            reply_username:null,
            detail:null
        }
        let reply_dialog = document.getElementById("reply_dialog")
        reply_dialog.showModal()
    }

    //editor control
    getCode(){
        console.log(this.#editor.getValue())
    }

    setLanguage(lang){
        let that = this
        this.#lang = lang
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

    //comment control
    #replyConfirm(){
        //complete context
        model.replyContext['detail'] = document.getElementById("reply_text").value
        model.replyContext['time'] = timeToStr(new Date())
        let ret = postAndWait("/reply",model.replyContext)
        if(ret['err'] == null){
            //insert comment
            if(model.replyContext['parent_comment_id'] === -1){
                //top level commewnt
                let cid = ret['cid'].toString()
                let username = model.replyContext['username']
                let img = null /* yourselves img*/
                let detail = model.replyContext['detail']
                let time = model.replyContext['time']
                let coments_detail = document.getElementById("comments_detail")
                //ids
                let comment_container_id_str = "comment_container_"+cid
                let comment_icon_id_str = "comment_icon_"+cid
                let comment_username_id_str = "comment_username_"+cid
                let comment_time_id_str = "comment_time_"+cid
                let comment_detail_id_str = "comment_detail_"+cid
                let comment_show_bnt_id_str = "comment_show_"+cid
                let comment_reply_bnt_id_str = "comment_reply_"+cid
                let comment_share_bnt_id_str = "comment_share_"+cid
                let comment_replies_id_str = "comment_replies_"+cid
                //create div
                let commentHtml = ''+
                    '                <div cid="'+cid+'" id="'+comment_container_id_str+'" class="comment_container">\n' +
                    '                    <img id="'+comment_icon_id_str+'" class="comment_icon" src="./img/defaut_img.jpg">\n' +
                    '                    <a id="'+comment_username_id_str+'" class="comment_username" href="/userinfo-'+username+'">'+username+'</a>\n' +
                    '                    <a id="'+comment_time_id_str+'" class="comment_time">'+time+'</a>\n' +
                    '                    <p id="'+comment_detail_id_str+'" class="comment_detail">'+detail+'</p>\n' +
                    '                    <button id="'+comment_show_bnt_id_str+'" class="comment_bnt">查看评论</button>\n' +
                    '                    <button id="'+comment_reply_bnt_id_str+'" class="comment_bnt">回复</button>\n' +
                    '                    <button id="'+comment_share_bnt_id_str+'" class="comment_bnt">分享</button>\n' +
                    '                    <div id="'+comment_replies_id_str+'" class="comment_replies"></div>\n' +
                    '                </div>'
                coments_detail.innerHTML = commentHtml + coments_detail.innerHTML
            }else{
                //reply
                let cid = model.replyContext['parent_comment_id'].toString()
                let subcid = ret['cid'].toString()
                let subusername = model.replyContext['username']
                let subreply_username = model.replyContext['reply_username']
                let subtime = model.replyContext['time']
                let subimg = null /* yourselves img*/
                let subdetail = model.replyContext['detail']
                let comment_replies = document.getElementById("comment_replies_"+model.replyContext['parent_comment_id'])
                //ids
                let sub_comment_reply_id_str = "comment_reply_container_" + subcid + "_to_" + cid
                let sub_comment_icon_id_str = "comment_icon_" + subcid + "_to_" + cid
                let sub_comment_username_id_str = "comment_username_" + subcid + "_to_" + cid
                let sub_comment_time_id_str = "comment_time_" + subcid + "_to_" + cid
                let sub_comment_detail_id_str = "comment_detail_" + subcid + "_to_" + cid
                let sub_comment_reply_bnt_id_str = "comment_reply_" + subcid + "_to_" + cid
                let sub_comment_share_bnt_id_str = "comment_share_" + subcid + "_to_" + cid
                let replyHtml = '' +
                    '                        <div cid="' + subcid + '" id="' + sub_comment_reply_id_str + '" class="comment_reply">\n' +
                    '                            <img id="' + sub_comment_icon_id_str + '" class="comment_icon" src="./img/defaut_img.jpg">\n' +
                    '                            <a id="' + sub_comment_username_id_str + '" class="comment_username" href="/userinfo-'+subusername+'">' + subusername + '</a>\n' +
                    '                            <a id="' + sub_comment_time_id_str + '" class="comment_time">' + subtime + '</a>\n' +
                    '                            <p id="' + sub_comment_detail_id_str + '" class="comment_detail"><a class="comment_reply_username" href="/userinfo-'+subreply_username+'">@' + subreply_username + '</a>' + subdetail + '</p>\n' +
                    '                            <button id="' + sub_comment_reply_bnt_id_str + '" class="comment_bnt">回复</button>\n' +
                    '                            <button id="' + sub_comment_share_bnt_id_str + '" class="comment_bnt">分享</button>\n' +
                    '                        </div>'
                comment_replies.innerHTML = replyHtml + comment_replies.innerHTML
            }
        }else{
            showError(ret['err'])
            return
        }
        //close
        model.replyReset()
        document.getElementById("reply_dialog").close()
    }

    #replyCancel(){
        //close
        model.replyReset()
        document.getElementById("reply_dialog").close()
    }

    replyReset(){
        model.replyContext = null
        document.getElementById("reply_text").value = ""
    }

    //tab control
    selectTab(tabName){
        let tabs={
            question:{tab:document.getElementById("tab_panel_opt_question"),panel:document.getElementById("question_detail")},
            comments:{tab:document.getElementById("tab_panel_opt_comments"),panel:document.getElementById("comments_root")}
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

    showRunDialog(){
        let dialog = document.getElementById("run_dialog")
        let run_code_bnt = document.getElementById("run_code_bnt")
        let run_output = document.getElementById("run_output")
        run_code_bnt.style.background = ""
        run_code_bnt.innerText = "执行"
        run_code_bnt.addEventListener("click", function (event){
            switch(event.target.innerText){
                case "执行":{
                    let run_code_bnt = document.getElementById("run_code_bnt")
                    run_code_bnt.style.background = "#bbbbbb"
                    run_code_bnt.innerText = ". . . . . ."
                    //run
                    let that = model
                    let input = document.getElementById("run_input").value
                    postNoWait("/run",{input:input,platform:that.#lang,code:that.#editor.getValue()},function (ajax){
                        if (ajax.readyState===4 && ajax.status===200){
                            let ret = JSON.parse(ajax.responseText)
                            if(ret['err']==null){
                                let output = JSON.parse(ret['result']['result'])
                                let str = "----------执行----------\n"
                                str += "内存："+output[1]["MemoryUsed"]+"KB\n"
                                str += "耗时："+output[1]["TimeUsed"]+"MS\n"
                                str += "----------输出----------\n"
                                if(output[1]["ResultInfo"]===""){
                                    str+=ret['result']['output']
                                }else{
                                    str+=output[1]["ResultInfo"]
                                }
                                that.showOutputOfRun(str)
                            }else{
                                that.showOutputOfRun(ret['err'])
                            }
                        }
                    })
                    break
                }
                case ". . . . . .":{
                    break
                }
                case "确定":{
                    let dialog = document.getElementById("run_dialog")
                    let run_code_bnt = document.getElementById("run_code_bnt")
                    run_code_bnt.style.background = ""
                    run_code_bnt.innerText = "执行"
                    run_code_bnt.removeEventListener("click", this.clickOnConfirm)
                    dialog.close()
                    break
                }
            }
        })
        run_output.innerText = "请在输入框中输入内容后,点击”执行“......"
        dialog.showModal()
    }

    showOutputOfRun(str){
        let run_code_bnt = document.getElementById("run_code_bnt")
        run_code_bnt.style.background = ""
        run_code_bnt.innerText = "确定"
        //show
        let run_output = document.getElementById("run_output")
        run_output.value = str
    }
}

let model = new ProblemsWebModel()