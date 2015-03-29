'use strict';

var _ = require('lodash');
var request = require("../request");
var config = require('../../config/environment');
var request = require('request');
var url = require('url');

var default_headers = {
	'Authorization': config.activiti.auth.basic
}

var getRequestURL = function(options) {
	return url.format({
		protocol: config.activiti.prot,
		hostname: config.activiti.host,
		port: config.activiti.port,
		pathname: '/' + config.activiti.rest + '/' + options.path,
		query: options.query
	});
}

var getRequestOptions = function(options) {
	return {
		url: getRequestURL(options),
		headers: _.merge(options.headers, default_headers) || default_headers
	};
};


exports.get = function(options, onResult) {
	request
		.get(getRequestOptions(options), function(error, response, body) {
			console.log(response + ' ' + body) // 200 
			onResult(response.statusCode, body);
		});
}

exports.post = function(options, data, onResult) {
	request.postJSON(getRequestOptions(options), data, onResult);
}