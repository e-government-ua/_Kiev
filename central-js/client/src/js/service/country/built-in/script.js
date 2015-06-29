define('service.general.country.built-in', ['angularAMD'], function (angularAMD) {
    var app = angular.module('service.general.country.built-in', []);

    app.config(['$stateProvider', function ($stateProvider) {
        $stateProvider
            .state('service.general.country.built-in', {
                url: '/built-in',
                views: {
                    'content@service.general.country': angularAMD.route({
                        templateProvider: ['$templateCache', function($templateCache) {
							return $templateCache.get('html/service/country/built-in/index.html');
						}],
						controller: 'ServiceBuiltInController',
                        controllerUrl: 'service/built-in/controller'
                    })
                }
            })
			.state('service.general.country.built-in.bankid', {
				url: '/built-in/?code',
				parent: 'service.general.country',
				data: {
					region: null,
					city: null
				},
				resolve: {
					oService: ['$stateParams', 'service', function($stateParams, service) {
						return service;
					}],
					oServiceData: ['$stateParams', 'service', function($stateParams, service) {
						var aServiceData = service.aServiceData;
						return aServiceData[0];
					}],
					BankIDLogin: function($q, $state, $location, $stateParams, BankIDService) {
						return BankIDService.isLoggedIn().then(function () {
							return {loggedIn: true};
						}).catch(function() {
							return $q.reject(null);
						});
					},
					BankIDAccount: ['BankIDService', 'BankIDLogin', function(BankIDService, BankIDLogin) {
						return BankIDService.account();
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
					'content@service.general.country': angularAMD.route({
                        templateProvider: ['$templateCache', function($templateCache) {
							return $templateCache.get('html/service/country/built-in/bankid.html');
						}],
						controller: 'ServiceBuiltInBankIDController',
						controllerUrl: 'service/built-in/bankid/controller'
					})
				}
			})
			.state('service.general.country.built-in.bankid.submitted', {
				url: null,
				data: { id: null },
				onExit:['$state', function($state) {
					var state = $state.get('service.general.country.built-in.bankid.submitted');
					state.data = { id: null };
				}],
				views: {
					'content@service.general.country': angularAMD.route({
                        templateProvider: ['$templateCache', function($templateCache) {
							return $templateCache.get('html/service/country/built-in/bankid.submitted.html');
						}],
						controller: ['$state', '$scope', function($state, $scope) {
							$scope.state = $state.get('service.general.country.built-in.bankid.submitted');
						}]
					})
				}
			});
    }]);
    return app;
});

