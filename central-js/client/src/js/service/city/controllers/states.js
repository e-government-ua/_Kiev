define('state/service/city/controller', ['angularAMD'], function (angularAMD) {
	angularAMD.controller('ServiceCityController', ['$state', '$rootScope', '$scope', 'service', 'places',
		function ($state, $rootScope, $scope, service, places) {
			$scope.service = service;
			$scope.places = places;
			
                        $scope.sFieldLabel = function(sField) {
                          var s="";
                          if (sField !== null) {
                            var a=sField.split(";");
                            s=a[1].trim();
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
                        };                    
                        
			$scope.data = {
				region: null,
				city: null
			};
			
			$scope.step1 = function() {
				$scope.data = {
					region: null,
					city: null
				};
				return $state.go('service.general.city', {id: $scope.service.nID});
			};
			
			$scope.step2 = function() {
				var aServiceData = $scope.service.aServiceData;
				var serviceType = { nID: 0 };
				angular.forEach(aServiceData, function(value, key) {
					if(value.nID_City.nID == $scope.data.city.nID) {
						serviceType = value.nID_ServiceType;
					}
				});
				
				switch(serviceType.nID) {
					case 1:
						return $state.go('service.general.city.link', {id: $scope.service.nID});
					case 4:
						return $state.go('service.general.city.built-in', {id: $scope.service.nID});
					default:
						return $state.go('service.general.city.error', {id: $scope.service.nID});
				}
			};
			
			if($state.current.name == 'service.general.city.built-in.bankid') {
				return true;
			}
			
			$scope.$watchCollection('data.city', function(newValue, oldValue) {
				return (newValue == null) ? null: $scope.step2();
			});

		}
	]);
});