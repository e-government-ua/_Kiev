var compose = require('composable-middleware');
var config = require('../../config');
var documents = require('../service/documents.controller');

function isAuthenticated() {
	return compose().use(function(req, res, next) {
		if (req.session && req.session.subject) {
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
					res.code(response.statusCode);
					res.send(error);
					res.end();
				} else {
					var document = body.document;
					if (document.oSubject.nID === req.session.subject.nID) {
						next();
					} else {
						res.code(401);
						res.send("Not your document");
						res.end();
					}					
				}
			});
	});
}

exports.isAuthenticated = isAuthenticated;
exports.isDocumentOwner = isDocumentOwner;