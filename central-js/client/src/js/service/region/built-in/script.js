define('service.general.region.built-in', ['angularAMD'], function (angularAMD) {
    var app = angular.module('service.general.region.built-in', []);

    app.config(['$stateProvider', function ($stateProvider) {
        $stateProvider
            .state('service.general.region.built-in', {
                url: '/built-in',
                views: {
                    'content@service.general.region': angularAMD.route({
                        templateProvider: ['$templateCache', function($templateCache) {
							return $templateCache.get('html/service/region/built-in/index.html');
						}],
						controller: 'ServiceBuiltInController',
                        controllerUrl: 'service/built-in/controller'
                    })
                }
            })
			.state('service.general.region.built-in.bankid', {
				url: '/built-in/region/{region:int}/?code',
				parent: 'service.general.region',
				resolve: {
					BankIDLogin: ['$q', '$state', '$location', '$stateParams', 'BankIDService', function($q, $state, $location, $stateParams, BankIDService) {
						var url = $location.protocol()
							+'://'
							+$location.host()
							+':'
							+$location.port()
							+$state.href('service.general.region.built-in.bankid', { id: $stateParams.id, region: $stateParams.region });
						
						return BankIDService.login($stateParams.code, url).then(function(data) {
							return data.hasOwnProperty('error') ? $q.reject(null): data;
						});
					}],
					BankIDAccount: ['BankIDService', 'BankIDLogin', function(BankIDService, BankIDLogin) {
						return BankIDService.account(BankIDLogin.access_token);
					}],
					ActivitiForm: ['$stateParams', 'ActivitiService', 'service', 'places', function($stateParams, ActivitiService, service, places) {
						var aServiceData = service.aServiceData;
						var oServiceData = null;
						angular.forEach(aServiceData, function(value, key) {
							if(value.nID_Region.nID == $stateParams.region) {
								oServiceData = value;
							}
						});
						return ActivitiService.getForm(oServiceData);
					}]
				},
				views: {
					'content@service.general.region': angularAMD.route({
                        templateProvider: ['$templateCache', function($templateCache) {
							return $templateCache.get('html/service/region/built-in/bankid.html');
						}],
						controller: 'ServiceBuiltInBankIDController',
						controllerUrl: 'service/built-in/bankid/controller'
					})
				}
			})
    }]);
    return app;
});

