function getAjax(){
    let xmlhttp;
    if (window.XMLHttpRequest) {
        // code for IE7+, Firefox, Chrome, Opera, Safari
        xmlhttp=new XMLHttpRequest();
    }
    else {
        // code for IE6, IE5
        xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
    }
    return xmlhttp
}

function showError(msg){
    document.getElementById("err_msg").innerText=msg
    document.getElementById("err_dialog").showModal()
}

function hiddenError(){
    document.getElementById("err_dialog").close()
}

function logout(){
    removeCookie("token")
    window.location.href="/"
}

//init of page
function initialize(){
    document.getElementById("err_bnt").addEventListener("click",()=>{hiddenError()})
    document.getElementById("logout_bnt").addEventListener("click", ()=>{logout()})
}
initialize()