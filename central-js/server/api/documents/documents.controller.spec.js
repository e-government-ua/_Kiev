'use strict';

var should = require('should');

require('./documents.mock.js');
var appTest = require('../../app.spec');
var testRequest = appTest.testRequest;

describe('GET /api/documents/search/getDocumentTypes', function () {
  it('should respond with 200', function (done) {
    testRequest
      .get('/api/documents/search/getDocumentTypes')
      .expect(200)
      .end(function (err, res) {
        if (err) return done(err);
        done();
      });
  });
});

describe('GET /api/documents', function () {
  var agent;
  before(function (done) {
    appTest.loginWithBankID(function (error, loginAgent) {
      if (error) {
        done(error)
      } else {
        agent = loginAgent;
        done();
      }
    });
  });

  it('should respond with 200', function (done) {
    var getDocuments = testRequest.get('/api/documents');
    agent.attachCookies(getDocuments);

    getDocuments
      .expect(200)
      .end(function (err, res) {
        if (err) return done(err);
        done();
      });
  });
});
