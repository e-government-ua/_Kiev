define('service.country.link', ['angularAMD', 'service.link'], function (angularAMD) {
    var app = angular.module('service.country.link', []);

    app.config(['$stateProvider', function ($stateProvider) {
        $stateProvider
            .state('service.country.link', {
                url: '/link',
                views: {
                    '': angularAMD.route({
                        templateProvider: ['$templateCache', function($templateCache) {
							return $templateCache.get('html/service/country/link/index.html');
						}],
						controller: 'ServiceLinkController',
                        controllerUrl: 'service/link/controller'
                    })
                }
            })
    }]);
    return app;
});

