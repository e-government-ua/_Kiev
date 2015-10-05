'use strict';
angular.module('dashboardJsApp').directive('printDialog', [
  'PrintTemplateService',
  function(PrintTemplateService) {
    return {
      restrict: 'E',
      scope: {
        printModalState: '=',
        selectedTask: '=',
        selectedTaskForm: '=',
        printTemplateName: '='
      },
      templateUrl: 'app/tasks/form-buttons/printDialog.html',
      link: function($scope, $elem, attrs) {
        console.log('printDialog $scope, attrs', $scope, attrs);
        $scope.$watch('printModalState.show', function(newval, oldval) {
          if (oldval != newval) {
            if (newval == true) {
              // load form from api or use default as a fallback
              console.log("loading print template");
              $scope.specificTemplate = 'Завантаження форми. Зачекайте, будь ласка.';
              var templatePromise = PrintTemplateService.getPrintTemplate($scope.selectedTask, $scope.selectedTaskForm, $scope.printTemplateName);
              templatePromise.then(function(template){
                $scope.specificTemplate = template;
              }, function(error) {
                console.log("error loading form, error", error);
                $scope.specificTemplate = 'Error loading form';
              });
            } else if (newval == false) {
              console.log("show print modal off?");
            }
          }
        });
      }
    };
  }
]);