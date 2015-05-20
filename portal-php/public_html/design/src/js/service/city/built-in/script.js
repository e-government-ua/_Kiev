define('service.city.built-in', ['angularAMD'], function (angularAMD) {
    var app = angular.module('service.city.built-in', []);

    app.config(['$stateProvider', function ($stateProvider) {
        $stateProvider
            .state('service.city.built-in', {
                url: '/built-in',
                views: {
                    '': angularAMD.route({
                        templateProvider: ['$templateCache', function($templateCache) {
							return $templateCache.get('html/service/city/built-in/index.html');
						}],
						controller: 'ServiceBuiltInController',
                        controllerUrl: 'service/built-in/controller'
                    })
                }
            })
			.state('service.city.built-in.bankid', {
				url: '/region/{region:int}/city/{city:int}/?code',
				views: {
					'@service.city': {
                        templateProvider: ['$templateCache', function($templateCache) {
							return $templateCache.get('html/service/city/built-in/index.html');
						}],
						controller: ['$state', '$stateParams', '$scope', function($state, $stateParams, $scope) {
							angular.forEach($scope.places.regions, function(value, key) {
								if($stateParams.region == value.id) {
									$scope.data.region = value;
								}
							});
							angular.forEach($scope.data.region.cities, function(value, key) {
								if($stateParams.city == value.id) {
									$scope.data.city = value;
								}
							});
							console.log('service.city.built-in.bankid');
						}]
					}
				}
			})
    }]);
    return app;
});

