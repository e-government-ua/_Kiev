'use strict';

var _ = require('lodash');
var activiti = require('../../components/activiti');

// Get list of tasks
exports.index = function(req, res) {
  var options = {
    path: 'runtime/tasks'
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
