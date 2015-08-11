'use strict';

var activiti = require('../../components/activiti');

exports.index = function(req, res) {
  var options = {
    path: 'flow/getFlowSlots_ServiceData',
    query: {
      'nID_ServiceData': req.query.nID_ServiceData,
    }
  };

  activiti.get(options, function(error, statusCode, result) {
    if (error) {
      res.send(error);
    } else {
      res.json(result);
    }
  });
};

exports.getSchedule = function(req, res) {
  var options = {
    path: '/flow/getSheduleFlowIncludes',
    query: {
      sID_BP: req.query.sID_BP
    }
  };

  activiti.get(options, function(error, statusCode, result) {
    if (error) {
      res.send(error);
    } else {
      res.json(result);
    }
  });
};

exports.setSchedule = function(req, res) {
  var options = {
    path: '/flow/setSheduleFlowInclude',
    query: {
      sID_BP: req.query.sID_BP,
      nID: req.query.nID,
      sName: req.query.sName,
      sRegionTime: req.query.sRegionTime,
      saRegionWeekDay: req.query.saRegionWeekDay,
      sDateTimeAt: req.query.sDateTimeAt,
      sDateTimeTo: req.query.sDateTimeTo,
    }
  };

  activiti.get(options, function(error, statusCode, result) {
    if (error) {
      res.send(error);
    } else {
      res.json(result);
    }
  });
};

exports.deleteSchedule = function(req, res) {
  var options = {
    path: '/flow/removeSheduleFlowInclude',
    query: {
      sID_BP: req.query.sID_BP,
      nID: req.query.nID,
    }
  };

  activiti.get(options, function(error, statusCode, result) {
    if (error) {
      res.send(error);
    } else {
      res.json(result);
    }
  });
};

exports.statistic = function (req, res) {
  var options = {
    path: 'rest/file/download_bp_timing',
    query: {
      'sID_BP_Name': req.query.sID_BP_Name,
      'sDateAt':req.query.sDateAt,
      'sDateTo':req.query.sDateTo
    }
  };
  activiti.filedownload(req, res, options);
};
