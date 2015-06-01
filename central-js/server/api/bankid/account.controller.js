var request = require('request');

module.exports.index = function(options, callback) {
	var url = options.protocol + '://' + options.hostname + options.path + '/checked/data';
	return request.post({
		'url': url,
		'headers': {
			'Content-Type': 'application/json',
			'Authorization': 'Bearer ' + options.params.access_token + ', Id ' + options.params.client_id,
			'Accept': 'application/json'
		},
		json: true,
		body: {
			"type": "physical",
			"fields": ["firstName", "middleName", "lastName", "phone", "inn", "clId", "clIdText", "birthDay"],
			"documents": [{
				"type": "passport",
				"fields": ["series", "number", "issue", "dateIssue", "dateExpiration", "issueCountryIso2"]
			}]
		}
	}, callback);
};

module.exports.prepareScansRequest = function(options) {
	var url = options.protocol + '://' + options.hostname + options.path + '/checked/data';
	return request.post({
		'url': url,
		'headers': {
			'Content-Type': 'application/json',
			'Authorization': 'Bearer ' + options.params.access_token + ', Id ' + options.params.client_id,
			'Accept': 'application/json'
		},
		json: true,
		body: {
			"type": "physical",
			"fields": ["firstName", "middleName", "lastName", "phone", "inn", "clId", "clIdText", "birthDay"],
			"scans": [{
				"type": "passport",
				"fields": ["link", "dateCreate"]
			}]
		}
	});
};

module.exports.prepareScanContentRequest = function(options) {
	var o = {
		'url': options.url,
		'headers': {
			'Content-Type': 'application/json',
			'Authorization': 'Bearer ' + options.params.access_token + ', Id ' + options.params.client_id,
			'Accept': 'application/json'
		},
		json: true
	};
	return request.get(o);
};