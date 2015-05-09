define('documents', ['angularAMD'], function (angularAMD) {
    var app = angular.module('Documents', []);

    app.config(['$stateProvider', function ($stateProvider) {
        $stateProvider
            .state('documents', {
                url: '/documents',
                views: {
                    '': angularAMD.route({
                        templateProvider: ['$templateCache', function($templateCache) {
							return $templateCache.get('html/documents/index.html');
						}],
						controller: 'DocumentsController',
                        controllerUrl: 'state/documents/controller'
                    })
                }
            })
    }]);
    return app;
});

