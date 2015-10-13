'use strict';

var chai = require('chai');
var should = require('should');
var formTemplate = require('./form.template');

suite('formTemplate', function() {
  suite('#createHhtml()', function() {
    test('should create simple correct html', function() {
      var templateData = {
        formData : [
          {name: 'fieldEnum1', type: 'enum', enumValues : ['e1','e2'], value: 'e1'},
          {name: 'fieldText1', type: 'text', value: 'textValue'}
        ],
        processName: 'Тестовый процес',
        businessKey: '12312333333',
        creationDate: ''+ new Date()
      };

      var resultHtml = formTemplate.createHtml(templateData);
      console.log(resultHtml);
      chai.assert.notStrictEqual(resultHtml, undefined, 'result html cant\'t be undefined');
      chai.assert.notEqual(resultHtml, '', 'result html cant\'t be empty');
    });
  });
});

