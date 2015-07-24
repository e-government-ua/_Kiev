angular.module("app").directive("errorsContainer", function() {
  return {
    restrict: "E",
    templateUrl: "app/common/components/errors/templates/errors.html",
    controller: "ErrorsController"
  }
});
