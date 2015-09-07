angular.module('app').service('FieldAttributesService', ['MarkersFactory', FieldAttributesService]);

function FieldAttributesService(MarkersFactory) {
  this.markers = MarkersFactory.getMarkers();
  var self = this;

  function grepByPrefix(prefix) {
    var a = [];
    for (var key in self.markers.attributes)
      if (self.markers.attributes.hasOwnProperty(key) && key.indexOf(prefix) === 0)
        a.push(self.markers.attributes[key]);
    return a;
  }

  this.EditableStatus = {
    EDITABLE: 1,
    READ_ONLY: 2,
    NOT_SET: 3
  };

  this.editableStatusFor = function(fieldId) {
    var result = self.EditableStatus.NOT_SET;
    grepByPrefix('Editable_').some(function(e) {
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
