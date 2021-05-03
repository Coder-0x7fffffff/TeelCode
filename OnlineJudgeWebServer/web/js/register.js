class RegisterWebModel{
    constructor() {
        document.getElementById("register_bnt").addEventListener('click', ()=>{
            let id = document.getElementById("username_input").value
            let password = document.getElementById("password_input").value
            let confirm_password = document.getElementById("confirm_password_input").value
            let question = document.getElementById("question_input").value
            let answer = document.getElementById("answer_input").value
            if(password!==confirm_password){
                showError("两次密码输入不一致")
                return
            }
            let ajax = getAjax()
            ajax.open("post","/register",false)
            ajax.setRequestHeader("Content-Type","application/json")
            ajax.send(JSON.stringify({id:id,password:password,question:question,answer:answer}))
            showError(ajax.responseText)
        })
    }
}

new RegisterWebModel()