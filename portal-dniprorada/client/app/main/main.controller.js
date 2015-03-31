'use strict';

angular.module('portalDniproradaApp')
	.controller('MainCtrl', function($scope, $http, $cookieStore, $window) {
		$scope.processDefinitions = [];

		$scope.authorize = function(processDefinitionID, processDefinitionName) {
			$cookieStore.put('lastFormProcessID', processDefinitionID);
			$cookieStore.put('lastFormProcessName', processDefinitionName);
			if ($cookieStore.get('disableBankID')) {
				$window.location.href = '/process-form/' + processDefinitionID;
			} else {
				if (!$cookieStore.get('bankdIDToken')) {
					$window.location.href = '/auth/bankID';
				}
			}

		};

		$http.get('/api/process-definitions').success(function(result) {
			/*$http.get('/rest/getProcessDefinitions').success(function(result) {*/
			$scope.processDefinitions = result.data;
			/*if(result.data.length>0){
			    $scope.processDefinitions = [result.data[result.data.length-1]];
			}*/
		}).error(function(data, status, headers, config) {
			$scope.processDefinitions = {};
		});
	});