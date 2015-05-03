define('service.city', ['angularAMD', 'service.city.link', 'service.city.built-in'], function (angularAMD) {
    var app = angular.module('service.city', []);

    app.config(['$stateProvider', function ($stateProvider) {
        $stateProvider
            .state('service.city', {
                url: '/city',
				resolve: {
					places: ['$stateParams', 'ServiceService', function($stateParams, ServiceService) {
						return ServiceService.getPlaces();
					}]
				},
                views: {
                    '': angularAMD.route({
                        templateProvider: ['$templateCache', function($templateCache) {
							return $templateCache.get('html/service/city/index.html');
						}],
						controller: 'ServiceCityController',
                        controllerUrl: 'state/service/city/controller'
                    }),
					'content@service.city': angularAMD.route({
                        templateProvider: ['$templateCache', function($templateCache) {
							return $templateCache.get('html/service/city/content.html');
						}]
                    })
                }
            })
            .state('service.city.error', {
                url: '/error',
                views: {
					'content@service.city': angularAMD.route({
                        templateProvider: ['$templateCache', function($templateCache) {
							return $templateCache.get('html/service/city/error.html');
						}]
                    })
                }
            })
    }]);
    return app;
});