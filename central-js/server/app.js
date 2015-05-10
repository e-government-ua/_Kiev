var express = require('express');
var ejs = require('ejs');
var fs = require('fs');

var config = require('./config');
try {
    var local_config = require('./local_config');
    var _ = require('lodash');
    _.extend(config, local_config);
}
catch( e ) {
    if ( e.code === 'MODULE_NOT_FOUND' ) {
        // do nothing
    }
}

var app = express();
app.engine('html', ejs.renderFile);
app.use(require('./routes'));

var server = null;
switch(config.server.protocol) {
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

module.exports = server;