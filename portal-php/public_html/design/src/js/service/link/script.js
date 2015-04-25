define('service.link', ['angularAMD'], function (angularAMD) {
    var app = angular.module('service.link', []);

    app.config(['$stateProvider', function ($stateProvider) {
        $stateProvider
            .state('service.link', {
                url: '/link',
                views: {
                    '': angularAMD.route({
                        templateProvider: ['$templateCache', function($templateCache) {
							return $templateCache.get('html/service/link/index.html');
						}],
						controller: 'ServiceLinkController',
                        controllerUrl: 'state/service/link/controller'
                    })
                }
            })
    }]);
    return app;
});

