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

function showError(msg, callback){
    document.getElementById("err_msg").innerText=msg
    document.getElementById("err_dialog").showModal()

    let elem = document.getElementById("err_bnt")
    if(elem != null){
        elem.addEventListener("click",()=>{
            hiddenError()
            if(callback!==undefined && callback!=null)
                callback()
        })
    }
}

function hiddenError(){
    document.getElementById("err_dialog").close()
}

function timeToStr(date){
    let year = date.getFullYear().toString()
    let month = date.getMonth().toString()
    let day = date.getDay().toString()
    let hour = date.getHours().toString()
    let minute = date.getMinutes().toString()
    return year+"-"+month+"-"+day+" "+hour+":"+minute
}

function resetTableColor(table){
    for(let i=0;i<table.rows.length;i++){
        if(i%2===0)
            table.rows[i].style.background = "#fafafafa"
        else
            table.rows[i].style.background = "white"
    }
}

function logout(){
    removeCookie("token")
    removeCookie("id")
    removeCookie("username")
    removeCookie("isAdmin")
    window.location.href="/"
}

//init of page
function initialize(){
    let elem  = document.getElementById("logout_bnt")
    if(elem != null){
        elem.addEventListener("click", ()=>{logout()})
    }
}
initialize()