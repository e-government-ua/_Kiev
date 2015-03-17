'use strict';

angular.module('portalDniproradaApp')
	.controller('ProcessFormCtrl', function($scope, $routeParams, $http) {
		$scope.processDefinitionId = $routeParams.processDefinitionId;
		$http.get('/api/process-form/' + $routeParams.processDefinitionId)
			.success(function(result) {
				$scope.processFormData = result;
			});
	});