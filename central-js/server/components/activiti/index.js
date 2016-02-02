var options;
var request = require('request');
var _ = require('lodash');

var NodeCache = require("node-cache" );

var aServerCache = new NodeCache();


module.exports.asErrorMessages = function(asMessageDefault, oData, onCheckMessage){
        /*var oData = {"s":"asasas"};
        $.extend(oData,{sDTat:"dddddd"});
        var a=[];
        a=a.concat(["1"]);*/ //{"code":"SYSTEM_ERR","message":null}
        if(!asMessageDefault || asMessageDefault===null){
            asMessageDefault=[];
        }
        var asMessage = [];
        try{
            if (!oData) {
                asMessage=asMessage.concat(['Пуста відповідь на запит!']);
            }else{
                var n=0;
                if (oData.hasOwnProperty('message')) {
                    if(onCheckMessage!==null){
                        var asMessageNew = onCheckMessage(oData.message);
                        if(asMessageNew!==null){
                            asMessage=asMessage.concat(asMessageNew);
                        }else{
                            asMessage=asMessage.concat(['Message: '+oData.message]);
                        }
                    }else{
                        asMessage=asMessage.concat(['Message: '+oData.message]);
                    }
                    oData.message=null;
                    n++;
                }
                if (oData.hasOwnProperty('code')) {
                    asMessage=asMessage.concat(['Code: '+oData.code]);
                    oData.code=null;
                    n++;
                }
                if(n<2){
                    asMessage=asMessage.concat(['oData: '+oData]);
                }
            }
        }catch(sError){
            asMessage=asMessage.concat(['Невідома помилка!','oData: '+oData,'sError: '+sError]);
        }
        if(asMessage.length>0){
            asMessage=asMessageDefault.concat(asMessage);
            console.error('[asErrorMessages]:asMessage='+asMessage);
        }
        return asMessage;
};

module.exports.bExist = function(oValue){
        return oValue && oValue !== null && oValue !== undefined && !!oValue;
};

module.exports.bExistNotSpace = function(oValue){
        return bExist(oValue) && oValue.trim()!=="";
};


module.exports.getConfigOptions = function () {

	if (options)
		return options;

	var config = require('../../config/environment');
	var activiti = config.activiti;

	options = {
		protocol: activiti.protocol,
		hostname: activiti.hostname,
		port: activiti.port,
		path: activiti.path,
		username: activiti.username,
		password: activiti.password
	};

	return options;
};

module.exports.getRequestUrl = function (apiURL, sHost) {
	var options = this.getConfigOptions();
	return (sHost!==null && sHost !== undefined ? sHost : options.protocol + '://' + options.hostname + options.path) + apiURL;
};

module.exports.buildRequest = function (req, apiURL, params, sHost) {
  var sURL = this.getRequestUrl(apiURL, sHost);
	return {
		'url': sURL,
		'auth': this.getAuth(),
		'qs': _.extend(params, {nID_Subject: req.session.subject ? req.session.subject.nID : null})
	};
};

module.exports.getAuth = function () {
	var options = this.getConfigOptions();
	return {
		'username': options.username,
		'password': options.password
	};
};

module.exports.getDefaultCallback = function (res){
  return function (error, response, body) {
    if (error) {
      res.statusCode = 500;
      res.send(error);
    } else {
      res.statusCode = response.statusCode;
      res.send(body);
    }
    res.end();
  }
};

module.exports.sendGetRequest = function (req, res, apiURL, params, callback, sHost) {
	var _callback = callback ? callback : this.getDefaultCallback(res);
	var url = this.buildRequest(req, apiURL, params, sHost);
	return request(url, _callback);
};

module.exports.sendPostRequest = function (req, res, apiURL, params, callback, sHost) {
	var apiReq = this.buildRequest(req, apiURL, params, sHost);
  return this.executePostRequest(apiReq, res, callback);
};

module.exports.executePostRequest = function(apiReq, res, callback) {
  var _callback = callback ? callback : this.getDefaultCallback(res);
  return request.post(apiReq, _callback);
};

module.exports.sendDeleteRequest = function (req, res, apiURL, params, callback, sHost) {
  var _callback = callback ? callback : this.getDefaultCallback(res);
  var url = this.buildRequest(req, apiURL, params, sHost);
  return request.del(url, _callback);
};

/*
module.exports.buildRequestFromServer = function (sPagePath, oParams, sHost) {
  var sURL = this.getRequestUrl(sPagePath, sHost);
	return {
		'url': sURL,
		'auth': this.getAuth(),
		'qs': oParams
	};
};


module.exports.getRegionURL = function (res, nID) {
	//var _callback = callback ? callback : this.getDefaultCallback(res);
	//var url = this.buildRequest(req, apiURL, params, sHost);
	//return request(url, _callback);
	var sURL = this.buildRequestFromServer('/subject/getServer', {nID: nID});
	var oServer = request(sURL, this.getDefaultCallback(res));
        //var oServer = this.sendGetRequest(req, res, '/subject/getServer', _.extend(req.query, {nID: nID}));
        return oServer !== null ? oServer.sURL : null;
};
*/

module.exports.getServerRegionHost = function (nID_Server, fCompleted) {
            this.getServerRegion(nID_Server, function(oServer){
                console.log("[getServerRegionHost]oServer="+oServer);
                var sHost=null;
                if(oServer && oServer!==null){
                    sHost = oServer.sURL;
                }
                console.log("[getServerRegionHost]sHost="+sHost);
                //return sHost;
                if(fCompleted!==null){
                    fCompleted(sHost);
                }
            });
};

module.exports.getServerRegion = function (nID_Server, fCompleted) {
    var options = this.getConfigOptions();
    console.log("[getServerRegion]:nID_Server="+nID_Server);
    var sResourcePath = '/subject/getServer?nID='+nID_Server;
    console.log("[getServerRegion]:sResourcePath="+sResourcePath);
    var sURL = options.protocol+'://'+options.hostname+options.path+sResourcePath;
    console.log("[getServerRegion]:sURL="+sURL);
    var oServerCache = aServerCache.get(sResourcePath) || null;
    //var structureValue = getStructureServer(nID_Server);
    if(oServerCache&&oServerCache!==null) {
        console.log("[getServerRegion]:oServerCache="+oServerCache);
        //fCompleted
        if(fCompleted!==null){
            fCompleted(oServerCache);
        }//return oServerCache;
    }else{
        process.env.NODE_TLS_REJECT_UNAUTHORIZED = "0";
        return request.get({
                'url': sURL,
                'auth': {
                        'username': options.username,
                        'password': options.password
                }
        }, function(error, response, body) {
            console.log("[getServerRegion]:error="+error+",body="+body+",response="+response);
            var oServer = JSON.parse(body);
            aServerCache.set(sResourcePath, oServer, 86400); //'api/places/server?nID='+nID_Server
            //console.log("body="+body);
            if(fCompleted!==null){
                fCompleted(oServer);
            }//return oServerCache;
            //return JSON.parse(body);
        });        
    }
};
