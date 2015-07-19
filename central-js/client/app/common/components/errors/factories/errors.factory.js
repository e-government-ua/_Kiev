
angular.module("app").factory("ErrorsFactory", function() {
  return function() {
    /*display an error*/
    this.display = function(errorMsg) {
      alert(errorMsg);
    }
  };
});
