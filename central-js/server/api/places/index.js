var express = require('express');
var router = express.Router();
var NodeCache = require( "node-cache" );
var myCache = new NodeCache();

router.use(function(req, res, next) {

	process.env.NODE_TLS_REJECT_UNAUTHORIZED = "0";
	var places = require('./index.controller');
	var config = require('../../config');
	var activiti = config.activiti;
	
	var options = {
		protocol: activiti.protocol,
		hostname: activiti.hostname,
		port: activiti.port,
		path: activiti.path,
		username: activiti.username,
		password: activiti.password
	};

	var cacheKey = 'api/places';
	
	var callback = function(error, response, body) {
		// cache successful answer for 1 day (86400 seconds)
		if (error === null) {
			myCache.set(cacheKey, body, 86400);
		} else {
			// clear cache in case of error
			myCache.del(cacheKey);
		}
		res.send(body);
		res.end();
	}
	// use cached value if available, otherwise make an index call
	if (myCache.get(cacheKey) !== undefined) {
		res.send(myCache.get(cacheKey));
		res.end();
	} else {
		places.index(options, callback);
	}
	
});

module.exports = router;