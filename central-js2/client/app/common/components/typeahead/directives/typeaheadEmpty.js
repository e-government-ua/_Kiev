angular.module('app').directive('typeaheadEmpty', function($timeout) {
  return {
    require: 'ngModel',
    link: function(scope, element, attrs, modelCtrl) {
      var secretEmptyKey = '[$empty$]';
      element.on('focus', function(e) {
        $timeout(function() {
          modelCtrl.$setViewValue(modelCtrl.$modelValue || secretEmptyKey);
          modelCtrl.$$parseAndValidate();
        });
      });
      element.on('input', function(e) {
        $timeout(function() {
          if (modelCtrl.$viewValue === "") {
            modelCtrl.$setViewValue(modelCtrl.$modelValue || secretEmptyKey);
            modelCtrl.$$parseAndValidate();
          }
        });
      });
      // this parser run before typeahead's parser
      modelCtrl.$parsers.unshift(function(inputValue) {
        var value = (inputValue ? inputValue : secretEmptyKey); // replace empty string with secretEmptyKey to bypass typeahead-min-length check
        return value;
      });
      // this parser run after typeahead's parser
      modelCtrl.$parsers.push(function(inputValue) {
        return inputValue === secretEmptyKey ? '' : inputValue; // set the secretEmptyKey back to empty string
      });
    }
  }
});