define('journal', ['angularAMD'], function (angularAMD) {
    var app = angular.module('journal', []);

    app.config(['$stateProvider', function ($stateProvider) {
        $stateProvider
            .state('journal', {
                url: '/journal',
                views: {
                    '': angularAMD.route({
                        template: '',
                        controllerUrl: 'state/journal/controller'
                    })
                }
            })
    }]);
    return app;
});

