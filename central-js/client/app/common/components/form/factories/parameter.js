angular.module('app').factory('ParameterFactory', function() {
  var parameter = function() {
    this.value = null;
  };

  parameter.prototype.get = function() {
    return this.value;
  };

  parameter.prototype.isFit = function(property){
    return true;
  };

  return parameter;
});
