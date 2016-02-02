'use strict';

//angular.module('dashboardJsApp').service('PrintTemplateService', ['tasks', 'PrintTemplateProcessor', '$q', '$templateRequest', '$lunaService', function(tasks, PrintTemplateProcessor, $q, $templateRequest, lunaService) {
angular.module('dashboardJsApp').service('PrintTemplateService', ['tasks', 'PrintTemplateProcessor', '$q', '$templateRequest', function(tasks, PrintTemplateProcessor, $q, $templateRequest) {
  // TODO: move code from PrintTemplateProcessor here
  // helper function to get path to a print template based on it's ID
  function findPrintTemplate (form, sCustomFieldID) {
    var s = ((sCustomFieldID!==null && sCustomFieldID !== undefined && sCustomFieldID!=='-') ? sCustomFieldID : 'sBody');
    var printTemplateResult = form.filter(function (item) {
      return item.id === s;
    });
    var retval = printTemplateResult.length !== 0 ? printTemplateResult[0].name.replace(/\[pattern(.+)\].*/, '$1') : "";
    return retval;
  };
  // object for caching loaded templates
  var loadedTemplates = {};
  var service = {
    // method to get list of available print templates based on task form.
    getTemplates: function(form) {
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
      return templates;
    },
    // method to get parsed template 
//    getPrintTemplate: function(task, form, printTemplateName, lunaService) {
    getPrintTemplate: function(task, form, printTemplateName) {
      var deferred = $q.defer();
      if (!printTemplateName) {
        deferred.reject('Неможливо завантажити форму: немає назви');
        return deferred.promise;
      }
      // normal flow: load raw template and then process it
      var parsedForm;
      if (!angular.isDefined(loadedTemplates[printTemplatePath])) {
        var printTemplatePath = findPrintTemplate(form, printTemplateName);
        tasks.getPatternFile(printTemplatePath).then(function(originalTemplate){
          // cache template
          loadedTemplates[printTemplatePath] = originalTemplate;
          //parsedForm = PrintTemplateProcessor.getPrintTemplate(task, form, originalTemplate, lunaService);
          parsedForm = PrintTemplateProcessor.getPrintTemplate(task, form, originalTemplate);
          deferred.resolve(parsedForm);
        }, function() {
          deferred.reject('Помилка завантаження форми');
        });
      } else {
        // resolve deferred in case the form was cached
        //parsedForm = PrintTemplateProcessor.getPrintTemplate(task, form, loadedTemplates[printTemplatePath], lunaService);
        parsedForm = PrintTemplateProcessor.getPrintTemplate(task, form, loadedTemplates[printTemplatePath]);
        deferred.resolve(parsedForm);
      }
      // return promise
      return deferred.promise;
    }
  };
  return service;
}]);
