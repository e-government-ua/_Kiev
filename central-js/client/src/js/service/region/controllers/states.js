define('state/service/region/controller', ['angularAMD'], function (angularAMD) {
	angularAMD.controller('ServiceRegionController', [
		'$state', '$rootScope', '$scope', '$sce', 'RegionListFactory', 'PlacesService', 'ServiceService', 'service', 'regions', 'AdminService',
		function ($state, $rootScope, $scope, $sce, RegionListFactory, PlacesService, ServiceService, service, regions, AdminService) {
			$scope.service = service;
			$scope.regions = regions;

			$scope.bAdmin = AdminService.isAdmin();

			$scope.regionList = new RegionListFactory();
			$scope.regionList.initialize(regions);
			
			$scope.loadRegionList = function(search) {
				return $scope.regionList.load(service, search);
			};
			
			$scope.onSelectRegionList = function($item, $model, $label) {
				$scope.data.region = $item;
				$scope.regionList.select($item, $model, $label);
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
				return $state.go('service.general.region', {id: $scope.service.nID});
			};
			
			$scope.step2 = function() {
				var aServiceData = $scope.service.aServiceData;
				var serviceType = { nID: 0 };
				angular.forEach(aServiceData, function(value, key) {
					if(value.nID_Region.nID == $scope.data.region.nID) {
						serviceType = value.nID_ServiceType;
						$scope.serviceData = value;
						$scope.serviceData.sNote = $sce.trustAsHtml($scope.serviceData.sNote);
					}
				});
				
				switch(serviceType.nID) {
					case 1:
						return $state.go('service.general.region.link', {id: $scope.service.nID}, { location: false });
					case 4:
						return $state.go('service.general.region.built-in', {id: $scope.service.nID}, { location: false });
					default:
						return $state.go('service.general.region.error', {id: $scope.service.nID}, { location: false });
				}
			};
			
			if($state.current.name == 'service.general.region.built-in.bankid') {
				return true;
			}
			
			$scope.$watchCollection('data.region', function(newValue, oldValue) {
				return (newValue == null) ? null: $scope.step2();
			});
		}
	]);
});



define('state/service/region/absent/controller', ['angularAMD'], function (angularAMD) {
	angularAMD.controller('ServiceRegionAbsentController', [
        '$state',
        '$rootScope',
        '$scope',
        'service',
        'MessagesService',
		'AdminService',
        function (
            $state,
            $rootScope,
            $scope,
            service,
            MessagesService,
			AdminService
        ) {
            $scope.service = service;
			$scope.bAdmin = AdminService.isAdmin();
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

                // @todo Fix hardcoded region name, we should pass it into state
                var data = {
                    sMail: absentMessage.email,
                    sHead: "Закликаю владу перевести цю послугу в електронну форму!",
                    sBody: $scope.$parent.$parent.data.region.sName + " - " + service.sName
                };
                MessagesService.setMessage(data, 'Дякуємо! Ви будете поінформовані, коли ця послуга буде доступна через Інтернет');
            }
	    }
    ]);
});