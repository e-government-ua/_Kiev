'use strict';
angular.module('dashboardJsApp').directive('textareaField', function() {
  return {
    
    restrict: 'E',
   
    templateUrl: 'app/tasks/form-fields/text-area-field.html'
   
  };
});