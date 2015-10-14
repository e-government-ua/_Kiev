'use strict';

var chai = require('chai');
var should = require('should');
var formTemplate = require('./form.template');

suite('formTemplate', function () {
  suite('#createHhtml()', function () {
    test('should create simple correct html', function () {
      var templateData = {
        formProperties: [
          {
            "id": "bankId_scan_inn", "name": "тест скан инн", "type": "file",
            "value": '1111123333333', "readable": true, "writable": true, "required": false,
            "datePattern": null, "enumValues": []
          },
          {
            "id": "bankId_scan_passport", "name": "скан паспорта", "type": "file",
            "value": 'DD1123333', "readable": true, "writable": true, "required": false,
            "datePattern": null, "enumValues": []
          },
          {
            id: 'fieldEnum1', name: 'some fieldEnum1', type: 'enum', enumValues: ['e1', 'e2'], value: 'e1'
          },
          {
            id: 'fieldText1', name: 'some fieldText1', type: 'text', value: 'textValue'
          }
        ],
        processName: 'Тестовый процес',
        businessKey: '12312333333',
        creationDate: '' + new Date()
      };

      var resultHtml = formTemplate.createHtml(templateData);
      console.log(resultHtml);
      chai.assert.notStrictEqual(resultHtml, undefined, 'result html cant\'t be undefined');
      chai.assert.notEqual(resultHtml, '', 'result html cant\'t be empty');
      //TODO add more asssert for html result. Test is not completed
    });
  });
});

