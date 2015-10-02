'use strict';
angular.module('dashboardJsApp').directive('printDialog', [
  'PrintTemplate', 'PrintTemplateService',
  function(PrintTemplate, PrintTemplateService) {
    return {
      restrict: 'E',
      scope: true,
      templateUrl: 'app/tasks/form-buttons/printDialog.html',
      link: function($scope, $elem, attrs) {
        console.log('printDialog $scope', $scope);
        $scope.$watch('showPrintModal', function(newval, oldval) {
          if (oldval != newval && newval == true) {
            // load form from api or use default as a fallback
            console.log("loading print template");
            var templatePromise = PrintTemplateService.getPrintTemplate($scope.selectedTask, $scope.taskForm);
            templatePromise.then(function(template){
              $scope.specificTemplate = template;
            }, function(error) {
              console.log("error loading form, error", error);
              $scope.specificTemplate = 'Error loading form';
            });
          }
        });
      }
    };
  }
]);