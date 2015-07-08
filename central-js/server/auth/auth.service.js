'use strict';

var compose = require('composable-middleware');
var config = require('../config/environment');
var documents = require('../api/documents/documents.controller.js');

function isAuthenticated() {
	return compose().use(function(req, res, next) {
		if (req.session && req.session.access && req.session.subject) {
			next();
		} else {
			res.status(401);
			res.end();
		}
	});
}

function isDocumentOwner() {
	return compose().use(isAuthenticated()).use(function(req, res, next) {
		documents.getDocumentInternal(req, res,
			function(error, response, body) {
				if (error) {
					res.status(response.statusCode);
					res.send(error);
					res.end();
				} else {
					try {
						var document = JSON.parse(body);
						if (document.oSubject && document.oSubject.nID === req.session.subject.nID) {
							next();
						} else {
							res.status(401).send({error: "User can have access only to his own documents"});
						}
					} catch (e) {
						res.status(404).send({error: "There is no such document"});
					}

				}
			});
	});
}

exports.isAuthenticated = isAuthenticated;
exports.isDocumentOwner = isDocumentOwner;
