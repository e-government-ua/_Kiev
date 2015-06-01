define('server', ['angularAMD'], function (angularAMD) {
    var app = angular.module('server', ['ngMockE2E']);

	app.run(['$http', '$httpBackend', function($http, $httpBackend) {
		$httpBackend.whenGET(/\/api\/services/).passThrough();
		$httpBackend.whenGET(/\/api\/service/).passThrough();
		$httpBackend.whenPOST(/\/api\/service/).passThrough();
		$httpBackend.whenGET(/\/api\/places/).passThrough();
		$httpBackend.whenGET(/\/api\/process-definitions/).passThrough();
		
		$httpBackend.whenGET('./data.json').passThrough();
		$httpBackend.whenGET(/\/api\/bankid/).passThrough();
		$httpBackend.whenGET(/\/api\/process-form/).passThrough();
		$httpBackend.whenPOST(/\/api\/process-form/).passThrough();
	}]);
	
    return app;
});