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
	proxyReq.path = req.query.url;
	proxyReq.setHeader('Authorization', authBase);
});

proxy.on('proxyRes', function (proxyRes, req, res) {

});

router.use(function(req, res, next) {
	proxy.web(req, res, {
		target: req.query.url,
		secure: false
	}, function(e) {
		if (e) {

		}
	});
});

module.exports = router;
