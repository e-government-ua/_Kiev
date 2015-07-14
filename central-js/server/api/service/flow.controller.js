'use strict';

var activiti = require('../../components/activiti');

var config = require('../../config/environment');
var sHostPrefix = config.server.sServerRegion;
console.log('1)sHostPrefix='+sHostPrefix);

if(sHostPrefix==null){
    sHostPrefix = "https://test.region.igov.org.ua";
    console.log('2)sHostPrefix='+sHostPrefix);
}

var sHost = sHostPrefix + "/wf-region/service";


module.exports.getFlowSlots_ServiceData = function (req, res) {
        activiti.sendGetRequest(req, res, '/flow/getFlowSlots_ServiceData?nID_ServiceData=' + req.params.nID, null, null, sHost);
};

module.exports.setFlowSlot_ServiceData = function (req, res) {
        /*var sHost = "https://test.region.igov.org.ua/wf-region/service";
	var config = require('../../config/environment');
	var sHostPrefix = config.server.sServerRegion;

	sHostPrefix = "https://test.region.igov.org.ua";
        var sHost = sHostPrefix + "/wf-region/service";*/
        
        
	activiti.sendPostRequest(req, res, '/flow/setFlowSlot_ServiceData', {
		nID_FlowSlot: req.params.nID
	}, null, sHost);
};
