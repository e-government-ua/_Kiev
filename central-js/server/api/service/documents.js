var express = require('express');
var router = express.Router();
var documents = require('./documents.controller');
var auth = require('../auth/auth.service');

router.get('/', auth.isAuthenticated(), documents.index);
router.get('/:nID', documents.getDocument);
router.get('/download/:nID', auth.isDocumentOwner(), documents.getDocumentFile);
router.post('/initialupload', documents.initialUpload);
router.get('/:nID/share', documents.shareDocument);

module.exports = router;