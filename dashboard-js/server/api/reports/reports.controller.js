'use strict';

var activiti = require('../../components/activiti');

exports.index = function (req, res) {
  var options = {
    path: 'rest/file/downloadTasksData',
    query: {
      'sID_BP': req.query.sID_BP,
      'sID_State_BP': req.query.sID_State_BP,
      'sDateAt':req.query.sDateAt,
      'sDateTo':req.query.sDateTo,
      'saFields': req.query.saFields,
      'sID_Codepage':req.query.sID_Codepage,
      'nASCI_Spliter':req.query.nASCI_Spliter,
      'sDateCreateFormat':req.query.sDateCreateFormat,
      'sFileName':req.query.sFileName
    }
  };
  activiti.filedownload(req, res, options);
};

exports.statistic = function (req, res) {
  var options = {
    path: 'rest/file/download_bp_timing',
    query: {
      'sID_BP_Name': req.query.sID_BP,
      'sDateAt':req.query.sDateAt,
      'sDateTo':req.query.sDateTo
    }
  };
  activiti.filedownload(req, res, options);
};

