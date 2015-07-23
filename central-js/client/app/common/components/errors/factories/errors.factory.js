
angular.module("app").factory("ErrorsFactory", function() {
  /* existing error types */
  var errorTypes = ["warning", "danger", "success", "info"],
      errors = []; /*errors container*/

  return {
    push: function(message) {
      if(!message) return;
      message.type = errorTypes.indexOf(message.type) >= 0 ? message.type : "danger";
      errors.push(message);
    },
    getErrors: function() {
      return errors;
    },
    /*display an error*/
    display: function(errorMsg) {
      alert(errorMsg);
    }
  }
});
