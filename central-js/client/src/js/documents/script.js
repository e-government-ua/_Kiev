define('documents', ['angularAMD', 'service'], function (angularAMD) {
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
                url: '/bankid?code',
                parent: 'documents',
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
                parent: 'documents',
                resolve: {
                    BankIDLogin: ['$q', '$state', '$location', '$stateParams', 'BankIDService', function($q, $state, $location, $stateParams, BankIDService) {
                        var url = $location.protocol()
                            +'://'
                            +$location.host()
                            +':'
                            +$location.port()
                            +$state.href('documents.bankid');

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
                    documents: ['$q', 'customer', function($q, customer) {
                        var deferred = $q.defer();

                        try {

                            // TODO: missing function
                            /*
                             syncSubject(customer.inn).then(function (data) {
                             if (data.hasOwnProperty('error')) { return deferred.reject(null); }
                             $window.nID_Subject = data.nID;
                             DocumentsService.getDocuments($window.nID_Subject).then(function (data) {
                             return data.hasOwnProperty('error') ? deferred.reject(null): data;
                             })
                             })
                             */

                            var documents = [
                                {
                                    nID : "1",
                                    nID_Subject : "1",
                                    sID_Content : "content",
                                    sName : "Громадянський паспорт",
                                    sFile : "doc.jpg",
                                    //undescribed in task fields, but exists in the mock layout
                                    sDate_Upload : new Date("2015-05-25 11:12:35.000").getTime(),
                                    sID_Subject_Upload : "OshadBank",
                                    sSubjectName_Upload : "Ощадбанк"
                                },
                                {
                                    nID : "2",
                                    nID_Subject : "2",
                                    sID_Content : "content",
                                    sName : "Водійське посвідчення",
                                    sFile : "doc.jpg",
                                    //undescribed in task fields, but exists in the mock layout
                                    sDate_Upload : new Date("2015-04-04 13:10:35.000").getTime(),
                                    sID_Subject_Upload : "GYMVS_Lviv",
                                    sSubjectName_Upload: "ГУМВС м. Львів"
                                },
                                {
                                    nID : "3",
                                    nID_Subject : "3",
                                    sID_Content : "content",
                                    sName : "Довідка про несудимість",
                                    sFile : "doc.jpg",
                                    //undescribed in task fields, but exists in the mock layout
                                    sDate_Upload : new Date("2013-02-11 18:00:12.000").getTime(),
                                    sID_Subject_Upload : "GYMVS_Lviv",
                                    sSubjectName_Upload: "ГУМВС м. Львів"
                                },
                                {
                                    nID : "4",
                                    nID_Subject : "4",
                                    sID_Content : "content",
                                    sName : "Довідка про несудимість",
                                    sFile : "doc.jpg",
                                    //undescribed in task fields, but exists in the mock layout
                                    sDate_Upload : new Date("2015-02-13 17:00:11.000").getTime(),
                                    sID_Subject_Upload : "GYMVS_Lviv",
                                    sSubjectName_Upload: "ГУМВС м. Львів"
                                },
                                {
                                    nID : "5",
                                    nID_Subject : "5",
                                    sID_Content : "content",
                                    sName : "Ідентифікаційний номер платника податків",
                                    sFile : "doc.jpg",
                                    //undescribed in task fields, but exists in the mock layout
                                    sDate_Upload : new Date("2012-06-02 14:31:16.000").getTime(),
                                    sID_Subject_Upload : "Podatkova_Inspekcia_Lviv",
                                    sSubjectName_Upload: "Податкова інспекція м. Львів"
                                }

                            ];
                            deferred.resolve(documents);
                            return deferred.promise;
                        }
                        catch (err) {
                            return deferred.reject(err);
                        }
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

