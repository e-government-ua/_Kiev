'use strict';

var activiti = require('../../components/activiti');

// Get list of processs
exports.index = function(req, res) {
	var options = {
		path: 'repository/process-definitions'
	};

	activiti.get(options, function(error, statusCode, result) {
		if (error) {
			res.send(error);
		} else {
			res.json(result);
		}
	});
};