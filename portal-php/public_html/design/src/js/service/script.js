define('service', ['angularAMD', 'service.link', 'service.built-in'], function (angularAMD) {
    var app = angular.module('service', []);

    app.config(['$stateProvider', function ($stateProvider) {
        $stateProvider
            .state('service', {
                url: '/service/{id:int}',
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
				url: '/general'
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

