'use strict';

var _ = require('lodash');
var request = require('request');
var url = require('url')
var parseString = require('xml2js').parseString;
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
			console.log(response + ' ' + body);
			parseString(body, function(err, userString) {
				var fio = userString.message.fio;
				var user = {
					fio: {
						firstName: fio[0].firstName[0],
						lastName: fio[0].lastName[0],
						middleName: fio[0].middleName[0]
					}
				}
				onResult(response.statusCode, user);
			});
		});
}