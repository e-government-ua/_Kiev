var request = require('request');
var NodeCache = require("node-cache" );
var uuid = require("uuid");

module.exports.index = function(options, callback) {
	var url = options.protocol + '://' + options.hostname + options.path + '/checked/data';

	// проверка доступа админа
	var adminCheckCallback = function(error, response, body){

		var adminKeysCache = new NodeCache();
		var cacheKey = 'admin-keys-map';

		var getAdminKeys = function () {
			var result = adminKeysCache.get(cacheKey);
			if (!result) {
				result = {};
				setAdminKeys(result);
			}
			return result;
		};

		var setAdminKeys = function (value) {
			adminKeysCache.set(cacheKey, value);
		};

		var generateAdminToken = function (inn) {
			var result = uuid.v1();
			var keys = getAdminKeys();
			keys[inn] = result;
			setAdminKeys(keys);
			return result;
		};

		// ИНН админов
		var aAdminInn = [
			'3119325858'
		];
		if (body.customer && aAdminInn.indexOf(body.customer.inn) > -1) {
			body.admin = {
				inn: body.customer.inn,
				token: generateAdminToken(body.customer.inn)
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