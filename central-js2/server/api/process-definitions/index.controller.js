var request = require('request');

module.exports.index = function(options, callback) {
	var url = options.params.url+'service/repository/process-definitions';
	console.log(url);
	return request.get({
		'url': url,
		'auth': {
			'username': options.username,
			'password': options.password
		},
		'qs': {
			'latest': options.params.latest
			,'size': 1000
		}
	}, callback);
};