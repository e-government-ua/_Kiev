var express = require('express');
var router = express.Router();
var documents = require('./documents.controller');
var auth = require('../../auth/auth.service.js');

router.get('/', auth.isAuthenticated(), documents.index);
router.get('/:nID', documents.getDocument);
router.get('/download/:nID', auth.isDocumentOwner(), documents.getDocumentFile);
router.post('/initialUpload', auth.isAuthenticated(), documents.initialUpload);
router.post('/upload', auth.isAuthenticated(), documents.upload);
router.get('/:nID/share', auth.isDocumentOwner(), documents.shareDocument);
router.get('/search/download/:nID/:sCode_DocumentAccess/:nID_DocumentOperator_SubjectOrgan/:nID_DocumentType/:sPass', documents.getDocumentAbstract);
router.get('/search/getDocumentTypes', documents.getDocumentTypes);
router.get('/search/getDocumentOperators', documents.getDocumentOperators);
router.post('/search/searchDocument', documents.searchDocument);

module.exports = router;
