var express = require('express');
var router = express.Router();

router.post(function (req, res, next) {
    process.env.NODE_TLS_REJECT_UNAUTHORIZED = "0";
    var messageCntrl = require('./message.controller');
    var callback = function (error, response, body) {
        res.send(body);
        res.end();
    };
    console.log('router.post')
    messageCntrl.setMessage(getOptions(req), res, callback);
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

