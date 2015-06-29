define('journal', ['angularAMD', 'config', 'service'], function (angularAMD) {
    var app = angular.module('journal', []);

    app.config(['$stateProvider', function ($stateProvider) {
        $stateProvider
            .state('journal', {
                url: '/journal',
                views: {
                    '': angularAMD.route({
                        templateProvider: ['$templateCache', function($templateCache) {
							return $templateCache.get('html/journal/index.html');
						}],
						controller: 'JournalController',
                        controllerUrl: 'state/journal/controller'
                    })
                }
            })
            .state('journal.bankid', {
                url: '/bankid?code',
                parent: 'journal',
                views: {
                    'bankid': angularAMD.route({
                        templateProvider: ['$templateCache', function($templateCache) {
							return $templateCache.get('html/journal/bankid/index.html');
						}],
						controller: 'JournalBankIdController',
                        controllerUrl: 'state/journal/bankid/controller'
                    })
                }
            })
            .state('journal.content', {
                url: '/content?code',
                parent: 'journal',
                resolve: {
                    BankIDLogin: function($q, $state, $location, $stateParams, BankIDService) {
                        return BankIDService.isLoggedIn().then(function () {
                            return {loggedIn: true};
                        }).catch(function() {
                            return $q.reject(null);
                        });
                    },
                    BankIDAccount: ['BankIDService', 'BankIDLogin', function(BankIDService, BankIDLogin) {
                        return BankIDService.account();
                    }],
                    customer: ['BankIDAccount', function (BankIDAccount) {
                        return BankIDAccount.customer;
                    }],
                    journal: ['$q', '$state', 'ServiceService', 'customer', function($q, $state, ServiceService, customer) {
                        return ServiceService.getJournalEvents();
                    }]
                },
                views: {
                    'content': angularAMD.route({
                        templateProvider: ['$templateCache', function($templateCache) {
							return $templateCache.get('html/journal/content.html');
						}],
						controller: 'JournalContentController',
                        controllerUrl: 'state/journal/content/controller'
                    })
                }
            })
    }]);
    return app;
});

