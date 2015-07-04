angular.module('documents', []).config(function($stateProvider) {
  $stateProvider
    .state('documents', {
      url: '/documents',
      views: {
        '': {
          templateUrl: 'html/documents/index.html',
          controller: 'DocumentsController'
        }
      }
    })
    .state('documents.user', {
      url: '/user',
      views: {
        'content': {templateUrl: 'html/documents/user/index.html'}
      }
    })
    .state('documents.bankid', {
      url: '/bankid?error',
      parent: 'documents.user',
      views: {
        'content': {
          templateUrl: 'html/documents/bankid/index.html',
          controller: 'DocumentsBankIdController'
        }
      }
    })
    .state('documents.view', {
      url: '/view',
      parent: 'documents.user',
      views: {
        'content': {templateUrl: 'html/documents/view.html'}
      }
    })
    .state('documents.content', {
      url: '/content',
      parent: 'documents.user',
      resolve: {
        BankIDLogin: function($q, $state, $location, $stateParams, BankIDService) {
          return BankIDService.isLoggedIn().then(function() {
            return {loggedIn: true};
          }).catch(function() {
            return $q.reject(null);
          });
        },
        BankIDAccount: function(BankIDService, BankIDLogin) {
          return BankIDService.account();
        },
        customer: function(BankIDAccount) {
          return BankIDAccount.customer;
        },
        documents: function($q, $state, ServiceService, BankIDLogin, customer) {
          return ServiceService.getOrUploadDocuments()
            .then(function(data) {
              return data;
            });
        }
      },
      views: {
        'content': {
          templateUrl: 'html/documents/content.html',
          controller: 'DocumentsContentController'
        }
      }
    })
    .state('documents.search', {
      url: '/search',
      views: {
        'content': {
          templateUrl: 'html/documents/search/index.html'
        }
      }
    })
    .state('documents.notary', {
      url: '/notary',
      views: {
        'content': {
          templateUrl: 'html/documents/notary/index.html'
        }
      }
    });
});
