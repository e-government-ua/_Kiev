'use strict';
angular.module('dashboardJsApp').directive('submittaskButton', function() {
  return {    
    restrict: 'E',   
    templateUrl: 'app/tasks/form-buttons/submitTaskButton.html'   
  };
});