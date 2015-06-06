'use strict';

angular.module('dashboardJsApp').directive('fileField', ['uiUploader', function() {
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
		template: '<form><button type="button" class="btn btn-success"><span class="glyphicon glyphicon-file" aria-hidden="true"></span><span ng-disabled="isFormPropertyDisabled(item)">Завантажити файл</span><input type="file" style="display:none" ng-disabled="isFormPropertyDisabled(item)"></button><div ng-if="item.fileName">Файл: {{item.fileName}}</div></form>',
		replace: true,
		transclude: true
	};
}]);