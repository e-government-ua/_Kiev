'use strict';

angular.module('dashboardJsApp').factory('PrintTemplate', function() {
  var PrintTemplate = function() {
    this.task = undefined;
    this.form = undefined;
    this.showPrintModal = false;
  };

  PrintTemplate.prototype.findPrintTemplate = function (form) {
    var printTemplateResult = form.filter(function (item) {
      return item.id === 'sBody0' || item.id === 'sBody';
    });
    return printTemplateResult.length !== 0 ? printTemplateResult[0].value : "";
  };

  PrintTemplate.prototype.containsPrintTemplate = function () {
    return this.form && this.findPrintTemplate(this.form) !== "";
  };

  PrintTemplate.prototype.processPrintTemplate = function (form, printTemplate, reg, fieldGetter) {
    var _printTemplate = printTemplate;
    var templatesAndIDs = _printTemplate.match(reg);
    if (templatesAndIDs) {

      var templates = templatesAndIDs.filter(function (item, i) {
        return i % 2 !== 0;
      });
      var ids = templatesAndIDs.filter(function (item, i) {
        return i !== 0 && i % 2 === 0;
      });

      templates.forEach(function (templateID, i) {
        var id = ids[i];
        if (id) {
          var item = form.filter(function (item) {
            return item.id === id;
          })[0];
          if (item) {
            _printTemplate = _printTemplate.replace(templateID, fieldGetter(item));
          }
        }
      });
      return this.processPrintTemplate(form, _printTemplate, reg, fieldGetter);
    } else {
      return _printTemplate;
    }
  };

  PrintTemplate.prototype.getPrintTemplate = function () {
    if (!this.form) {
      return "";
    } else {
      var printTemplate = this.findPrintTemplate(this.form);
      printTemplate = this.processPrintTemplate(this.form, printTemplate, /(\[(\w+)])/, function (item) {
        if (item.type === 'enum') {
          var enumID = item.value;
          return item.enumValues.filter(function (enumObj) {
            return enumObj.id === enumID;
          })[0].name;
        } else {
          return item.value;
        }
      });
      printTemplate = this.processPrintTemplate(this.form, printTemplate, /(\[label=(\w+)])/, function (item) {
        return item.name;
      });
      return printTemplate;
    }
  };

  return PrintTemplate;
});
