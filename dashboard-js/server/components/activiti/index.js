'use strict';

var _ = require('lodash');
var config = require('../../config/environment');
var request = require('request');
var url = require('url');

var Buffer = require('buffer').Buffer;

var authBase = 'Basic ' + new Buffer(
			config.activiti.username +
			':' +
			config.activiti.password)
		.toString('base64');

var httpProxy = require('http-proxy');
var default_headers = {
	'Authorization': authBase,
};
/*	'Authorization': config.activiti.auth.basic*/

request.debug = config.request.debug;

var createUploadProxy = function() {
	var uploadProxy = httpProxy.createProxyServer({});
	uploadProxy.on('proxyReq', function(proxyReq, req, res, options) {
		proxyReq.path = options.target.path;
		proxyReq.setHeader('Authorization', authBase);
		/*proxyReq.setHeader('Authorization', config.activiti.auth.basic);*/
	});
	uploadProxy.on('proxyRes', function(proxyRes, req, res) {

	});
	return uploadProxy;
};

var getRequestURL = function(options) {
	return url.format({
		protocol: config.activiti.prot,
		hostname: config.activiti.host,
    port: config.activiti.port,
		pathname: '/' + (options.root || config.activiti.rest) + '/' + options.path,
		query: options.query
	});
};

var getRequestOptions = function(options) {
	var headers = options.headers;
	/*if (config.activiti.auth.basic) {*/
	if (config.activiti.password) {
		headers = _.merge(options.headers, default_headers) || default_headers;
	}

	return {
		url: getRequestURL(options),
		headers: headers,
    json : options.json ? options.json : false
	};
};

var prepareRequest = function(req, options, data) {
	var r = null;
	if (req.method === 'POST') {
		r = request.post(_.merge(getRequestOptions(options),
			data ? {
				json: true,
				body: data
			} : {
				json: true,
				body: req.body
			}));
	} else {
		r = request(getRequestOptions(options));
	}
	return r;
};

var uploadProxy = createUploadProxy();

exports.getRequestURL = getRequestURL;
exports.getRequestOptions = getRequestOptions;

exports.get = function(options, onResult) {
	request.get(getRequestOptions(options), function(error, response, body) {
			if (!error) {
				onResult(null, response.statusCode, body, response.headers);
			} else {
				onResult(error, null, null);
			}
		});
};

exports.filedownload = function(req, res, options) {
	var r = prepareRequest(req, options);
	req.pipe(r).on('response', function(response) {
		response.headers['content-type'] = 'application/octet-stream';
	}).pipe(res);
};

//downloads the file with the specified content type
exports.typedfiledownload = function(req, res, options) {
	var r = prepareRequest(req, options);
	req.pipe(r).on('response', function(response) {
		response.headers['content-type'] = options.contentType;
	}).pipe(res);
};

exports.fileupload = function(req, res, options) {
	uploadProxy.web(req, res, {
		target: options.url,
		secure: false
	}, function(e) {
		if (e) {

		}
	});
};

exports.pipe = function(req, res, options, data) {
	var r = null;
	if (req.method === 'POST') {
		r = request.post(_.merge(getRequestOptions(options),
			data ? {
				json: true,
				body: data
			} : {
				json: true,
				body: req.body
			}));
	} else {
		r = request(getRequestOptions(options));
	}
	req.pipe(r).pipe(res);
};

exports.post = function(options, onResult, data, json) {
  if (typeof(json) == 'undefined')
    json = true;
	request.post(_.merge(getRequestOptions(options), data ? {
			json: json,
			body: data
		} : {
			json: json
		}),
		function(error, response, body) {
			if (!error) {
				onResult(null, response.statusCode, body, response.headers);
			} else {
				onResult(error, null, null);
			}
		});
};

exports.put = function(options, onResult, data) {
  request.put(_.merge(getRequestOptions(options), data ? {
      json: true,
      body: data
    } : {
      json: true
    }),
    function(error, response, body) {
      if (!error) {
        onResult(null, response.statusCode, body, response.headers);
      } else {
        onResult(error, null, null);
      }
    });
};

exports.del = function(options, onResult) {
  request.del(getRequestOptions(options), function(error, response, body) {
      if (!error) {
        onResult(null, response.statusCode, body, response.headers);
      } else {
        onResult(error, null, null);
      }
    });
};
