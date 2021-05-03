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

function postAndWait(path,JSONdata){
    let ajax = getAjax()
    ajax.open("post",path,false)
    ajax.setRequestHeader("Content-Type","application/json")
    ajax.send(JSON.stringify(JSONdata))
    let ret = JSON.parse(ajax.responseText)
    return ret
}

function postNoWait(path,JSONdata,callback){
    let ajax = getAjax()
    ajax.open("post",path,true)
    ajax.setRequestHeader("Content-Type","application/json")
    ajax.onreadystatechange = function (){
        callback(ajax)
    }
    ajax.send(JSON.stringify(JSONdata))
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
    removeCookie("username")
    window.location.href="/"
}

//init of page
function initialize(){
    let elem = document.getElementById("err_bnt")
    if(elem != null){
        elem.addEventListener("click",()=>{hiddenError()})
    }
    elem  = document.getElementById("logout_bnt")
    if(elem != null){
        elem.addEventListener("click", ()=>{logout()})
    }
}
initialize()