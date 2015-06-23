/**
 * Express configuration
 */
'use strict';

var session = require('cookie-session');
var bodyParser = require('body-parser');
var morgan = require('morgan');
var ejs = require('ejs');
var config = require('../config');

module.exports = function(app) {
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
};