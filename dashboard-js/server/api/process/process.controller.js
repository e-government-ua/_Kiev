'use strict';

var activiti = require('../../components/activiti');

exports.index = function (req, res) {

  var query = {};
  query.size = 500;

  var options = {
    path: 'repository/process-definitions',
    query: query
  };

  activiti.get(options, function (error, statusCode, result) {
    if (error) {
      res.send(error);
    } else {
      res.status(200).send(result);
    }
  });
};

exports.getLoginBPs = function (req, res) {
  var user = JSON.parse(req.cookies.user);

  var query = {
    'sLogin' : user.id
  };
  var options = {
    path: 'rest/getLoginBPs',
    query: query
  };
  activiti.get(options, function (error, statusCode, result) {
    if (error) {
      res.send(error);
    } else {
      res.json(result);
    }
  });
};
