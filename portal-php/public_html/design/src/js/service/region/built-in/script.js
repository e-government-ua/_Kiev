define('service.region.built-in', ['angularAMD'], function (angularAMD) {
    var app = angular.module('service.region.built-in', []);

    app.config(['$stateProvider', function ($stateProvider) {
        $stateProvider
            .state('service.region.built-in', {
                url: '/built-in',
                views: {
                    '': angularAMD.route({
                        templateProvider: ['$templateCache', function($templateCache) {
							return $templateCache.get('html/service/region/built-in/index.html');
						}],
						controller: 'ServiceBuiltInController',
                        controllerUrl: 'service/built-in/controller'
                    })
                }
            })
			.state('service.region.built-in.bankid', {
				url: '/region/{region:int}/?code',
				views: {
					'@service.region': {
                        templateProvider: ['$templateCache', function($templateCache) {
							return $templateCache.get('html/service/region/built-in/index.html');
						}],
						controller: ['$state', '$stateParams', '$scope', function($state, $stateParams, $scope) {
							angular.forEach($scope.places.regions, function(value, key) {
								if($stateParams.region == value.id) {
									$scope.data.region = value;
								}
							});
							console.log('service.region.built-in.bankid');
						}]
					}
				}
			})
    }]);
    return app;
});

