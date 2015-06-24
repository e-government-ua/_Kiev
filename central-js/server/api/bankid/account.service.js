var request = require('request');
var Admin = require('../../components/admin');

module.exports.index = function(options, callback) {
	var url = options.protocol + '://' + options.hostname + options.path + '/checked/data';
	
	var adminCheckCallback = function(error, response, body){
		if (body.customer && Admin.isAdminInn(body.customer.inn)) {
			body.admin = {
				inn: body.customer.inn,
				token: Admin.generateAdminToken()
			};
		}
		callback(error, response, body);
	};

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
	}, adminCheckCallback);
};

module.exports.scansRequest = function(options, callback) {
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
			}, {
				"type": "zpassport",
				"fields": ["link", "dateCreate"]
			}]
		}
	}, callback);
};

module.exports.prepareScanContentRequest = function(options) {
	var o = {
		'url': options.url,
		'headers': {
			'Authorization': 'Bearer ' + options.params.access_token + ', Id ' + options.params.client_id
		}
	};
	return request.get(o);
};