var request = require('request');

module.exports.index = function(options, callback) {
	var url = options.protocol+'://'+options.hostname+options.path+'/checked/data';

	return request.post({
		'url': url,
		'headers': {
			'Content-Type': 'application/json',
			'Authorization': 'Bearer ' + options.params.access_token,
			'Accept': 'application/json'
		},
		'qs': {
			'client_id': options.params.client_id
		},
		json: true,
		body: {
			"type": "physical",
			"fields": ["firstName", "middleName", "lastName", "phone", "inn", "clId", "clIdText", "birthDay"]
		}
	}, callback);
};