'use strict';

var activiti = require('../../components/activiti');

exports.getAllRules = function (req, res) {
  var options = {
    path: 'escalation/getEscalationRules',
    query: {
    }
  };

  activiti.get(options, function (error, statusCode, result) {
    if (error) {
      res.send(error);
    } else {
      res.status(statusCode).json(result);
    }
  });
};

exports.setRule = function (req, res) {
  var options = {
    path: 'escalation/setEscalationRule',
    query: {
      nID: req.query.nID,
      sID_BP: req.query.sID_BP,
      sID_UserTask: req.query.sID_UserTask,
      sCondition: req.query.sCondition,
      soData: req.query.soData,
      sPatternFile: req.query.sPatternFile,
      nID_EscalationRuleFunction: req.query.nID_EscalationRuleFunction,
    }
  };


  activiti.get(options, function (error, statusCode, result) {
    if (error) {
      res.send(error);
    } else {
      res.status(statusCode).json(result);
    }
  });
};

exports.deleteRule = function (req, res) {
  var options = {
    path: 'escalation/removeEscalationRule',
    query: {
      nID: req.query.nID
    }
  };


  activiti.get(options, function (error, statusCode, result) {
    if (error) {
      res.send(error);
    } else {
      res.status(statusCode).json(result);
    }
  });
};

exports.getAllEscalationFunctions = function (req, res) {
  var options = {
    path: 'escalation/getEscalationRuleFunctions',
    query: {
    }
  };

  activiti.get(options, function (error, statusCode, result) {
    if (error) {
      res.send(error);
    } else {
      res.status(statusCode).json(result);
    }
  });
};


exports.setRuleFunction = function (req, res) {
  var queryToSend = {
    sName: req.query.sName,
    sBeanHandler: req.query.sBeanHandler
  };
  if (typeof req.query.nID !== 'undefined') {
    queryToSend.nID = req.query.nID;
  }
  var options = {
    path: 'escalation/setEscalationRuleFunction',
    query: queryToSend
  };

  activiti.get(options, function (error, statusCode, result) {
    if (error) {
      res.send(error);
    } else {
      res.status(statusCode).json(result);
    }
  });
};

exports.deleteRuleFunction = function (req, res) {
  var options = {
    path: 'escalation/removeEscalationRuleFunction',
    query: {
      nID: req.query.nID
    }
  };


  activiti.get(options, function (error, statusCode, result) {
    if (error) {
      res.send(error);
    } else {
      res.status(statusCode).json(result);
    }
  });
};