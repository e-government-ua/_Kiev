var request = require('request');

module.exports.index = function(options, callback) {
	return request.get({
		'url': options.params.url,
		'auth': {
			'username': options.username,
			'password': options.password
		}
	}, callback);
};