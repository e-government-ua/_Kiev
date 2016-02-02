var request = require('request');
var _ = require('lodash');

var oUtil = require('../../components/activiti');
var oAuth = require('../../components/admin');

function getOptions(req) {
    var config = require('../../config/environment');
    var activiti = config.activiti;

    return {
        protocol: activiti.protocol,
        hostname: activiti.hostname,
        port: activiti.port,
        path: activiti.path,
        username: activiti.username,
        password: activiti.password
    };
}

module.exports.post = function(req, res) {
    //options, callback
    var options = getOptions(req);
    var url = options.protocol + '://' + options.hostname + options.path + '/subject/message/setMessage';

    var data = req.body;

    var callback = function(error, response, body) {
        res.send(body);
        res.end();
    };

    return request.post({
        'url': url,
        'auth': {
            'username': options.username,
            'password': options.password
        },
        'qs': {
            'sMail': data.sMail,
            'sHead': data.sHead,
            'sBody': data.sBody
        }
    }, callback);
};

module.exports.get = function(req, res) {
    //options, callback
    var options = getOptions(req);
    var url = options.protocol + '://' + options.hostname + options.path + '/subject/message/getMessages';

    var callback = function(error, response, body) {
        res.send(body);
        res.end();
    };

    return request.get({
        'url': url,
        'auth': {
            'username': options.username,
            'password': options.password
        }
    }, callback);
};

module.exports.findFeedback = function(req, res){

  var options = getOptions(req);
  var url = options.protocol + '://'
    + options.hostname
    + options.path
    + '/subject/message/getMessageFeedbackExtended?sID_Order='
    + req.param('sID_Order')
    + '&sToken='+req.param('sToken');

  var callback = function(error, response, body) {
    res.send(body);
    res.end();
  };

  return request.get({
    'url': url,
    'auth': {
      'username': options.username,
      'password': options.password
    }
  }, callback);
};

module.exports.postFeedback = function(req, res){
  var options = getOptions(req);
  var url = options.protocol + '://' + options.hostname + options.path + '/subject/message/setMessageFeedbackExtended';

  var data = req.body;

  var callback = function(error, response, body) {
    res.send(body);
    res.end();
  };

  return request.post({
    'url': url,
    'auth': {
      'username': options.username,
      'password': options.password
    },
    'qs': {
      'sID_Order': data.sID_Order,
      'sToken': data.sToken,
      'sBody': data.sBody
    }
  }, callback);
};

module.exports.postServiceMessage = function(req, res){
  var oData = req.body;
  var sToken = oData.sToken;
  if (!!req.session.subject.nID || oUtil.bExist(sToken)){
    var nID_Subject = (oUtil.bExist(req.session) && req.session.hasOwnProperty('subject') && req.session.subject.hasOwnProperty('nID')) ? req.session.subject.nID : null;
  //if (!!req.session.subject.nID){
  //  var nID_Subject = req.session.subject.nID;
    var options = getOptions(req);
    var sURL = options.protocol + '://' + options.hostname + options.path + '/subject/message/setServiceMessage';

    var bAdmin=false;
    if(req.session&&req.session.subject){
        bAdmin=oAuth.isAdminInn(req.session.subject.sID);
        console.log("[searchOrderBySID]:bAdmin="+bAdmin);
    }

    var callback = function(error, response, body) {
      res.send(body);
      res.end();
    };


    var oDateNew = {
        'sID_Order': oData.sID_Order,
        'sBody': oData.sBody,
        'nID_SubjectMessageType' : 8
        , 'bAuth': !bAdmin
    };
    if(oUtil.bExist(sToken)){
        oDateNew = _.extend(oDateNew,{'sToken': sToken});
    }
    if(oUtil.bExist(nID_Subject)){
        oDateNew = _.extend(oDateNew,{'nID_Subject': nID_Subject});
    }

    return request.post({
      'url': sURL,
      'auth': {
        'username': options.username,
        'password': options.password
      },
      'qs': oDateNew/* {
        'sID_Order': oData.sID_Order,
        'sBody': oData.sBody,
        'nID_SubjectMessageType' : 8,
        'nID_Subject' : nID_Subject
      }*/
    }, callback);

  } else {
    res.end();
  }

};

module.exports.findServiceMessages = function(req, res){
  var sToken = req.param('sToken');
  if (!!req.session.subject.nID || oUtil.bExist(sToken)){
    //var nID_Subject = (req.session && req.session!==null && req.session.hasOwnProperty('subject') && req.session.subject.hasOwnProperty('nID')) ? req.session.subject.nID : null;
    var nID_Subject = (oUtil.bExist(req.session) && req.session.hasOwnProperty('subject') && req.session.subject.hasOwnProperty('nID')) ? req.session.subject.nID : null;
    
    //var nID_Subject = req.session.subject.nID;

    var bAdmin=false;
    if(req.session&&req.session.subject){
        bAdmin=oAuth.isAdminInn(req.session.subject.sID);
        console.log("[searchOrderBySID]:bAdmin="+bAdmin);
    }

    var options = getOptions(req);
    var url = options.protocol + '://'
      + options.hostname
      + options.path
      + '/subject/message/getServiceMessages?'
      + 'sID_Order=' + req.param('sID_Order')
      //+ '&nID_Subject=' + nID_Subject
      + (oUtil.bExist(nID_Subject)?'&nID_Subject=' + nID_Subject:"") 
      + (oUtil.bExist(sToken)?'&sToken=' + sToken:"") 
      + '&bAuth='+(!bAdmin)
      ;

    var callback = function(error, response, body) {
      var resout = {
        messages : JSON.parse(body)
        //,nID_Subject: req.session.subject.nID
      };
      res.send(resout);
      res.end();
    };

    return request.get({
      'url': url,
      'auth': {
        'username': options.username,
        'password': options.password
      }
    }, callback);
  } else {
    res.end();
  }

};
