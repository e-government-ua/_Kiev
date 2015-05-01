var express = require('express');
var router = express.Router();

router.use('/', express.static(__dirname + '../../client/build/'));

router.param('code', function (req, res, next, id) {
  console.log('CALLED ONLY ONCE');
  next();
})

router.use('/api/account', require('./api/account/index'));
router.get('/api/bankid/login', require('./api/bankid/login'));
router.use('/api/bankid/account', require('./api/bankid/account'));
router.use('/api/documents', require('./api/documents/index'));
router.use('/api/login', require('./api/login/index'));
router.use('/api/logout', require('./api/logout/index'));
router.use('/api/places', require('./api/places/index'));
router.use('/api/process-definitions', require('./api/process-definitions/index'));
router.use('/api/service', require('./api/service/index'));
router.use('/api/services', require('./api/services/index'));

router.use('/', function (req, res, next) {
	res.render('../../client/build/index.html');
	next();
});

module.exports = router;