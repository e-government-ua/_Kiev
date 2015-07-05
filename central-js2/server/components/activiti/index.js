var options;
var request = require('request');
var _ = require('lodash');

module.exports.getConfigOptions = function () {

	if (options)
		return options;

	var config = require('../../config');
	var activiti = config.activiti;

	options = {
		protocol: activiti.protocol,
		hostname: activiti.hostname,
		port: activiti.port,
		path: activiti.path,
		username: activiti.username,
		password: activiti.password
	};

	return options;

};

module.exports.getRequestUrl = function (apiURL) {
	var options = this.getConfigOptions();
	return options.protocol + '://' + options.hostname + options.path + apiURL;
};

module.exports.buildRequest = function (req, apiURL, params) {
	return {
		'url': this.getRequestUrl(apiURL),
		'auth': this.getAuth(),
		'qs': _.extend(params, {nID_Subject: req.session.subject.nID})
	};
};

module.exports.getAuth = function () {
	var options = this.getConfigOptions();
	return {
		'username': options.username,
		'password': options.password
	};
};

module.exports.sendGetRequest = function (req, res, apiURL, params, callback) {
	var _callback = callback ? callback : function (error, response, body) {
		res.send(body);
		res.end();
	};
	var url = this.buildRequest(req, apiURL, params);
	return request(url, _callback);
};

module.exports.sendPostRequest = function (req, res, apiURL, params, callback) {
	var _callback = callback ? callback : function (error, response, body) {
		res.send(body);
		res.end();
	};
	var url = this.buildRequest(req, apiURL, params);
	return request.post(url, _callback);
};

