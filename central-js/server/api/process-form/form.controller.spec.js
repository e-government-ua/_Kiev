'use strict';

var should = require('should');
var appTest = require('../../app.spec');
var testRequest = appTest.testRequest;

describe('GET /api/process-form/sign/check', function () {
  function assertErrorResult(res){
    res.should.have.property('body');
    res.body.should.have.property('code');
    res.body.should.have.property('message');
  }

  function assertErrorNestedResult(res){
    res.should.have.property('body');
    res.body.should.have.property('code');
    res.body.should.have.property('message');
    res.body.should.have.property('nested');
  }

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

  it('should respond with 400 if no fileID', function (done) {
    var signCheck = testRequest.get('/api/process-form/sign/check');
    agent.attachCookies(signCheck);
    signCheck.expect(400).then(function (res) {
      assertErrorResult(res);
      done();
    }).catch(function (err) {
      done(err)
    });
  });

  it('should respond with 400 if no sURL', function (done) {
    var signCheck = testRequest.get('/api/process-form/sign/check?fileID=1122233');
    agent.attachCookies(signCheck);
    signCheck.expect(400).then(function (res) {
      assertErrorResult(res);
      done();
    }).catch(function (err) {
      done(err)
    });
  });

  it('should respond with 200 with sign object', function (done) {
    var signCheck = testRequest.get('/api/process-form/sign/check?fileID=1&sURL=http://test.region.service/');
    agent.attachCookies(signCheck);
    signCheck.expect(200).then(function (res) {
      done();
    }).catch(function (err) {
      done(err)
    });
  });

  it('should respond with 500 with error object', function (done) {
    var signCheck = testRequest.get('/api/process-form/sign/check?fileID=2&sURL=http://test.region.service/');
    agent.attachCookies(signCheck);
    signCheck.expect(500).then(function (res) {
      assertErrorNestedResult(res);
      done();
    }).catch(function (err) {
      done(err)
    });
  });
});
