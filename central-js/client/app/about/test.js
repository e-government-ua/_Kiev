angular.module('app').controller('TestController', ['$scope', '$timeout', 'ValidationService', function($scope, $timeout, ValidationService) {

	$scope.debugMode = true;

	$scope.masterData = {};

	$scope.formSubmit = function(formData) {
		console.log('Form submit:', formData);
		$scope.masterData = angular.copy(formData);

		ValidationService.validateByMarkers($scope.testForm, $scope.markers);
	};

	$scope.reset = function() {
		var testForm = $scope.testForm;
		console.log('Form reset:', testForm);
		if (testForm) {
			testForm.$setPristine();
			testForm.$setUntouched();
		}
		$scope.formData = angular.copy($scope.masterData);
	};

	$scope.reset();

	// $timeout(function() {
	// 	ValidationService.validateByMarkers($scope.testForm);
	// }, 0);

}]);