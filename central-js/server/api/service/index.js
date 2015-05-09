var express = require('express');
var router = express.Router();

router.use(function(req, res, next) {
	res.send('service');
	res.end();
});

module.exports = router;