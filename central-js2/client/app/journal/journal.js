angular.module('journal').config(function($stateProvider) {

  $stateProvider
    .state('journal', {
      url: '/journal',
      views: {
        '': {
          templateUrl: 'html/journal/index.html',
          controller: 'JournalController'
        }
      }
    })
    .state('journal.bankid', {
      url: '/bankid?code&error',
      parent: 'journal',
      views: {
        'bankid': {
          templateUrl: 'html/journal/bankid/index.html',
          controller: 'JournalBankIdController'
        }
      }
    })
    .state('journal.content', {
      url: '/content?code',
      parent: 'journal',
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
        journal: function($q, $state, ServiceService) {
          return ServiceService.getJournalEvents();
        }
      },
      views: {
        'content': {
          templateUrl: 'html/journal/content.html',
          controller: 'JournalContentController'
        }
      }
    });
})
;

