angular.module('app').directive('optionsClass', function($parse, $timeout) {
  return {
    require: 'select',
    link: function(scope, elem, attrs, ngSelect) {
      var NG_OPTIONS_REGEXP = /^\s*([\s\S]+?)(?:\s+as\s+([\s\S]+?))?(?:\s+group\s+by\s+([\s\S]+?))?(?:\s+disable\s+when\s+([\s\S]+?))?\s+for\s+(?:([\$\w][\$\w]*)|(?:\(\s*([\$\w][\$\w]*)\s*,\s*([\$\w][\$\w]*)\s*\)))\s+in\s+([\s\S]+?)(?:\s+track\s+by\s+([\s\S]+?))?$/;

      var match = attrs.ngOptions.match(NG_OPTIONS_REGEXP);
      var optionsSourceStr = match[8],
        getOptionsClass = $parse(attrs.optionsClass);

      scope.$watch(optionsSourceStr, function(items) {
        angular.forEach(items, function(item, index) {
          var classes = getOptionsClass(item),
            options = elem.find('option');

          angular.forEach(options, function(option) {
            var jQueryOption = angular.element(option);
            if (jQueryOption.val() != item.nID) {
              return;
            }
            angular.forEach(classes, function(value, key) {
              switch (value) {
                case 'green':
                  jQueryOption.addClass('color-green');
                  break;
                case 'red':
                  jQueryOption.addClass('color-red');
                  break;
              }
            });
          });
        });
      });
    }
  };
});