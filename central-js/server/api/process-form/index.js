var express = require('express');
var router = express.Router();
var form = require('./form.controller');
var auth = require('../../auth/auth.service.js');

router.get('/', auth.isAuthenticated(), form.index);
router.post('/', auth.isAuthenticated(), form.submit);
router.get('/sign', auth.isAuthenticated(), form.signForm);
router.use('/sign/callback', auth.isAuthenticated(), form.signFormCallback);
router.get('/sign/check', auth.isAuthenticated(), form.signCheck);
router.post('/save', auth.isAuthenticated(), form.saveForm);
router.get('/load', auth.isAuthenticated(), form.loadForm);
router.post('/scansUpload', auth.isAuthenticated(), form.scanUpload);

module.exports = router;
