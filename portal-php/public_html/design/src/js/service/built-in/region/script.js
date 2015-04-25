define('service.built-in.region', ['angularAMD'], function (angularAMD) {
    var app = angular.module('service.built-in.region', []);

    app.config(['$stateProvider', function ($stateProvider) {
        $stateProvider
            .state('service.built-in.region', {
                url: '/region',
                views: {
                    '': angularAMD.route({
                        templateProvider: ['$templateCache', function($templateCache) {
							return $templateCache.get('html/service/built-in/region/index.html');
						}],
						controller: 'ServiceBuiltInRegionController',
                        controllerUrl: 'state/service/built-in/region/controller'
                    })
                }
            })
    }]);
    return app;
});

