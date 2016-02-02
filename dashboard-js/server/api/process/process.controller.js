'use strict';

var activiti = require('../../components/activiti');
//var logger = require('../../components/logger').setup();
      //logger.info('Express server listening on %d, in %s mode', config.port, app.get('env'));

exports.index = function (req, res) {

  var query = {};
  //query.size = 750;
  query.size = 1500;
  query.latest = true;

  var options = {
    path: 'repository/process-definitions',
    query: query
  };

  activiti.get(options, function (error, statusCode, result) {
    if (error) {
      res.send(error);
    } else {
        
      //logger.info('Express server listening on %d, in %s mode', config.port, app.get('env'));
//      logger.info('result='+result);
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
    path: 'action/task/getLoginBPs',
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
