'use strict';

//angular.module('dashboardJsApp').factory('PrintTemplateProcessor', ['$sce', 'Auth', '$filter', 'FieldMotionService', '$lunaService', function ($sce, Auth, $filter, FieldMotionService, lunaService) {
angular.module('dashboardJsApp').factory('PrintTemplateProcessor', ['$sce', 'Auth', '$filter', 'FieldMotionService', function ($sce, Auth, $filter, FieldMotionService) {
  function processMotion(printTemplate, form, fieldGetter) {
    var formData = form.reduce(function(prev, curr) {
      prev[curr.id] = curr;
      return prev;
    }, {});
    var template = $('<div/>').append(printTemplate);
    FieldMotionService.getElementIds().forEach(function(id) {
      var el = template.find('#' + id);
      if (el.length > 0 && !FieldMotionService.isElementVisible(id, formData))
        el.remove();
    });
    var splittingRules = FieldMotionService.getSplittingRules();
    form.forEach(function(e) {
      var val = fieldGetter(e);
      if (val && _.has(splittingRules, e.id)) {
        var rule = splittingRules[e.id];
        var a = val.split(rule.splitter);
        template.find('#' + rule.el_id1).html(a[0]);
        a.shift();
        template.find('#' + rule.el_id2).html(a.join(rule.splitter));
      }
    });
    return template.html();
  }

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
          if (enumItem && enumItem.name) {
            var enumItemName = enumItem.name;
            var enumItemNameArray = enumItemName.split('|');
            if (!_.isEmpty(enumItemNameArray[1])) {
              return enumItemNameArray[1];
            }
            else {
              return enumItemNameArray[0];
            }
          }
        }
        else {
          return item.value;
        }
      }
      
      
        function getLunaValue(id) {

          // Number 2187501 must give CRC=3
          // Check: http://planetcalc.ru/2464/
          if(id===null || id === 0){
            return null;
          }
          var n = parseInt(id);
          var nFactor = 1;
          var nCRC = 0;
          var nAddend;

          while (n !== 0) {
            nAddend = Math.round(nFactor * (n % 10));
            nFactor = (nFactor === 2) ? 1 : 2;
            nAddend = nAddend > 9 ? nAddend - 9 : nAddend;
            nCRC += nAddend;
            n = parseInt(n / 10);
          }

          nCRC = nCRC % 10;
          return nCRC;
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
      
      //№{{task.processInstanceId}}{{lunaService.getLunaValue(task.processInstanceId)}}
      //$scope.lunaService = lunaService;
      //lunaService.getLunaValue(
      printTemplate = this.populateSystemTag(printTemplate, "[sID_Order]", task.processInstanceId+getLunaValue(task.processInstanceId)+"");
      
      return $sce.trustAsHtml(processMotion(printTemplate, form, fieldGetter));
    }
  }
}]);
