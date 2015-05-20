define('service.general.city.link', ['angularAMD'], function (angularAMD) {
    var app = angular.module('service.general.city.link', []);

    app.config(['$stateProvider', function ($stateProvider) {
        $stateProvider
            .state('service.general.city.link', {
                url: '/link',
                views: {
                    'content@service.general.city': angularAMD.route({
                        templateProvider: ['$templateCache', function($templateCache) {
							return $templateCache.get('html/service/city/link/index.html');
						}],
						controller: 'ServiceLinkController',
                        controllerUrl: 'service/link/controller'
                    })
                }
            })
    }]);
    return app;
});

