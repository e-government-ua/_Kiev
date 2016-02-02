'use strict';
var _ = require('lodash');
var activiti = require('../../components/activiti');
var environmentConfig = require('../../config/environment');
var config = environmentConfig.activiti;
var request = require('request');
var catalogController = require('../catalog/catalog.controller.js');

var sHost = config.protocol + '://' + config.hostname + config.path;

var buildUrl = function(path){
  return sHost + path;
};

module.exports.index = function(req, res) {
  activiti.sendGetRequest(req, res, '/action/item/getService?nID=' + req.query.nID);
};

module.exports.getServiceStatistics = function(req, res) {
  activiti.sendGetRequest(req, res, '/action/event/getStatisticServiceCounts?nID_Service=' + req.params.nID);
};

module.exports.setService = function(req, res) {
  var callback = function (error, response, body) {
    catalogController.pruneCache();
    res.send(body);
    res.end()
  };

  var url = buildUrl('/action/item/setService');

  request.post({
    'url': url,
    'auth': {
      'username': config.username,
      'password': config.password
    },
    'qs': {
      'nID_Subject': req.session.subject.nID
    },
    'headers': {
      'Content-Type': 'application/json; charset=utf-8'
    },
    'json': true,
    'body': req.body
  }, callback);
};

module.exports.removeServiceData = function(req, res) {

  var callback = function (error, response, body) {
    catalogController.pruneCache();
    res.send(body);
    res.end();
  };

  var url = buildUrl('/action/item/removeServiceData');

  request.del({
    'url': url,
    'auth': {
      'username': config.username,
      'password': config.password
    },
    'qs': {
      'nID': req.query.nID,
      'bRecursive': req.query.bRecursive,
      'nID_Subject': req.session.subject.nID
    }
  }, callback);
};
