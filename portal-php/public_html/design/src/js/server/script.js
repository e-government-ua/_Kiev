define('server', ['angularAMD'], function (angularAMD) {
    var app = angular.module('server', ['ngMockE2E']);

	app.run(['$httpBackend', function($httpBackend) {
		$httpBackend.whenGET('./data.json').passThrough();
	}]);
	
    return app;
});