'use strict';

var chai = require('chai');
var should = require('should');
var formTemplate = require('./form.template');

suite('formTemplate', function() {
  suite('#createHhtml()', function() {
    test('should create simple correct html', function() {
      var formData = {};
      var resultHtml = formTemplate.createHtml(formData);
      chai.assert.notStrictEqual(resultHtml, undefined, 'result html cant\'t be undefined');
      chai.assert.notEqual(resultHtml, '', 'result html cant\'t be empty');
    });
  });
});

