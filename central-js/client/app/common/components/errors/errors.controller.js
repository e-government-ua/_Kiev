/*
  ErrorsController displays error messages to user
*/
angular.module("app").controller("ErrorsController", function($scope, ErrorsFactory) {

  var errorTypes = ["warning", "danger", "success", "info"];

  $scope.errors = ErrorsFactory.getErrors();

  /* adding a new error */
  $scope.add = ErrorsFactory.push;
  /* removes an error from array*/
  $scope.close = function(el) {
    $scope.errors.splice($scope.errors.indexOf(el), 1);
  };
});
