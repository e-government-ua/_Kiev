'use strict';

var activiti = require('../../components/activiti');
var config = require('../../config/environment');

var guid = function guid() {
	function s4() {
		return Math.floor((1 + Math.random()) * 0x10000)
			.toString(16)
			.substring(1);
	}
	return s4() + s4() + '-' + s4() + '-' + s4() + '-' +
		s4() + '-' + s4() + s4() + s4();
};

var expiresUserInMs = function() {
	return new Date(Date.now() + 1000 * 60 * 60);
}

exports.ping = function(req, res) {
	res.send();
}

exports.authenticate = function(req, res) {
	var user = req.body;
	var options = {
		path: 'identity/users/' + user.login
	};

	activiti.get(options, function(error, statusCode, result) {
		res.statusCode = statusCode;
		//TODO call check password
		if (error) {
			res.send(error);
		} else {
			res.cookie('user', JSON.stringify(result), {
				expires: expiresUserInMs()
			});
			res.cookie('session', guid(), {
				expires: expiresUserInMs()
			});
			res.cookie('sessionSettings', JSON.stringify({
				sessionIdle : 60 * 8,//sec show warning
				timeOut : 60 * 2, //sec close session after warning
				interval : 60 * 10 //sec update session
			}), {
				expires: expiresUserInMs()
			});
			res.send(result);
		}
	});
}