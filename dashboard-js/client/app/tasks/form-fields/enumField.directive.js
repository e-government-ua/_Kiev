'use strict';
angular.module('dashboardJsApp').directive('enumField', function() {
  return {
    
    restrict: 'E',
   
    templateUrl: 'app/tasks/form-fields/enum-field.html'
   
  };
});