'use strict';
angular.module('dashboardJsApp').directive('printDialog', function() {
  return {    
    restrict: 'E',   
    templateUrl: 'app/tasks/form-buttons/printDialog.html'   
  };
});