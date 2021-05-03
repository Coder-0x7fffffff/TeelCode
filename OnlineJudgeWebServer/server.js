const express = require("express")
const cookieParser = require("cookie-parser")
const bodyParser = require("body-parser")
const server = express()
const request = require("request")

let OJServer = "http://oj.xiami.space:8080/OnlineJudge/"

//request
function httpRequest(url, method, body){
    return new Promise((resolve, reject)=>{
        let option ={
            url: url,
            method: method,
            json: true,
            headers: {
                "content-type": "application/json",
            },
            body: body
        }
        request(option, function(error, response, body) {
            if(error != null){
                resolve(error)
                return
            }
            if (response.statusCode === 200) {
                resolve(body)
            }else{
                resolve({err:response.statusCode, body:body})
            }
        });
    });
}

//server contact
function checkUser(oldToken){
    let img = null
    let online=false
    let id = null
    let username=null
    let token=null
    let err=null
    let question=null
    let answer=null
    //get user info
    if(oldToken === "123456789"){
        online=true
        id="xiami"
        username="虾米"
        token="123456789"
        question="q"
        answer="a"
    }
    //return
    return {online:online, id:id, username:username, token: token, img:img , question:question, answer:answer, err:err}
}

function login(req, res){
    let body = req.body
    //get data
    let id = body['id']
    let password = body['password']
    //return define
    let img=null
    let online=false
    let username=null
    let token=null
    let err=null
    //login
    if(id === "xiami"){
        username="虾米"
        online=true
        token=123456789
        err=null
    }else err="请输入正确的账号密码"
    //return
    res.send({online:online, id:id, username:username, token: token, img:img, err:err})
}

function register(req, res){
    let body = req.body
    //get data
    let id = body['id']
    let password = body['password']
    let question = body['question']
    let answer = body['answer']
    //return define
    let err=null
    //register
    let requestData = {id: id, pwd: password, problem: question, answer: answer}
    console.log(requestData)
    let ret = httpRequest(OJServer + "Register", "POST", requestData).then(function (r) {
        console.log(r)
    })
    //return
    res.send({err:err})
}

function queryQuestion(req, res){
    let body = req.body
    //get data
    let problem_id = body['problem_id']
    //return define
    let err = null
    let problem = {
        problem_id:problem_id,
        problem_name:"",
        problem_detail:"# abc"
    }
    if(problem_id!==1){
        err = "没有题号为"+problem_id.toString()+"的题目"
    }
    //return
    res.send({err:err,problem:problem})
}

function queryComments(req, res){
    let body = req.body
    //get data
    let problem_id = body['problem_id']
    let page = body['page']
    let offset = body['offset']
    //return define
    let err = null

    let comment1 = {
        cid:1,
        username:"xiami",
        img:null,
        detail:"啦啦啦",
        time:"2020-10-15 15:33",
        replies:[
            {
                cid:3,
                username:"xiamigame",
                reply_username:"xiami",
                time:"2020-10-15 17:45",
                img:null,
                detail:"哈哈哈"
            },
            {
                cid:2,
                username:"xiamiking",
                reply_username:"xiami",
                time:"2020-10-15 16:45",
                img:null,
                detail:"嘻嘻嘻"
            }
        ]
    }
    let comment2 = {
        cid:4,
        username:"xiami",
        img:null,
        detail:"啦啦啦",
        time:"2020-10-15 15:33",
        replies:[
            {
                cid:6,
                username:"xiamigame",
                reply_username:"xiami",
                time:"2020-10-15 17:45",
                img:null,
                detail:"哈哈哈"
            },
            {
                cid:5,
                username:"xiamiking",
                reply_username:"xiami",
                time:"2020-10-15 16:45",
                img:null,
                detail:"嘻嘻嘻"
            }
        ]
    }
    let comment3 = {
        cid:7,
        username:"xiami",
        img:null,
        detail:"啦啦啦",
        time:"2020-10-15 15:33",
        replies:[]
    }

    let comments=[comment1,comment2,comment3]
    if(problem_id!==1){
        err = "没有题号为"+problem_id.toString()+"的题目"
    }
    //return
    res.send({err:err,comments:comments})
}

function reply(req, res){
    let body = req.body
    //get data

    //return
    res.send({err:null, cid:11})
}

function getUIInfo(req, res){
    let body = req.body
    //get date
    let data = {
        difficulties:[
                {
                    value:0,
                    name:"难度"
                },
                {
                    value:1,
                    name:"简单"
                },
                {
                    value:2,
                    name:"中等"
                },
                {
                    value:3,
                    name:"困难"
                },
            ],
        classes:[
            {
                cid:0,
                cname:"分类"
            },
            {
                cid:1,
                cname:"搜索"
            },
            {
                cid:1,
                cname:"动态规划"
            },
            {
                cid:1,
                cname:"贪心"
            }
        ],
        status:[
            {
                value:0,
                name:"完成状态"
            },
            {
                value:1,
                name:"完成"
            },
            {
                value:2,
                name:"未完成"
            }
        ]
    }
    res.send(data)
}

