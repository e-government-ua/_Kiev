'use strict';

/*
POST
{
  "processDefinitionId" : "5",
  "businessKey" : "myKey",
  "properties" : [
    {
      "id" : "room",
      "value" : "normal"
    }
  ]
}
*/

var guid = function guid() {
	function s4() {
		return Math.floor((1 + Math.random()) * 0x10000)
			.toString(16)
			.substring(1);
	}
	return s4() + s4() + '-' + s4() + '-' + s4() + '-' +
		s4() + '-' + s4() + s4() + s4();
};

var createProperties = function(formProperties, clientForm) {
	var arr = Array.apply(null, new Array(formProperties.length));
	return arr.map(function(x, i) {
		var formProperty = formProperties[i];
		return {
			'id': formProperty.id,
			'value': clientForm[formProperty.id].$modelValue
		};
	});
};

var fillInValues = function(formProperties, user) {
	if (user) {
		formProperties.forEach(function(item) {
			if (user.fio[item.id]) {
				item.value = user.fio[item.id];
			}
		});
	}
};

angular.module('portalDniproradaApp')
	.controller('ProcessFormCtrl',
		function($scope, $routeParams, $http, $window, $cookieStore, $cookies) {
			$scope.disbleStartProcess = !$cookieStore.get('user');
			//TODO be ready for redirect from BANKID service
			$scope.processDefinitionId = $routeParams.processDefinitionId;
			if ($scope.processDefinitionId) {
				$http.get('/api/process-form/' + $routeParams.processDefinitionId)
					.success(function(result) {
						var user = $cookieStore.get('user');
						fillInValues(result.formProperties, user);
						$scope.processFormData = result;
					});
			} else {
				$http.get('/api/process-form')
					.success(function(result) {
						var user = $cookieStore.get('user');
						fillInValues(result.formProperties, user);

						$scope.processFormData = result;
						$scope.processDefinitionId = result.processDefinitionId;
					});
			}

			$scope.startProcess = function(form) {
				if (form.$invalid) {
					return;
				}
				var processDefinitionId = $scope.processDefinitionId;
				var formProperties = $scope.processFormData.formProperties;
				// Default values for the request.
				var startProcessData = {
					'processDefinitionId': processDefinitionId,
					'businessKey': guid(),
					'properties': createProperties(formProperties, form)
				};

				if ($cookieStore.get('user')) {
					$http.post('/api/process-form/' + processDefinitionId, startProcessData)
						.success(function(result) {
							$window.alert('Process has been started ' + JSON.stringify(result));
						});
				}
			};

			$scope.authorize = function() {
				$window.location.href = '/auth/bankID';
			};
		});
                
/*
angular.module('ui.bootstrap.demo').controller('DatepickerDemoCtrl', function ($scope) {
  $scope.today = function() {
    $scope.dt = new Date();
  };
  $scope.today();

  $scope.clear = function () {
    $scope.dt = null;
  };

  // Disable weekend selection
  $scope.disabled = function(date, mode) {
    return ( mode === 'day' && ( date.getDay() === 0 || date.getDay() === 6 ) );
  };

  $scope.toggleMin = function() {
    $scope.minDate = $scope.minDate ? null : new Date();
  };
  $scope.toggleMin();

  $scope.open = function($event) {
    $event.preventDefault();
    $event.stopPropagation();

    $scope.opened = true;
  };

  $scope.dateOptions = {
    formatYear: 'yy',
    startingDay: 1
  };

  //$scope.formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];
  //$scope.formats = ['dd.MM.yyyy'];
  //$scope.format = $scope.formats[0];
  $scope.format = 'dd.MM.yyyy';
});                
*/