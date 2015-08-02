/**
 * Main application routes
 */

'use strict';

var errors = require('./components/errors');
var path = require('path');

module.exports = function(app) {

  // Insert routes below
  app.use('/auth', require('./auth'));
  app.use('/api/bankid', require('./api/bankid'));
  app.use('/api/documents', require('./api/documents'));
  app.use('/api/journal', require('./api/journal'));
  app.use('/api/order', require('./api/order'));
  app.use('/api/places', require('./api/places/index'));
  app.use('/api/process-definitions', require('./api/process-definitions/index'));
  app.use('/api/process-form', require('./api/process-form'));
  app.get('/api/service', require('./api/service/index'));
  app.use('/api/service/flow', require('./api/service/flow'));
  app.use('/api/messages', require('./api/messages/index'));
  app.use('/api/services', require('./api/services'));
  app.post('/api/uploadfile', require('./api/uploadfile/post'));
  app.use('/api/organs', require('./api/organs'));
  app.use('/api/countries', require('./api/countries'));

  // All undefined asset or api routes should return a 404
  app.route('/:url(api|auth|components|app|bower_components|assets)/*')
   .get(errors[404]);

  // All other routes should redirect to the index.html
  var indexHtml = app.get('appPath') + '/index.html';
  app.route('/*')
    .get(function(req, res) {
      res.sendFile(indexHtml);
    });
};
