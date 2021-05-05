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
            postNoWait("/register", {id:id,password:password,question:question,answer:answer}, function (ajax){
                if (ajax.readyState===4 && ajax.status===200){
                    let ret = JSON.parse(ajax.responseText)
                    if(ret['err']==null){
                        showError("注册成功",function (){window.location.href = "./login"})

                    }else{
                        showError(ret['err'],)
                    }
                }
            })
        })
    }
}

new RegisterWebModel()