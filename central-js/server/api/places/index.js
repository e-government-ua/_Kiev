var express = require('express');
var router = express.Router();
var arrayQuery = require('array-query');

router.use(function(req, res, next) {

	var config = require('../../config/environment');
	var activiti = config.activiti;

	var options = {
		protocol: activiti.protocol,
		hostname: activiti.hostname,
		port: activiti.port,
		path: activiti.path,
		username: activiti.username,
		password: activiti.password
	};

	process.env.NODE_TLS_REJECT_UNAUTHORIZED = "0";

	var controller = require('./index.controller.js');
	controller.getPlaces(options, next);
})

/*router.get('/server', function(req, res, next, nID_Server) {
	var config = require('../../config/environment');
	var activiti = config.activiti;

	var options = {
		protocol: activiti.protocol,
		hostname: activiti.hostname,
		port: activiti.port,
		path: activiti.path,
		username: activiti.username,
		password: activiti.password
	};

	process.env.NODE_TLS_REJECT_UNAUTHORIZED = "0";

	var oPlacesController = require('./index.controller');
	var oServer = oPlacesController.getServer(options, next, nID_Server);//req.query.nID_Server || null
	res.send(oServer);
	res.end();
});*/

// api/places/regions - return list of regions (w/o cities)
router.get('/regions', function(req, res, next) {
	var places = require('./index.controller');
	var aRegion = places.getRegions(req.query.sFind || null);

	res.send(aRegion);
	res.end();
});
// api/places/region/{region} - return region (w/o cities)
router.get('/region/:region([0-9]+)', function(req, res, next) {
	var places = require('./index.controller');
	var oRegion = places.getRegion(req.params.region);

	res.send(oRegion);
	res.end();
});

// api/places/region/{region}/cities - return list of cities
// optional query parameter: ?sFind=STRING - filter cities by name
router.get('/region/:region([0-9]+)/cities', function(req, res, next) {
	var places = require('./index.controller');
	var aCity = places.getCities(req.params.region, req.query.sFind || null);

	res.send(aCity);
	res.end();
});

// api/places/region/{region}/city/{city} - return city
router.get('/region/:region([0-9]+)/city/:city([0-9]+)', function(req, res, next) {
	var places = require('./index.controller');
	var oCity = places.getCity(req.params.region, req.params.city);

	res.send(oCity);
	res.end();
});

module.exports = router;
