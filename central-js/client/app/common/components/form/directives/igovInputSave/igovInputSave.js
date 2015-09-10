angular.module('app').directive("igovInputSave", ['userInputSaveService', function(userInputSaveService) {
  var directive = {
    restrict: 'A',
    link: function($scope, $el, $attr) {
      $scope.$watch($attr.ngModel, function(newValue) {
        userInputSaveService.save($scope[$attr.ngModel]);
      });
    }
  };
  return directive;
}])