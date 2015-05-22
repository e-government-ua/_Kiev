/*define('state/journal/controller', ['angularAMD'], function (angularAMD) {
	angularAMD.controller('JournalController', ['$rootScope', function ($rootScope) {
		console.log('$rootScope');
    }]);
});*/

define('state/journal/controller', ['angularAMD'], function (angularAMD) {
	angularAMD.controller('JournalController', ['$rootScope', '$state', '$window', '$location', function ($rootScope, $state, $window, $location) {
		console.log('$rootScope');
		console.log('JournalController');
        console.log($state.current);
        if ($state.is('journal.bankid') && !!$state.params.code) {
            console.log('journal.content');
            $window.location.href = $location.protocol()
                + '://'
                + $location.host()
                + ':'
                + $location.port()
                + $state.href('journal.content', {code: $state.params.code});
        }
    }]);
});

define('state/journal/bankid/controller', ['angularAMD'], function (angularAMD) {
	angularAMD.controller('JournalBankIdController', ['$rootScope', '$scope', '$location', '$state', '$window', function ($rootScope, $scope, $location, $state, $window) {
		console.log('$rootScope');
        console.log('JournalBankIdController');

        $scope.loginWithBankId = function () {
            var stateForRedirect = $state.href('journal.bankid', {});
            var redirectURI = $location.protocol() + '://' + $location.host() + ':' + $location.port() + stateForRedirect;
            $window.location.href = 'https://bankid.privatbank.ua/DataAccessService/das/authorize?response_type=code&client_id=dniprorada&redirect_uri=' + redirectURI;
        }
    }]);
});
define('state/journal/content/controller', ['angularAMD'], function (angularAMD) {
	angularAMD.controller('JournalContentController', [
        '$rootScope', '$scope', '$state', 'journal',
        function ($rootScope, $scope, $state, journal) {
		console.log('$rootScope');
        console.log('JournalContentController');
        console.log($state);
        console.log(journal);
        /*angular.forEach(journal, function (item) {
            if (item.oDate_Upload === null) {
                item.oDate_Upload = new Date();
            }
        });*/
        $scope.journal = journal;
    }]);
});