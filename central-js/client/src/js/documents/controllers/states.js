define('state/documents/controller', ['angularAMD'], function (angularAMD) {
	angularAMD.controller('DocumentsController', ['$rootScope', '$state', function ($rootScope, $state) {
		console.log('$rootScope');
        console.log('DocumentsController');
        if (!$state.params.code) {
            $state.go('documents.bankid', {}, { location: true });
        }
    }]);
});
define('state/documents/bankid/controller', ['angularAMD'], function (angularAMD) {
	angularAMD.controller('DocumentsBankIdController', ['$rootScope', '$scope', '$location', '$state', '$window', function ($rootScope, $scope, $location, $state, $window) {
		console.log('$rootScope');
        console.log('DocumentsBankIdController');
        var stateForRedirect = $state.href('documents.content', {});
        var redirectURI = $location.protocol() + '://' + $location.host() + ':' + $location.port() + stateForRedirect;
        var bankIdLoginRefUrl = 'https://bankid.privatbank.ua/DataAccessService/das/authorize?response_type=code&client_id=dniprorada&redirect_uri=' + redirectURI;
        $scope.loginWithBankId = function () {
            $window.location.href = bankIdLoginRefUrl;
        }
    }]);
});
define('state/documents/content/controller', ['angularAMD'], function (angularAMD) {
	angularAMD.controller('DocumentsContentController', [
        '$rootScope', '$scope', '$state', '$stateParams', 'BankIDLogin',
        function ($rootScope, $scope, $state, $stateParams, BankIDLogin) {
		console.log('$rootScope');
        console.log('DocumentsContentController');
        console.log($stateParams);
        console.log(BankIDLogin);
    }]);
});