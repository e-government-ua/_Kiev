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
}

angular.module('portalDniproradaApp')
	.controller('ProcessFormCtrl', function($scope, $routeParams, $http, $window) {
		$scope.processDefinitionId = $routeParams.processDefinitionId;
		$http.get('/api/process-form/' + $routeParams.processDefinitionId)
			.success(function(result) {
				$scope.processFormData = result;
			});

		var createProperties = function(formProperties, clientForm) {
			var arr = Array.apply(null, Array(formProperties.length));
			return arr.map(function(x, i) {
				var formProperty = formProperties[i];
				return {
					"id": formProperty.id,
					"value": clientForm[formProperty.id].$modelValue
				};
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
				"properties": createProperties(formProperties, form)
			};
			$http.post('/api/process-form/' + processDefinitionId, startProcessData)
				.success(function(result) {
				$window.alert("Process has been started " + JSON.stringify(result));
			});
			
		};
	});