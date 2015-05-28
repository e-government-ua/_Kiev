/* @preserve
 *
 * angular-bootstrap-file
 * https://github.com/itslenny/angular-bootstrap-file-field
 *
 * Version: 0.1.3 - 02/21/2015
 * License: MIT
 */

define('file/directive', ['angularAMD'], function (angularAMD) {
	angularAMD.directive('fileField', ['uiUploader', function() {
		return {
			require:'ngModel',
			restrict: 'E',
			link: function (scope, element, attrs, ngModel) {
				console.log(scope);
				/*
				//set default bootstrap class
				if(!attrs.class && !attrs.ngClass){
					element.addClass('btn');
				}
				*/

				var fileField = element.find('input');

				fileField.bind('change', function(event){
					scope.$apply(function() {
						var oFile = scope.data.formData.params[ngModel.$name];
						oFile.setFiles(event.target.files);
						oFile.upload(scope.oServiceData);
					});
				});

				fileField.bind('click',function(e){
					e.stopPropagation();
				});
				element.bind('click',function(e){
					e.preventDefault();
					fileField[0].click();
				});        
			},
			template:'<form><button type="button" class="btn btn-success"><span class="glyphicon glyphicon-file" aria-hidden="true"></span><span>Завантажити файл</span><input type="file" style="display:none"></button><div ng-if="data.formData.params[property.id].value">Файл: {{data.formData.params[property.id].fileName}}</div></form>',
			replace:true,
			transclude:true
		};
	}]);
});