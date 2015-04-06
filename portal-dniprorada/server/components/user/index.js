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
				var customer = userString.message.customer;
				var user = {
					fio: {
						firstName: customer[0].firstName[0],
						lastName: customer[0].lastName[0],
						middleName: customer[0].middleName[0]
					}
				}
				onResult(response.statusCode, user);
			});
		});
}