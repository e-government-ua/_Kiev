angular.module('app').directive("igovInputRestore", ['userInputSaveService', function(userInputSaveService) {
  var directive = {
    restrict: 'A',
    //priority: 1001,
    link: function($scope, $el, $attr) {
      $scope[$attr.ngModel] = userInputSaveService.restore();
    }
  };
  return directive;
}])