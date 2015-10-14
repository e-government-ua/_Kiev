'use strict';

var activiti = require('../../components/activiti');

var config = require('../../config/environment');
var sHostPrefix = config.server.sServerRegion;
console.log('1)sHostPrefix='+sHostPrefix);

var sHost = sHostPrefix + "/wf/service";

function buildSHost(sHostPrefix) {
  console.log('2)sHostPrefix='+sHostPrefix);
  return sHostPrefix + "service";
}

module.exports.getFlowSlots_ServiceData = function (req, res) {
  sHost = buildSHost(req.query.sURL);
  activiti.sendGetRequest(req, res, '/flow/getFlowSlots_ServiceData', {
    nID_ServiceData: req.params.nID,
    nID_SubjectOrganDepartment: req.query.nID_SubjectOrganDepartment
  }, null, sHost);
};

module.exports.setFlowSlot_ServiceData = function (req, res) {
  sHost = buildSHost(req.query.sURL);
	activiti.sendPostRequest(req, res, '/flow/setFlowSlot_ServiceData', {
		nID_FlowSlot: req.params.nID
	}, null, sHost);
};
