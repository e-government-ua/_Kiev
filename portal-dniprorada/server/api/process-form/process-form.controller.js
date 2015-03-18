'use strict';

var _ = require('lodash');
var activiti = require('../../components/activiti');

// Get list of process-forms
exports.getFormByProcessDefinitionId = function(req, res) {
	var processDefinitionId = req.params.processDefinitionId;

	var options = {
		path: 'form/form-data?processDefinitionId=' + processDefinitionId,
		method: 'GET'
	};
	activiti.get(options, function(statusCode, result) {
		res.statusCode = statusCode;
		res.send(result);
	});
};

/*
POST
{
  "processDefinitionId" : "5",
  "businessKey" : "myKey",
  "properties" : [
    {
      "id" : "room",
      "value" : "normal"
    }
  ]
}
*/
exports.submitForm = function(req, res) {
	var processDefinitionId = req.params.processDefinitionId;
	var options = {
		path: 'form/form-data',
		method: 'POST'
	};
	activiti.post(options, req.body, function(statusCode, result) {
		res.statusCode = statusCode;
		res.send(result);
	});
}