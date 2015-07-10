angular.module('journal').controller('JournalBankIdController', function($rootScope, $scope, $location, $state, $window, BankIDService) {

  $scope.loginWithBankId = function() {
    var stateForRedirect = $state.href('index.journal.bankid', {});
    var redirectURI = $location.protocol() +
      '://' + $location.host() + ':'
      + $location.port()
      + stateForRedirect;
    $window.location.href = './auth/bankID?link=' + redirectURI;
  };

    $scope.loginWithEds = function () {
        var stateForRedirect = $state.href('index.journal.bankid', {error: ''});
        var redirectURI = $location.protocol() +
            '://' + $location.host() + ':'
            + $location.port()
            + stateForRedirect;
        $window.location.href = './auth/eds?link=' + redirectURI;
    };

  if ($state.is('index.journal.bankid')) {
    if ($state.params.error) {
      $scope.error = JSON.parse($state.params.error).error;
    } else {
      BankIDService.isLoggedIn().then(function() {
        return $state.go('index.journal.content');
      });
    }
  }
});
