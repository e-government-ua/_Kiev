define('service.country', ['angularAMD', 'service.country.link', 'service.country.built-in'], function (angularAMD) {
    var app = angular.module('service.country', []);

    app.config(['$stateProvider', function ($stateProvider) {
        $stateProvider
            .state('service.country', {
                url: '/country',
				resolve: {
					places: ['$stateParams', 'ServiceService', function($stateParams, ServiceService) {
						return ServiceService.getPlaces();
					}]
				},
                views: {
                    '': angularAMD.route({
                        templateProvider: ['$templateCache', function($templateCache) {
							return $templateCache.get('html/service/country/index.html');
						}],
						controller: 'ServiceCountryController',
                        controllerUrl: 'state/service/country/controller'
                    })
                }
            })
            .state('service.country.error', {
                url: '/error',
                views: {
                    'content@service.country': angularAMD.route({
                        templateProvider: ['$templateCache', function($templateCache) {
							return $templateCache.get('html/service/country/error.html');
						}]
                    })
                }
            })
    }]);
    return app;
});