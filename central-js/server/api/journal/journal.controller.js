var request = require('request');

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

module.exports.getHistoryEvents = function(req, res) {

    var options = getOptions(req);
    var url = options.protocol
        + '://'
        + options.hostname
        + options.path
        + '/action/event/getHistoryEvents';

    var callback = function(error, response, body) {
        res.send(body);
        res.end();
    };

    return request.get({
        'url': url,
        'auth': {
            'username': options.username,
            'password': options.password
        },
        'qs': {
            'nID_Subject': req.session.subject.nID
        }
    }, callback);
};

module.exports.setHistoryEvent = function(req, res) {

    var options = getOptions(req);
    var url = options.protocol
        + '://'
        + options.hostname
        + options.path
        + '/action/event/setHistoryEvent';

    var callback = function(error, response, body) {
        console.log(body);
        res.send(body);
        res.end();
    };

//    oHistoryEvent:{
//        "nID": 445
//            ,"nID_Subject": 2
//            ,"nID_HistoryEventType":3
//            ,"sEventName":"" - консолидированное название события
//            ,"sMessage":""
//            ,"sDate":"2015-11-25 23:23:59.325"
//    }

    return request.post({
        'url': url,
        'auth': {
            'username': options.username,
            'password': options.password
        },
        'qs': {
            "nID": 445,
            "nID_Subject": 2,
            "nID_HistoryEventType": 3,
            "sEventName": "",
            "sMessage": "",
            "sDate": "2015-11-25 23:23:59.325"
        }
    }, callback);
};
