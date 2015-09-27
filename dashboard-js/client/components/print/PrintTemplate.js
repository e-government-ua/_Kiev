'use strict';

angular.module('dashboardJsApp').factory('PrintTemplate', function($sce, $q, tasks, PrintTemplateProcessor) {

  var loadedTemplates = {};

  var PrintTemplate = function() {
    this.task = undefined;
    this.form = undefined;
    this.showPrintModal = false;
  };

  PrintTemplate.prototype.findPrintTemplate = function (form, sCustomFieldID) {
//    console.log("[findPrintTemplate]")
    var s = ((sCustomFieldID!==null && sCustomFieldID !== undefined && sCustomFieldID!=='-') ? sCustomFieldID : 'sBody');
//    console.log("[findPrintTemplate]s="+s)
    //$('.aPatternPrint').val();
    var printTemplateResult = form.filter(function (item) {
      return item.id === s;
    });
//    console.log("[findPrintTemplate]printTemplateResult.length="+printTemplateResult.length)
    return printTemplateResult.length !== 0 ? printTemplateResult[0].name.replace(/\[pattern(.+)\].*/, '$1') : "";
  };


  PrintTemplate.prototype.aPatternPrintNew = function (form, sCustomFieldID) {
//    console.log("[aPatternPrintNew]")
    var s = ((sCustomFieldID!==null && sCustomFieldID !== undefined && sCustomFieldID!=='-') ? sCustomFieldID : 'sBody');
//    console.log("[aPatternPrintNew]s="+s)
    //$('.aPatternPrint').val();
    var printTemplateResult = null;
    if(this.form){
        printTemplateResult = this.form.filter(function (item) {//form
            //if(item.id && item.id.indexOf('sBody') >= 0 && item.value !== "" ){
          return item.id && item.id.indexOf('sBody') >= 0 && item.value !== "";//item.id === s
        });
//        console.log("[aPatternPrintNew]printTemplateResult.length="+printTemplateResult.length)
    }
    //return printTemplateResult.length !== 0 ? printTemplateResult[0].value : "";
    return (printTemplateResult!==null && printTemplateResult.length !== 0) ? printTemplateResult : [];
  };


  /*
  PrintTemplate.prototype.containsPrintTemplate = function () {
    return this.form && this.findPrintTemplate(this.form) !== "";
  };
  */

  //PrintTemplate.prototype.containsPrintTemplate = function (sCustomFieldID) {
  PrintTemplate.prototype.containsPrintTemplate = function () {
    if(this.form){
//        console.log("[containsPrintTemplate]")
        var printTemplateResult = this.form.filter(function (item) {
          return item && item.id && item.id.indexOf("sBody")>=0;
        });
//        console.log("[containsPrintTemplate]printTemplateResult.length="+printTemplateResult.length)
        return printTemplateResult.length > 0 && printTemplateResult[0].name !== "";
    }else{
//        console.log("[containsPrintTemplate]")
        return false;
    }
  };

  PrintTemplate.prototype.aPatternPrint = function () {
//    console.log("[aPatternPrint]")
    var form = this.form;
    var a=[];
    /*if(form){
        form.forEach(function (item, i) {
          if(item && item.id && item.id.indexOf('sBody') >= 0 && item.value !== "" ){
              a=a.concat([{sID:item.id,sLabel:item.name}])
          }
        });
    }*/
//    console.log("[aPatternPrint]a.length="+a.length)

    var aForm = [];

    if(this.form){
        aForm = this.form.filter(function (item) {
          return item && item.id && item.id.indexOf("sBody")>=0;
        });
    }

//    console.log("[aPatternPrint]0_aForm.length="+aForm.length);

    //if(this.form){
    if(aForm){
    //if(form){
//        console.log("[aPatternPrint]0_");
        if (aForm.length > 0) {
            aForm.forEach(function (item, i) {//this.form
//                console.log("[aPatternPrint]i="+i);
//                console.log("[aPatternPrint]item!=null:"+(item!=null));
              if(item){
                //if(item && item.id && item.id.indexOf('sBody') >= 0 && item.value !== "" ){
//                console.log("[aPatternPrint]item.id="+item.id);
//                console.log("[aPatternPrint]item.value="+item.value);
                if(item.id && item.id.indexOf('sBody') >= 0 && item.value !== "" ){
//                    console.log("[aPatternPrint]ADD");
//                    a=a.concat([{sID:item.id,sLabel:item.name}])
                    a.push({sID:item.id,sLabel:item.name});
                }
              }
            });
        }
    }

//    console.log("[aPatternPrint]a.length(before)="+a.length)
    if(a.length===0){
        a=a.concat([{sID:"sBody_0".id,sLabel:"-"}])
    }
//    console.log("[aPatternPrint]a.length(after)="+a.length)

    return a;
  };


  PrintTemplate.prototype.processPrintTemplate = function (form, printTemplate, reg, fieldGetter) {
    return PrintTemplateProcessor.processPrintTemplate(form, printTemplate, reg, fieldGetter);
  };

  PrintTemplate.prototype.getPrintTemplate = function () {//sCustomFieldID
    if (!this.form) {
      return "";
    } else {

      /*if(sCustomFieldID === null || sCustomFieldID === undefined){
        sCustomFieldID = $('.aPatternPrint').val();
      }*/
//      console.log("[getPrintTemplate]1");
      var sCustomFieldID = $('.aPatternPrint').val();
//      console.log("[getPrintTemplate]sCustomFieldID="+sCustomFieldID);
      if(sCustomFieldID === null || sCustomFieldID === undefined || sCustomFieldID === "" || sCustomFieldID === "-"){
//          alert("Не обран шаблон для друку!");
          //return;
      }
      //console.log("[getPrintTemplate]sCustomFieldID="+sCustomFieldID);

//      console.log("[getPrintTemplate]2");
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
