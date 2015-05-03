define('service', ['angularAMD', 'service.country', 'service.region', 'service.city', 'service/service', 'bankid/service', 'activiti/service'], function (angularAMD) {
    var app = angular.module('service', []);

    app.config(['$stateProvider', function ($stateProvider) {
        $stateProvider
            .state('service', {
                url: '/service/{id:int}',
				resolve: {
					service: ['$stateParams', 'ServiceService', function($stateParams, ServiceService) {
						return ServiceService.get($stateParams.id);
					}]
				},
                views: {
                    '': angularAMD.route({
                        templateProvider: ['$templateCache', function($templateCache) {
							return $templateCache.get('html/service/index.html');
						}],
						controller: 'ServiceController',
                        controllerUrl: 'state/service/controller'
                    })
                }
            })
			.state('service.general', {
				url: '/general',
                views: {
                    '': angularAMD.route({
                        templateProvider: ['$templateCache', function($templateCache) {
							return $templateCache.get('html/service/general.html');
						}],
						controller: 'ServiceGeneralController',
                        controllerUrl: 'state/service/general/controller'
                    })
                }
			})
			.state('service.instruction', {
				url: '/instruction'
			})
			.state('service.legislation', {
				url: '/legislation'
			})
			.state('service.questions', {
				url: '/questions'
			})
			.state('service.discussion', {
				url: '/discussion'
			})
    }]);
    return app;
});

