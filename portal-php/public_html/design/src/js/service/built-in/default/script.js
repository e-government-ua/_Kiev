define('service.built-in.default', ['angularAMD'], function (angularAMD) {
    var app = angular.module('service.built-in.default', []);

    app.config(['$stateProvider', function ($stateProvider) {
        $stateProvider
            .state('service.built-in.default', {
                url: '/default',
                views: {
                    '': angularAMD.route({
                        templateProvider: ['$templateCache', function($templateCache) {
							return $templateCache.get('html/service/built-in/default/index.html');
						}],
						controller: 'ServiceBuiltInDefaultController',
                        controllerUrl: 'state/service/built-in/default/controller'
                    })
                }
            })
    }]);
    return app;
});

