var express = require('express');
var router = express.Router();

router.use(function(req, res, next) {
	process.env.NODE_TLS_REJECT_UNAUTHORIZED = "0";
	var processDefinitions = require('./index.controller');

	var config = require('../../config/environment');
	var activiti = config.activiti;

        console.log("req.query.nID_Server="+req.query.nID_Server);
        //activiti.getServerRegionURL(req.query.nID_Server);
        //getServerRegionURL

	var options = {
		protocol: activiti.protocol,
		hostname: activiti.hostname,
		port: activiti.port,
		path: activiti.path,
		username: activiti.username,
		password: activiti.password,
		params: {
			//url: req.query.url || null,
			nID_Server: req.query.nID_Server || null//,
			//latest: req.query.latest || null
			//size: req.query.size|| null
			//,size: 1000
		}
	};


	var callback = function(error, response, body) {
		res.send(body);
		res.end();
	}

	processDefinitions.index(options, callback);
});

module.exports = router;
