'use strict';

angular.module('dashboardJsApp').factory('PrintTemplate', function($sce) {
  var PrintTemplate = function() {
    this.task = undefined;
    this.form = undefined;
    this.showPrintModal = false;
  };

  PrintTemplate.prototype.findPrintTemplate = function (form) {
    var printTemplateResult = form.filter(function (item) {
      return item.id === 'sBody';
    });
    return printTemplateResult.length !== 0 ? printTemplateResult[0].value : "";
  };

  PrintTemplate.prototype.containsPrintTemplate = function () {
    return this.form && this.findPrintTemplate(this.form) !== "";
  };

  PrintTemplate.prototype.processPrintTemplate = function (form, printTemplate, reg, fieldGetter) {
    var _printTemplate = printTemplate;
    var templates = [], ids = [], found;
    while (found = reg.exec(_printTemplate)) {
      templates.push(found[1]);
      ids.push(found[2]);
    }
    if (templates.length > 0 && ids.length > 0) {
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
    }
    return _printTemplate;
  };

  PrintTemplate.prototype.getPrintTemplate = function () {
    if (!this.form) {
      return "";
    } else {
      var printTemplate = this.findPrintTemplate(this.form);
      printTemplate = this.processPrintTemplate(this.form, printTemplate, /(\[(\w+)])/g, function (item) {
        if (item.type === 'enum') {
          var enumID = item.value;
          return item.enumValues.filter(function (enumObj) {
            return enumObj.id === enumID;
          })[0].name;
        } else {
          return item.value;
        }
      });
      printTemplate = this.processPrintTemplate(this.form, printTemplate, /(\[label=(\w+)])/g, function (item) {
        return item.name;
      });
      return $sce.trustAsHtml(printTemplate);
    }
  };

  return PrintTemplate;
});
