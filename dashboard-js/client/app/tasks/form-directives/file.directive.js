'use strict';
angular.module('dashboardJsApp').directive('fileField', function() {
  return {
    require: 'ngModel',
    restrict: 'E',
    link: function(scope, element, attrs, ngModel) {
      var fileField = element.find('input');

      fileField.bind('change', function(event) {
        scope.$apply(function() {
          scope.upload(event.target.files, attrs.name);
        });
      });

      fileField.bind('click', function(e) {
        e.stopPropagation();
      });
      element.bind('click', function(e) {
        e.preventDefault();
        fileField[0].click();
      });
    },
    template: '<form><button type="button" class="btn btn-success"><span class="glyphicon glyphicon-file" aria-hidden="true"></span><span ng-disabled="isFormPropertyDisabled(item)">Завантажити файл</span><input type="file" style="display:none" ng-disabled="isFormPropertyDisabled(item)"></button> <label ng-if="item.fileName">Файл: {{item.fileName}}</label></form>', //ID: {{item.value}}
    replace: true,
    transclude: true
  };
});