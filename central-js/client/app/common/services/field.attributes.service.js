angular.module('app').service('FieldAttributesService', ['MarkersFactory', FieldAttributesService]);

function FieldAttributesService(MarkersFactory) {
  var self = this;

  this.EditableStatus = {
    EDITABLE: 1,
    READ_ONLY: 2,
    NOT_SET: 3
  };

  this.editableStatusFor = function(fieldId) {
    var result = self.EditableStatus.NOT_SET;
    MarkersFactory.grepByPrefix('attributes', 'Editable_').some(function(e) {
       if(_.indexOf(e.aField_ID, fieldId) > -1) {
         result = e.bValue ? self.EditableStatus.EDITABLE : self.EditableStatus.READ_ONLY;
         return true;
       } else {
         return false;
       }
    });
    return result;
  }
}
