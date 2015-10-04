'use strict';

angular.module('dashboardJsApp').factory('PrintTemplate', function($sce, $q, tasks, PrintTemplateProcessor) {

  var loadedTemplates = {};

  var PrintTemplate = function() {
    this.task = undefined;
    this.form = undefined;
    this.showPrintModal = false;
  };

  PrintTemplate.prototype.findPrintTemplate = function (form, sCustomFieldID) {
    var s = ((sCustomFieldID!==null && sCustomFieldID !== undefined && sCustomFieldID!=='-') ? sCustomFieldID : 'sBody');
    var printTemplateResult = form.filter(function (item) {
      return item.id === s;
    });
    return printTemplateResult.length !== 0 ? printTemplateResult[0].name.replace(/\[pattern(.+)\].*/, '$1') : "";
  };

  PrintTemplate.prototype.aPatternPrintNew = function (form, sCustomFieldID) {
    var s = ((sCustomFieldID!==null && sCustomFieldID !== undefined && sCustomFieldID!=='-') ? sCustomFieldID : 'sBody');
    var printTemplateResult = null;
    if(this.form){
        printTemplateResult = this.form.filter(function (item) {//form
          return item.id && item.id.indexOf('sBody') >= 0 && item.value !== "";//item.id === s
        });
    }
    return (printTemplateResult!==null && printTemplateResult.length !== 0) ? printTemplateResult : [];
  };

  PrintTemplate.prototype.containsPrintTemplate = function () {
    if(this.form){
        var printTemplateResult = this.form.filter(function (item) {
          return item && item.id && item.id.indexOf("sBody")>=0;
        });
        return printTemplateResult.length > 0 && printTemplateResult[0].name !== "";
    }else{
        return false;
    }
  };

  PrintTemplate.prototype.aPatternPrint = function () {
    var form = this.form;
    var a=[];

    var aForm = [];

    if(this.form){
        aForm = this.form.filter(function (item) {
          return item && item.id && item.id.indexOf("sBody")>=0;
        });
    }

    if(aForm){
        if (aForm.length > 0) {
            aForm.forEach(function (item, i) {//this.form
              if(item){
                if(item.id && item.id.indexOf('sBody') >= 0 && item.value !== "" ){
                    a.push({sID:item.id,sLabel:item.name});
                }
              }
            });
        }
    }

    if(a.length===0){
        a=a.concat([{sID:"sBody_0".id,sLabel:"-"}]);
    }

    return a;
  };

  PrintTemplate.prototype.processPrintTemplate = function (form, printTemplate, reg, fieldGetter) {
    return PrintTemplateProcessor.processPrintTemplate(form, printTemplate, reg, fieldGetter);
  };

  PrintTemplate.prototype.getPrintTemplate = function () {//sCustomFieldID
    if (!this.form) {
      return "";
    } 
    else {

      var sCustomFieldID = $('.aPatternPrint').val();
      if(sCustomFieldID === null || sCustomFieldID === undefined || sCustomFieldID === "" || sCustomFieldID === "-"){
//          alert("Не обран шаблон для друку!");
          //return;
      }

      var printTemplateName = this.findPrintTemplate(this.form, sCustomFieldID);
      if (!angular.isDefined(loadedTemplates[printTemplateName])) {
        loadedTemplates[printTemplateName] = 'Завантаження форми...';
        tasks.getPatternFile(printTemplateName).then(function(result){
          loadedTemplates[printTemplateName] = result;
        }, function() {
          loadedTemplates[printTemplateName] = 'Помилка завантаження форми!';
        });
      }
      return PrintTemplateProcessor.getPrintTemplate(this.form, loadedTemplates[printTemplateName]);
    }
  };

  return PrintTemplate;
});
