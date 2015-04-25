define('service.built-in', ['angularAMD'], function (angularAMD) {
    var app = angular.module('service.link', []);

    app.config(['$stateProvider', function ($stateProvider) {
        $stateProvider
            .state('service.built-in', {
                url: '/built-in',
                views: {
                    '': angularAMD.route({
                        templateProvider: ['$templateCache', function($templateCache) {
							return $templateCache.get('html/service/built-in/index.html');
						}],
						controller: 'ServiceBuiltInController',
                        controllerUrl: 'state/service/built-in/controller'
                    })
                }
            })
    }]);
    return app;
});

