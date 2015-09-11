angular.module('app').service('FieldMotionService', ['MarkersFactory', FieldMotionService]);

function FieldMotionService(MarkersFactory) {

  function getData() {
    return {
      showOnNotEmpty: MarkersFactory.grepByPrefix('motion', 'ShowFieldsOnNotEmpty_'),
      showOnCondition: MarkersFactory.grepByPrefix('motion', 'ShowFieldsOnCondition_')
    }
  }

  this.isFieldMentioned = function(fieldId) {
    return _.values(getData()).some(function(entry) {
      return entry.some(function(showEntry) {
        return _.contains(showEntry.aField_ID, fieldId);
      });
    });
  };

  this.isFieldVisible = function(fieldId, formData) {
    var data = getData();
    var b1 = data.showOnNotEmpty.some(function(e) {
      return formData[e.sField_ID_s]
        && $.trim(formData[e.sField_ID_s].value)
        && _.contains(e.aField_ID, fieldId);
    });
    return b1 || data.showOnCondition.some(function(entry) {
      if (!_.contains(entry.aField_ID, fieldId)) return false;
      var toEval = entry.sCondition.replace(/\[(\w+)]/g, function(str, alias) {
        var fId = entry.asID_Field[alias];
        if (!fId) alert('Cant resolve original fieldId by alias:' + alias);
        var result = '';
        if (formData[fId] && formData[fId].value)
          result = formData[fId].value.replace(/'/g, "\\'");
        switch(alias.charAt(0)) {
          case 's': result = "'" + result + "'"; break;
          case 'n': result = result ? parseFloat(result) : 0; break;
          default: alert('invalid alias format, alias:' + alias);
        }
        return result;
      });
        try {
          return eval(toEval);
        } catch (e) {
          alert('OnCondition expression error\n' + e.name + '\n' + e.message
            + '\nexpression:' + entry.sCondition
            + '\nresolved expression:' + toEval);
          throw e;
        }
    });
  }
}
