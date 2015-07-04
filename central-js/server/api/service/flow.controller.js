var activiti = require('../../components/activiti');

module.exports.getFlowSlots_ServiceData = function (req, res) {
        var sHost = "https://test.region.igov.org.ua/wf-region/service";
	activiti.sendGetRequest(req, res, '/flow/getFlowSlots_ServiceData?nID_ServiceData=' + req.params.nID, null, null, sHost);
};

module.exports.setFlowSlot_ServiceData = function (req, res) {
        var sHost = "https://test.region.igov.org.ua/wf-region/service";
	activiti.sendPostRequest(req, res, '/flow/setFlowSlot_ServiceData', {
		nID_FlowSlot: req.params.nID
	}, sHost);
};
