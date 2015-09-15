angular.module('app').config(function($stateProvider) {
  $stateProvider
    .state('index.service.general.placefix.built-in', { // FIXME-5 // region
      url: '/built-in',
      views: {
        // 'place-content'
        'content@index.service.general.placefix': {
          // TODO: look if Place Fix Controller can work here also
          templateUrl: 'app/service/placefix/content.html',
          // controller: 'PlaceFixController'
          controller: 'ServiceBuiltInController'
        }
      }
    })
    .state('index.service.general.placefix.built-in.bankid', { // city - moved from app.states
      // url: '/built-in/?code',        // country
      // url: '/{region:int}/region',   // region from .state('index.service.general.placefix.built-in.region', {
      url: '/built-in/region/{region:int}/city/{city:int}/?code',
      parent: 'index.service.general.placefix',
      data: {
        region: null,
        city: null
      },
      views: {
        'content@index.service.general.placefix': {
          templateUrl: 'app/service/placefix/built-in/bankid.html',
          controller: 'ServiceBuiltInBankIDController'
        }
      },
      resolve: {
        region: function($state, $stateParams, PlacesService) {
          return PlacesService.getRegion($stateParams.region).then(function(response) {
            var currentState = $state.get('index.service.general.placefix.built-in.bankid');
            currentState.data.region = response.data;
            return response.data;
          });
        },
        city: function($state, $stateParams, PlacesService) {
          return PlacesService.getCity($stateParams.region, $stateParams.city).then(function(response) {
            var currentState = $state.get('index.service.general.placefix.built-in.bankid');
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
          if ($stateParams.city > 0) {
            angular.forEach(aServiceData, function(value, key) {
              // if city is available for this service
              if (value.nID_City && value.nID_City.nID === $stateParams.city) {
                oServiceData = value;
              }
            });
          } else {
            angular.forEach(aServiceData, function(value, key) {
              // if city isn't, but region is available for this service
              if (value.nID_Region && value.nID_Region.nID === $stateParams.region) {
                oServiceData = value;
              }
            });
          }
          return oServiceData;
        },
        BankIDLogin: function($q, $state, $location, $stateParams, BankIDService) {
          return BankIDService.isLoggedIn().then(function() {
            return {
              loggedIn: true
            };
          }).catch(function() {
            return $q.reject(null);
          });
        },
        BankIDAccount: function(BankIDService) {
          return BankIDService.account();
        },
        processDefinitions: function(ServiceService, oServiceData) {
          return ServiceService.getProcessDefinitions(oServiceData, true);
        },
        processDefinitionId: function(oServiceData, processDefinitions) {
          var sProcessDefinitionKeyWithVersion = oServiceData.oData.oParams.processDefinitionId;
          var sProcessDefinitionKey = sProcessDefinitionKeyWithVersion.split(':')[0];

          var sProcessDefinitionName = 'тест';

          angular.forEach(processDefinitions.data, function(value, key) {
            if (value.key === sProcessDefinitionKey) {
              sProcessDefinitionKeyWithVersion = value.id;
              sProcessDefinitionName = '(' + value.name + ')';
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
      }
    })
    .state('index.service.general.placefix.built-in.bankid.submitted', { // city - moved from app.states
      url: null,
      data: {
        id: null
      },
      onExit: function($state) {
        var state = $state.get('index.service.general.placefix.built-in.bankid.submitted');
        state.data = {
          id: null
        };
      },
      views: {
        'content@index.service.general.placefix': {
          templateUrl: 'app/service/placefix/built-in/bankid.submitted.html',
          controller: 'PlaceFixController' // function moved to PlaceFixController state startupFunction
        }
      }
    });
});
// region, country - merged