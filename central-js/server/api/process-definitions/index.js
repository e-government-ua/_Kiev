var express = require('express');
var router = express.Router();

router.use(function(req, res, next) {
	process.env.NODE_TLS_REJECT_UNAUTHORIZED = "0";
	var processDefinitions = require('./index.controller');
	
	var options = {
		protocol: 'https',
		hostname: '52.17.126.64',
		port: 8080,
		path: '/wf-dniprorada/service',
		method: 'GET',
		username: 'activiti-master',
		password: 'UjhtJnEvf!'
	};
	
	var callback = function(error, response, body) {
		res.send(body);
		res.end();
	}
	
	processDefinitions.index(options, callback);
});

module.exports = router;