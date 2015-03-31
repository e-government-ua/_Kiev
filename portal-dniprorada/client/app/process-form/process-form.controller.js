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
        //console.log("open");
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
                    

/*
    $scope.formData      = {};
    $scope.formData.date = "";
                    */
    $scope.opened        = false;

    //Datepicker
    $scope.dateOptions = {
            'year-format': "'yy'",
            'show-weeks' : false
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
                                            
                    
                    
			$scope.disbleStartProcess = !$cookieStore.get('user');
			//TODO be ready for redirect from BANKID service
			$scope.processDefinitionId = $routeParams.processDefinitionId;
			if ($scope.processDefinitionId) {
				$http.get('/api/process-form/' + $routeParams.processDefinitionId)
					.success(function(result) {
						fillInUserValues(result.formProperties, $cookieStore.get('user'));
						fillInTokenValues(result.formProperties, $cookieStore.get('token'));
						$scope.processFormData = result;
					}).error(function(data, status, headers, config) {
						$scope.processFormData = {};
					});
			} else {
				$http.get('/api/process-form')
					.success(function(result) {
						fillInUserValues(result.formProperties, $cookieStore.get('user'));
						fillInTokenValues(result.formProperties, $cookieStore.get('token'));
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
                


