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
		formProperties.forEach(function(item) {
			if (user.fio[item.id]) {
				item.value = user.fio[item.id];
			}
		});
	}
};

var fillInTokenValues = function(formProperties, token) {
	if (token) {
		formProperties.forEach(function(item) {
			if (token[item.id]) {
				item.value = token[item.id];
			}
		});
	}
};


/*angular.module('ui.bootstrap.demo')
        .controller('DatepickerDemoCtrl', function ($scope) {

});                */




angular.module('portalDniproradaApp')

//Datepicker
.directive("mydatepicker", function(){
  return {
    restrict: "E",
    scope:{
      ngModel: "=",
      dateOptions: "=",
      opened: "=",
    },
    link: function($scope, element, attrs) {
      $scope.open = function(event){
        console.log("open");
        event.preventDefault();
        event.stopPropagation();
        $scope.opened = true;
      };

      $scope.clear = function () {
        $scope.ngModel = null;
      };
    },
    templateUrl: 'datepicker.html'
  }
})

	.controller('ProcessFormCtrl',
		function($scope, $routeParams, $http, $window, $cookieStore) {

			$scope.today = function() {
				$scope.dt = new Date();
			};
			$scope.today();

			$scope.clear = function() {
				$scope.dt = null;
			};

			// Disable weekend selection
			$scope.disabled = function(date, mode) {
				return (mode === 'day' && (date.getDay() === 0 || date.getDay() === 6));
			};

			$scope.toggleMin = function() {
				$scope.minDate = $scope.minDate ? null : new Date();
			};
			$scope.toggleMin();

			$scope.open = function($event) {
				$event.preventDefault();
				$event.stopPropagation();

				$scope.opened = false;
			};

			$scope.dateOptions = {
				formatYear: 'yy',
				startingDay: 1
			};

			    $scope.formData      = {};
    $scope.formData.date = "";
    $scope.opened        = false;

    //Datepicker
    $scope.dateOptions = {
            'year-format': "'yy'",
            'show-weeks' : false
    };

			//$scope.formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];
			//$scope.formats = ['dd.MM.yyyy'];
			//$scope.format = $scope.formats[0];
			//$scope.format = 'dd.MM.yyyy';                        
			$scope.format = 'dd/MM/yyyy';
		$scope.disbleStartProcess = !$cookieStore.get('user');
			$scope.processDefinitionId = $routeParams.processDefinitionId;
			if ($scope.processDefinitionId) {
				$http.get('/api/process-form/' + $routeParams.processDefinitionId)
					.success(function(result) {
						deleteAccessTokenProperty(result.formProperties);
						fillInUserValues(result.formProperties, $cookieStore.get('user'));
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
						fillInUserValues(result.formProperties, $cookieStore.get('user'));
						$scope.processFormData = result;
						$scope.processDefinitionId = result.processDefinitionId;
						$scope.processDefinitionName =
							$cookieStore.get('lastFormProcessName') || result.processDefinitionId;
						$cookieStore.put('lastFormProcessName', processDefinitionName);
					}).error(function(data, status, headers, config) {
						$scope.processFormData = {};
					});
			}

			$scope.startProcess = function(form) {
				if (form.$invalid) {
					return;
				}
				if (!$cookieStore.get('disableBankID')) {
					if (!$cookieStore.get('user')) {
						return;
					}
					if (!$cookieStore.get('bankdIDToken')) {
						return;
					}
				}

				var token = $cookieStore.get('bankdIDToken') || {
					accessToken: 'aaa'
				};
				var processDefinitionId = $scope.processDefinitionId;
				var formProperties = $scope.processFormData.formProperties;
				// Default values for the request.
				var startProcessData = {
					'processDefinitionId': processDefinitionId,
					'businessKey': guid(),
					'properties': createProperties(formProperties, token.accessToken)
				};

				$http.post('/api/process-form/' + processDefinitionId, startProcessData)
					.success(function(result) {
						$window.alert('Process has been started ' + JSON.stringify(result));
					});
			};


/*
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

    $scope.opened = false;
  };

  $scope.dateOptions = {
    formatYear: 'yy',
    startingDay: 1
  };

  //$scope.formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];
  //$scope.formats = ['dd.MM.yyyy'];
  //$scope.format = $scope.formats[0];
  //$scope.format = 'dd.MM.yyyy';                        
  $scope.format = 'dd/MM/yyyy';                        
*/
                                            
                    
                    
			
		});
