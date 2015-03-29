'use strict';

var _ = require('lodash');
var request = require('request');
var url = require('url')
var config = require('../../config/environment');

//curl -k -H "Authorization: Bearer 44f21a7d-b94a-4800-99ad-b16f012deb18" 
//https://bankid.privatbank.ua/DataAccessService/checked/fio%3Faccess_token=44f21a7d-b94a-4800-99ad-b16f012deb18
exports.findUser = function(accessToken, onResult) {
	var userDataUrl = url.format({
		protocol: config.bankid.prot,
		hostname: config.bankid.host,
		pathname: config.bankid.user.path,
		query: {
			'access_token': accessToken
		}
	});
	var options = {
		url: userDataUrl,
		headers: {
			'Authorization': 'Bearer ' + accessToken
		}
	};
	request
		.get(options, function(error, response, body) {
			console.log(response + ' ' + body) // 200 
			onResult(response.statusCode, body);
		});
}