'use strict';

angular.module('dashboardJsApp').factory('PrintTemplate', function($sce) {
  var PrintTemplate = function() {
    this.task = undefined;
    this.form = undefined;
    this.showPrintModal = false;
  };

  PrintTemplate.prototype.findPrintTemplate = function (form, sCustomFieldID) {
    console.log("[findPrintTemplate]")
    var s = ((sCustomFieldID!==null && sCustomFieldID !== undefined && sCustomFieldID!=='-') ? sCustomFieldID : 'sBody');
    console.log("[findPrintTemplate]s="+s)
    //$('.aPatternPrint').val();
    var printTemplateResult = form.filter(function (item) {
      return item.id === s;
    });
    console.log("[findPrintTemplate]printTemplateResult.length="+printTemplateResult.length)
    return printTemplateResult.length !== 0 ? printTemplateResult[0].value : "";
  };

  
  PrintTemplate.prototype.aPatternPrintNew = function (form, sCustomFieldID) {
    console.log("[aPatternPrintNew]")
    var s = ((sCustomFieldID!==null && sCustomFieldID !== undefined && sCustomFieldID!=='-') ? sCustomFieldID : 'sBody');
    console.log("[aPatternPrintNew]s="+s)
    //$('.aPatternPrint').val();
    var printTemplateResult = null;
    if(this.form){
        printTemplateResult = this.form.filter(function (item) {//form
            //if(item.id && item.id.indexOf('sBody') >= 0 && item.value !== "" ){
          return item.id && item.id.indexOf('sBody') >= 0 && item.value !== "";//item.id === s
        });
        console.log("[aPatternPrintNew]printTemplateResult.length="+printTemplateResult.length)
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
        console.log("[containsPrintTemplate]")
        var printTemplateResult = this.form.filter(function (item) {
          return item && item.id && item.id.indexOf("sBody")>=0;
        });
        console.log("[containsPrintTemplate]printTemplateResult.length="+printTemplateResult.length)
        return printTemplateResult.length > 0 && printTemplateResult[0].value !== "";
    }else{
        console.log("[containsPrintTemplate]")
        return false;
    }
  };

  PrintTemplate.prototype.aPatternPrint = function () {
    console.log("[aPatternPrint]")
    var form = this.form;
    var a=[];
    /*if(form){
        form.forEach(function (item, i) {
          if(item && item.id && item.id.indexOf('sBody') >= 0 && item.value !== "" ){
              a=a.concat([{sID:item.id,sLabel:item.name}])
          }
        });    
    }*/
    console.log("[aPatternPrint]a.length="+a.length)
    
    var aForm = [];
    
    if(this.form){
        aForm = this.form.filter(function (item) {
          return item && item.id && item.id.indexOf("sBody")>=0;
        });
    }

    console.log("[aPatternPrint]0_aForm.length="+aForm.length);
      
    //if(this.form){
    if(aForm){
    //if(form){
        console.log("[aPatternPrint]0_");
        if (aForm.length > 0) {
            aForm.forEach(function (item, i) {//this.form
                console.log("[aPatternPrint]i="+i);
                console.log("[aPatternPrint]item!=null:"+(item!=null));
              if(item){
                //if(item && item.id && item.id.indexOf('sBody') >= 0 && item.value !== "" ){
                console.log("[aPatternPrint]item.id="+item.id);
                console.log("[aPatternPrint]item.value="+item.value);
                if(item.id && item.id.indexOf('sBody') >= 0 && item.value !== "" ){
                    console.log("[aPatternPrint]ADD");
//                    a=a.concat([{sID:item.id,sLabel:item.name}])
                    a.push({sID:item.id,sLabel:item.name});
                }
              }
            });               
        }
    }
    
    console.log("[aPatternPrint]a.length(before)="+a.length)
    if(a.length===0){
        a=a.concat([{sID:"sBody_0".id,sLabel:"-"}])
    }
    console.log("[aPatternPrint]a.length(after)="+a.length)
    
    return a;
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

  PrintTemplate.prototype.getPrintTemplate = function () {//sCustomFieldID
    if (!this.form) {
      return "";
    } else {
        
      /*if(sCustomFieldID === null || sCustomFieldID === undefined){
        sCustomFieldID = $('.aPatternPrint').val();
      }*/
      console.log("[getPrintTemplate]1");
      var sCustomFieldID = $('.aPatternPrint').val();
      console.log("[getPrintTemplate]sCustomFieldID="+sCustomFieldID);
      if(sCustomFieldID === null || sCustomFieldID === undefined || sCustomFieldID === "" || sCustomFieldID === "-"){
          alert("Не обран шаблон для друку!");
          //return;
      }
      //console.log("[getPrintTemplate]sCustomFieldID="+sCustomFieldID);
        
      console.log("[getPrintTemplate]2");
      var printTemplate = this.findPrintTemplate(this.form, sCustomFieldID);
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
