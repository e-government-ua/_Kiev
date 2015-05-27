define('service.general.city.built-in', ['angularAMD'], function (angularAMD) {
    var app = angular.module('service.general.city.built-in', []);

    app.config(['$stateProvider', function ($stateProvider) {
        $stateProvider
            .state('service.general.city.built-in', {
                url: '/built-in',
                views: {
                    'content@service.general.city': angularAMD.route({
                        templateProvider: ['$templateCache', function($templateCache) {
							return $templateCache.get('html/service/city/built-in/index.html');
						}],
						controller: 'ServiceBuiltInController',
                        controllerUrl: 'service/built-in/controller'
                    })
                }
            })
			.state('service.general.city.built-in.bankid', {
				url: '/built-in/region/{region:int}/city/{city:int}/?code',
				parent: 'service.general.city',
				data: {
					region: null,
					city: null
				},
				resolve: {
					region: ['$state', '$stateParams', 'PlacesService', function($state, $stateParams, PlacesService) {
						return PlacesService.getRegion($stateParams.region).then(function(response) {
							var currentState = $state.get('service.general.city.built-in.bankid');
							currentState.data.region = response.data;
							return response.data;
						});
					}],
					city: ['$state', '$stateParams', 'PlacesService', function($state, $stateParams, PlacesService) {
						return PlacesService.getCity($stateParams.region, $stateParams.city).then(function(response) {
							var currentState = $state.get('service.general.city.built-in.bankid');
							currentState.data.city = response.data;
							return response.data;
						});
					}],
					oServiceData: ['$stateParams', 'service', function($stateParams, service) {
						var aServiceData = service.aServiceData;
						var oServiceData = null;
						angular.forEach(aServiceData, function(value, key) {
							if(value.nID_City.nID == $stateParams.city) {
								oServiceData = value;
							}
						});
						return oServiceData;
					}],
					BankIDLogin: ['$q', '$state', '$location', '$stateParams', 'BankIDService', function($q, $state, $location, $stateParams, BankIDService) {
						var url = $location.protocol()
							+'://'
							+$location.host()
							+':'
							+$location.port()
							+$state.href('service.general.city.built-in.bankid', { id: $stateParams.id, region: $stateParams.region, city: $stateParams.city });
						
						return BankIDService.login($stateParams.code, url).then(function(data) {
							return data.hasOwnProperty('error') ? $q.reject(null): data;
						});
					}],
					BankIDAccount: ['BankIDService', 'BankIDLogin', function(BankIDService, BankIDLogin) {
						return BankIDService.account(BankIDLogin.access_token);
					}],
					processDefinitions: ['ServiceService', 'oServiceData', function(ServiceService, oServiceData) {
						return ServiceService.getProcessDefinitions(oServiceData, true);
					}],
					processDefinitionId: ['oServiceData', 'processDefinitions', function(oServiceData, processDefinitions) {
						var sProcessDefinitionKeyWithVersion = oServiceData.oData.oParams.processDefinitionId;
						var sProcessDefinitionKey = sProcessDefinitionKeyWithVersion.split(':')[0];
						
						var sProcessDefinitionName = "тест";
                                                //sProcessDefinitionName = "name2";
                                                //var currentState = $state.get('service.general.city.built-in.bankid');
                                                //currentState.data.sProcessDefinitionName2 = "name3";
                                                
						angular.forEach(processDefinitions.data, function(value, key) {
							if(value.key == sProcessDefinitionKey) {
								sProcessDefinitionKeyWithVersion = value.id;
                                                                sProcessDefinitionName = "("+value.name+")";
							}
						});

						//return processDefinitionKeyWithVersion;
						return {sProcessDefinitionKeyWithVersion:sProcessDefinitionKeyWithVersion,sProcessDefinitionName:sProcessDefinitionName};
					}],
					ActivitiForm: ['ActivitiService', 'oServiceData', 'processDefinitionId', function(ActivitiService, oServiceData, processDefinitionId) {
						return ActivitiService.getForm(oServiceData, processDefinitionId);
					}]
				},
				views: {
					'content@service.general.city': angularAMD.route({
                        templateProvider: ['$templateCache', function($templateCache) {
							return $templateCache.get('html/service/city/built-in/bankid.html');
						}],
						controller: 'ServiceBuiltInBankIDController',
						controllerUrl: 'service/built-in/bankid/controller'
					})
				}
			})
			.state('service.general.city.built-in.bankid.submitted', {
				url: null,
				data: { id: null },
				onExit:['$state', function($state) {
					var state = $state.get('service.general.city.built-in.bankid.submitted');
					state.data = { id: null };
				}],
				views: {
					'content@service.general.city': angularAMD.route({
                        templateProvider: ['$templateCache', function($templateCache) {
							return $templateCache.get('html/service/city/built-in/bankid.submitted.html');
						}],
						controller: ['$state', '$scope', function($state, $scope) {
							$scope.state = $state.get('service.general.city.built-in.bankid.submitted');
						}]
					})
				}
			});
    }]);	
    return app;
});

