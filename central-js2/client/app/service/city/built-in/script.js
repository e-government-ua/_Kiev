angular.module('app').config(function($stateProvider) {
  $stateProvider
    .state('index.service.general.city.built-in', {
      url: '/built-in',
      views: {
        'content@service.general.city': {
          templateUrl: 'html/service/city/built-in/index.html',
          controller: 'ServiceBuiltInController'
        }
      }
    })
    .state('index.service.general.city.built-in.bankid', {
      url: '/built-in/region/{region:int}/city/{city:int}/?code',
      parent: 'index.service.general.city',
      data: {
        region: null,
        city: null
      },
      resolve: {
        region: function($state, $stateParams, PlacesService) {
          return PlacesService.getRegion($stateParams.region).then(function(response) {
            var currentState = $state.get('service.general.city.built-in.bankid');
            currentState.data.region = response.data;
            return response.data;
          });
        },
        city: function($state, $stateParams, PlacesService) {
          return PlacesService.getCity($stateParams.region, $stateParams.city).then(function(response) {
            var currentState = $state.get('service.general.city.built-in.bankid');
            currentState.data.city = response.data;
            return response.data;
          });
        },
        oService: function($stateParams, service) {
          return service;
        },
        oServiceData: function($stateParams, service) {
          var aServiceData = service.aServiceData;
          var oServiceData = null;
          angular.forEach(aServiceData, function(value, key) {
            if (value.nID_City && value.nID_City.nID === $stateParams.city) {
              oServiceData = value;
            }
          });
          return oServiceData;
        },
        BankIDLogin: function($q, $state, $location, $stateParams, BankIDService) {
          return BankIDService.isLoggedIn().then(function() {
            return {loggedIn: true};
          }).catch(function() {
            return $q.reject(null);
          });
        },
        BankIDAccount: function(BankIDService, BankIDLogin) {
          return BankIDService.account();
        },
        processDefinitions: function(ServiceService, oServiceData) {
          return ServiceService.getProcessDefinitions(oServiceData, true);
        },
        processDefinitionId: function(oServiceData, processDefinitions) {
          var sProcessDefinitionKeyWithVersion = oServiceData.oData.oParams.processDefinitionId;
          var sProcessDefinitionKey = sProcessDefinitionKeyWithVersion.split(':')[0];

          var sProcessDefinitionName = "тест";

          angular.forEach(processDefinitions.data, function(value, key) {
            if (value.key == sProcessDefinitionKey) {
              sProcessDefinitionKeyWithVersion = value.id;
              sProcessDefinitionName = "(" + value.name + ")";
            }
          });

          return {
            sProcessDefinitionKeyWithVersion: sProcessDefinitionKeyWithVersion,
            sProcessDefinitionName: sProcessDefinitionName
          };
        },
        ActivitiForm: function(ActivitiService, oServiceData, processDefinitionId) {
          return ActivitiService.getForm(oServiceData, processDefinitionId);
        }
      },
      views: {
        'content@service.general.city': {
          templateUrl: 'html/service/city/built-in/bankid.html',
          controller: 'ServiceBuiltInBankIDController'
        }
      }
    })
    .state('index.service.general.city.built-in.bankid.submitted', {
      url: null,
      data: {id: null},
      onExit: function($state) {
        var state = $state.get('service.general.city.built-in.bankid.submitted');
        state.data = {id: null};
      },
      views: {
        'content@service.general.city': {
          templateUrl: 'html/service/city/built-in/bankid.submitted.html',
          controller: function($state, $scope, $sce) {
            $scope.state = $state.get('service.general.city.built-in.bankid.submitted');
            $scope.getHtml = function(html) {
              return $sce.trustAsHtml(html);
            };
          }
        }
      }
    });
});

