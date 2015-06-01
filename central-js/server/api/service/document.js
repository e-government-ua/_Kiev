var express = require('express');
var router = express.Router();
router.use(function(req, res, next) {
    process.env.NODE_TLS_REJECT_UNAUTHORIZED = "0";
    console.log('hello3');
    var documents = require('./documents.controller');
    var callback = function(error, response, body) {
    console.log('hello4');
        res.send(body);
        res.end();
    };

    documents.index(getOptions(req), callback);
});

router.get('/documents/:nID', function(req, res, next) {
  var callback = function(error, response, body) {
    console.log('hello1');
    res.send(body);
    res.end();
  };

  console.log('hello2');
  var documents = require('./documents.controller');
  documents.getDocument(req.params.nID, getOptions(req), callback);
});

function getOptions (req) {
    var config = require('../../config');
    var activiti = config.activiti;
    return {
        protocol: activiti.protocol,
        hostname: activiti.hostname,
        port: activiti.port,
        path: activiti.path,
        username: activiti.username,
        password: activiti.password,
        params: {
            sID_Subject: req.query.sID_Subject || null
        }
    };
}

module.exports = router;