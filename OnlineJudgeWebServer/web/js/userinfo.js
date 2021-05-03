class Model{

    #data = null

    initPanel(){
        let username = getCookie("username")
        let expectUsername = decodeURI(window.location.href.substr(window.location.href.lastIndexOf('-')+1))
        let panel = ''
        if(username === expectUsername){
            panel = ''+
                '<li id="tab_panel_opt_info" class="tab_panel_opt">个人资料</li>\n' +
                '<li id="tab_panel_opt_pro" class="tab_panel_opt">做题记录</li>' +
                '<li id="tab_panel_opt_change" class="tab_panel_opt">修改密码</li>' +
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
                        }else{
                            showError(ret['err'])
                        }
                        document.getElementById("info_name").innerHTML = model.#data['user']['username']
                    }
                })
                break
            case "做题记录":
                document.getElementById("detail_detail").innerHTML = '' +
                    '<table id="question_table" class="question_table">\n' +
                    '    <tr class="table_title">\n' +
                    '        <th class="table_head">编号</th>\n' +
                    '        <th class="table_head text_align_left">题目</th>\n' +
                    '        <th class="table_head">难度</th>\n' +
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
                    cell.innerText=this.#data["problems"][i]['pdiff'].toString()
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
        }
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