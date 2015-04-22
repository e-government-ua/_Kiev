'use strict';

var _ = require('lodash');
var activiti = require('../../components/activiti');

// Get list of tasks
exports.index = function(req, res) {
  var user = JSON.parse(req.cookies.user);
  var options = {
    path: 'runtime/tasks',
    query: {
      // 'candidateUser': user.id,
      'unassigned': true
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
