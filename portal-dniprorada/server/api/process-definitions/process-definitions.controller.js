'use strict';

var _ = require('lodash');
var http = require("http");
var config = require('../../config/environment');

// Get list of process-definitionss
exports.index = function(req, res) {
	var options = {
		host: config.activiti.host,
		port: config.activiti.port,
		path: '/' + config.activiti.rest + '/process-definitions',
		method: 'GET',
		headers: {
			'Content-Type': 'application/json',
			'Authorization': config.activiti.auth.basic
		}
	};
	getJSON(options,
		function(statusCode, result) {
			res.statusCode = statusCode;
			res.send(result);
		});
};

/**
 * getJSON:  REST get request returning JSON object(s)
 * @param options: http options object
 * @param callback: callback to pass the results JSON object(s) back
 */
function getJSON(options, onResult) {

	var prot = options.port == 443 ? https : http;
	var req = prot.request(options, function(res) {
		var output = '';
		res.setEncoding('utf8');

		res.on('data', function(chunk) {
			output += chunk;
		});

		res.on('end', function() {
			var obj = JSON.parse(output);
			onResult(res.statusCode, obj);
		});
	});

	req.on('error', function(err) {
		//res.send('error: ' + err.message);
		console.log(err.message);
		req.end();
	});

	req.end();
};