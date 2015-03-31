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
					}).error(function(data, status, headers, config) {
						$scope.processFormData = {};
					});
			} else {
				$http.get('/api/process-form')
					.success(function(result) {
						var user = $cookieStore.get('user');
						fillInValues(result.formProperties, user);

						$scope.processFormData = result;
						$scope.processDefinitionId = result.processDefinitionId;
					}).error(function(data, status, headers, config) {
						$scope.processFormData = {};
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