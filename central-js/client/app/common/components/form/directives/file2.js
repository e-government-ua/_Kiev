angular.module('app').directive('fileUploadA', function() {
  return {
    restrict: 'A',
    link: function(scope, element, attrs) {
      var fileField = element.find('input');

      fileField.bind('change', function(event) {
        scope.$apply(function() {
          var oFile = scope.file;
          oFile.setFiles(event.target.files);
        });
      });

      fileField.bind('click', function(e) {
        e.stopPropagation();
      });
      element.bind('click', function(e) {
        e.preventDefault();
        fileField[0].click();
      });
    }
  };
});

angular.module('app').directive('fileUploadButton', function() {
  return {
    restrict: 'A',
    link: function(scope, element, attrs) {
      var button = element;

      button.bind('click', function(event) {
        scope.$apply(function() {
          scope.file.uploadDocument();
        });
      });
    }
  };
});