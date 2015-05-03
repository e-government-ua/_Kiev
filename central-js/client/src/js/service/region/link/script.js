define('service.region.link', ['angularAMD'], function (angularAMD) {
    var app = angular.module('service.region.link', []);

    app.config(['$stateProvider', function ($stateProvider) {
        $stateProvider
            .state('service.region.link', {
                url: '/link',
                views: {
                    'content@service.region': angularAMD.route({
                        templateProvider: ['$templateCache', function($templateCache) {
							return $templateCache.get('html/service/region/link/index.html');
						}],
						controller: 'ServiceLinkController',
                        controllerUrl: 'service/link/controller'
                    })
                }
            })
    }]);
    return app;
});

