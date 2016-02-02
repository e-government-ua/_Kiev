'use strict';
var _ = require('lodash');
var activiti = require('../../components/activiti');

module.exports.getCountryList = function (req, res) {
  activiti.sendGetRequest(req, res, '/object/place/getCountries', _.extend(req.query, req.params));
};

module.exports.getCountry = function (req, res) {
  activiti.sendGetRequest(req, res, '/object/place/getCountry', _.extend(req.query, req.params));
};
