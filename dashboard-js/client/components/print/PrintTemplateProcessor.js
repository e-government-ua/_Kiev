'use strict';

angular.module('dashboardJsApp').factory('PrintTemplateProcessor', function ($sce, Auth) {
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
              var sValue = fieldGetter(item);
              if (sValue === null){
                  sValue = "";
              }
              _printTemplate = _printTemplate.replace(templateID, sValue);//fieldGetter(item)
            }
          }
        });
      }
      return _printTemplate;
    },
    populateSystemTag: function (printTemplate, tag, replaceWith) {
      var replacement = replaceWith();
      return printTemplate.replace(new RegExp(this.escapeRegExp(tag), 'g'), replacement);
    },
    escapeRegExp: function (str) {
      return str.replace(/([.*+?^=!:${}()|\[\]\/\\])/g, "\\$1");
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
      printTemplate = this.populateSystemTag(printTemplate, "[sUserInfo]", function () {
        var user = Auth.getCurrentUser();
        return user.lastName + ' ' + user.firstName ;
      });
      return $sce.trustAsHtml(printTemplate);
    }
  };
});
