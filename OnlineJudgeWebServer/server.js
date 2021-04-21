const express = require("express")
const cookieParser = require("cookie-parser")
const bodyParser = require("body-parser")

const server = express()
//server contact
function getUserInfo(oldToken){
    let img = null
    let online=false
    let userName=null
    let token=null
    let err=null
    //get user info
    if(oldToken === "123456789"){
        online=true
        userName="xiami"
        token="123456789"
    }
    //return
    return {online:online, username:userName, token: token, img:img, err:err}
}

function login(username, password){
    let img=null
    let online=false
    let userName=username
    let token=null
    let err=null
    //login
    if(username === "xiami"){
        online=true
        token=123456789
        err=null
    }else err="请输入正确的账号密码"
    //return
    return {online:online, username:userName, token: token, img:img, err:err}
}

function register(username, password, question, answer){
    let err=null
    //register
    //return
    return {err:err}
}

//ejs constructor
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
    let userName=user['username']
    //execute panel
    let userPanel = ""
    if(!online){
        userPanel = '<div class="list_item_right"><a href="/register" class="list_item_text">注册</a></div><div class="list_item_right"><a href="/login" class="list_item_text">登录</a></div>'
    }else{
        userPanel = '<div class="list_item_right"><button class="logout_bnt" id="logout_bnt">退出</button></div><div class="list_item_right"><a href="/userinfo" ><img class="list_item_icon" src="./img/defaut_img.jpg"/></a></div>'
    }
    //execute header
    return '' +
        '    <div id="page_header" class="page_header">\n' +
        '        <div id="header_panel" class="main_detail">\n' +
        '            <nav class="nav_list">\n' +
        '                <img src="../img/icon.png" class="list_item_icon"/>\n' +
        '                <div class="list_item"><a href="/" class="list_item_text">练习</a></div>\n' +
        '                <div class="list_item"><a href="/" class="list_item_text">竞赛</a></div>\n' +
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

function start(port){
    //static
    server.use(cookieParser())
    server.use(bodyParser.urlencoded({extended: false}));
    server.use(bodyParser.json());
    server.use(express.static(__dirname + "/web"))
    //get
    server.get("/",(req, res)=>{
        //judge online
        let user = getUserInfo(req.cookies['token'])
        if(!user["online"]){
            let opts = getPageFrame(user, ['./js/login.js'], ['./css/login.css'], {})
            res.render(__dirname+"/web/login.ejs",opts)
            return
        }
        let opts = getPageFrame(user, ['./js/index.js'], ['./css/index.css'], {})
        res.render(__dirname+"/web/index.ejs",opts)
    })
    server.get("/register",(req, res)=>{
        let user = getUserInfo(req.cookies['token'])
        let opts = getPageFrame(user, ['./js/register.js'], ['./css/register.css'], {})
        res.render(__dirname+"/web/register.ejs",opts)
    })
    server.get("/login",(req, res)=>{
        let user = getUserInfo(req.cookies['token'])
        let opts = getPageFrame(user, ['./js/login.js'], ['./css/login.css'], {})
        res.render(__dirname+"/web/login.ejs",opts)
    })
    server.get("/userinfo",(req, res)=>{
        //judge online
        let user = getUserInfo(req.cookies['token'])
        if(!user["online"]){
            let opts = getPageFrame(user, ['./js/login.js'], ['./css/login.css'], {})
            res.render(__dirname+"/web/login.ejs",opts)
            return
        }
        let opts = getPageFrame(user, [], [], {})
        res.render(__dirname+"/web/userinfo.ejs",opts)
    })
    server.get("/problems-*",(req, res)=>{
        //judge online
        let user = getUserInfo(req.cookies['token'])
        if(!user["online"]){
            let opts = getPageFrame(user, ['./js/login.js'], ['./css/login.css'], {})
            res.render(__dirname+"/web/login.ejs",opts)
            return
        }
        //pick question date
        let urls = req.url.split('-')
        let qid = parseInt(urls[urls.length-1])
        let title = qid.toString()
        //get question detail
        let question = {title:"abc"}
        //return
        let scripts = [
            './js/codemirror/lib/codemirror.js',
            './js/codemirror/mode/clike/clike.js',
            './js/codemirror/mode/python/python.js',
            './js/codemirror/addon/hint/show-hint.js',
            './js/codemirror/addon/hint/anyword-hint.js',
            './js/markdown-browser/markdown.js',
            './js/problems.js'
        ]
        let csss = [
            './js/codemirror/lib/codemirror.css',
            './js/codemirror/addon/hint/show-hint.css',
            './css/problems.css'
        ]
        let opts = getPageFrame(user, scripts, csss,{title:title})
        res.render(__dirname+"/web/problems.ejs",opts)
    })
    //post
    server.post('/login',(req, res)=>{
        res.send(login(req.body['username'], req.body['password']))
    })
    server.post('/register', (req, res)=>{
        res.send(register(req.body['username'], req.body['password'], req.body['question'], req.body['answer']))
    })
    //listen
    server.listen(port,()=>{
        console.log("Server start...")
    })
}

module.exports = {
    start
}