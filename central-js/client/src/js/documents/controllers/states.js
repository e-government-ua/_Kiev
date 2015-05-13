define('state/documents/controller', ['angularAMD'], function (angularAMD) {
	angularAMD.controller('DocumentsController', ['$rootScope','$scope','DocumentsService', function ($rootScope, $scope, DocumentsService) {
		console.log('$rootScope');
    }]);
});