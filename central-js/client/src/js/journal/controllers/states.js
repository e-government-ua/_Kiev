/*define('state/journal/controller', ['angularAMD'], function (angularAMD) {
	angularAMD.controller('JournalController', ['$rootScope', function ($rootScope) {
		console.log('$rootScope');
    }]);
});*/

define('state/journal/controller', ['angularAMD'], function (angularAMD) {
	angularAMD.controller('JournalController', ['$rootScope', '$state', '$window', '$location', function ($rootScope, $state, $window, $location) {
		if ($state.is('journal')) {
			return $state.go('journal.bankid');
		}
    }]);
});

define('state/journal/bankid/controller', ['angularAMD'], function (angularAMD) {
	angularAMD.controller('JournalBankIdController', ['$rootScope', '$scope', '$location', '$state', '$window', function ($rootScope, $scope, $location, $state, $window) {

        $scope.loginWithBankId = function () {
            var stateForRedirect = $state.href('journal.bankid', {});
            var redirectURI = $location.protocol() + '://' + $location.host() + ':' + $location.port() + stateForRedirect;
            $window.location.href = 'https://bankid.privatbank.ua/DataAccessService/das/authorize?response_type=code&client_id=9b0e5c63-9fcb-4b11-84ff-31fc2cea8801&redirect_uri=' + redirectURI;
        }
		
        if ($state.is('journal.bankid') && !!$state.params.code) {
			return $state.go('journal.content', {code: $state.params.code});
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