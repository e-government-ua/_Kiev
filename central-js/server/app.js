var express = require('express');
var ejs = require('ejs');

var config = require('./config');

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