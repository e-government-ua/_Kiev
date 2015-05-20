'use strict';

var _ = require('lodash');
var request = require('request');
var url = require('url')
var parseString = require('xml2js').parseString;
var config = require('../../config/environment');

request.debug = true;
//curl -k -H "Authorization: Bearer 44f21a7d-b94a-4800-99ad-b16f012deb18" 
//https://bankid.privatbank.ua/DataAccessService/checked/fio%3Faccess_token=44f21a7d-b94a-4800-99ad-b16f012deb18
exports.findUser = function(accessToken, onResult) {
	var userDataUrl = url.format({
		protocol: config.bankid.prot,
		hostname: config.bankid.host,
		pathname: config.bankid.user.path,
		query: {
			'client_id': config.bankid.appid
		}
	});

	var data = {
		"type": "physical",
		"fields": ["firstName", "middleName", "lastName", "phone", "inn", "clId", "clIdText", "birthDay"]
	};

	var options = {
		url: userDataUrl,
		headers: {
			'Authorization': 'Bearer ' + accessToken
		},
		json: true,
		body: data
	};

	request.post(options,
		function(error, response, result) {
			if (!error) {
				onResult(null, response.statusCode, result);
			} else {
				onResult(error, null, null);
			}
		});
}