
function getPageBottom(){
    return ''+
        '       <div id="page_bottom" class="page_bottom">\n' +
        '            <div id="bottom_detail" class="main_detail">\n' +
        '                bottom\n' +
        '            </div>\n' +
        '        </div>'
}

function getPageHeader(user){
    //get online or not with token
    let online=user['online']
    let username=user['username']
    //execute panel
    let userPanel = ""
    if(!online){
        userPanel = '<div class="list_item_right"><a href="/register" class="list_item_text">注册</a></div><div class="list_item_right"><a href="/login" class="list_item_text">登录</a></div>'
    }else{
        userPanel = '<div class="list_item_right"><button class="logout_bnt" id="logout_bnt">退出</button></div><div class="list_item_right"><a href="/userinfo-'+username+'" ><img class="list_item_icon" src="./img/defaut_img.jpg"/></a></div>'
    }
    //execute header
    return '' +
        '    <div id="page_header" class="page_header">\n' +
        '        <div id="header_panel" class="main_detail">\n' +
        '            <nav class="nav_list">\n' +
        '                <img src="../img/icon.png" class="list_item_icon"/>\n' +
        '                <div class="list_item"><a href="/" class="list_item_text">首页</a></div>\n' +
        //'                <div class="list_item"><a href="/admin" class="list_item_text">管理</a></div>\n' +
        '            </nav>\n' +
        '            <nav id="user_panel" class="nav_list_right">' + userPanel + '</nav>\n' +
        '        </div>\n' +
        '    </div>\n' +
        '    <hr id="gap_hr" class="gap_hr">'
}

function getScriptList(paths){
    let utilPaths = ['./js/cookie.js','./js/util.js']
    let scriptsHtml = ''
    //util
    for(let i=0;i<utilPaths.length;i++){
        scriptsHtml+='<script src="'+utilPaths[i]+'"></script>'
    }
    //customer
    for(let i=0;i<paths.length;i++){
        scriptsHtml+='<script src="'+paths[i]+'"></script>'
    }
    return scriptsHtml
}

function getCSSList(paths){
    let utilPaths = ['./css/util.css']
    let CSSList = ''
    //util
    for(let i=0;i<utilPaths.length;i++){
        CSSList+='<link rel="stylesheet" href="'+utilPaths[i]+'" type="text/css">'
    }
    //customer
    for(let i=0;i<paths.length;i++){
        CSSList+='<link rel="stylesheet" href="'+paths[i]+'" type="text/css">'
    }
    return CSSList
}

function getErrorDialog(){
    return ''+
        '    <dialog id="err_dialog" class="err_dialog">\n' +
        '        <p id="err_msg" class="err_msg">error</p>\n' +
        '        <button id="err_bnt" class="err_bnt">确定</button>\n' +
        '    </dialog>'
}

function getPageFrame(user, scriptPaths, CSSPaths, extendOpts){
    let opts = {pageHeader:getPageHeader(user), pageBottom:getPageBottom(), scriptList:getScriptList(scriptPaths), CSSList:getCSSList(CSSPaths), errorDialog:getErrorDialog()}
    let title = "TeelCode - 在线评测系统"
    if(!(extendOpts === undefined)){
        if(!("title" in extendOpts))
            extendOpts['title'] = title
        for(k in extendOpts)
            opts[k] = extendOpts[k]
    }
    return opts
}

module.exports = {
    getPageBottom,
    getPageHeader,
    getScriptList,
    getCSSList,
    getErrorDialog,
    getPageFrame
}