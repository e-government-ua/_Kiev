angular.module('journal').config(function ($stateProvider, statesRepositoryProvider) {
  statesRepositoryProvider.init(window.location.host);
//  if (statesRepositoryProvider.isCentral()) {
    $stateProvider
      .state('index.journal', {
        url: 'journal',
        views: {
          'main@': {
            templateUrl: 'app/journal/journal.html',
            controller: 'JournalController'
          }
        }
      })
      .state('index.journal.bankid', {
        url: '/bankid?error',
        parent: 'index.journal',
        views: {
          'bankid': {
            templateUrl: 'app/journal/bankid/index.html',
            controller: 'JournalBankIdController'
          }
        }
      })
      .state('index.journal.content', {
        url: '/content',
        parent: 'index.journal',
        resolve: {
          BankIDLogin: function ($q, $state, $location, $stateParams, BankIDService) {
            return BankIDService.isLoggedIn().then(function () {
              return {loggedIn: true};
            }).catch(function () {
              return $q.reject(null);
            });
          },
          journal: function ($q, $state, ServiceService) {
            return ServiceService.getJournalEvents();
          }
        },
        views: {
          'content': {
            templateUrl: 'app/journal/content.html',
            controller: 'JournalContentController'
          }
        }
      });
//  }
});

