'use strict';

var activiti = require('../../components/activiti');

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
     res.status(statusCode).json(result);
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
      sDateTimeTo: req.query.sDateTimeTo
    }
  };

  activiti.get(options, function(error, statusCode, result) {
    if (error) {
      res.send(error);
    } else {
     res.status(statusCode).json(result);
    }
  });
};

exports.deleteSchedule = function(req, res) {
  var options = {
    path: '/flow/removeSheduleFlowInclude',
    query: {
      sID_BP: req.query.sID_BP,
      nID: req.query.nID
    }
  };

  activiti.get(options, function(error, statusCode, result) {
    if (error) {
      res.send(error);
    } else {
     res.status(statusCode).json(result);
    }
  });
};

exports.getExemptions = function(req, res) {
  var options = {
    path: '/flow/getSheduleFlowExcludes',
    query: {
      sID_BP: req.query.sID_BP
    }
  };

  activiti.get(options, function(error, statusCode, result) {
    if (error) {
      res.send(error);
    } else {
     res.status(statusCode).json(result);
    }
  });
};

exports.setExemption = function(req, res) {
  var options = {
    path: '/flow/setSheduleFlowExclude',
    query: {
      sID_BP: req.query.sID_BP,
      nID: req.query.nID,
      sName: req.query.sName,
      sRegionTime: req.query.sRegionTime,
      saRegionWeekDay: req.query.saRegionWeekDay,
      sDateTimeAt: req.query.sDateTimeAt,
      sDateTimeTo: req.query.sDateTimeTo
    }
  };

  activiti.get(options, function(error, statusCode, result) {
    if (error) {
      res.send(error);
    } else {
     res.status(statusCode).json(result);
    }
  });
};

exports.deleteExemption = function(req, res) {
  var options = {
    path: '/flow/removeSheduleFlowExclude',
    query: {
      sID_BP: req.query.sID_BP,
      nID: req.query.nID
    }
  };

  activiti.get(options, function(error, statusCode, result) {
    if (error) {
      res.send(error);
    } else {
     res.status(statusCode).json(result);
    }
  });
};

exports.getFlowSlots = function(req, res) {
  var options = {
    path: '/flow/getFlowSlots_ServiceData',
    query: {
      sID_BP: req.query.sID_BP,
     //bAll: req.query.bAll,
     //nDays: req.query.nDays,
     //sDate: req.query.sDate
    }
  };

  activiti.get(options, function(error, statusCode, result) {
    if (error) {
      res.send(error);
    } else {
     res.status(statusCode).json(result);
    }
  });
};

exports.buildFlowSlots = function(req, res) {
  var options = {
    path: '/flow/buildFlowSlots',
    query: {
      sID_BP: req.query.sID_BP,
      sDateStart: req.query.sDateStart,
      sDateStop: req.query.sDateStop
    }
  };

  activiti.post(options, function(error, statusCode, result) {
    res.statusCode = statusCode;
    res.send(result);
  });
};

exports.deleteFlowSlots = function(req, res) {
  var options = {
    path: '/flow/clearFlowSlots',
    query: {
      sID_BP: req.query.sID_BP,
      sDateStart: req.query.sDateStart,
      sDateStop: req.query.sDateStop,
      bWithTickets: req.query.bWithTickets
    }
  };

  activiti.delete(options, function(error, statusCode, result) {
    if (error) {
      res.send(error);
    } else {
     res.status(statusCode).json(result);
    }
  });
};
