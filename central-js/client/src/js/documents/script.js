define('documents', ['angularAMD', 'bankid/service'], function (angularAMD) {
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
            .state('documents.bankid', {
                url: '/bankid',
                views: {
                    'bankid': angularAMD.route({
                        templateProvider: ['$templateCache', function($templateCache) {
							return $templateCache.get('html/documents/bankid/index.html');
						}],
						controller: 'DocumentsBankIdController',
                        controllerUrl: 'state/documents/bankid/controller'
                    })
                }
            })
            .state('documents.content', {
                url: '/content?code',
                resolve: {
                    BankIDLogin: ['$q', '$http', '$state', '$location', '$stateParams', function ($q, $http, $state, $location, $stateParams) {
                        var deferred = $q.defer();
                        // TODO: BankIDService login should be used instead of code below.
                        var redirectURL = $location.protocol()
                            +'://'
                            +$location.host()
                            +':'
                            +$location.port()
                            +$state.href('documents.content', { code: $stateParams.code });
                        var bankidURL = 'https://bankid.privatbank.ua/DataAccessService/oauth/token?grant_type=authorization_code' +
                            '&client_id=dniprorada&client_secret=NzVmYTI5NGJjMDg3OThlYjljNDY5YjYxYjJiMjJhNA==&code=' + $stateParams.code +
                            '&redirect_uri=' + redirectURL;

                        return $http.get(bankidURL)
                            .success(function(data, status, headers, config) {
                                    console.log(status);
                                    return data;
                            })
                            .error(function(data, status, headers, config) {
                                    console.log(data);
                                    return data;
                            });
                    }]
                },
                views: {
                    'content': angularAMD.route({
                        templateProvider: ['$templateCache', function($templateCache) {
							return $templateCache.get('html/documents/content.html');
						}],
						controller: 'DocumentsContentController',
                        controllerUrl: 'state/documents/content/controller'
                    })
                }
            })
    }]);
    return app;
});

