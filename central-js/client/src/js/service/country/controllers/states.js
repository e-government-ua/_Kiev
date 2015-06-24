define('state/service/country/controller', ['angularAMD'], function (angularAMD) {
	angularAMD.controller('ServiceCountryController', ['$state', '$rootScope', '$scope', '$sce', 'service', 'AdminService',
		function ($state, $rootScope, $scope, $sce, service, AdminService) {
			$scope.service = service;

			$scope.bAdmin = AdminService.isAdmin();

			$scope.data = {
				region: null,
				city: null
			};
			
			$scope.step1 = function() {
				var aServiceData = $scope.service.aServiceData;
				var serviceType = { nID: 0 };
				angular.forEach(aServiceData, function(value, key) {
					serviceType = value.nID_ServiceType;
					$scope.serviceData = value;
					$scope.serviceData.sNote = $sce.trustAsHtml($scope.serviceData.sNote);
				});
					
				switch(serviceType.nID) {
					case 1:
						return $state.go('service.general.country.link', {id: $scope.service.nID}, { location: false });
					case 4:
						return $state.go('service.general.country.built-in', {id: $scope.service.nID}, { location: false });
					default:
						return $state.go('service.general.country.error', {id: $scope.service.nID}, { location: false });
				}
			}
			
			if($state.current.name == 'service.general.country.built-in.bankid') {
				return true;
			}
			
			$scope.step1();
		}
	]);
});


define('state/service/country/absent/controller', ['angularAMD'], function (angularAMD) {
	angularAMD.controller('ServiceCountryAbsentController', [
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
                    sBody: "Україна - " + service.sName
                };
                MessagesService.setMessage(data, 'Дякуємо! Ви будете поінформовані, коли ця послуга буде доступна через Інтернет');
            }
	    }
    ]);
});
