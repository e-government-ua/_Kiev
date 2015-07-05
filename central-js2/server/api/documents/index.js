var express = require('express');
var router = express.Router();
var documents = require('./documents.controller');
var auth = require('../../auth/auth.service.js');

router.get('/', auth.isAuthenticated(), documents.index);
router.get('/:nID', documents.getDocument);
router.get('/download/:nID', auth.isDocumentOwner(), documents.getDocumentFile);
router.post('/initialUpload', auth.isAuthenticated(), documents.initialUpload);
router.get('/:nID/share', auth.isDocumentOwner(), documents.shareDocument);

module.exports = router;