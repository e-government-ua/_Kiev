'use strict';

angular.module('portalDniproradaApp')
	.controller('MainCtrl', function($scope, $http) {
		$scope.processDefinitions = [];

		$http.get('/api/process-definitions').success(function(result) {
			$scope.processDefinitions = result.data;
		}).error(function(data, status, headers, config) {
			$scope.processDefinitions = {};
		});

	});