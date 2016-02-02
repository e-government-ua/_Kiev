'use strict';
var _ = require('lodash');
var activiti = require('../../components/activiti');

module.exports.getSubjectOrganJoin = function (req, res) {
  activiti.sendGetRequest(req, res, '/subject/getSubjectOrganJoins', _.extend(req.query, req.params)); // {nID_SubjectOrgan:1} for test
};

module.exports.getOrganAttributes = function(req, res) {
  var apiReq = activiti.buildRequest(req, '/subject/getSubjectOrganJoinAttributes', _.extend(req.query, req.params));
  apiReq.body = req.body;
  apiReq.json = true;
  activiti.executePostRequest(apiReq, res);
};
