const {httpRequest} = require("./ServerUtil");

let OJServer = "http://localhost:8080/OnlineJudge/"

//server context
function setOJServer(url){
    OJServer = url
}

function getOJServer(){
    return OJServer
}

//server contact
async function checkUser(oldToken) {
    let online = false
    let img = null
    let id = null
    let username = null
    let sex = null
    let dscp = null
    let token = null
    let err = null
    let question = null
    let answer = null
    let isAdmin = false
    //get user info
    if (oldToken !== undefined && oldToken != null && oldToken !== "") {
        let requestData = {token: oldToken}
        let ret = await httpRequest(OJServer + "GetUid", "POST", requestData)
        let uid = ret['uid']
        if (uid === -1) {
            online = false
        }else{
            let requestData2 = {uid: uid}
            ret = await httpRequest(OJServer + "GetUserInfo", "POST", requestData2)
            if(ret != null){
                online = true
                id = ret['uid']
                username = ret['name']
                sex = ret['sex']
                dscp = ret['dscp']
                question = ret['question']
                answer = ret['answer']
                img = ret['img']
                token = ret['token']
                isAdmin = ret['isAdmin']
            }else{
                online = false
                err = "获取用户信息失败"
            }
        }
    }
    return {
        online: online,
        id: id,
        username: username,
        sex: sex,
        dscp: dscp,
        token: token,
        img: img,
        question: question,
        answer: answer,
        err: err,
        isAdmin: isAdmin
    }
}

function login(req, res){
    let body = req.body
    //get data
    let id = body['id']
    let password = body['password']
    //login
    let err=null
    //register
    let requestData = {id: id, pwd: password}
    httpRequest(OJServer + "Login", "POST", requestData).then(function (r) {
        let img=null
        let online=false
        let username=null
        let isAdmin = false
        if(r['result'] !== true){
            online = false
            err = "账号或密码错误"
            res.send({err:err,token:r['token'], online:online, id:id, username:username, img:img, isAdmin:isAdmin})
        }
        else{
            checkUser(r['token']).then(function (r){
                online = r['online']
                username = r['username']
                img = r['img']
                isAdmin = r['isAdmin']
                err = r['err']
                res.send({err:err,token:r['token'], online:online, id:id, username:username, img:img, isAdmin:isAdmin})
            })
        }
    })
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
    httpRequest(OJServer + "Register", "POST", requestData).then(function (r) {
        if(r['result'] !== true)
            err = "注册失败"
        res.send({err:err})
    })
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
        problem_detail:""
    }
    let requestData = {token: req.cookies['token'], id: problem_id}
    httpRequest(OJServer+"GetProblem", "POST",requestData).then(function (r){
        problem["problem_name"] = r['result']['problem']['pname']
        problem["problem_detail"] = r['result']['problem']['pdscp']
        res.send({err:err,problem:problem})
    })
}

function queryComments(req, res){
    let body = req.body
    //get data
    let problem_id = body['problem_id']
    let page = body['page']
    let offset = body['offset']
    //return define
    let err = null
    let requestData = {token: req.cookies['token'], problem_id:problem_id, page: page, offset: offset}
    httpRequest(OJServer + "GetComments","POST",requestData).then(function (r){
        res.send({err:err,comments:r["comments"]})
    })
}

function reply(req, res){
    let body = req.body
    //get data
    let requestData = {
        token: req.cookies['token'],
        problem_id: body["problem_id"],
        username: body["username"],
        parent_comment_id:body["parent_comment_id"],
        reply_username:body["reply_username"]==null?-1:body["reply_username"],
        detail:body['detail'],
        time:body['time']
    }
    httpRequest(OJServer+"AddComment","POST",requestData).then(function (r){
        res.send({err:null, cid:r['cid']})
    })
}

