'use strict';

angular.module('portalDniproradaApp')
	.controller('MainCtrl', function($scope, $http) {
		$scope.processDefinitions = [];

		$http.get('/api/process-definitions').success(function(result) {
			$scope.processDefinitions = result.data;
                        /*if(result.data.length>0){
                            $scope.processDefinitions = [result.data[result.data.length-1]];
                        }*/
		}).error(function(data, status, headers, config) {
			$scope.processDefinitions = {};
		});

	});