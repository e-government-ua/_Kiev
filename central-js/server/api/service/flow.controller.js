'use strict';

var activiti = require('../../components/activiti');

var config = require('../../config/environment');

/*
var sHostPrefix = config.server.sServerRegion;
console.log('1)sHostPrefix='+sHostPrefix);

var sHost = sHostPrefix + "/wf/service";

function buildSHost(sHostPrefix) {
  console.log('2)sHostPrefix='+sHostPrefix);
  return sHostPrefix + "service";
}*/

module.exports.getFlowSlots_ServiceData = function (req, res) {

//        var data = {
//          //sURL: scope.serviceData.sURL,
//          nID_Server: scope.serviceData.nID_Server,
//          nID_Service: (scope && scope.service && scope.service!==null ? scope.service.nID : null)
//        };
//        return $http.get('/api/service/flow/' + scope.serviceData.nID, {params:data}).then(function(response) {
    
    var nID_Server = req.query.nID_Server;
    activiti.getServerRegionHost(nID_Server, function(sHost){
    //  sHost = sHost+'/service';

      //sHost = buildSHost(req.query.sURL);
      //activiti.sendGetRequest(req, res, '/action/flow/getFlowSlots_ServiceData', {
      activiti.sendGetRequest(req, res, '/service/action/flow/getFlowSlots_ServiceData', {
        nID_Service: req.query.nID_Service,
        nID_ServiceData: req.params.nID,
        nID_SubjectOrganDepartment: req.query.nID_SubjectOrganDepartment
      }, null, sHost);
    });
};

module.exports.setFlowSlot_ServiceData = function (req, res) {
    
//          //$http.post('/api/service/flow/set/' + newValue.nID + '?sURL=' + scope.serviceData.sURL).then(function(response) {
//          $http.post('/api/service/flow/set/' + newValue.nID + '?nID_Server=' + scope.serviceData.nID_Server).then(function(response) {
  
    var nID_Server = req.query.nID_Server;
    activiti.getServerRegionHost(nID_Server, function(sHost){
        //var sURL = sHost+'/service/object/file/upload_file_to_redis';
        //console.log("sURL="+sURL);
    //  sHost = sHost+'/service';

      //sHost = buildSHost(req.query.sURL);
    //	activiti.sendPostRequest(req, res, '/action/flow/setFlowSlot_ServiceData', {
        activiti.sendPostRequest(req, res, '/service/action/flow/setFlowSlot_ServiceData', {
                nID_FlowSlot: req.params.nID
        }, null, sHost);
    });
};
