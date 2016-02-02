var express = require('express');
var router = express.Router();
var controller = require('./catalog.controller.js');
var auth = require('../../auth/auth.service.js');

router.get('/', controller.getServicesTree);
router.post('/', auth.isAuthenticated(), controller.setServicesTree);
router.delete('/category', auth.isAuthenticated(), controller.removeCategory);
router.delete('/subcategory', auth.isAuthenticated(), controller.removeSubcategory);
router.delete('/service', auth.isAuthenticated(), controller.removeService);
router.delete('/servicesTree', auth.isAuthenticated(), controller.removeServicesTree);

module.exports = router;
