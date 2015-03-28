'use strict';

var _ = require('lodash');
var request = require("../request");
var config = require('../../config/environment');

// getRequestOptions()
// Object
// host: "bankid.privatbank.ua"
// method: "GET"
// path: "checked/fio?access_token=undefined"
// prot: "https"
// __proto__: Object
// https://bankid.privatbank.ua/DataAccessService/checked/fio?access_token=caef896e-3d84-4c16-8a2b-f4f2264db6b1

var getRequestOptions = function(accessToken) {
	return {
		prot: config.bankid.prot,
		host: config.bankid.host,
		path: config.bankid.user.path + '?access_token=' + accessToken,
		method: 'GET'
	};
}

exports.findUser = function(accessToken, onResult) {
	request.getJSON(getRequestOptions(accessToken), onResult);
}