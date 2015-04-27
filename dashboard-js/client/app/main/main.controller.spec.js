'use strict';
describe('Controller: MainCtrl', function() {
	var ctrl,
		scope;

	beforeEach(module('dashboardJsApp'));
	beforeEach(inject(function($controller, $rootScope) {
		scope = $rootScope.$new();
		ctrl = $controller('MainCtrl', {
			$scope: scope
		});
	}));
});
