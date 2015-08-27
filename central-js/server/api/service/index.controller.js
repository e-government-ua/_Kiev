'use strict';
var _ = require('lodash');
var activiti = require('../../components/activiti');

module.exports.index = function(req, res) {
  activiti.sendGetRequest(req, res, '/services/getService?nID=' + req.query.nID);
};

module.exports.getServiceStatistics = function(req, res) {
  activiti.sendGetRequest(req, res, '/services/getStatisticServiceCounts?nID_Service=' + req.params.nID);
};
