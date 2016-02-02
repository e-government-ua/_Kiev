var express = require('express');
var router = express.Router();

var messages = require('./index.controller');

router.get('/', messages.get);
router.post('/', messages.post);

router.get('/feedback', messages.findFeedback);
router.post('/feedback', messages.postFeedback);

router.get('/service', messages.findServiceMessages);
router.post('/service', messages.postServiceMessage);

module.exports = router;
