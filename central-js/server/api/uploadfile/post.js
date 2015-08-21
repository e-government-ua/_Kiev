var express = require('express');
var router = express.Router();
var request = require('request');

var config = require('../../config/environment');
var proxy = require('../../components/proxy');

router.use(function(req, res, next) {
  proxy.upload(req, res, req.query.url);
});

module.exports = router;
