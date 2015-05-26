'use strict';

var _ = require('lodash');
var activiti = require('../../components/activiti');

// Get list of tasks
exports.index = function(req, res) {
  var user = JSON.parse(req.cookies.user);
  var query = {};
  //https://test.igov.org.ua/wf-dniprorada/service/runtime/tasks?size=20
  query.size = 100;

  if (req.query.filterType === 'selfAssigned') {
    query.assignee = user.id;
  } else if (req.query.filterType === 'unassigned') {
    //query.candidateUser = user.id;
    query.unassigned = true;
  } else if (req.query.filterType === 'finished') {
    query.candidateUser = user.id;
    query.delegationState = 'resolved';
  }

  var options = {
    path: 'runtime/tasks',
    query: query
  };

  activiti.get(options, function(error, statusCode, result) {
    if (error) {
      res.send(error);
    } else {
      res.json(result);
    }
  });
};

// Get list of task events
exports.getAllTaskEvents = function(req, res) {
  var options = {
    path: '/runtime/tasks/' + req.params.taskId + '/events'
  };

  activiti.get(options, function(error, statusCode, result) {
    if (error) {
      res.send(error);
    } else {
      res.json(result);
    }
  });

};

exports.getForm = function(req, res) {
  var options = {
    path: 'form/form-data',
    query: {
      'taskId': req.params.taskId
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

exports.getAttachments = function(req, res) {
  var options = {
    path: 'runtime/tasks/' + req.params.taskId + '/attachments'
    //path: 'runtime/tasks/32569/attachments'
  };

  activiti.get(options, function(error, statusCode, result) {
    if (error) {
      res.send(error);
    } else {
      res.status(statusCode).json(result);
    }
  });
};

exports.getAttachmentContent = function(req, res) {
  var options = {
    path: 'rest/file/download_file_from_db',
    query: {
      'taskId': req.params.taskId,
      'nFile': req.params.nFile
    }
  };
  activiti.filedownload(req, res, options);
};

exports.submitForm = function(req, res) {
  var options = {
    path: 'form/form-data'
  };
  activiti.post(options, function(error, statusCode, result) {
    res.statusCode = statusCode;
    res.send(result);
  }, req.body);
};

exports.updateTask = function(req, res) {
  var options = {
    path: 'runtime/tasks/' + req.params.taskId
  };
  activiti.put(options, function(error, statusCode, result) {
    res.statusCode = statusCode;
    res.send(result);
  }, req.body);
};
