/* @preserve
 *
 * angular-bootstrap-file
 * https://github.com/itslenny/angular-bootstrap-file-field
 *
 * Version: 0.1.3 - 02/21/2015
 * License: MIT
 */

define('file/directive', ['angularAMD'], function (angularAMD) {
	angularAMD.directive('fileField', function() {
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

				/*
				fileField.bind('change', function(event){
					scope.$evalAsync(function () {
					  ngModel.$setViewValue(event.target.files[0]);
					  if(attrs.preview){
						var reader = new FileReader();
						reader.onload = function (e) {
							scope.$evalAsync(function(){
								scope[attrs.preview]=e.target.result;
							});
						};
						reader.readAsDataURL(event.target.files[0]);
					  }
					});
				});
				*/
				fileField.bind('click',function(e){
					e.stopPropagation();
				});
				element.bind('click',function(e){
					e.preventDefault();
					fileField[0].click();
				});        
			},
			template:'<button type="button" class="btn btn-success"><span class="glyphicon glyphicon-file" aria-hidden="true"></span><span>Завантажити файл</span><input type="file" style="display:none"></button>',
			replace:true,
			transclude:true
		};
	});
});