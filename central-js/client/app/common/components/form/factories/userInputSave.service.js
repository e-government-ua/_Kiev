/* This simple service should be used to store user input for filtering
anything on the site and to share that input between different pages/states
*/
angular.module('app').service('userInputSaveService', [function () {
  var userInput = '';
  var userInputSaveService = {
    save: function(filterText) {
      console.log('set filter', filterText);
      userInput = filterText;
    },
    reset: function() {
      userInput = '';
    },
    restore: function() {
      return userInput;
    }
  };
  return userInputSaveService;
}]);
