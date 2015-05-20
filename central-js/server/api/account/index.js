var express = require('express');
var router = express.Router();

router.use(function(req, res, next) {
	res.send('account');
	res.end();
});

module.exports = router;