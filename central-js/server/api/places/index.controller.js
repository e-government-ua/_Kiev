var request = require('request');
var NodeCache = require( "node-cache" );
var myCache = new NodeCache();
var cacheKey = 'api/places';
var _ = require('lodash');

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
process.env.NODE_TLS_REJECT_UNAUTHORIZED = "0";

function updateCache (body) {
    //cache reply for 1 day (86400 seconds)
    myCache.set(cacheKey, body, 86400);

    var regionsAndCities = JSON.parse(body);
    var regions = [];
    for (var i = 0, len = regionsAndCities.length; i < len; ++i) {
        var item = regionsAndCities[i];
        //cache region (with cities)
        myCache.set(cacheKey+'/regions/'+item['nID'], item);

        // delete city information from region befor adding it to regions
        if (item['aCity'] !== undefined) {
            item = _.cloneDeep(item);
            delete item['aCity'];
        }
        regions.push(item);
    }
    // cache regions for 1 day (86400 seconds)
    myCache.set(cacheKey+'/regions', regions);
}

module.exports.init = function(callback) {
    // if cache is up to date - just execute callback
    if (myCache.get(cacheKey)) {
        callback();
        return;
    }
    // if cache is missing, then query activity
	var url = options.protocol+'://'+options.hostname+options.path+'/services/getPlaces';
	console.log(url);
	return request.get({
		'url': url,
		'auth': {
			'username': options.username,
			'password': options.password
		}
	}, function(error, response, body) {
        if (error === null && body) {
            updateCache(body);
        } else {
            console.warn('Got error or empty reply from', url);
            console.warn('Error', error, 'response', response);
        }
        callback();
    });
};

module.exports.getRegions = function () {
    return myCache.get(cacheKey+'/regions');
}

module.exports.getRegionCities = function (regionId)  {
    var regions = myCache.get(cacheKey + '/regions/' + regionId);
    if (regions && regions['aCity']) {
        return regions['aCity'];
    }
}
