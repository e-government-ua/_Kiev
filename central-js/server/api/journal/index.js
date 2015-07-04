var express = require('express');
var router = express.Router();
var journal = require('./journal.controller');
var auth = require('../../auth/auth.service.js');

router.get('/', auth.isAuthenticated(), journal.getHistoryEvents);
router.post('/', auth.isAuthenticated(), journal.setHistoryEvent);
//router.get('/:nID', journal.getHistoryEvent);

module.exports = router;