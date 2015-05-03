define('service.city.built-in', ['angularAMD'], function (angularAMD) {
    var app = angular.module('service.city.built-in', []);

    app.config(['$stateProvider', function ($stateProvider) {
        $stateProvider
            .state('service.city.built-in', {
                url: '/built-in',
                views: {
                    'content@service.city': angularAMD.route({
                        templateProvider: ['$templateCache', function($templateCache) {
							return $templateCache.get('html/service/city/built-in/index.html');
						}],
						controller: 'ServiceBuiltInController',
                        controllerUrl: 'service/built-in/controller'
                    })
                }
            })
			.state('service.city.built-in.bankid', {
				url: '/built-in/region/{region:int}/city/{city:int}/?code',
				parent: 'service.city',
				resolve: {
					BankIDLogin: ['$q', '$state', '$location', '$stateParams', 'BankIDService', function($q, $state, $location, $stateParams, BankIDService) {
						var url = $location.protocol()
							+'://'
							+$location.host()
							+':'
							+$location.port()
							+$state.href('service.city.built-in.bankid', { id: $stateParams.id, region: $stateParams.region, city: $stateParams.city });
						
						return BankIDService.login($stateParams.code, url).then(function(data) {
							return data.hasOwnProperty('error') ? $q.reject(null): data;
						});
					}],
					BankIDAccount: ['BankIDService', 'BankIDLogin', function(BankIDService, BankIDLogin) {
						return BankIDService.account(BankIDLogin.access_token);
					}]
				},
				views: {
					'content@service.city': angularAMD.route({
                        templateProvider: ['$templateCache', function($templateCache) {
							return $templateCache.get('html/service/city/built-in/bankid.html');
						}],
						controller: 'ServiceBuiltInBankIDController',
						controllerUrl: 'service/built-in/bankid/controller'
					})
				}
			})
    }]);	
    return app;
});

