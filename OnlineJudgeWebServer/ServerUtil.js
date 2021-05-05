const request = require("request")

//transfer
function objToQuery(obj){
    let keys = Object.keys(obj)
    let query = ''
    for(let i = 0;i<keys.length;i++){
        query += keys[i]+"="+encodeURIComponent(obj[keys[i]])+((i<keys.length-1)?"&":"")
    }
    return query
}
//request
function httpRequest(url, method, body){
    return new Promise((resolve, reject)=>{
        let query = objToQuery(body)
        let option ={
            url: url,
            method: method,
            json: true,
            headers: {
                //"Content-Length": query.length,
                "content-type": "application/x-www-form-urlencoded",
            },
            body: query
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

module.exports = {
    objToQuery,
    httpRequest
}