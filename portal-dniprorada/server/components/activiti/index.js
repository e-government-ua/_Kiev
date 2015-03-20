'use strict';

var _ = require('lodash');
var request = require("../request");
var config = require('../../config/environment');

var default_headers = {
	'Authorization': config.activiti.auth.basic
}

var getRequestOptions = function(options) {
	return {
		prot: config.activiti.prot,
		host: config.activiti.host,
		port: config.activiti.port,
		path: '/' + config.activiti.rest + '/' + options.path,
		method: options.method,
		headers: _.merge(options.headers, default_headers) || default_headers
	};
}

exports.get = function(options, onResult) {
	request.getJSON(getRequestOptions(options), onResult);
}

exports.post = function(options, data, onResult) {
	request.postJSON(getRequestOptions(options), data, onResult);
}