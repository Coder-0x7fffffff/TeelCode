class LoginWebModel{
    constructor() {
        removeCookie("token")
        removeCookie("id")
        removeCookie("username")
        removeCookie("isAdmin")
        document.getElementById("login_bnt").addEventListener('click', ()=>{
            let id = document.getElementById("id_input").value
            let password = document.getElementById("password_input").value
            let ajax = getAjax()
            ajax.open("post","/login",false)
            ajax.setRequestHeader("Content-Type","application/json")
            ajax.send(JSON.stringify({id:id,password:password}))
            let ret = JSON.parse(ajax.responseText)
            if(!ret["online"]){
                showError(ret["err"])
                return
            }
            setCookie("token", ret["token"],-1)
            setCookie("id", ret["id"],-1)
            setCookie("username", ret["username"],-1)
            setCookie("isAdmin", ret['isAdmin'], -1)
            window.location.href="/"
        })
    }
}

new LoginWebModel()