'use strict';

var _ = require('lodash');
var http = require("http");

// Get list of process-definitionss
exports.index = function(req, res) {
	var options = {
		host: 'localhost',
		port: 8080,
		path: '/activiti-rest/service/repository/process-definitions',
		method: 'GET',
		headers: {
			'Content-Type': 'application/json',
			'Authorization': 'Basic a2VybWl0Omtlcm1pdA=='
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
	});

	req.end();
};