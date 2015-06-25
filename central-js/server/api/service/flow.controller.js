var activiti = require('../../components/activiti');

module.exports.getFlowSlots_ServiceData = function (req, res) {
	activiti.sendGetRequest(req, res, '/flow/getFlowSlots_ServiceData?nID_ServiceData=' + req.params.nID);
};

module.exports.setFlowSlot_ServiceData = function (req, res) {
	activiti.sendPostRequest(req, res, '/flow/setFlowSlot_ServiceData', {
		nID_FlowSlot: req.params.nID
	});
};
