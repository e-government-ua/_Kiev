var express = require('express');
var router = express.Router();
var arrayQuery = require('array-query');

// api/places/regions/{nID} - return list of cities that belong to region nID
// optional query parameter: ?sFind=STRING - filter cities by name
router.get('/regions/:id', function(req, res, next) {
	var places = require('./index.controller');

	function callback() {
		var cities = places.getRegionCities(req.params.id);
                var sFind=req.query['sFind'];
		if (sFind) {
                    sFind=sFind.trim();
                    if (sFind!="") {
			var regexp = new RegExp(sFind, 'i');
			var filtered = arrayQuery('sName').regex(regexp).limit(10).on(cities);
                        res.send(filtered);
                    }else{
			var filtered = arrayQuery('sName').limit(10).on(cities);
			res.send(filtered);
                    }
                    res.end();
                    return;
		}
		res.send(cities);
		res.end();
		return;
	}

	places.init(callback);
});

// api/places/regions - return list of regions (w/o cities)
router.get('/regions', function(req, res, next) {
	var places = require('./index.controller');
	function callback() {
		res.send(places.getRegions());
		res.end();
	}
	places.init(callback);
});

module.exports = router;