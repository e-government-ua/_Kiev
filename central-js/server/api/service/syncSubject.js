var express = require('express');
var router = express.Router();

router.use(function(req, res, next) {
    process.env.NODE_TLS_REJECT_UNAUTHORIZED = "0";
    var syncSubject = require('./syncSubject.controller');

    var config = require('../../config');
    var activiti = config.activiti;

    var options = {
        protocol: activiti.protocol,
        hostname: activiti.hostname,
        port: activiti.port,
        path: activiti.path,
        username: activiti.username,
        password: activiti.password,
        params: {
            sINN: req.query.sINN || null
        }
    };

    var callback = function(error, response, body) {
        res.send(body);
        res.end();
    };

    syncSubject.index(options, callback);
});

module.exports = router;
