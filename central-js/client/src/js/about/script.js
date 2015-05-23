define('about', ['angularAMD'], function (angularAMD) {
    var app = angular.module('about', []);

    app.config(['$stateProvider', function ($stateProvider) {
        $stateProvider
            .state('about', {
                url: '/about',
                views: {
                    '': angularAMD.route({
                        templateProvider: ['$templateCache', function($templateCache) {
							return $templateCache.get('html/about/index.html');
						}],
						controller: 'AboutController',
                        controllerUrl: 'state/about/controller'
                    })
                }
            })
    }]);
    return app;
});

