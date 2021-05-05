class Model{

    #data = null
    #page = 1

    initPanel(){
        let isAdmin = getCookie("isAdmin")
        let username = getCookie("username")
        let expectUsername = decodeURI(window.location.href.substr(window.location.href.lastIndexOf('-')+1))
        let panel = ''
        if(username === expectUsername){
            panel = ''+
                '<li id="tab_panel_opt_info" class="tab_panel_opt">个人资料</li>\n' +
                '<li id="tab_panel_opt_pro" class="tab_panel_opt">做题记录</li>' +
                '<li id="tab_panel_opt_change" class="tab_panel_opt">修改密码</li>' +
                (isAdmin === "1"?'<hr class="menu_hr"/>\n':'') +
                (isAdmin === "1"?'<li id="tab_panel_opt_manage_class" class="tab_panel_opt">分类管理</li>':'') +
                (isAdmin === "1"?'<li id="tab_panel_opt_manage_problem" class="tab_panel_opt">题目管理</li>':'') +
                '<hr class="menu_hr"/>\n' +
                '<li id="tab_panel_opt_comments" class="tab_panel_opt table_panel_opt_red">退出</li>'
        }else{
            panel = ''+
                '<li id="tab_panel_opt_info" class="tab_panel_opt">个人资料</li>'
        }
        document.getElementById("menu_list1").innerHTML = panel
        //select
        this.selectMenu(document.getElementById("tab_panel_opt_info"))
    }

    selectMenu(target){
        if(target.tagName.toLowerCase() !== "li")
            return
        if(target.innerText==="退出")
            logout()
        //menu select
        if(target.classList.contains("table_panel_opt_selected"))
            return
        let list = target.parentElement
        let selected = list.getElementsByClassName("table_panel_opt_selected")
        for(let i=0;i<selected.length;i++)
            selected.item(i).classList.remove("table_panel_opt_selected")
        target.classList.add("table_panel_opt_selected")
        //title change
        document.getElementById("detail_title").innerText = target.innerText
        //detail panel

        let username = getCookie("username")
        switch (target.innerText){
            case "个人资料":
                document.getElementById("detail_detail").innerHTML = ''+
                    '<div class="info_panel">' +
                    '    <img class="img_panel" src="./img/defaut_img.jpg">\n' +
                    '    <div class="info_detail_infos">\n' +
                    '        <p class="info_label">用户ID：</p><a class="info_text">'+this.#data['user']['id']+'</a>' +
                    '        <p class="info_label">用户名：</p><div  id="info_name" class="info_text '+(username === this.#data['user']['username']?'info_self_name':'')+'">'+this.#data['user']['username']+'</div>\n' +
                    (username === this.#data['user']['username']?'<button id="info_change_name" class="info_self_change_name_bnt">修改</button>':'') +
                    '        <p class="info_label">描述：</p><div id="info_dscp" class="info_text '+(username === this.#data['user']['username']?'info_self_name':'')+'">'+this.#data['user']['dscp']+'</div>\n' +
                    (username === this.#data['user']['username']?'<button id="info_change_dscp" class="info_self_change_name_bnt">修改</button>':'') +
                    '    </div>' +
                    '</div>'
                document.getElementById("info_change_name").addEventListener("click", function (event){
                    if(event.target.innerText === "修改"){
                        event.target.innerText = "确定"
                        document.getElementById("info_name").innerHTML = '<input placeholder="'+model.#data['user']['username']+'" class="info_text" id="info_name_change_input">'
                    }else{
                        event.target.innerText = "修改"
                        let name = document.getElementById("info_name_change_input").value
                        let ret = postAndWait("/changeName",{name:name})
                        if(ret['err']==null){
                            model.#data['user']['username'] = name
                            setCookie("username", name,-1)
                        }else{
                            showError(ret['err'])
                        }
                        document.getElementById("info_name").innerHTML = model.#data['user']['username']
                    }
                })
                document.getElementById("info_change_dscp").addEventListener("click", function (event){
                    if(event.target.innerText === "修改"){
                        event.target.innerText = "确定"
                        document.getElementById("info_dscp").innerHTML = '<input placeholder="'+model.#data['user']['dscp']+'" class="info_text" id="info_dscp_change_input">'
                    }else{
                        event.target.innerText = "修改"
                        let dscp = document.getElementById("info_dscp_change_input").value
                        let ret = postAndWait("/changeDscp",{dscp:dscp})
                        if(ret['err']==null){
                            model.#data['user']['dscp'] = dscp
                        }else{
                            showError(ret['err'])
                        }
                        document.getElementById("info_dscp").innerHTML = model.#data['user']['dscp']
                    }
                })
                break
            case "做题记录":
                document.getElementById("detail_detail").innerHTML = '' +
                    '<table id="question_table" class="question_table">\n' +
                    '    <tr class="table_title">\n' +
                    '        <th class="table_head">编号</th>\n' +
                    '        <th class="table_head text_align_left">题目</th>\n' +
                    '        <th class="table_head">状态</th>\n' +
                    '    </tr>\n' +
                    '</table>'
                let table = document.getElementById("question_table")
                for(let i=0;i<this.#data["problems"].length;i++){
                    let row = table.insertRow()
                    if(i%2===0)
                        row.style.background = "#fafafafa"
                    let cell = row.insertCell()
                    cell.style.cssText = "text-align: center"
                    cell.innerText=this.#data["problems"][i]['pid'].toString()
                    cell = row.insertCell()
                    cell.style.cssText = "text-align: left"
                    cell.innerText=this.#data["problems"][i]['pname'].toString()
                    cell = row.insertCell()
                    cell.style.cssText = "text-align: center"
                    cell.innerText=this.#data["problems"][i]['state'].toString()
                }
                break
            case "修改密码":
                document.getElementById("detail_detail").innerHTML = '' +
                    '<div class="change_panel">' +
                    '    <p>'+this.#data['user']['question']+'</p>\n' +
                    '    <input class="change_input" type="text" placeholder="问题答案" id="answer_input">\n' +
                    '    <input class="change_input" type="password" placeholder="新密码" id="password_input">\n' +
                    '    <input class="change_input" type="password" placeholder="确认密码" id="confirm_password_input">\n' +
                    '    <button class="change_submit" id="change_submit">修改</button>' +
                    '</div>'
                document.getElementById("change_submit").addEventListener("click", function (event){
                    let answer = document.getElementById("answer_input").value
                    let password = document.getElementById("password_input").value
                    let confirm_password = document.getElementById("confirm_password_input").value
                    if(password!==confirm_password){
                        showError("两次密码输入不一致")
                        return
                    }
                    let ret = postAndWait("/changePassword",{answer:answer, password:password})
                    if(ret['err']==null){
                        showError("修改成功")
                    }else{
                        showError(ret['err'])
                    }
                })
                break
            case "分类管理":
                document.getElementById("detail_detail").innerHTML = '' +
                    '<button id="class_add" class="bnt_add">添加分类</button>' +
                    '<table id="class_table" class="question_table">\n' +
                    '    <tr class="table_title">\n' +
                    '        <th class="table_head">编号</th>\n' +
                    '        <th class="table_head text_align_left">名称</th>\n' +
                    '        <th class="table_head">#</th>\n' +
                    '    </tr>\n' +
                    '</table>' +
                    '<dialog id="add_dialog" class="add_dialog">\n' +
                    '    <label for="add_name_input">名称</label><input id="add_name_input" class="add_name_input">' +
                    '    <button id="add_confirm_bnt" class="add_bnt">确定</button>\n' +
                    '    <button id="add_cancel_bnt" class="add_bnt">取消</button>\n' +
                    '</dialog>'
                let classes = postAndWait("/queryClasses",{})
                let classTable = document.getElementById("class_table")
                for(let i=0;i<classes.length;i++){
                    let row = classTable.insertRow()
                    if(i%2===0)
                        row.style.background = "#fafafafa"
                    let cell = row.insertCell()
                    cell.style.cssText = "text-align: center"
                    cell.innerText=classes[i]['cid']
                    cell = row.insertCell()
                    cell.style.cssText = "text-align: left"
                    cell.innerText=classes[i]['cname']
                    cell = row.insertCell()
                    cell.style.cssText = "text-align: center"
                    cell.innerHTML = '<button id="class_del">删除</button>'
                }
                resetTableColor(classTable)
                //add listener
                classTable.addEventListener("click",function (event){
                    if(event.target.id === "class_del"){
                        let cid = parseInt(event.target.parentElement.parentElement.cells[0].innerText)
                        let ret = postAndWait("/deleteClassification",{id:cid})
                        if(ret['err']===null){
                            event.target.parentElement.parentElement.remove()
                            resetTableColor(classTable)
                        }else{
                            showError(ret['err'])
                        }
                    }
                })
                document.getElementById("add_cancel_bnt").addEventListener("click",function (){
                    let dialog = document.getElementById("add_dialog")
                    dialog.close()
                })
                document.getElementById("add_confirm_bnt").addEventListener("click",function (){
                    let name = document.getElementById("add_name_input").value
                    if(name === ""){
                        showError("名称不能为空")
                        return
                    }
                    let classTable = document.getElementById("class_table")
                    let set = new Set()
                    for(let i = 0;i<classTable.rows.length;i++)
                        set.add(parseInt(classTable.rows[i].cells[0].innerText))
                    let id = 1
                    while(set.has(id))
                        id++
                    let ret = postAndWait("/addClass",{cid: id, cname: name})
                    if(ret['err']!=null){
                        showError(ret['err'])
                        return
                    }
                    let tempTable = document.getElementById("class_table")
                    let row = tempTable.insertRow()
                    let cell = row.insertCell()
                    cell.style.cssText = "text-align: center"
                    cell.innerText=id.toString()
                    cell = row.insertCell()
                    cell.style.cssText = "text-align: left"
                    cell.innerText=name
                    cell = row.insertCell()
                    cell.style.cssText = "text-align: center"
                    cell.innerHTML = '<button id="class_del">删除</button>'
                    resetTableColor(tempTable)
                    let dialog = document.getElementById("add_dialog")
                    dialog.close()
                })
                document.getElementById("class_add").addEventListener("click",function (event){
                    let dialog = document.getElementById("add_dialog")
                    dialog.showModal()
                    document.getElementById("add_name_input").value = ""
                })
                break
            case "题目管理":
                document.getElementById("detail_detail").innerHTML = '' +
                    '<button id="question_add" class="bnt_add">添加题目</button>' +
                    '<table id="manage_question_table" class="question_table">\n' +
                    '    <tr class="table_title">\n' +
                    '        <th class="table_head">编号</th>\n' +
                    '        <th class="table_head text_align_left">名称</th>\n' +
                    '        <th class="table_head">#</th>\n' +
                    '    </tr>\n' +
                    '</table>' +
                    '<button id="table_more" class="more_bnt">加载更多</button>' +
                    '<dialog id="add_dialog" class="question_add_dialog">\n' +
                    '    <a>题目：</a><input id="input_name"/>' +
                    '    <a class="label_gap">难度：</a><select id="select_diff"></select>' +
                    '    <a class="label_gap">分类：</a><input id="input_classes"/><a class="tips">分类使用 , 间隔</a>' +
                    '    <br/><br/>' +
                    '    <a>描述：</a><br/><textarea id="input_dscp" class="input_textarea"></textarea>' +
                    '    <br/>' +
                    '    <a>输入：</a><br/><textarea id="input_inputs" class="input_textarea"></textarea>' +
                    '    <br/>' +
                    '    <a>输出：</a><br/><textarea id="input_outputs" class="input_textarea"></textarea>' +
                    '    <br/>' +
                    '    <button id="add_cancel_bnt" class="question_add_bnt">取消</button>\n' +
                    '    <button id="add_confirm_bnt" class="question_add_bnt">确定</button>\n' +
                    '</dialog>'
                this.insertQuestions(1, false)
                //add listener
                let manageTable = document.getElementById("manage_question_table")
                manageTable.addEventListener("click",function (event){
                    if(event.target.id === "class_del"){
                        let pid = parseInt(event.target.parentElement.parentElement.cells[0].innerText)
                        let ret = postAndWait("/deleteProblem",{id:pid})
                        if(ret['err']===null){
                            event.target.parentElement.parentElement.remove()
                            resetTableColor(classTable)
                        }else{
                            showError(ret['err'])
                        }
                    }
                })
                document.getElementById("table_more").addEventListener("click",function (event){
                    model.insertQuestions(model.#page+1,false)
                })
                document.getElementById("question_add").addEventListener("click",function (event){
                    let dialog = document.getElementById("add_dialog")
                    dialog.showModal()
                })
                document.getElementById("add_confirm_bnt").addEventListener("click",function (){
                    let dialog = document.getElementById("add_dialog")
                    let name = document.getElementById("input_name").value
                    if(name === "") {
                        showError("名称不能为空")
                        return
                    }
                    let diff_select = document.getElementById("select_diff")
                    let difficulty = diff_select.options[diff_select.selectedIndex].value
                    let classification = document.getElementById("input_classes").value
                    let dscp = document.getElementById("input_dscp").value
                    let inputs = document.getElementById("input_inputs").value
                    let outputs = document.getElementById("input_outputs").value
                    let ret = postAndWait('/getNextProblemId', {})
                    let pid = ret['pid']
                    let reqData = {id: pid,name:name,difficulty:difficulty,classification:classification,dscp:dscp,inputs:inputs,outputs:outputs}
                    //add
                    ret = postAndWait("/addQuestion", reqData)
                    if(ret["err"] == null){
                        model.insertQuestions(1, true)
                        dialog.close()
                    }else{
                        showError(ret['err'])
                    }
                })
                document.getElementById("add_cancel_bnt").addEventListener("click",function (event){
                    let dialog = document.getElementById("add_dialog")
                    dialog.close()
                })
                break
        }
    }

    getQuestionList(page, offset, difficulty, clazz, status){
        //postAndWait queryQuestionList
        let ret = postAndWait('/queryQuestionList', {page:page,offset:offset,difficulty:difficulty,class:clazz,status:status})
        let list = []
        let problems = ret["problems"]
        for(let problem in problems){
            let pid = problems[problem]["problem"]["pid"]
            let pname =problems[problem]["problem"]["pname"]
            list.push([pid, pname])
        }
        return list
    }

    insertQuestions(page, reset){
        let manageTable = document.getElementById("manage_question_table")
        if(reset === true){
            while(manageTable.rows.length>1)manageTable.rows[manageTable.rows.length-1].remove()
        }
        let questionList = this.getQuestionList(page,20, 0, 0, -1)
        //show list
        let styles = ["text-align: center","text-align: left","text-align: center"]
        for(let i=0;i<questionList.length;i++){
            let row = manageTable.insertRow()
            row.setAttribute("pid",questionList[i][0])
            if(i%2===0)
                row.style.background = "#fafafafa"
            for(let j=0;j<2;j++){
                let cell = row.insertCell()
                cell.style.cssText = styles[j]
                cell.innerText=questionList[i][j].toString()
            }
            let cell = row.insertCell()
            cell.style.cssText = styles[2]
            cell.innerHTML = '<button id="class_del">删除</button>'
        }
        //add diffs
        let diff_elem = document.getElementById("select_diff")
        while(diff_elem.options.length>0)diff_elem.options.remove(0)
        let diffs = {"1":"简单","2":"中等","3":"困难"}
        let keys = Object.keys(diffs).sort()
        for(let i=0;i<keys.length;i++){
            diff_elem.options.add(new Option(diffs[keys[i]],keys[i].toString()))
        }
        this.#page = page
        //reset color
        resetTableColor(manageTable)
    }

    constructor() {
        let expectUsername = decodeURI(window.location.href.substr(window.location.href.lastIndexOf('-')+1))
        this.#data = postAndWait("/userinfo",{username:expectUsername})
        if(this.#data['err']!=null){
            showError(this.#data['err'])
        }
        let menu_list1 = document.getElementById("menu_list1")
        //init panel
        this.initPanel()
        //add listener
        menu_list1.addEventListener("click",function (event){
            model.selectMenu(event.target)
        })
    }
}

let model = new Model()