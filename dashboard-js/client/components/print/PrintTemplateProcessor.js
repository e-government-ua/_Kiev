'use strict';

angular.module('dashboardJsApp').factory('PrintTemplateProcessor', function ($sce) {
  return {
    processPrintTemplate: function (form, printTemplate, reg, fieldGetter) {
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
    },
    getPrintTemplate: function (form, originalPrintTemplate) {
      var printTemplate = this.processPrintTemplate(form, originalPrintTemplate, /(\[(\w+)])/g, function (item) {
        if (item.type === 'enum') {
          var enumID = item.value;
          return item.enumValues.filter(function (enumObj) {
            return enumObj.id === enumID;
          })[0].name;
        }
        else {
          return item.value;
        }
      });
      printTemplate = this.processPrintTemplate(form, printTemplate, /(\[label=(\w+)])/g, function (item) {
        return item.name;
      });
      return $sce.trustAsHtml(printTemplate);
    }
  }
});
