'use strict';

angular.module('portalDniproradaApp')
	.controller('MainCtrl', function($scope, $http, $cookieStore, $window, Modal) {
		$scope.processDefinitions = [];

		$scope.authorize = function(processDefinitionID, processDefinitionName) {
			$cookieStore.put('lastFormProcessID', processDefinitionID);
			$cookieStore.put('lastFormProcessName', processDefinitionName);

			if ($cookieStore.get('disableBankID')) {
				$window.location.href = '/process-form/' + processDefinitionID;
			} else {
				if (!$cookieStore.get('bankdIDToken')) {
					Modal.confirm.auth(function(event) {
						$window.location.href = '/auth/bankID';
					})('Сервіс авторизації BankID');
				} else {
					$window.location.href = '/process-form/' + processDefinitionID;
				}
			}


		};

		$http.get('/api/process-definitions').success(function(result) {
			$scope.processDefinitions = result.data;
		}).error(function(data, status, headers, config) {
			$scope.processDefinitions = {};
		});
	});