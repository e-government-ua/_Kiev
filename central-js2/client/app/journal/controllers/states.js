angular.module('journal').controller('JournalController', function($scope, $state, config, BankIDService) {
  $scope.config = config;

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

angular.module('journal').controller('JournalBankIdController', function($rootScope, $scope, $location, $state, $window, BankIDService) {

  $scope.loginWithBankId = function() {
    var stateForRedirect = $state.href('journal.bankid', {});
    var redirectURI = $location.protocol() +
      '://' + $location.host() + ':'
      + $location.port()
      + stateForRedirect;
    $window.location.href = './auth/bankID?link=' + redirectURI;
  }

  if ($state.is('journal.bankid')) {
    if ($state.params.error) {
      $scope.error = JSON.parse($state.params.error).error;
    } else {
      BankIDService.isLoggedIn().then(function() {
        return $state.go('journal.content', {code: $state.params.code});
      });
    }
    ;
  }
});
angular.module('journal').controller('JournalContentController', function($rootScope, $scope, $state, journal) {
  $scope.journal = journal;
  angular.forEach($scope.journal, function(item, index) {
    $scope.journal[index]['sDate'] = new Date(item.sDate);
  });
});
