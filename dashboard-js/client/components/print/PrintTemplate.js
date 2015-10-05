'use strict';

angular.module('dashboardJsApp').service('PrintTemplateService', ['tasks', 'PrintTemplateProcessor', '$q', '$templateRequest', function(tasks, PrintTemplateProcessor, $q, $templateRequest) {
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
  var loadedTemplates = {};
  var service = {
    getTemplates: function(form) {
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
    getPrintTemplate: function(task, form, printTemplateName) {
      var deferred = $q.defer();
      var parsedForm;
      if (!printTemplateName) {
        // load default template
        $templateRequest('/app/tasks/form-buttons/print-dialog-default.html')
          .then(function(originalTemplate) {
              parsedForm = PrintTemplateProcessor.getPrintTemplate(task, form, originalTemplate);
              deferred.resolve(parsedForm);
            }, function() {
              deferred.reject('Помилка завантаження стандартної форми');
            }
          );
        return deferred.promise;
      }
      if (!angular.isDefined(loadedTemplates[printTemplatePath])) {
        var printTemplatePath = findPrintTemplate(form, printTemplateName);
        tasks.getPatternFile(printTemplatePath).then(function(originalTemplate){
          // cache template
          loadedTemplates[printTemplatePath] = originalTemplate;
          var parsedForm = PrintTemplateProcessor.getPrintTemplate(task, form, originalTemplate);
          deferred.resolve(parsedForm);
        }, function() {
          deferred.reject('Помилка завантаження форми');
        });
      } else {
        // resolve deferred in case the form was cached
        parsedForm = PrintTemplateProcessor.getPrintTemplate(task, form, loadedTemplates[printTemplatePath]);
        deferred.resolve(parsedForm);
      }
      return deferred.promise;
    }

  };
  return service;
}]);
