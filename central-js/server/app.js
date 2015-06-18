var express = require('express');
var session = require('cookie-session');
var ejs = require('ejs');
var bodyParser = require('body-parser');
var multer = require('multer');
var fs = require('fs');
var morgan = require('morgan');
var config = require('./config');

var app = express();

app.engine('html', ejs.renderFile);
app.use(morgan(
	config.server.debug ?
	'dev' :
	':method :url :status :response-time ms - :res[content-length]'));
app.use(bodyParser.json()); // for parsing application/json
app.use(bodyParser.urlencoded({
	extended: true
})); // for parsing application/x-www-form-urlencoded
app.use(session({
	secret: config.server.session.secret,
	keys: config.server.session.keys,
	secure: config.server.session.secure,
	signed: true
}));
app.use(require('./routes'));

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

module.exports = server;