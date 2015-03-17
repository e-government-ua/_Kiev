'use strict';

angular.module('portalDniproradaApp')
	.controller('ProcessFormCtrl', function($scope, $routeParams, $http, $window) {
		$scope.processDefinitionId = $routeParams.processDefinitionId;
		$scope.startProcess = function(form) {
			$window.alert("Should call process started with " + form.toString());
		};
		$http.get('/api/process-form/' + $routeParams.processDefinitionId)
			.success(function(result) {
				$scope.processFormData = result;
			});
	});