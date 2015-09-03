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

  this.isEditable = function(fieldId) {
    var result = 3;
    grepByPrefix('Editable_').some(function(e) {
       if(_.indexOf(e.aField_ID, fieldId) > -1) {
         result = e.bValue ? 1 : 2;
         return true;
       } else {
         return false;
       }
    });
    return result;
  }
}
