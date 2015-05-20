var express = require('express');
var router = express.Router();

router.use(function(req, res, next) {
	res.send('documents');
	res.end();
});

module.exports = router;