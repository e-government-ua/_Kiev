define('service.general.city.built-in', ['angularAMD'], function (angularAMD) {
    var app = angular.module('service.general.city.built-in', []);

    app.config(['$stateProvider', function ($stateProvider) {
        $stateProvider
            .state('service.general.city.built-in', {
                url: '/built-in',
                views: {
                    'content@service.general.city': angularAMD.route({
                        templateProvider: ['$templateCache', function($templateCache) {
							return $templateCache.get('html/service/city/built-in/index.html');
						}],
						controller: 'ServiceBuiltInController',
                        controllerUrl: 'service/built-in/controller'
                    })
                }
            })
			.state('service.general.city.built-in.bankid', {
				url: '/built-in/region/{region:int}/city/{city:int}/?code',
				parent: 'service.general.city',
				resolve: {
					oServiceData: ['$stateParams', 'service', 'places', function($stateParams, service, places) {
						var aServiceData = service.aServiceData;
						var oServiceData = null;
						angular.forEach(aServiceData, function(value, key) {
							if(value.nID_City == $stateParams.city) {
								oServiceData = value;
							}
						});
						return oServiceData;
					}],
					BankIDLogin: ['$q', '$state', '$location', '$stateParams', 'BankIDService', function($q, $state, $location, $stateParams, BankIDService) {
						var url = $location.protocol()
							+'://'
							+$location.host()
							+':'
							+$location.port()
							+$state.href('service.general.city.built-in.bankid', { id: $stateParams.id, region: $stateParams.region, city: $stateParams.city });
						
						return BankIDService.login($stateParams.code, url).then(function(data) {
							return data.hasOwnProperty('error') ? $q.reject(null): data;
						});
					}],
					BankIDAccount: ['BankIDService', 'BankIDLogin', function(BankIDService, BankIDLogin) {
						return BankIDService.account(BankIDLogin.access_token);
					}],
					ActivitiForm: ['ActivitiService', 'oServiceData', function(ActivitiService, oServiceData) {
						return ActivitiService.getForm(oServiceData);
					}]
				},
				views: {
					'content@service.general.city': angularAMD.route({
                        templateProvider: ['$templateCache', function($templateCache) {
							return $templateCache.get('html/service/city/built-in/bankid.html');
						}],
						controller: 'ServiceBuiltInBankIDController',
						controllerUrl: 'service/built-in/bankid/controller'
					})
				}
			})
			.state('service.general.city.built-in.bankid.submitted', {
				url: null,
				data: { id: null },
				onExit:['$state', function($state) {
					var state = $state.get('service.general.city.built-in.bankid.submitted');
					state.data = { id: null };
				}],
				views: {
					'content@service.general.city': angularAMD.route({
                        templateProvider: ['$templateCache', function($templateCache) {
							return $templateCache.get('html/service/city/built-in/bankid.submitted.html');
						}],
						controller: ['$state', '$scope', function($state, $scope) {
							$scope.state = $state.get('service.general.city.built-in.bankid.submitted');
						}]
					})
				}
			});
    }]);	
    return app;
});

