/*
  This factory is for displaying error messages
  to add new message to display you should use a push method
  the error object has two fields:
    type - defines a message style
    text - message displaying to user
*/
angular.module("app").factory("ErrorsFactory", function() {
  /* existing error types */
  var errorTypes = ["warning", "danger", "success", "info"],
      errors = []; /*errors container for objects like {type: "...", text: "..."}*/

  return {
    /*
      returns all existing errors
    */
    getErrors: function() {
      return errors;
    },
    /*
      adding a new error message to errors collection
      @example ... ErrorsFactory.push({type:"warning", text: "Critical Error"});
    */
    push: function(message) {
      if(!message) return;
      message.type = errorTypes.indexOf(message.type) >= 0 ? message.type : "danger";
      errors.push(message);
    }
  }
});
