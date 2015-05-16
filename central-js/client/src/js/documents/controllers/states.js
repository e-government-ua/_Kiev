define('state/documents/controller', ['angularAMD'], function (angularAMD) {
	angularAMD.controller('DocumentsController', ['$rootScope', '$state', function ($rootScope, $state) {
		console.log('$rootScope');
        console.log('DocumentsController');
        console.log($state.current);
        if ($state.is('documents.bankid') && !!$state.params.code) {
            console.log('documents.content');
            $state.go('documents.content', {code: $state.params.code});
        }
        else {
            console.log('documents.bankid');
            $state.go('documents.bankid', {});
        }
    }]);
});
define('state/documents/bankid/controller', ['angularAMD'], function (angularAMD) {
	angularAMD.controller('DocumentsBankIdController', ['$rootScope', '$scope', '$location', '$state', '$window', function ($rootScope, $scope, $location, $state, $window) {
		console.log('$rootScope');
        console.log('DocumentsBankIdController');

        $scope.loginWithBankId = function () {
            var stateForRedirect = $state.href('documents.bankid', {});
            var redirectURI = $location.protocol() + '://' + $location.host() + ':' + $location.port() + stateForRedirect;
            $window.location.href = 'https://bankid.privatbank.ua/DataAccessService/das/authorize?response_type=code&client_id=dniprorada&redirect_uri=' + redirectURI;
        }
    }]);
});
define('state/documents/content/controller', ['angularAMD'], function (angularAMD) {
	angularAMD.controller('DocumentsContentController', [
        '$rootScope', '$scope', '$state', 'BankIDLogin',
        function ($rootScope, $scope, $state, BankIDLogin) {
		console.log('$rootScope');
        console.log('DocumentsContentController');
        console.log($state);
        console.log(BankIDLogin);
    }]);
});