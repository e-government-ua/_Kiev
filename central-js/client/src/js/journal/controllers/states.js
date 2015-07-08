define('state/journal/controller', ['angularAMD'], function (angularAMD) {
	angularAMD.controller('JournalController', ['$scope', '$state', 'config', 'BankIDService', function ($scope, $state, config, BankIDService) {
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
    }]);
});

define('state/journal/bankid/controller', ['angularAMD'], function (angularAMD) {
	angularAMD.controller('JournalBankIdController', ['$rootScope', '$scope', '$location', '$state', '$window', 'BankIDService', function ($rootScope, $scope, $location, $state, $window, BankIDService) {

        $scope.loginWithBankId = function () {
            var stateForRedirect = $state.href('journal.bankid', {});
            var redirectURI = $location.protocol() +
                '://' + $location.host() + ':'
                + $location.port()
                + stateForRedirect;
            $window.location.href = './auth/bankID?link=' + redirectURI;
        }
		
        $scope.loginWithEds = function () {
            var stateForRedirect = $state.href('journal.bankid', {error: ''});
            var redirectURI = $location.protocol() +
                '://' + $location.host() + ':'
                + $location.port()
                + stateForRedirect;
            $window.location.href = './auth/eds?link=' + redirectURI;
        };

                
        if ($state.is('journal.bankid')) {
            if($state.params.error){
                $scope.error = JSON.parse($state.params.error).error;
            } else {
                BankIDService.isLoggedIn().then(function () {
                    return $state.go('journal.content', {code: $state.params.code});
                });
            };
        }
    }]);
});
define('state/journal/content/controller', ['angularAMD'], function (angularAMD) {
	angularAMD.controller('JournalContentController', [
        '$rootScope', '$scope', '$state', 'journal',
        function ($rootScope, $scope, $state, journal) {
            $scope.journal = journal;
            angular.forEach($scope.journal, function(item, index) {
                $scope.journal[index]['sDate'] = new Date(item.sDate);
            });
        }
    ]);
});
