angular.module('app').directive('ngTelField', function() {
	'use strict';
  	return {
  		require: '?ngModel',
    	restrict: 'A',
    	link: function link( scope, element, attrs, ngModel ) {
			if (!ngModel) { 
				return;
			}

			var elmt = angular.element(element);

			// Set tel input validator to cotnrol (ngModel), 
			// it will validate like $(element).intlTelInput("isValidNumber")
			ngModel.$validators.tel = function(modelValue) {
	    		var isEmpty = ngModel.$isEmpty(modelValue);
				var isValid = elmt.intlTelInput('isValidNumber');
				return !isEmpty && isValid;
			};

			// Сreate Angular telephone input field from jQuery's plugin, intlTelInput/
			// Just like described here: amitgharat.wordpress.com/2013/02/03/an-approach-to-use-jquery-plugins-with-angularjs/
			elmt.intlTelInput(scope.$eval(attrs.ngTelField));
		}
  	};
});