define('service.general.region', ['angularAMD', 'service.general.region.link', 'service.general.region.built-in'], function (angularAMD) {
    var app = angular.module('service.general.region', []);

    app.config(['$stateProvider', function ($stateProvider) {
        $stateProvider
            .state('service.general.region', {
                url: '/region',
				resolve: {
					places: ['$stateParams', 'ServiceService', function($stateParams, ServiceService) {
						return ServiceService.getPlaces();
					}]
				},
                views: {
                    '@service': angularAMD.route({
                        templateProvider: ['$templateCache', function($templateCache) {
							return $templateCache.get('html/service/region/index.html');
						}],
						controller: 'ServiceRegionController',
                        controllerUrl: 'state/service/region/controller'
                    }),
					'content@service.general.region': angularAMD.route({
                        templateProvider: ['$templateCache', function($templateCache) {
							return $templateCache.get('html/service/region/content.html');
						}]
                    })
                }
            })
            .state('service.general.region.error', {
                url: '/absent',
                views: {
					'content@service.general.region': angularAMD.route({
                        templateProvider: ['$templateCache', function($templateCache) {
							return $templateCache.get('html/service/region/absent.html');
						}]
                    })
                }
            })
    }]);
    return app;
});

