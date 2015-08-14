angular.module('app').controller('TestController', ['$scope', '$timeout', 'ValidationService', function($scope, $timeout, ValidationService) {

	$timeout(function() {
		ValidationService.validateByMarkers($scope.testForm, null, false);
	}, 0);

	$scope.formSubmit = function(formData) {
		console.log('Form submit:', formData);
	};

	$scope.reset = function() {
		var testForm = $scope.testForm;
		if (testForm) {
			testForm.$setPristine();
			testForm.$setUntouched();
		}
	};

}]);