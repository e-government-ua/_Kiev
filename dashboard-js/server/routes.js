/**
 * Main application routes
 */

'use strict';

var errors = require('./components/errors');

module.exports = function(app) {

  // Insert routes below
  app.use('/api/processes', require('./api/process'));
  app.use('/api/tasks', require('./api/tasks'));
  app.use('/api/reports', require('./api/reports'));
  app.use('/api/schedule', require('./api/schedule'));
  app.use('/api/escalations', require('./api/escalations'));
  app.use('/api/env', require('./api/env'));
  app.use('/auth', require('./auth'));

  // All undefined asset or api routes should return a 404
  app.route('/:url(api|auth|components|app|bower_components|assets)/*')
   .get(errors[404]);

  // All other routes should redirect to the index.html
  app.route('/*')
    .get(function(req, res) {
      res.sendfile(app.get('appPath') + '/index.html');
    });
};
