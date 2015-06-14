define('documents', ['angularAMD', 'service', 'file2/directive', 'file/factory'], function(angularAMD) {
  var app = angular.module('Documents', []);

  app.config(function($stateProvider) {
    $stateProvider
      .state('documents', {
        url: '/documents',
        views: {
          '': angularAMD.route({
            templateProvider: function($templateCache) {
              return $templateCache.get('html/documents/index.html');
            },
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
            templateProvider: function($templateCache) {
              return $templateCache.get('html/documents/bankid/index.html');
            },
            controller: 'DocumentsBankIdController',
            controllerUrl: 'state/documents/bankid/controller'
          })
        }
      })
      .state('documents.view', {
        url: '/view',
        parent: 'documents',
        views: {
          'view': angularAMD.route({
            templateProvider: function($templateCache) {
              return $templateCache.get('html/documents/view.html');
            }
          })
        }       
      })
      .state('documents.content', {
        url: '/content?code',
        parent: 'documents',
        resolve: {
          BankIDLogin: function($q, $state, $location, $stateParams, BankIDService) {
            var url = $location.protocol()
              + '://'
              + $location.host()
              + ':'
              + $location.port()
              + $state.href('documents.bankid', {code: ''});

            return BankIDService.login($stateParams.code, url).then(function(data) {
              return data.hasOwnProperty('error') ? $q.reject(null) : data;
            });
          },
          BankIDAccount: function(BankIDService, BankIDLogin) {
            return BankIDService.account(BankIDLogin.access_token);
          },
          customer: function(BankIDAccount) {
            return BankIDAccount.customer;
          },
          subject: function($q, $state, ServiceService, customer) {
            $state.customer = customer;
            return ServiceService.syncSubject(customer.inn).then(function(data) {
              return data.hasOwnProperty('error') ? $q.reject(null) : data;
            });
          },
          documents: function($q, $state, subject, ServiceService, BankIDLogin) {
            $state.nID_Subject = subject.nID;
            $state.sID_Subject = subject.sID;
            return ServiceService.getOrUploadDocuments(
                            BankIDLogin.access_token,
                            $state.nID_Subject,
                            $state.sID_Subject)
                        .then(function(data) {
                            return data;
                        });
          }
        },
        views: {
          'content': angularAMD.route({
            templateProvider: function($templateCache) {
              return $templateCache.get('html/documents/content.html');
            },
            controller: 'DocumentsContentController',
            controllerUrl: 'state/documents/content/controller'
          })
        }
      });
  });
  return app;
});