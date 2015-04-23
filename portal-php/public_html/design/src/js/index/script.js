define('index', ['angularAMD'], function (angularAMD) {
    var app = angular.module('index', []);

    app.config(['$stateProvider', function ($stateProvider) {
        $stateProvider
            .state('index', {
                url: '/index',
                views: {
                    '': angularAMD.route({
                        template: '',
                        controllerUrl: 'state/index/controller'
                    })
                }
            })
    }]);
    return app;
});

