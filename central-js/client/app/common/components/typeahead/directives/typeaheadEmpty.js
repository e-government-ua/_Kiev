angular.module('app').directive('typeaheadEmpty', function ($timeout) {
  return {
    require: 'ngModel',
    link: function (scope, element, attrs, modelCtrl) {
      var secretEmptyKey = '[$empty$]',
        isEmpty = function (val) {
          return val === '' || val === null || val === undefined;
        };

      element.on('click', function (e) {
        $timeout(function () {
          modelCtrl.$setViewValue(modelCtrl.viewValue);
          $(e.target).trigger('change');
        });
      });

      // this parser run before typeahead's parser
      modelCtrl.$parsers.unshift(function (inputValue) {
        // replace empty string with secretEmptyKey to bypass typeahead-min-length check
        var value = (!isEmpty(inputValue) ? inputValue : secretEmptyKey);
        modelCtrl.$setViewValue(value); // this $viewValue must match the inputValue pass to typehead directive
        return value;
      });

      // this parser run after typeahead's parser
      modelCtrl.$parsers.push(function (inputValue) {
        return inputValue === secretEmptyKey ? '' : inputValue;
      });
    }
  };
});