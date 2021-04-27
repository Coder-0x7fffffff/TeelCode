class LoginWebModel{
    constructor() {
        document.getElementById("login_bnt").addEventListener('click', ()=>{
            let username = document.getElementById("username_input").value
            let password = document.getElementById("password_input").value
            let ajax = getAjax()
            ajax.open("post","/login",false)
            ajax.setRequestHeader("Content-Type","application/json")
            ajax.send(JSON.stringify({username:username,password:password}))
            let ret = JSON.parse(ajax.responseText)
            if(!ret["online"]){
                showError(ret["err"])
                return
            }
            setCookie("token", ret["token"],-1)
            setCookie("username", ret["username"],-1)
            window.location.href="/"
        })
    }
}

new LoginWebModel()