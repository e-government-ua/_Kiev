angular.module('documents').config(function ($stateProvider, statesRepositoryProvider) {
  statesRepositoryProvider.init(window.location.host);
  if (statesRepositoryProvider.isCentral()) {
    $stateProvider
      .state('index.documents', {
        url: 'documents',
        views: {
          'main@': {
            templateUrl: 'app/documents/index.html',
            controller: 'DocumentsController'
          }
        }
      })
      .state('index.documents.user', {
        url: '/user',
        views: {
          'content': {templateUrl: 'app/documents/user/index.html'}
        }
      })
      .state('index.documents.bankid', {
        url: '/bankid?error',
        parent: 'index.documents.user',
        views: {
          'content': {
            templateUrl: 'app/documents/bankid/index.html',
            controller: 'DocumentsBankIdController'
          }
        }
      })
      .state('index.documents.view', {
        url: '/view',
        parent: 'index.documents.user',
        views: {
          'content': {templateUrl: 'app/documents/view.html'}
        }
      })
      .state('index.documents.content', {
        url: '/content',
        parent: 'index.documents.user',
        resolve: {
          documents: function ($q, $state, ServiceService) {
            return ServiceService.getOrUploadDocuments();
          }
        },
        views: {
          'content': {
            templateUrl: 'app/documents/content.html',
            controller: 'DocumentsContentController'
          }
        }
      })
      .state('index.documents.search', {
        url: '/search',
        //parent: 'index.documents.user',
        resolve: {
          types: function ($q, $state, ServiceService) {
            return ServiceService.getDocumentTypes();
          },
          operators: function ($q, $state, ServiceService) {
            return ServiceService.getDocumentOperators();
          }
        },
        views: {
          'content': {
            templateUrl: 'app/documents/search/index.html',
            controller: 'DocumentsSearchController'
          }
        }
      })
      .state('index.documents.notary', {
        url: '/notary',
        views: {
          'content': {
            templateUrl: 'app/documents/notary/index.html'
          }
        }
      });
  }
});
