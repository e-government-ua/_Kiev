'use strict';

var _ = require('lodash');
var activiti = require('../../components/activiti');

// Get list of process-forms
exports.getFormByProcessDefinitionId = function(req, res) {
	var processDefinitionId = req.params.processDefinitionId;

	var options = {
		path: 'form/form-data?processDefinitionId=' + processDefinitionId,
		method: 'GET',
	};
	activiti.rest(options, function(statusCode, result) {
		res.statusCode = statusCode;
		res.send(result);
	});
};