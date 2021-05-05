const express = require("express")
const fs = require("fs")
const cookieParser = require("cookie-parser")
const bodyParser = require("body-parser")
const {getPageFrame} = require("./PageConstructor");
const {setOJServer, getOJServer, checkUser, login, register, queryQuestion, queryComments, reply, getUIInfo, queryQuestionList, run, submit, userinfo, changePassword, changeName, changeDscp, queryClasses, addClass, getDifficulties, getNextProblemId, addQuestion, deleteClassification, deleteProblem, handleRequest, getSubmits} = require("./HTTPHandler");

const server = express()
let port = 80

function init(configPath = "./server.config"){
    let contextBuffer = fs.readFileSync(configPath)
    let context = JSON.parse(contextBuffer.toString())
    let keys = Object.keys(context)
    for(let i = 0;i<keys.length;i++){
        switch(keys[i]){
            case "OJServer":
                setOJServer(context[keys[i]])
                break
            case "WebServerPort":
                port = context[keys[i]]
                break
            default:
                console.log("Useless option: " + keys[i] + " = " + context[keys[i]])
                break
        }
    }
}

function start(port){
    //static
    server.use(cookieParser())
    server.use(bodyParser.urlencoded({extended: false}));
    server.use(bodyParser.json());
    server.use(express.static(__dirname + "/web"))
    //get
    server.get("/", async (req, res)=>{
        //judge online
        let user = await checkUser(req.cookies['token'])
        if(!user["online"]){
            let opts = getPageFrame(user, ['./js/login.js'], ['./css/login.css'], {})
            res.render(__dirname+"/web/login.ejs",opts)
            return
        }
        let opts = getPageFrame(user, ['./js/index.js'], ['./css/index.css'], {})
        res.render(__dirname+"/web/index.ejs",opts)
    })
    server.get("/register",async (req, res)=>{
        let user = await checkUser(req.cookies['token'])
        let opts = getPageFrame(user, ['./js/register.js'], ['./css/register.css'], {})
        res.render(__dirname+"/web/register.ejs",opts)
    })
    server.get("/login",async (req, res)=>{
        let user = checkUser(req.cookies['token'])
        let opts = getPageFrame(user, ['./js/login.js'], ['./css/login.css'], {})
        res.render(__dirname+"/web/login.ejs",opts)
    })
    server.get("/userinfo-*",async (req, res)=>{
        //judge online
        let user = await checkUser(req.cookies['token'])
        if(!user["online"]){
            let opts = getPageFrame(user, ['./js/login.js'], ['./css/login.css'], {})
            res.render(__dirname+"/web/login.ejs",opts)
            return
        }
        //render
        let opts = getPageFrame(user, ['./js/userinfo.js'], ['./css/userinfo.css'], {})
        res.render(__dirname+"/web/userinfo.ejs",opts)
    })
    server.get("/problems-*", async (req, res)=>{
        //judge online
        let user = await checkUser(req.cookies['token'])
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
    server.post('/changeDscp', (req, res)=>{
        handleRequest(req, res, true, changeDscp)
    })
    server.post('/queryClasses', (req, res)=>{
        handleRequest(req, res, true, queryClasses)
    })
    server.post('/addClass', (req, res)=>{
        handleRequest(req, res, true, addClass)
    })
    server.post('/getDifficulties', (req, res)=>{
        handleRequest(req, res, true, getDifficulties)
    })
    server.post('/getNextProblemId', (req, res)=>{
        handleRequest(req, res, true, getNextProblemId)
    })
    server.post('/addQuestion', (req, res)=>{
        handleRequest(req, res, true, addQuestion)
    })
    server.post('/deleteClassification', (req, res)=>{
        handleRequest(req, res, true, deleteClassification)
    })
    server.post('/deleteProblem', (req, res)=>{
        handleRequest(req, res, true, deleteProblem)
    })
    server.post('/getSubmits', (req, res)=>{
        handleRequest(req, res, true, getSubmits)
    })
    //listen
    server.listen(port,()=>{
        console.log("Server start...")
    })
}

module.exports = {
    init,
    start
}