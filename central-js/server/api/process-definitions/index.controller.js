var request = require('request');

var activiti = require('../../components/activiti');

module.exports.index = function(options, callback) {
        //var sHost = options.params.url;
        //module.exports.getRegionURL
        //activiti.exports.getRegionURL()
        
        var nID_Server = options.params.nID_Server;
        console.log("nID_Server="+nID_Server);
        
	//var config = require('../../config/environment');
	//var activiti = config.activiti;
        return activiti.getServerRegionHost(nID_Server, function(sHost){
            console.log("sHost="+sHost);
            var sURL = sHost+'/service/repository/process-definitions';
            console.log("sURL="+sURL);
            /*if(nID_Server!==null){
                //router.get('/server', function(req, res, next, nID_Server) {
                //var oPlacesController = require('./index.controller');
                var oPlacesController = require('../places/index.controller');
                var oServer = oPlacesController.getServer(null, null, nID_Server);//req.query.nID_Server || null
                //res.send(oServer);
                //res.end();
                console.log("oServer="+oServer);
                if(oServer && oServer!==null){
                    sHost = oServer.sURL;
                    console.log("sHost="+sHost);
                }
                //var oPlaces = require('../places');
                //oPlaces.exports.getRegionURL()
            }
            console.log("oServer="+oServer);
            if(oServer && oServer!==null){
                sHost = oServer.sURL;
                console.log("sHost="+sHost);
            }*/
            return request.get({
                    'url': sURL,
                    'auth': {
                            'username': options.username,
                            'password': options.password
                    },
                    'qs': {
                            'latest': true //options.params.latest
                            ,'size': 1000
                    }
            }, callback);            

        });

};

/*module.exports.getCountryList = function (req, res) {
  activiti.sendGetRequest(req, res, '/object/place/getCountries', _.extend(req.query, req.params));
};*/


/*




*/