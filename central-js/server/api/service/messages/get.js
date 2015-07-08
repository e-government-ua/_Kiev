var express = require('express');
var router = express.Router();

router.get('/getMessage/:nID', function (req, res, next) {
    var callback = function (error, response, body) {
        res.send(body);
        res.end();
    };
    var messageCntrl = require('./message.controller');
    messageCntrl.getMessageById(getOptions(req), callback);
});

router.get('/getMessages', function (req, res, next) {
    var callback = function (error, response, body) {
        res.send(body);
        res.end();
    };
    var messageCntrl = require('./message.controller');
    messageCntrl.getMessages(getOptions(req), callback);
});

function getOptions(req) {
    var config = require('../../../config/environment');
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

module.exports = router;
