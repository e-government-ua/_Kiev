var request = require('request');

var config = require('../../config/environment');

module.exports.index = function(req, res) {

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
	for(var id in options.formData.params) {
    var value = options.formData.params[id];
    if(id === 'nID_Subject'){
      value = req.session.subject.nID;
    }
		properties.push({
			'id': id,
			'value': value
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
