var compose = require('composable-middleware');
var config = require('../../config');
var documents = require('../service/documents.controller');

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
							res.status(401);
							res.send("Not your document");
							res.end();
						}
					} catch (e) {
						res.status(404);
						res.send("There is no such document");
						res.end();
					}

				}
			});
	});
}

exports.isAuthenticated = isAuthenticated;
exports.isDocumentOwner = isDocumentOwner;