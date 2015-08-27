var express = require('express');
var router = express.Router();
var request = require('request');

var httpProxy = require('http-proxy');
var config = require('../../config/environment');

var Buffer = require('buffer').Buffer;
var authBase = 'Basic ' + new Buffer(
    config.activiti.username +
    ':' +
    config.activiti.password)
    .toString('base64');

var proxy = httpProxy.createProxyServer({});

proxy.on('proxyReq', function(proxyReq, req, res, options) {
  proxyReq.path = options.target.href;
  proxyReq.setHeader('Authorization', authBase);
});

proxy.on('error', function(e) {
  console.log(e);
});

proxy.on('proxyRes', function (proxyRes, req, res) {
  console.log('result');
});

module.exports.upload = function(req, res, target, error){
  proxy.web(req, res, {
    target: target,
    secure: false
  }, function(e) {
    if(error){
      error(e);
    }
  });
};
