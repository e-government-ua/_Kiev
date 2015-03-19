'use strict';

var _ = require('lodash');
var url = require('url')
var extIP = require('external-ip');
var config = require('../../config/environment');

var getIP = extIP({
    replace: true,
    services: ['http://ifconfig.co/x-real-ip', 'http://ifconfig.me/ip'],
    timeout: 400,
    getIP: 'parallel'
});

var prepareUrlForRedirect = function(ip, req, res) {
	var processDefinitionId = req.params.processDefinitionId;
	var back_redirect_uri = ip;
	var back_redirect = url.format({
		protocol: 'http',
		hostname: back_redirect_uri,
		pathname: '/process-form/' + processDefinitionId
	});
	var auth_redirect = url.format({
		protocol: 'https',
		hostname: config.bankid.host,
		pathname: config.bankid.path,
		query: {
			'response_type': 'code',
			'client_id': config.bankid.appid,
			'redirect_uri': back_redirect
		}
	})

	console.log(auth_redirect);

	res.statusCode = 200;
	res.send(auth_redirect);
}


// Get list of bankIDs
exports.auth = function(req, res) {
	var back_redirect_uri = req.headers['x-forwarded-for'] ||
		req.connection.remoteAddress ||
		req.socket.remoteAddress ||
		req.connection.socket.remoteAddress;
	if (back_redirect_uri == '127.0.0.1') {
		getIP(function(err, ip) {
			if (err) {
				// every service in the list has failed 
				throw err;
			}
			prepareUrlForRedirect(ip, req, res);
		});
	} else {
		prepareUrlForRedirect(back_redirect_uri, req, res);
	}
};