angular.module('app').service('FieldMotionService', ['MarkersFactory', FieldMotionService]);

function FieldMotionService(MarkersFactory) {

  this.isFieldMentioned = {
    'in': function(fieldId, prefix) {
        return grepByPrefix(prefix).some(function(entry) {
          return _.contains(entry.aField_ID, fieldId);
        });
    },
    inShow: function(fieldId) {
      return this.in(fieldId, 'ShowFieldsOn');
    },
    inRequired: function(fieldId) {
      return this.in(fieldId, 'RequiredFieldsOn');
    }
  };

  this.isFieldVisible = function(fieldId, formData) {
    var showOnNotEmpty = grepByPrefix('ShowFieldsOnNotEmpty_');
    var showOnCondition = grepByPrefix('ShowFieldsOnCondition_');
    var b1 = showOnNotEmpty.some(function(e) {
      return formData[e.sField_ID_s]
        && $.trim(formData[e.sField_ID_s].value)
        && _.contains(e.aField_ID, fieldId);
    });
    return b1 || showOnCondition.some(function(entry) {
      return evalCondition(entry, fieldId, formData);
    });
  };

  this.isFieldRequired = function(fieldId, formData) {
    return grepByPrefix('RequiredFieldsOnCondition_').some(function(entry) {
      return evalCondition(entry, fieldId, formData);
    })
  };

  function evalCondition(entry, fieldId, formData) {
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
  }

  function grepByPrefix(prefix) {
    return MarkersFactory.grepByPrefix('motion', prefix);
  }
}
