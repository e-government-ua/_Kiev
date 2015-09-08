'use strict';
angular.module('dashboardJsApp').directive('simpleField', function() {
  return {
    
    restrict: 'E',
   
    templateUrl: 'app/tasks/form-fields/simple-field.html'
   
  };
});