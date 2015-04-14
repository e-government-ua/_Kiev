'use strict';

var activiti = require('../../components/activiti');

var expiresUserInMs = function() {
	return new Date(Date.now() + 1000 * 60 * 60 * 20);
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
			res.send(result);
		}
	});
}