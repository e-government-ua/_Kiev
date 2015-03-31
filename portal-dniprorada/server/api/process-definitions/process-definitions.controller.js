'use strict';

var _ = require('lodash');
var activiti = require('../../components/activiti');


// Get list of process-definitionss
exports.index = function(req, res) {
	var options = {
		path: 'repository/process-definitions',
        query: {
			'latest' : true
		}
	};
	activiti.get(options, function(error, statusCode, result) {
		if (error) {
			res.statusCode = 400;
			res.send(error);
		} else {
			res.statusCode = statusCode;
			res.send(result);
		}
	});
};