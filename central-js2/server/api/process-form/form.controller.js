var request = require('request');

module.exports.index = function(req, res) {
	var config = require('../../config');
	var activiti = config.activiti;

	var options = {
		protocol: activiti.protocol,
		hostname: activiti.hostname,
		port: activiti.port,
		path: activiti.path,
		username: activiti.username,
		password: activiti.password,
		params: {
			url: req.query.url || null
		}
	};

	var callback = function(error, response, body) {
		res.send(body);
		res.end();
	};

	return request.get({
		'url': options.params.url,
		'auth': {
			'username': options.username,
			'password': options.password
		}
	}, callback);
};

module.exports.submit = function(req, res) {
	var config = require('../../config');
	var activiti = config.activiti;

	var options = {
		protocol: activiti.protocol,
		hostname: activiti.hostname,
		port: activiti.port,
		path: activiti.path,
		username: activiti.username,
		password: activiti.password,
		formData: req.body
	};

	var callback = function(error, response, body) {
		res.send(body);
		res.end();
	};

	var properties = [];
	for(var i in options.formData.params) {
		properties.push({
			'id': i,
			'value': options.formData.params[i]
		});
	};
	
	return request.post({
		'url': options.formData.url || null,
		'auth': {
			'username': options.username,
			'password': options.password
		},
		'body': {
			'processDefinitionId': options.formData.processDefinitionId,
			'businessKey': "key",
			'properties': properties
		},
		'json': true
	}, callback);
};