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
