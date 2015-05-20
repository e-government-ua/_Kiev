define('index', ['angularAMD', 'catalog/service'], function (angularAMD) {
    var app = angular.module('index', []);

    app.config(['$stateProvider', function ($stateProvider) {
        $stateProvider
            .state('index', {
                url: '/index',
				resolve: {
					catalog: ['CatalogService', function(CatalogService) {
						return CatalogService.getServices();
					}]
				},
                views: {
                    '': angularAMD.route({
                        templateProvider: ['$templateCache', function($templateCache) {
							return $templateCache.get('html/catalog/services.html');
						}],
						controller: 'IndexController',
                        controllerUrl: 'state/index/controller'
                    })
                }
            })
    }]).run(function($rootScope, $state) {
        $rootScope.state = $state;
    });

    return app;
});

