define('service', ['angularAMD'], function (angularAMD) {
    var app = angular.module('service', []);

    app.config(['$stateProvider', function ($stateProvider) {
        $stateProvider
            .state('service', {
                url: '/service',
                views: {
                    '': angularAMD.route({
                        template: '',
                        controllerUrl: 'state/service/controller'
                    })
                }
            })
    }]);
    return app;
});

