var express = require('express');
var router = express.Router();
var journal = require('./journal.controller');

router.get('/', journal.getHistoryEvents);
router.post('/', journal.setHistoryEvent);
//router.get('/:nID', journal.getHistoryEvent);

module.exports = router;