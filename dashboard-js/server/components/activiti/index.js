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
		pathname: '/' + (options.root || config.activiti.rest) + '/' + options.path,
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
				onResult(null, response.statusCode, body, response.headers);
			} else {
				onResult(error, null, null);
			}
		});
}

exports.post = function(options, onResult, data) {
	request.post(_.merge(getRequestOptions(options), data ? {
			json: true,
			body: data
		} : {
			json: true
		}),
		function(error, response, body) {
			console.log(error + ' ' + response + ' ' + body);
			if (!error) {
				onResult(null, response.statusCode, body, response.headers);
			} else {
				onResult(error, null, null);
			}
		});
}