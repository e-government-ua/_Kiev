'use strict';

var _ = require('lodash');
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
	var headers = options.headers;
	if (config.activiti.auth.basic) {
		headers = _.merge(options.headers, default_headers) || default_headers;
	}
	return {
		url: getRequestURL(options),
		headers: headers
	};
};


exports.get = function(options, onResult) {
	request
		.get(getRequestOptions(options), function(error, response, body) {
			console.log(error + ' ' + response + ' ' + body);
			if (!error) {
				onResult(null, response.statusCode, body);
			} else {
				onResult(error, null, null);
			}
		});
}

exports.post = function(options, data, onResult) {
	request.post(_.merge(getRequestOptions(options), {
			json: true,
			body: data
		}),
		function(error, response, result) {
			console.log(error + ' ' + response + ' ' + result);
			if (!error) {
				onResult(null, response.statusCode, result);
			} else {
				onResult(error, null, null);
			}
		});
}