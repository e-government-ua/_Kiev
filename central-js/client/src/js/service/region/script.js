define('service.region', ['angularAMD', 'service.region.link', 'service.region.built-in'], function (angularAMD) {
    var app = angular.module('service.region', []);

    app.config(['$stateProvider', function ($stateProvider) {
        $stateProvider
            .state('service.region', {
                url: '/region',
				resolve: {
					places: ['$stateParams', 'ServiceService', function($stateParams, ServiceService) {
						return ServiceService.getPlaces();
					}]
				},
                views: {
                    '': angularAMD.route({
                        templateProvider: ['$templateCache', function($templateCache) {
							return $templateCache.get('html/service/region/index.html');
						}],
						controller: 'ServiceRegionController',
                        controllerUrl: 'state/service/region/controller'
                    }),
					'content@service.region': angularAMD.route({
                        templateProvider: ['$templateCache', function($templateCache) {
							return $templateCache.get('html/service/region/content.html');
						}]
                    })
                }
            })
    }]);
    return app;
});