function queryQuestionList(req, res){
    let body = req.body
    //get data
    let page = body["page"]
    let offset = body["offset"]
    let difficulty = body["difficulty"]
    let clazz = body["class"]
    //get data
    let data = {
        problems:[
            {
                problem:{
                    pid:1,
                    pname:"最长上升子序列",
                    pdifficulty:1,
                    ppass:56,
                    psubmit:104
                },
                class:{
                    cid:2,
                    cname:"动态规划"
                },
                passed:1
            },
            {
                problem:{
                    pid:2,
                    pname:"最少跳跃次数",
                    pdifficulty:3,
                    ppass:12,
                    psubmit:105
                },
                class:{
                    cid:3,
                    cname:"贪心"
                },
                passed:0
            },
        ]
    }
    res.send(data)
}

function run(req, res){
    let body = req.body
    //get data
    let platform = body['platform']
    let code = body['code']
    //return
    res.send({err:null, result:platform+"\n"+code})
}

function submit(req, res){
    let body = req.body
    //get data
    let platform = body['platform']
    let code = body['code']
    //return
    res.send({err:null, result:platform+"\n"+code})
}

function userinfo(req, res){
    let body = req.body
    //get data
    let err=null
    //user
    let user = checkUser(req.cookies['token'])
    let id = user['id']
    let username = user['username']
    let img = user['img']
    let question = user['question']
    //problems
    let problems = []
    for(let i=0;i<5;i++){
        problems.push({pid:i+1,pname:"最短路径",pdiff:1})
    }
    //return
    res.send({user:{id:id, username:username, img:img, question:question}, problems:problems, err:err})
}

function changePassword(req, res){
    let body = req.body
    //get data
    let user = checkUser(req.cookies['token'])
    let answer = body['answer']
    let password = body['password']
    let err = null
    //judge and change
    if(user['answer'] === answer){
        //change password
    }else{
        err = "问题答案不正确"
    }
    //return
    res.send({err:err})
}

function changeName(req, res){
    let body = req.body
    //get data
    let err = null
    let newName = body['name']
    //change
    if(newName === ""){
        err = "用户名不能为空"
    }
    else if(newName === "repeat"){
        err = "用户名重复"
    }
    //return
    res.send({err:err})
}

function handleRequest(req, res, shouldOnline, callback){
    if(shouldOnline){
        //judge online
        let user = checkUser(req.cookies['token'])
        if(!user["online"]){
            let opts = getPageFrame(user, ['./js/login.js'], ['./css/login.css'], {})
            res.render(__dirname+"/web/login.ejs",opts)
            return
        }
    }
    callback(req, res)
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
        //'                <div class="list_item"><a href="/" class="list_item_text">竞赛</a></div>\n' +
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
        let user = checkUser(req.cookies['token'])
        if(!user["online"]){
            let opts = getPageFrame(user, ['./js/login.js'], ['./css/login.css'], {})
            res.render(__dirname+"/web/login.ejs",opts)
            return
        }
        let opts = getPageFrame(user, ['./js/index.js'], ['./css/index.css'], {})
        res.render(__dirname+"/web/index.ejs",opts)
    })
    server.get("/register",(req, res)=>{
        let user = checkUser(req.cookies['token'])
        let opts = getPageFrame(user, ['./js/register.js'], ['./css/register.css'], {})
        res.render(__dirname+"/web/register.ejs",opts)
    })
    server.get("/login",(req, res)=>{
        let user = checkUser(req.cookies['token'])
        let opts = getPageFrame(user, ['./js/login.js'], ['./css/login.css'], {})
        res.render(__dirname+"/web/login.ejs",opts)
    })
    server.get("/userinfo-*",(req, res)=>{
        //judge online
        let user = checkUser(req.cookies['token'])
        if(!user["online"]){
            let opts = getPageFrame(user, ['./js/login.js'], ['./css/login.css'], {})
            res.render(__dirname+"/web/login.ejs",opts)
            return
        }
        //render
        let opts = getPageFrame(user, ['./js/userinfo.js'], ['./css/userinfo.css'], {})
        res.render(__dirname+"/web/userinfo.ejs",opts)
    })
    server.get("/problems-*",(req, res)=>{
        //judge online
        let user = checkUser(req.cookies['token'])
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
        handleRequest(req, res, false, login)
    })
    server.post('/register', (req, res)=>{
        handleRequest(req, res, false, register)
    })
    server.post('/queryQuestion', (req, res)=>{
        handleRequest(req, res, true, queryQuestion)
    })
    server.post('/queryComments', (req, res)=>{
        handleRequest(req, res, true, queryComments)
    })
    server.post('/reply', (req, res)=>{
        handleRequest(req, res, true, reply)
    })
    server.post('/getUIInfo', (req, res) => {
        handleRequest(req, res, true, getUIInfo)
    })
    server.post('/queryQuestionList', (req, res)=>{
        handleRequest(req, res, true, queryQuestionList)
    })
    server.post('/run', (req, res)=>{
        handleRequest(req, res, true, run)
    })
    server.post('/submit', (req, res)=>{
        handleRequest(req, res, true, submit)
    })
    server.post('/userinfo', (req, res)=>{
        handleRequest(req, res, true, userinfo)
    })
    server.post('/changePassword', (req, res)=>{
        handleRequest(req, res, true, changePassword)
    })
    server.post('/changeName', (req, res)=>{
        handleRequest(req, res, true, changeName)
    })
    //listen
    server.listen(port,()=>{
        console.log("Server start...")
    })
}

module.exports = {
    start
}