angular.module('app').factory('SignFactory', function () {

  var sign = function Scan() {
    this.value = null;
  };

  sign.prototype.isFit = function (property) {
    return property.type === 'file' && property.id === 'form_signed';
  };

  return sign;
});
