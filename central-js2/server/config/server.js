'use strict';

var fs = require('fs');
var config = require('./environment');

module.exports = function(app) {
  var server = null;
  switch (config.server.protocol) {
    case 'https':
      var credentials = {
        key: fs.readFileSync(config.server.key).toString(),
        cert: fs.readFileSync(config.server.cert).toString()
      };

      server = require('https').createServer(credentials, app);
      break;
    case 'http':
    default:
      server = require('http').createServer(app);
  }

  server.listen(config.server.port, function() {
    console.log('Express server listening on %d', config.server.port);
  });
}
