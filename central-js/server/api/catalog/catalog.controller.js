'use strict';

var request = require('request');
var _ = require('lodash');
var NodeCache = require("node-cache" );
var config = require('../../config/environment');
var activiti = config.activiti;

var sHost = activiti.protocol + '://' + activiti.hostname + activiti.path;

var cache = new NodeCache();
var cacheTtl = 300; // 300 seconds = 5 minutes time to live for a cache

var buildUrl = function(path){
  var url = sHost + path;
  return url;
};
// helper to build key for cache operations
var buildKey = function(params) {
  var key = 'catalog';
  if (params) {
    for (var k in params) {
      key += '&' + k + '=' + params[k];
    }
  }
  return key;
};
// remove all the keys that starts from buildKey() result from the cache
var pruneCache = function() {
  cache.keys( function( err, keys ){
    if (err) {
      return;
    }
    var keysToDelete = [];
    // keyBase is a string in start of every key to delete from cache
    var keyBase = buildKey();
    keys.forEach(function(key) {
      if (key.indexOf(keyBase) === 0) {
        keysToDelete.push(key);
      }
    });
    // prune cache
    cache.del(keysToDelete);
  });
};

module.exports.getServicesTree = function (req, res) {
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
  //var cachedReply = cache.get(buildKey(options.params));
  //if (cachedReply) {
  //  res.send(cachedReply);
  //  res.end;
  //  return;
  //}

  var callback = function (error, response, body) {
    // set cache key for this particular request
    if (!error) {
      cache.set(buildKey(options.params), body, cacheTtl);
    }
    res.send(body);
    res.end();
  };

  var url = buildUrl('/services/getServicesTree');

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

  var callback = function (error, response, body) {
    res.send(body);
    res.end();
  };

  var url = buildUrl('/services/setServicesTree');

  process.env['NODE_TLS_REJECT_UNAUTHORIZED'] = '0'; // Ignore 'UNABLE_TO_VERIFY_LEAF_SIGNATURE' authorization error

  request.post({
    //'proxy': "http://127.0.0.1:8888",
    'url': url,
    'auth': {
      'username': activiti.username,
      'password': activiti.password
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

  pruneCache();
};

var remove = function(path, req, res){

  var callback = function (error, response, body) {
    res.send(body);
    res.end();
  };

  var url = buildUrl(path);

  request.del({
    'url': url,
    'auth': {
      'username': activiti.username,
      'password': activiti.password
    },
    'qs': {
      'nID': req.query.nID,
      'bRecursive': req.query.bRecursive,
      'nID_Subject': req.session.subject.nID
    }
  }, callback);

  pruneCache();
};

module.exports.removeCategory = function(req, res) {
  return remove('services/removeCategory', req, res);
};

module.exports.removeSubcategory = function(req, res) {
  return remove('services/removeSubcategory', req, res);
};

module.exports.removeService = function(req, res) {
  return remove('services/removeService', req, res);
};

module.exports.removeServiceData = function(req, res) {
  return remove('services/removeServiceData', req, res);
};

module.exports.removeServicesTree = function(req, res) {

  var callback = function (error, response, body) {
    res.send(body);
    res.end();
  };

  var url = buildUrl('services/removeServicesTree');

  request.del({
    'url': url,
    'auth': {
      'username': activiti.username,
      'password': activiti.password
    },
    'qs': {
      'nID_Subject': req.session.subject.nID
    }
  }, callback);

  pruneCache();
};

