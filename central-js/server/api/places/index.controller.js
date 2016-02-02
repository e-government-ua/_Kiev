var request = require('request');
var NodeCache = require("node-cache" );
var arrayQuery = require('array-query');

var placesCache = new NodeCache();
//var aServerCache = new NodeCache();


/*function findServer(nID_Server) {
	var sURL_Key = 'api/places/server?nID=' + nID_Server;
	var sURL_Value = aServerCache.get(sURL_Key);

	if(sURL_Value) {
		return sURL_Value;
	}
        return null;
};*/

/*function getStructureServer(nID_Server) {
	var structureKey = 'api/places/server?nID='+nID_Server;
	return aServerCache.get(structureKey) || null;
};*/

function getStructure() {
	var structureKey = 'api/places';
	return placesCache.get(structureKey) || null;
};

function findRegions(search) {
	var regionsKey = 'api/places/regions?sFind=' + search;
	var regionsValue = placesCache.get(regionsKey);

	if(regionsValue) {
		return regionsValue;
	}

	var structureValue = getStructure();
	return (search == null) ?
		structureValue:
		arrayQuery('sName').regex(new RegExp(search, 'i')).limit(10).on(structureValue);
};

function findRegion(region) {
	var regionKey = 'api/places/region/' + region;
	var cacheValue = placesCache.get(regionKey);

	if(cacheValue) {
		return cacheValue;
	};

	var structureValue = getStructure();
	if(structureValue == null) {
		return null;
	}

	for(var key in structureValue) {
		var oRegion = structureValue[key];
		if(oRegion.nID == region) {
			placesCache.set(regionKey, oRegion);
			return oRegion;
		}
	};

	return null;
};

function findCity(region, city) {
	var cityKey = 'api/places/region/' + region + '/city/' + city;
	var cacheValue = placesCache.get(cityKey);

	if(cacheValue) {
		return cacheValue;
	}

	var oRegion = findRegion(region);
	if(oRegion == null) {
		return null;
	}

	var aCity = oRegion.aCity;
	for(var key in aCity)	{
		var oCity = aCity[key];
		if(oCity.nID == city) {
			placesCache.set(cityKey, oCity);
			return oCity;
		}
	};

	return null;
};

function findCities(region, search) {
	var citiesKey = 'api/places/region/' + region + '/cities?sFind=' + search;
	var cacheValue = placesCache.get(citiesKey);

	if(cacheValue) {
		return cacheValue;
	}

	var oRegion = findRegion(region);
	if(oRegion == null) {
		return [];
	}

	return (search == null) ?
		oRegion.aCity:
		arrayQuery('sName').regex(new RegExp(search, 'i')).limit(10).on(oRegion.aCity);
};

module.exports = {
	/*getServer: function(options, next, nID_Server) {
            
                if(options && options!==null){
                    console.log("options=none");
                }else{
                    console.log("options="+options);
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

                }
                process.env.NODE_TLS_REJECT_UNAUTHORIZED = "0";
            
		var structureValue = getStructureServer(nID_Server);

		if(structureValue) {
                    console.log("structureValue="+structureValue);
                        if(next && next!==null){
                    console.log("next()");
                            next();
                        }
			return structureValue;
		}

		var sURL = options.protocol+'://'+options.hostname+options.path+'/subject/getServer?nID='+nID_Server;
                    console.log("sURL="+sURL);
		return request.get({
			'url': sURL,
			'auth': {
				'username': options.username,
				'password': options.password
			}
		}, function(error, response, body) {
                    console.log("body="+body);
			aServerCache.set('api/places/server?nID='+nID_Server, JSON.parse(body), 86400);
                        if(next && next!==null){
                    console.log("next()");
                            next();
                        }
                    console.log("body="+body);
			return JSON.parse(body);
		});
	},*/
	getPlaces: function(options, next) {
		var structureValue = getStructure();

		if(structureValue) {
			next();
			return;
		}

		var url = options.protocol+'://'+options.hostname+options.path+'/object/place/getPlaces';
		return request.get({
			'url': url,
			'auth': {
				'username': options.username,
				'password': options.password
			}
		}, function(error, response, body) {
			placesCache.set('api/places', JSON.parse(body), 86400);
			next();
			return;
		});
	},
	//getServer: findServer,
	getRegions: findRegions,
	getRegion: findRegion,
	getCities: findCities,
	getCity: findCity
};
