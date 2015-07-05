angular.module('documents').config(function($stateProvider) {
  $stateProvider
    .state('index.documents', {
      url: 'documents',
      views: {
        'main@': {
          templateUrl: 'html/documents/index.html',
          controller: 'DocumentsController'
        }
      }
    })
    .state('index.documents.user', {
      url: '/user',
      views: {
        'content': {templateUrl: 'html/documents/user/index.html'}
      }
    })
    .state('index.documents.bankid', {
      url: '/bankid?error',
      parent: 'index.documents.user',
      views: {
        'content': {
          templateUrl: 'html/documents/bankid/index.html',
          controller: 'DocumentsBankIdController'
        }
      }
    })
    .state('index.documents.view', {
      url: '/view',
      parent: 'index.documents.user',
      views: {
        'content': {templateUrl: 'html/documents/view.html'}
      }
    })
    .state('index.documents.content', {
      url: '/content',
      parent: 'index.documents.user',
      resolve: {
        BankIDLogin: function($q, $state, $location, $stateParams, BankIDService) {
          return BankIDService.isLoggedIn().then(function() {
            return {loggedIn: true};
          }).catch(function() {
            return $q.reject(null);
          });
        },
        BankIDAccount: function(BankIDService) {
          return BankIDService.account();
        },
        customer: function(BankIDAccount) {
          return BankIDAccount.customer;
        },
        documents: function($q, $state, ServiceService) {
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
    .state('index.documents.search', {
      url: '/search',
      views: {
        'content': {
          templateUrl: 'html/documents/search/index.html'
        }
      }
    })
    .state('index.documents.notary', {
      url: '/notary',
      views: {
        'content': {
          templateUrl: 'html/documents/notary/index.html'
        }
      }
    });
});
