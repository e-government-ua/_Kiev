'use strict';

var _ = require('lodash');
var request = require("../request");
var config = require('../../config/environment');

exports.rest = function(options, onResult) {
	var default_headers = {
		'Content-Type': 'application/json',
		'Authorization': config.activiti.auth.basic
	}
	var request_options = {
		host: config.activiti.host,
		port: config.activiti.port,
		path: '/' + config.activiti.rest + '/' + options.path,
		method: options.method,
		headers: _.merge(options.headers, default_headers) || default_headers
	};
	request.getJSON(request_options, onResult);
}