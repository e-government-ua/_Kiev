angular.module('journal').controller('JournalBankIdController', function($rootScope, $scope, $location, $state, $window, BankIDService) {

  $scope.loginWithBankId = function() {
    var stateForRedirect = $state.href('journal.bankid', {});
    var redirectURI = $location.protocol() +
      '://' + $location.host() + ':'
      + $location.port()
      + stateForRedirect;
    $window.location.href = './auth/bankID?link=' + redirectURI;
  };

  if ($state.is('journal.bankid')) {
    if ($state.params.error) {
      $scope.error = JSON.parse($state.params.error).error;
    } else {
      BankIDService.isLoggedIn().then(function() {
        return $state.go('journal.content', {code: $state.params.code});
      });
    }
  }
});
