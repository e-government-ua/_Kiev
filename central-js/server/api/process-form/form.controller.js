var request = require('request');

module.exports.get = function(options, callback) {
	return request.get({
		'url': options.params.url,
		'auth': {
			'username': options.username,
			'password': options.password
		}
	}, callback);
};