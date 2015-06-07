define('state/documents/controller', ['angularAMD'], function(angularAMD) {
  angularAMD.controller('DocumentsController', function($state, $window, $location) {
    if ($state.is('documents')) {
	  return $state.go('documents.bankid');
	}
  });
});
define('state/documents/bankid/controller', ['angularAMD'], function(angularAMD) {
  angularAMD.controller('DocumentsBankIdController', function($scope, $state, $location, $window) {

    $scope.loginWithBankId = function() {
      var stateForRedirect = $state.href('documents.bankid', {});
      var redirectURI = $location.protocol() + '://' + $location.host() + ':' + $location.port() + stateForRedirect;
      $window.location.href = 'https://bankid.privatbank.ua/DataAccessService/das/authorize?response_type=code&client_id=dniprorada&redirect_uri=' + redirectURI;
    }
	
    if ($state.is('documents.bankid') && !!$state.params.code) {
	  return $state.go('documents.content', {code: $state.params.code});
    }
  });
});
define('state/documents/content/controller', ['angularAMD'], function(angularAMD) {
  angularAMD.controller('DocumentsContentController',
    function($scope, $state, documents, ServiceService, $modal) {
      angular.forEach(documents, function(item) {
        if (item.oDate_Upload === null) {
          item.oDate_Upload = new Date();
        }
      });
      $scope.documents = documents;
      $scope.nDaysOptions = [{day: 1, title: '1 день'}, {day: 2, title: '2 дня'}, {day: 3, title: '3 дні'}, {
        day: 4,
        title: '4 дні'
      }];
      $scope.nDays = $scope.nDaysOptions[1];

      $scope.shareLink = function(document, sFIO, sTarget, sTelephone, sMail, nDays) {
        ServiceService.shareLink($state.nID_Subject, document.nID, sFIO,
          sTarget, sTelephone, sMail, nDays * 86400000).then(function(url) {
            $modal.open({
                animation: true,
                templateUrl: 'urlmodal.html',
                controller: 'ModalController',
                size: '',
                resolve: {
                  url: function() {
                    return url.value
                  }
                }
              }
            );
          });
      }
    });

  angularAMD.controller('ModalController',
    function($scope, $modalInstance, url) {
      $scope.url = url;

      $scope.close = function() {
        $modalInstance.close();
      };
    });
})
;