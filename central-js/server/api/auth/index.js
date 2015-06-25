var express = require('express');
var router = express.Router();
var auth = require('../auth/auth.service');

router.get('/check', auth.isAuthenticated(), function(req,res){
    res.status(200);
    res.end();
});

module.exports = router;