function getUIInfo(req, res){
    let body = req.body
    //get date
    let requestData = {token: req.cookies['token']}
    httpRequest(OJServer+"GetClassifications","POST", requestData).then(function (r){
        let classes = r['result']
        classes.unshift({
            cid: 0,
            cname: "全部"
        })
        let data = {
            difficulties:[
                {
                    value:0,
                    name:"全部"
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
            classes:classes,
            status:[
                {
                    value:-1,
                    name:"完成状态"
                },
                {
                    value:0,
                    name:"未完成"
                },
                {
                    value:1,
                    name:"完成"
                }
            ]
        }
        res.send(data)
    })
}

function queryQuestionList(req, res){
    let body = req.body
    //get data
    let page = body["page"]
    let offset = body["offset"]
    let difficulty = body["difficulty"]
    let clazz = body["class"]
    let status = body["status"]
    //get data
    let requestData = {token: req.cookies['token'], page: page, offset: offset, class: clazz, difficulty: difficulty, status: status}
    httpRequest(OJServer + "All", "POST", requestData).then(function (r){
        let problems = r['result']
        let data = {problems: problems}
        res.send(data)
    })
}

function run(req, res){
    let body = req.body
    //get data
    let platform = body['platform']
    let code = body['code']
    let language = 0
    switch(platform){
        case "c":
            language = 0
            break
        case "c++":
            language = 1
            break
        case "java":
            language = 2
            break
        case "python2":
            language = 3
            break
        case "python3":
            language = 4
            break
    }
    let input = body['input']
    let requestData = {token:req.cookies['token'], code:code, language:language, input:input}
    httpRequest(OJServer+"ExecuteCode","POST",requestData).then(function (r){
        res.send({err:null,result:r})
    })
}

function submit(req, res){
    let body = req.body
    //get data
    let platform = body['platform']
    let code = body['code']
    let id = body['id']
    let language = 0
    switch(platform){
        case "c":
            language = 0
            break
        case "c++":
            language = 1
            break
        case "java":
            language = 2
            break
        case "python2":
            language = 3
            break
        case "python3":
            language = 4
            break
    }
    let time = body['time']
    let requestData = {token:req.cookies['token'], id:id,code:code,language:language,time: time}
    httpRequest(OJServer+"Submit","POST",requestData).then(function (r){
        res.send({err:null,result:r})
    })
}

function userinfo(req, res){
    let body = req.body
    //get data
    let err=null
    //user
    checkUser(req.cookies['token']).then(function (user){
        let id = user['id']
        let username = user['username']
        let img = user['img']
        let question = user['question']
        //problems
        let problems = []//pid pname state
        /*record*/
        //return
        res.send({user:{id:id, username:username, img:img, question:question, dscp: user['dscp']}, problems:problems, err:err})
    })
}

function changePassword(req, res){
    let body = req.body
    //get data
    let requestData = {token:req.cookies['token'], answer: body['answer'], newpwd: body['password']}
    httpRequest(OJServer+"AlterPassword","POST", requestData).then(function (r){
        if(r[result] === true){
            res.send({err:null})
        }else{
            res.send({err:"答案错误"})
        }
    })
}

function changeName(req, res){
    let body = req.body
    checkUser(req.cookies['token']).then(function (user) {
        let err = null
        let newName = body['name']
        //change
        if(newName === ""){
            err = "用户名不能为空"
            res.send({err:err})
        }else{
            let requestData = {token: req.cookies['token'], name: newName, sex: user['sex'], dscp: user['dscp']}
            httpRequest(OJServer+"UpdateUser", "POST", requestData).then(function (r){
                if(r['result'] === true){
                    res.send({err:err})
                }else{
                    err = "用户名重复"
                    res.send({err:err})
                }
            })
        }
    })
}

function changeDscp(req, res){
    let body = req.body
    checkUser(req.cookies['token']).then(function (user) {
        let err = null
        let newDscp = body['dscp']
        //change
        let requestData = {token: req.cookies['token'], name: user['username'], sex: user['sex'], dscp: newDscp}
        httpRequest(OJServer+"UpdateUser", "POST", requestData).then(function (r){
            if(r['result'] === true){
                res.send({err:err})
            }else{
                err = "修改失败"
                res.send({err:err})
            }
        })
    })
}

function queryClasses(req, res){
    let body = req.body
    let requestData = {token: req.cookies['token']}
    httpRequest(OJServer+"GetClassifications","POST", requestData).then(function (r){
        let classes = r['result']
        res.send(classes)
    })
}

function addClass(req, res){
    let body = req.body
    let requestData = {token: req.cookies['token'], id: body['cid'], name: body['cname']}
    httpRequest(OJServer+"AddClassification","POST", requestData).then(function (r){
        if(r['result'] === true){
            res.send({err:null})
        }else{
            res.send({err:"添加失败"})
        }
    })
}

function getDifficulties(req, res){
    let body = req.body
    let requestData = {token: req.cookies['token']}
    httpRequest(OJServer+"GetDifficulties","POST", requestData).then(function (r){
        res.send(r)
    })
}

function getNextProblemId(req, res){
    let body = req.body
    let requestData = {token: req.cookies['token']}
    httpRequest(OJServer+"GetNextProblemId","POST", requestData).then(function (r){
        res.send(r)
    })
}

function addQuestion(req, res){
    let body = req.body
    let requestData = {
        token: req.cookies['token'],
        id:body['id'],
        name:body['name'],
        difficulty:body['difficulty'],
        dscp:body['dscp'],
        inputs:body['inputs'],
        outputs:body['outputs'],
        classification:body['classification']
    }
    httpRequest(OJServer+"AddProblem","POST", requestData).then(function (r){
        console.log(r)
        if(r["result"] === true){
            res.send({err:null})
        }else{
            res.send({err:"添加失败"})
        }
    })
}

function deleteClassification(req, res){
    let body = req.body
    let requestData = {token:req.cookies['token'], id: body['id']}
    httpRequest(OJServer + "DeleteClassification", "POST", requestData).then(function (r){
        if(r['result'] === true){
            res.send({err:null})
        }else{
            res.send({err:"删除失败"})
        }
    })
}

function deleteProblem(req, res){
    let body = req.body
    let requestData = {token:req.cookies['token'], id: body['id']}
    httpRequest(OJServer + "DeleteProblem", "POST", requestData).then(function (r){
        if(r['result'] === true){
            res.send({err:null})
        }else{
            res.send({err:"删除失败"})
        }
    })
}

function getSubmits(req, res){
    let body = req.body
    let requestData = {token:req.cookies['token'], uid: body['uid'], page:body['page'], offset:body['offset']}
    httpRequest(OJServer + "GetUserRecord", "POST", requestData).then(function (r){
        res.send({err:null,result:r})
    })
}

function handleRequest(req, res, shouldOnline, callback){
    if(shouldOnline){
        //judge online
        checkUser(req.cookies['token']).then(function(user){
            if(!user["online"]){
                let opts = getPageFrame(user, ['./js/login.js'], ['./css/login.css'], {})
                res.render(__dirname+"/web/login.ejs",opts)
                return
            }
            callback(req, res)
        })
    }else{
        callback(req, res)
    }
}

module.exports = {
    setOJServer,
    getOJServer,
    checkUser,
    login,
    register,
    queryQuestion,
    queryComments,
    reply,
    getUIInfo,
    queryQuestionList,
    run,
    submit,
    userinfo,
    changePassword,
    changeName,
    changeDscp,
    queryClasses,
    addClass,
    getDifficulties,
    getNextProblemId,
    addQuestion,
    deleteClassification,
    deleteProblem,
    handleRequest,
    getSubmits
}