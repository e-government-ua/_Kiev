'use strict';

var request = require('request');
var _ = require('lodash');
var config = require('../../config/environment');
var activiti = config.activiti;

var sHostPrefix = config.server.sServerRegion;
console.log('1)sHostPrefix='+sHostPrefix);

if(sHostPrefix==null){
  sHostPrefix = "https://test.region.igov.org.ua";
  console.log('2)sHostPrefix='+sHostPrefix);
}

var sHost = sHostPrefix + "/wf-region/service";

var buildUrl = function(path){
  var url = activiti.protocol + '://' + activiti.hostname + activiti.path + path;
  return url;
};

module.exports.getServicesTree = function (req, res) {

  //var callback = function (error, response, body) {
  //  res.send(body);
  //  res.end();
  //};
  //
  //activiti.sendGetRequest(req,
  //  res,
  //  '/services/getServicesTree',
  //  {
  //    'sFind': req.query.sFind,
  //    'asID_Place_UA': req.query.asIDPlaceUA
  //  },
  //  callback,
  //  sHost
  //);

  var options = {
    protocol: activiti.protocol,
    hostname: activiti.hostname,
    port: activiti.port,
    path: activiti.path,
    username: activiti.username,
    password: activiti.password,
    params: {
      sFind: req.query.sFind || null,
      asIDPlaceUA: req.query.asIDPlaceUA || null
    }
  };

  var callback = function (error, response, body) {
    res.send(body);
    res.end();
  };

  var url = activiti.protocol + '://' + activiti.hostname + activiti.path + '/services/getServicesTree';

  return request.get({
    'url': url,
    'auth': {
      'username': options.username,
      'password': options.password
    },
    'qs': {
      'sFind': options.params.sFind,
      'asID_Place_UA': options.params.asIDPlaceUA
    }
  }, callback);
};

module.exports.setServicesTree = function(req, res) {
  activiti.sendPostRequest(req, res, '/services/setServicesTree', {
    nID_Subject : req.session.subject.nID
  }, null, sHost);
};

var remove = function(path, req, res){

  activiti.sendDeleteRequest(req, res, path, {
      nID: req.query.nID,
      bRecursive: req.query.bRecursive,
      nID_Subject: req.session.subject.nID
    }, null, sHost);

  //var options = {
  //  path: path,
  //  query: {
  //    nID: req.query.nID,
  //    bRecursive: req.query.bRecursive,
  //    nID_Subject: req.session.subject.nID
  //  }
  //};

  //activiti.del(options, function(error, statusCode, result) {
  //  if (error) {
  //    res.send(error);
  //  } else {
  //    res.status(statusCode).json(result);
  //  }
  //});
};

module.exports.removeService = function(req, res) {
  return remove('services/removeService', req, res);
};

module.exports.removeServiceData = function(req, res) {
  return remove('services/removeServiceData', req, res);
};

module.exports.removeSubcategory = function(req, res) {
  return remove('services/removeSubcategory', req, res);
};

module.exports.removeCategory = function(req, res) {
  return remove('services/removeCategory', req, res);
};

module.exports.removeServicesTree = function(req, res) {
  var options = {
    path: 'services/removeServicesTree',
    query: {
      nID_Subject: req.query.nID_Subject
    }
  };

  activiti.del(options, function(error, statusCode, result) {
    if (error) {
      res.send(error);
    } else {
      res.status(statusCode).json(result);
    }
  });
};

