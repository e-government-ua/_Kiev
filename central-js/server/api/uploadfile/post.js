var express = require('express');
var router = express.Router();
var request = require('request');

var config = require('../../config/environment');
var proxy = require('../../components/proxy');

var activiti = require('../../components/activiti');

router.use(function(req, res, next) {
    
    //return './api/uploadfile?nID_Server=' + oServiceData.nID_Server + 'service/object/file/upload_file_to_redis';
    var nID_Server = req.query.nID_Server;
    activiti.getServerRegionHost(nID_Server, function(sHost){
        var sURL = sHost+'/service/object/file/upload_file_to_redis';
        console.log("sURL="+sURL);
        proxy.upload(req, res, sURL);//req.query.url
    });
});

module.exports = router;
