'use strict';

angular.module('dashboardJsApp').factory('PrintTemplateProcessor', ['$sce', 'Auth', '$filter', function ($sce, Auth, $filter) {
  return {
    processPrintTemplate: function (task, form, printTemplate, reg, fieldGetter) {
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
      var replacement;
      if (replaceWith instanceof Function) {
        replacement = replaceWith();
      } else {
        replacement = replaceWith;
      }
      return printTemplate.replace(new RegExp(this.escapeRegExp(tag), 'g'), replacement);
    },
    escapeRegExp: function (str) {
      return str.replace(/([.*+?^=!:${}()|\[\]\/\\])/g, "\\$1");
    },
    getPrintTemplate: function (task, form, originalPrintTemplate) {
      // helper function for getting field value for different types of fields
      function fieldGetter(item) {
        if (item.type === 'enum') {
          var enumID = item.value;
          var enumItem = item.enumValues.filter(function (enumObj) {
            return enumObj.id === enumID;
          })[0];
          if (enumItem) {
            return enumItem.name;
          }
        }
        else {
          return item.value;
        }
      }
      var printTemplate = this.processPrintTemplate(task, form, originalPrintTemplate, /(\[(\w+)])/g, fieldGetter);
      // What is this for? // Sergey P
      printTemplate = this.processPrintTemplate(task, form, printTemplate, /(\[label=(\w+)])/g, function (item) {
        return item.name;
      });
      printTemplate = this.populateSystemTag(printTemplate, "[sUserInfo]", function () {
        var user = Auth.getCurrentUser();
        return user.lastName + ' ' + user.firstName ;
      });
      printTemplate = this.populateSystemTag(printTemplate, "[sDateCreate]", $filter('date')(task.createTime, 'yyyy-MM-dd HH:mm'));
      return $sce.trustAsHtml(printTemplate);
    }
  }
}]);
