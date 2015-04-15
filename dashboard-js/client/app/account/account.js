'use strict';

angular.module('dashboardJsApp')
	.config(function($routeProvider) {
		$routeProvider
			.when('/login', {
				templateUrl: 'app/account/login/login.html',
				controller: 'LoginCtrl'
			});
	})
	.run(
		function($rootScope, $location, $cookieStore, Auth) {
			var lastPage;
			$rootScope.$on('$routeChangeStart', function(event, next) {
				if (next.access !== undefined) {
					if (!Auth.isLoggedIn()) {
						event.preventDefault();
						lastPage = next.originalPath;
						$location.path('/login').replace();
					} else {
						if (lastPage) {
							var lastPageTemp = lastPage;
							$location.path(lastPageTemp);
							lastPage = undefined;
						} else {
							$location.path(next.originalPath);
						}
					}
				}
			})
		});