angular.module('journal', []).controller('JournalController', function($scope, $state, BankIDService) {

  BankIDService.isLoggedIn().then(function() {
    if ($state.is('journal')) {
      return $state.go('journal.content');
    }
  }).catch(function() {
    if ($state.is('journal')) {
      return $state.go('journal.bankid');
    }
  });
});