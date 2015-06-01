define('state/documents/controller', ['angularAMD'], function(angularAMD) {
  angularAMD.controller('DocumentsController', ['$rootScope', '$state', '$window', '$location', function($rootScope, $state, $window, $location) {
    console.log('$rootScope');
    console.log('DocumentsController');
    console.log($state.current);
    if ($state.is('documents.bankid') && !!$state.params.code) {
      console.log('documents.content');
      $window.location.href = $location.protocol()
        + '://'
        + $location.host()
        + ':'
        + $location.port()
        + $state.href('documents.content', {code: $state.params.code});
    }
  }]);
});
define('state/documents/bankid/controller', ['angularAMD'], function(angularAMD) {
  angularAMD.controller('DocumentsBankIdController', ['$rootScope', '$scope', '$location', '$state', '$window', function($rootScope, $scope, $location, $state, $window) {
    console.log('$rootScope');
    console.log('DocumentsBankIdController');

    $scope.loginWithBankId = function() {
      var stateForRedirect = $state.href('documents.bankid', {});
      var redirectURI = $location.protocol() + '://' + $location.host() + ':' + $location.port() + stateForRedirect;
      $window.location.href = 'https://bankid.privatbank.ua/DataAccessService/das/authorize?response_type=code&client_id=dniprorada&redirect_uri=' + redirectURI;
    }
  }]);
});
define('state/documents/content/controller', ['angularAMD'], function (angularAMD) {
	angularAMD.controller('DocumentsContentController', [
        '$rootScope', '$scope', '$state', 'documents', 'ServiceService',
        function ($rootScope, $scope, $state, documents, ServiceService) {
		console.log('$rootScope');
        console.log('DocumentsContentController');
        console.log($state);
        console.log(documents);
        angular.forEach(documents, function (item) {
            if (item.oDate_Upload === null) {
                item.oDate_Upload = new Date();
            }
        });
        $scope.documents = documents;
          $scope.sFIO = '';
          $scope.sTarget = '';
          $scope.sTelephone = '';
          $scope.nDaysOptions = [{day: 1, title: '1 день'}, {day: 2, title: '2 дня'}, {day: 3, title: '3 дні'}, {
            day: 4,
            title: '4 дні'
          }];
          $scope.nDays = $scope.nDaysOptions[1];

          $scope.getDocument = function(document) {
            console.log(document);
            ServiceService.getDocument($state.nID_Subject, document.nID).then(function(data) {
              console.log(data);
              console.log(encodeURI(data));
              var element = angular.element('<a/>');
              element.attr({
                href: 'data:attachment/csv;charset=utf-8,' + encodeURI(data),
                target: '_blank',
                download: 'filename.csv'
              })[0].click();
            })
          };
          $scope.shareLink = function(document) {
            console.log('sFIO', $scope.sFIO);
            ServiceService.shareLink($state.nID_Subject, document.nID, $scope.sFIO,
              $scope.sTarget, $scope.sTelephone, $scope.nDays);}

        }]);
});