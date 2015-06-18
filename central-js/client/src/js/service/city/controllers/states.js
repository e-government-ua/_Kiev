define('state/service/city/controller', ['angularAMD'], function (angularAMD) {
	angularAMD.controller('ServiceCityController', [
		'$state', '$rootScope', '$scope', '$sce', 'RegionListFactory', 'LocalityListFactory', 'PlacesService', 'ServiceService', 'service', 'regions',
		function ($state, $rootScope, $scope, $sce, RegionListFactory, LocalityListFactory, PlacesService, ServiceService, service, regions) {
			$scope.service = service;
			$scope.regions = regions;
			
			$scope.regionList = new RegionListFactory();
			$scope.regionList.initialize(regions);
			
			$scope.localityList = new LocalityListFactory();
			
			$scope.loadRegionList = function(search) {
				return $scope.regionList.load(service, search);
			};
			
			$scope.onSelectRegionList = function($item, $model, $label) {
				$scope.data.region = $item;
				$scope.regionList.select($item, $model, $label);
				$scope.localityList.load(service, $item.nID, null).then(function(cities) {
					$scope.localityList.typeahead.defaultList = cities;
				});
			};
			
			$scope.loadLocalityList = function(search) {
				return $scope.localityList.load(service, $scope.data.region.nID, search);
			};
			
			$scope.onSelectLocalityList = function($item, $model, $label) {
				$scope.data.city = $item;
				$scope.localityList.select($item, $model, $label);
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
				
				$scope.regionList.reset();
				$scope.regionList.initialize(regions);
				
				$scope.localityList.reset();
				return $state.go('service.general.city', {id: $scope.service.nID});
			};
			
			$scope.step2 = function() {
				var aServiceData = $scope.service.aServiceData;
				var serviceType = { nID: 0 };
				angular.forEach(aServiceData, function(value, key) {
					if(value.nID_City.nID == $scope.data.city.nID) {
						serviceType = value.nID_ServiceType;
						$scope.serviceData = value;
						$scope.serviceData.sNote = $sce.trustAsHtml($scope.serviceData.sNote);
					}
				});
				
				switch(serviceType.nID) {
					case 1:
						return $state.go('service.general.city.link', {id: $scope.service.nID}, { location: false });
					case 4:
						return $state.go('service.general.city.built-in', {id: $scope.service.nID}, { location: false });
					default:
						return $state.go('service.general.city.error', {id: $scope.service.nID}, { location: false });
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

define('state/service/city/absent/controller', ['angularAMD'], function (angularAMD) {
	angularAMD.controller('ServiceCityAbsentController', [
        '$state',
        '$rootScope',
        '$scope',
        'service',
        'MessagesService',
        function (
            $state,
            $rootScope,
            $scope,
            service,
            MessagesService
        ) {
            $scope.service = service;
            $scope.hiddenCtrls = true; // $rootScope.hiddenCtrls; //Admin buttons visibility handling
            (function() {
                if (window.pluso)if (typeof window.pluso.start == "function") return;
                if (window.ifpluso==undefined) { window.ifpluso = 1;
                    var d = document, s = d.createElement('script'), g = 'getElementsByTagName';
                    s.type = 'text/javascript'; s.charset='UTF-8'; s.async = true;
                    s.src = ('https:' == window.location.protocol ? 'https' : 'http')  + '://share.pluso.ru/pluso-like.js';
                    var h=d[g]('body')[0];
                    h.appendChild(s);
                }})();

			if (!!window.pluso){
				window.pluso.build(document.getElementsByClassName("pluso")[0], false);
			}

            /*(function() {
                if (window.pluso)if (typeof window.pluso.start == "function") return;
                if (window.ifpluso==undefined) { window.ifpluso = 1;
                  var d = document, s = d.createElement('script'), g = 'getElementsByTagName';
                  s.type = 'text/javascript'; s.charset='UTF-8'; s.async = true;
                  s.src = ('https:' == window.location.protocol ? 'https' : 'http')  + '://share.pluso.ru/pluso-like.js';
                  var h=d[g]('body')[0];
                  h.appendChild(s);
            }})();*/

            // %Населенный пункт% – %Название услуги%
            $scope.absentMessage = {
                email: "",
				showErrors: false
            };

			$scope.emailKeydown = function () {
				$scope.absentMessage.showErrors = false;
			};

            $scope.sendAbsentMessage = function (absentMessageForm, absentMessage) {

				if (false === absentMessageForm.$valid) {
					$scope.absentMessage.showErrors = true;
					return false;
				}

                // @todo Fix hardcoded city name, we should pass it into state
                var data = {
                    sMail: absentMessage.email,
                    sHead: "Закликаю владу перевести цю послугу в електронну форму!",
                    sBody: $scope.$parent.$parent.data.city.sName + " - " + service.sName
                };
                MessagesService.setMessage(data, 'Дякуємо! Ви будете поінформовані, коли ця послуга буде доступна через Інтернет');
            }
	    }
    ]);
});
