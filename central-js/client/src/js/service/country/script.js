define('service.general.country', ['angularAMD', 'service.general.country.link', 'service.general.country.built-in'], function (angularAMD) {
    var app = angular.module('service.general.country', []);

    app.config(['$stateProvider', function ($stateProvider) {
        $stateProvider
            .state('service.general.country', {
                url: '/country',
                views: {
                    '@service': angularAMD.route({
                        templateProvider: ['$templateCache', function($templateCache) {
							return $templateCache.get('html/service/country/index.html');
						}],
						controller: 'ServiceCountryController',
                        controllerUrl: 'state/service/country/controller'
                    })
                }
            })
            .state('service.general.country.error', {
                url: '/absent',
                views: {
					'status@service.general.country': angularAMD.route({
                        templateProvider: ['$templateCache', function($templateCache) {
							return $templateCache.get('html/service/country/absent.html');
						}],
                        controller: 'ServiceCountryAbsentController',
                        controllerUrl: 'state/service/country/absent/controller'
                    })
                }
            })
    }]);
    return app;
});