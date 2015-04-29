define('service.country.built-in', ['angularAMD'], function (angularAMD) {
    var app = angular.module('service.country.built-in', []);

    app.config(['$stateProvider', function ($stateProvider) {
        $stateProvider
            .state('service.country.built-in', {
                url: '/built-in',
                views: {
                    '': angularAMD.route({
                        templateProvider: ['$templateCache', function($templateCache) {
							return $templateCache.get('html/service/country/built-in/index.html');
						}],
						controller: 'ServiceBuiltInController',
                        controllerUrl: 'service/built-in/controller'
                    })
                }
            })
			.state('service.country.built-in.bankid', {
				url: '/?code',
				onEnter: function() {
					console.log('service.country.built-in.bankid');
				},
				controller: [function() {
					console.log('service.country.built-in.bankid');
				}]
			})
    }]);
    return app;
});

