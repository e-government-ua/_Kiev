define('service', ['angularAMD', 'service.general.country', 'service.general.region', 'service.general.city', 'places/service', 'service/service', 'messages/service', 'bankid/service', 'activiti/service', 'options/class/directive'], function (angularAMD) {
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
				url: '/instruction',
                            views: {
                                '': angularAMD.route({
                                    templateProvider: ['$templateCache', function($templateCache) {
                                                                    return $templateCache.get('html/service/instruction.html');
                                                            }],
                                                            controller: 'ServiceInstructionController',
                                    controllerUrl: 'state/service/instruction/controller'
                                })
                            }
			})
			.state('service.legislation', {
				url: '/legislation',
                            views: {
                                '': angularAMD.route({
                                    templateProvider: ['$templateCache', function($templateCache) {
                                                                    return $templateCache.get('html/service/legislation.html');
                                                            }],
                                                            controller: 'ServiceLegislationController',
                                    controllerUrl: 'state/service/legislation/controller'
                                })
                            }
			})
			.state('service.questions', {
				url: '/questions',
                            views: {
                                '': angularAMD.route({
                                    templateProvider: ['$templateCache', function($templateCache) {
                                                                    return $templateCache.get('html/service/questions.html');
                                                            }],
                                                            controller: 'ServiceQuestionsController',
                                    controllerUrl: 'state/service/questions/controller'
                                })
                            }
			})
			.state('service.discussion', {
				url: '/discussion',
                            views: {
                                '': angularAMD.route({
                                    templateProvider: ['$templateCache', function($templateCache) {
                                                                    return $templateCache.get('html/service/discussion.html');
                                                            }],
                                                            controller: 'ServiceDiscussionController',
                                    controllerUrl: 'state/service/discussion/controller'
                                })
                            }
			})
    }]);
    return app;
});

