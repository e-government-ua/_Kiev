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

exports.logout = function(req, res) {
	var logoutRequest = {
		path: 'auth/logout'
	};

	activiti.get(logoutRequest, function(error, statusCode, result, headers) {
		res.statusCode = statusCode;

		if (error) {
			res.send(error);
		} else {
			res.send(result);
		}
	});
}

exports.authenticate = function(req, res) {
	var user = req.body;

	var checkLogin = {
		path: 'auth/login',
		query: {
			'sLogin': user.login,
			'sPassword': user.password
		}
	};

	var getUser = {
		path: 'identity/users/' + user.login
	};

	activiti.post(checkLogin, function(error, statusCode, result, headers) {
		res.statusCode = statusCode;

		if (error) {
			res.send(error);
			return;
		}

		if (result === true) {
			var jsessionCookie = headers['set-cookie'][0].split('JSESSIONID=')[1];
			activiti.get(getUser, function(error, statusCode, result) {
				res.statusCode = statusCode;
				//TODO call check password
				if (error) {
					res.send(error);
				} else {
					res.cookie('user', result, {
						expires: expiresUserInMs()
					});
					res.cookie('JSESSIONID', jsessionCookie, {
						expires: expiresUserInMs()
					});
					var sessionSettings = {
						sessionIdle: config.activiti.session.sessionIdle,
						timeOut: config.activiti.session.timeOut,
						interval: config.activiti.session.interval
					};
					res.cookie('sessionSettings', JSON.stringify(sessionSettings), {
						expires: expiresUserInMs()
					});
					res.json(result);
				}
			});
		} else {
			res.statusCode = 401;
			res.send({
				message: 'Відмовлено у авторізаціі. Перевірте логін/пароль'
			});
		}
	});
}