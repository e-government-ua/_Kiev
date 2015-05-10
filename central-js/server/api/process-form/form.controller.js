var request = require('request');

module.exports.get = function(options, callback) {
	return request.get({
		'url': options.params.url,
		'auth': {
			'username': options.username,
			'password': options.password
		}
	}, callback);
};

module.exports.post = function(options, callback) {
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