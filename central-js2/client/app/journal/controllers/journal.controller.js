angular.module('journal', []).controller('JournalController', function($scope, $state, BankIDService) {

  BankIDService.isLoggedIn().then(function() {
    if ($state.is('index.journal')) {
      return $state.go('index.journal.content');
    }
  }).catch(function() {
    if ($state.is('index.journal')) {
      return $state.go('index.journal.bankid');
    }
  });
});
