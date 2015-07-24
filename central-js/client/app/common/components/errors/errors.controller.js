/*
  ErrorsController displays error messages to user
  ErrorsFactory is required
*/
angular.module("app").controller("ErrorsController", function($scope, ErrorsFactory) {
  /* get error messages to display */
  $scope.errors = ErrorsFactory.getErrors();
  /* removes an error from array*/
  $scope.close = function(el) {
    $scope.errors.splice($scope.errors.indexOf(el), 1);
  };
});
