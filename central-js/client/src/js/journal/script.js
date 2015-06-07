define('journal', ['angularAMD', 'service'], function (angularAMD) {
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
                    BankIDLogin: ['$q', '$state', '$location', '$stateParams', 'BankIDService', function($q, $state, $location, $stateParams, BankIDService) {
                        var url = $location.protocol()
                            +'://'
                            +$location.host()
                            +':'
                            +$location.port()
                            +$state.href('journal.bankid', {code: null});

                        return BankIDService.login($stateParams.code, url).then(function(data) {
                            return data.hasOwnProperty('error') ? $q.reject(null): data;
                        });
                    }],
                    BankIDAccount: ['BankIDService', 'BankIDLogin', function(BankIDService, BankIDLogin) {
                        return BankIDService.account(BankIDLogin.access_token);
                    }],
                    customer: ['BankIDAccount', function (BankIDAccount) {
                        return BankIDAccount.customer;
                    }],
                    subject: ['$q', '$state', 'ServiceService', 'customer', function($q, $state, ServiceService, customer) {
                        $state.customer = customer;
                        return ServiceService.syncSubject(customer.inn).then(function(data) {
                            return data.hasOwnProperty('error') ? $q.reject(null): data;
                        });
                    }],
                    journal: ['$q', '$state', 'subject', 'ServiceService', function($q, $state, subject, ServiceService) {
                        $state.nID_Subject = subject.nID;
                        // @todo Error processing
                        return ServiceService.getJournalEvents($state.nID_Subject);
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

