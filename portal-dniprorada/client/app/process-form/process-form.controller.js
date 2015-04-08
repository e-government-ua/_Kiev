'use strict';

var guid = function guid() {
	function s4() {
		return Math.floor((1 + Math.random()) * 0x10000)
			.toString(16)
			.substring(1);
	}
	return s4() + s4() + '-' + s4() + '-' + s4() + '-' +
		s4() + '-' + s4() + s4() + s4();
};

var createProperties = function(formProperties, accessToken) {
	var arr = Array.apply(null, new Array(formProperties.length));
	var properties = arr.map(function(x, i) {
		var formProperty = formProperties[i];
		if (formProperty) {
			return {
				id: formProperty.id,
				value: formProperty.value
			};
		}
	});
	properties.push({
		id: 'access_token',
		value: accessToken
	});
	return properties;
};

var deleteAccessTokenProperty = function(formProperties) {
	var forDeletion = -1;
	formProperties.forEach(function(item, i) {
		if (item.id === 'access_token') {
			forDeletion = i;
		}
	});
	if (forDeletion !== -1) {
		formProperties.splice(forDeletion, 1);
	}
};

var fillInUserValues = function(formProperties, user) {
	if (user) {
		formProperties.forEach(function(formProperty) {
			if (user.fio[formProperty.id]) {
				formProperty.value = user.fio[formProperty.id];
			}
		});
	}
};

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
var startProcess = function(form, scope, http, cookieStore, window, Modal) {
	if (form.$invalid) {
		return;
	}
	if (!cookieStore.get('disableBankID')) {
		if (!cookieStore.get('user') || !cookieStore.get('bankdIDToken')) {
			Modal.inform.warning(function(event) {
				window.location.href = '/';
			})('Час авторізації закінчився');
			return;
		}
	}

	var token = cookieStore.get('bankdIDToken') || {
		accessToken: 'aaa'
	};
	var processDefinitionId = scope.processDefinitionId;
	var formProperties = scope.processFormData.formProperties;
	var properties = createProperties(formProperties, token.accessToken);
	// Default values for the request.
	var startProcessData = {
		'processDefinitionId': processDefinitionId,
		'businessKey': guid(),
		'properties': properties
	};

	http.post('/api/process-form/' + processDefinitionId, startProcessData)
		.success(function(result) {
			Modal.inform.success(function(event) {
				window.location.href = "/";
			})('Ваша заявка прийнята в обробку. Ваш код заявки : ' + result.businessKey);
		}).error(function(data, status, headers, config) {
			Modal.inform.error()('Помилка. Спробуйте ще раз');
		});
};

angular.module('portalDniproradaApp')
	.controller('ProcessFormCtrl',
		function($scope, $routeParams, $http, $window, $cookieStore, Modal) {

			$scope.formData = {};
			$scope.formData.dt = '';

			$scope.datepickers = {
				dt: false
			};

			$scope.today = function() {
				$scope.formData.dt = new Date();
			};
			$scope.today();

			$scope.clear = function() {
				$scope.formData.dt = null;
			};

			// Disable weekend selection
			$scope.disabled = function(date, mode) {
				return (mode === 'day' && (date.getDay() === 0 || date.getDay() === 6));
			};

			$scope.toggleMin = function() {
				$scope.minDate = $scope.minDate ? null : new Date();
			};
			$scope.toggleMin();

			$scope.open = function($event, which) {
				$event.preventDefault();
				$event.stopPropagation();
				$scope.datepickers[which] = true;
			};

			$scope.dateOptions = {
				formatYear: 'yy',
				startingDay: 1
			};

			$scope.format = 'dd/MM/yyyy';
			$scope.disableStartProcess = !$cookieStore.get('user') && !$cookieStore.get('disableBankID');
			$scope.processDefinitionId = $routeParams.processDefinitionId;
			if ($scope.processDefinitionId) {
				$http.get('/api/process-form/' + $routeParams.processDefinitionId)
					.success(function(result) {
						deleteAccessTokenProperty(result.formProperties);
						fillInUserValues(result.formProperties, $cookieStore.get('user'));
						if (result.formProperties !== null) {
							result.formProperties.forEach(function(formProperty) {
								if (formProperty.type === 'date') {
									$scope.formData.dt = formProperty.value;
								}
							});
						}

						$scope.processFormData = result;
						$scope.processDefinitionName =
							$cookieStore.get('lastFormProcessName') || result.processDefinitionId;
					}).error(function(data, status, headers, config) {
						$scope.processFormData = {};
					});
			} else {
				$http.get('/api/process-form')
					.success(function(result) {
						deleteAccessTokenProperty(result.formProperties);
						fillInUserValues(result.formProperties, $cookieStore.get('user'), $scope);
						if (result.formProperties !== null) {
							result.formProperties.forEach(function(formProperty) {
								if (formProperty.type === 'date') {
									$scope.formData.dt = formProperty.value;
								}
							});
						}

						$scope.processFormData = result;
						$scope.processDefinitionId = result.processDefinitionId;
						$scope.processDefinitionName =
							$cookieStore.get('lastFormProcessName') || result.processDefinitionId;
					}).error(function(data, status, headers, config) {
						$scope.processFormData = {};
					});
			}

			$scope.startProcess = function(form) {
				startProcess(form, $scope, $http, $cookieStore, $window, Modal);
			};

			$scope.isClientInfo = function(formProperty) {
				return function(str) {
					return formProperty.id.slice(0, str.length) === str;
				}('cl');				
			};
		});