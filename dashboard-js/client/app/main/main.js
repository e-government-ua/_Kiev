'use strict';

angular.module('dashboardJsApp')
	.config(function($routeProvider) {
		$routeProvider
			.when('/', {
				templateUrl: 'app/main/main.html',
				controller: 'MainCtrl'
			});
	});