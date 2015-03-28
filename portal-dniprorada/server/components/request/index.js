/**
 * http(s) requests
 */

'use strict';

var config = require('../../config/environment');
var _ = require('lodash');
var url = require('url')
var extIP = require('external-ip');

var getIP = extIP({
	replace: true,
	services: ['http://ifconfig.co/x-real-ip', 'http://ifconfig.me/ip'],
	timeout: 400,
	getIP: 'parallel'
});

var createProt = function(options) {
	var prot;
	if (options.prot) {
		prot = require(options.prot);
	} else {
		prot = options.port === 443 ? require('https') : require("http");
	}
	return prot;
}

exports.getProtocol = function() {
	return config.ssl.port ? 'https' : 'http';
}

exports.getBackRedirectURL = function(path) {
	var back_redirect_uri 
		= (config.host) + ':' + (config.port);
	return url.format({
		protocol: module.exports.getProtocol(),
		host: back_redirect_uri,
		pathname: path
	});
}

exports.getAsynchBackRedirectURL = function(path, onResult) {
	var back_redirect_uri 
		= (config.host) + ':' + (config.port);

	if (back_redirect_uri) {
		onResult(url.format({
			protocol: module.exports.getProtocol(),
			host: back_redirect_uri,
			pathname: path
		}));
	} else {
		getIP(function(err, ip) {
			if (err) {
				// every service in the list has failed 
				throw err;
			}
			onResult(url.format({
				protocol: module.exports.getProtocol(),
				host: ip,
				pathname: path
			}));
		});
	}
}

exports.postJSON = function(options, data, onResult) {
	var prot = createProt(options);
	var dataString = JSON.stringify(data);
	var default_headers = {
		'Content-Type': 'application/json',
		'Content-Length': dataString.length
	}
	options.headers =
		_.merge(options.headers, default_headers) || default_headers

	var req = prot.request(options, function(res) {
		var responseString = '';
		res.setEncoding('utf8');

		res.on('data', function(chunk) {
			responseString += chunk;
		});

		res.on('end', function() {
			onResult(res.statusCode, responseString);
		});

		req.on('error', function(err) {
			//res.send('error: ' + err.message);
			console.log(err.message);
			onResult(res.statusCode);
		});
	});

	req.write(dataString);
	req.end();
}

/**
 * getJSON:  REST get request returning JSON object(s)
 * @param options: http options object
 * @param callback: callback to pass the results JSON object(s) back
 */
exports.getJSON = function(options, onResult) {
	var prot = createProt(options);
	var req = prot.request(options, function(res) {
		var responseString = '';
		res.setEncoding('utf8');

		res.on('data', function(chunk) {
			responseString += chunk;
		});

		res.on('end', function() {
			onResult(res.statusCode, responseString);
		});

		req.on('error', function(err) {
			//res.send('error: ' + err.message);
			console.log(err.message);
			onResult(res.statusCode);
		});

	});

	req.end();
};