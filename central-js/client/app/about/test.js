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

	$scope.markers = {
		'validate': {
			'PhoneUA': {
				'aField_ID': ['privatePhone', 'workPhone']
			},
			'Mail': {
				'aField_ID': ['privateMail', 'someMail']
			},
			'TextUA': {
				'aField_ID': ['textUa']
			},
			'TextRU': {
				'aField_ID': ['textRu']
			},
			'DateFormat': {
				'aField_ID': ['dateFormat'],
				'sFormat': 'YYYY-MM-DD' //
			},
			'DateElapsed': {
				'aField_ID': ['dateOrder'],
				'bFuture': false, // якщо true, то дата modelValue має бути у майбутньому
				'bLess': true, // якщо true, то 'дельта' між modelValue та зараз має бути 'менше ніж' вказана нижніми параметрами
				'nDays': 10,
				'nMonths': 0,
				'nYears': 0
			}
		}
	};

	$scope.reset();

	// $timeout(function() {
	// 	ValidationService.validateByMarkers($scope.testForm, $scope.markers);
	// }, 0);

}]);