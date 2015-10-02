'use strict';

angular.module('dashboardJsApp').service('PrintTemplateService', ['tasks', 'PrintTemplateProcessor', '$q', function(tasks, PrintTemplateProcessor, $q) {
  // TODO: move code from PrintTemplateProcessor here
  // helper function to get path to a print template based on it's ID
  function findPrintTemplate (form, sCustomFieldID) {
    var s = ((sCustomFieldID!==null && sCustomFieldID !== undefined && sCustomFieldID!=='-') ? sCustomFieldID : 'sBody');
    var printTemplateResult = form.filter(function (item) {
      return item.id === s;
    });
    var retval = printTemplateResult.length !== 0 ? printTemplateResult[0].name.replace(/\[pattern(.+)\].*/, '$1') : "";
    console.log('findPrintTemplate returns', retval);
    return retval;
  };
  var service = {
    getTemplates: function(form) {
      console.log('getTemplates method of PrintTemplateService');
      console.log('form is', form);
      if (!form) {
        return [];
      }
      var templates = form.filter(function (item) {
        var result = false;
        if (item.id && item.id.indexOf('sBody') >= 0) {
          result = true;
          // На дашборде при вытягивани для формы печати пути к патерну, из значения поля -
          // брать название для каждого элемента комбобокса #792
          // https://github.com/e-government-ua/i/issues/792
          if (item.value && item.value.trim().length > 0 && item.value.length <= 100){
            item.displayTemplate = item.value;
          } else {
            item.displayTemplate = item.name;
          }
        }
        return result;
      });
      console.log('returned templates', templates);
      return templates;
    },
    getPrintTemplate: function(task, form) {
      var deferred = $q.defer();
      if (!form) {
        return "";
      } else {
        // FIXME: this should be fixed. selected pattern should be passed as a parameter
        var sCustomFieldID = $('.aPatternPrint').val();
        var printTemplateName = findPrintTemplate(form, sCustomFieldID);
        if (!angular.isDefined(loadedTemplates[printTemplateName])) {
          tasks.getPatternFile(printTemplateName).then(function(originalTemplate){
            var parsedForm = PrintTemplateProcessor.getPrintTemplate(task, form, originalTemplate);
            deferred.resolve(parsedForm);
          }, function() {
            deferred.reject('Помилка завантаження форми');
          });
        }
        return deferred.promise;
      }
    }

  };
  return service;
}]);
