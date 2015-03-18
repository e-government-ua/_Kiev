'use strict';

var _ = require('lodash');
var activiti = require('../../components/activiti');


// Get list of process-definitionss
exports.index = function(req, res) {
	var options = {
		path: 'repository/process-definitions',
		method: 'GET',
	};
	activiti.get(options, function(statusCode, result) {
		res.statusCode = statusCode;
		res.send(result);
	});
};