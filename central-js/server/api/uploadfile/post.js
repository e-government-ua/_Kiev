var express = require('express');
var router = express.Router();
var request = require('request');
var FormData = require('form-data');

router.use(function(req, res, next) {
	var config = require('../../config.js');
	var activiti = config.activiti;

	var options = {
		'url': req.query.url,
		'auth': {
			'username': activiti.username,
			'password': activiti.password,
		},
		multipart: [{
			'content-type': 'application/json',
			// 'file': req.files.file, ?????
			'body': req.body
		}]
	};

	var r = request.post(options);

	request.debug = config.server.debug;
	req.pipe(r).pipe(res);
});

module.exports = router;