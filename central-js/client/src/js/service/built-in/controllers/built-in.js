define('service/built-in/controller', ['angularAMD'], function (angularAMD) {
	angularAMD.controller('ServiceBuiltInController', ['$location', '$state', '$rootScope', '$scope', function ($location, $state, $rootScope, $scope) {
		$scope.$location = $location;
		$scope.$state = $state;
    }]);
});

define('service/built-in/bankid/controller', ['angularAMD', 'formData/factory'], function (angularAMD) {
	angularAMD.controller('ServiceBuiltInBankIDController', [
		'$state', '$stateParams', '$scope', 'FormDataFactory', 'ActivitiService', 'oServiceData', 'BankIDAccount', 'ActivitiForm',
		function($state, $stateParams, $scope, FormDataFactory, ActivitiService, oServiceData, BankIDAccount, ActivitiForm) {
			angular.forEach($scope.places, function(value, key) {
				if($stateParams.region == value.nID) {
					$scope.data.region = value;
				}
			});
			if($scope.data.region) {
				angular.forEach($scope.data.region.aCity, function(value, key) {
					if($stateParams.city == value.nID) {
						$scope.data.city = value;
					}
				});
			}
			
			$scope.account = BankIDAccount;
			$scope.ActivitiForm = ActivitiForm;
			
			$scope.data = $scope.data || {};
			$scope.data.formData = new FormDataFactory();
			$scope.data.formData.initialize(ActivitiForm);
			$scope.data.formData.setBankIDAccount(BankIDAccount);
                        
                        
                        angular.forEach($scope.ActivitiForm.formProperties, function(value, key) {
                                var sField = value.name;
                                var s;
                                if (sField == null) {
                                  sField="";
                                }
                                var a=sField.split(";");
                                s=a[0].trim();
                                value.sFieldLabel=s;
                                s=null;
                                if(a.length>1){
                                  s=a[1].trim();
                                  if(s==""){
                                      s=null;
                                  }
                                }
                                //$scope.data.formData.params[value.id].sFieldNotes=s;
                                //$scope.ActivitiForm.formProperties[value.id].sFieldNotes=s+"_"+sField;
                                value.sFieldNotes=s;
                        });
                        
                        /*$scope.sFieldLabel = function(sField) {
                          var s="";
                          if (sField !== null) {
                            var a=sField.split(";");
                            s=a[0].trim();
                          }
                          return s;
                        };
                        $scope.sFieldNotes = function(sField) {
                          var s=null;
                          if (sField !== null) {
                            var a=sField.split(";");
                            if(a.length>1){
                              s=a[1].trim();
                              if(s==""){
                                  s=null;
                              }
                            }
                          }
                          return s;
                        };*/                    
                        
			$scope.submit = function(form) {
				form.$setSubmitted();
				return form.$valid ?
					ActivitiService
						.submitForm(oServiceData, $scope.data.formData)
						.then(function(result) {
							var state = $state.$current;
							
							var submitted = $state.get(state.name + '.submitted');
							submitted.data.id = result.id;
							
							return $state.go(submitted, $stateParams);
						}):
					false;
			};
		}
	]);
});