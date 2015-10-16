angular.module('app').config(function($stateProvider) {
  $stateProvider
    .state('index.service.general.place', {
      url: '/place',
      views: {
        'content@index.service.general.place': {
          templateUrl: 'app/service/place/templates/content.html'
        },
        'content@index.service': {
          templateUrl: 'app/service/place/templates/content.html',
          controller: 'PlaceController'
        },
        'main@': {
          templateUrl: 'app/service/index.html',
          controller: 'PlaceController'
        }
      }
    })
    .state('index.service.general.place.error', {
      url: '/absent',
      views: {
        'content@index.service.general.place': {
          templateUrl: 'app/service/place/templates/absent.html',
          controller: 'PlaceAbsentController'
        }
      }
    })
    .state('index.service.general.place.link', {
      url: '/link',
      views: {
        'content@index.service.general.place': {
          templateUrl: 'app/service/place/templates/link.html',
          controller: 'PlaceController'
        }
      }
    }) // built-in:
    .state('index.service.general.place.built-in', {
      url: '/built-in',
      views: {
        'content@index.service.general.place': {
          templateUrl: 'app/service/place/templates/content.html',
          controller: 'PlaceController'
        }
      }
    })
    .state('index.service.general.place.built-in.bankid', {
      url: '/built-in/region/{region:int}/city/{city:int}/?code',
      parent: 'index.service.general.place',
      data: {
        region: null,
        city: null
      },
      views: {
        'content@index.service.general.place': {
          templateUrl: 'app/service/place/templates/built-in-bankid.html',
          controller: 'ServiceBuiltInBankIDController'
        }
      },
      resolve: {
        region: function($state, $stateParams, PlacesService) {
          return PlacesService.getRegion($stateParams.region).then(function(response) {
            var currentState = $state.get('index.service.general.place.built-in.bankid');
            currentState.data.region = response.data;
            return response.data;
          });
        },
        city: function($state, $stateParams, PlacesService) {
          return PlacesService.getCity($stateParams.region, $stateParams.city).then(function(response) {
            var currentState = $state.get('index.service.general.place.built-in.bankid');
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
              // country level: service is defined, but no region and no city is
              } else if (value.nID_ServiceType && !value.hasOwnProperty('nID_City') && !value.hasOwnProperty('nID_Region') && (value.nID_ServiceType.nID === 4 || value.nID_ServiceType.nID === 1 ) ) {
              //} else if (value.nID_ServiceType && (value.nID_ServiceType.nID === 4 || value.nID_ServiceType.nID === 1 ) ) {
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
    .state('index.service.general.place.built-in.bankid.submitted', {
      url: null,
      data: {
        id: null
      },
      onExit: function($state) {
        var state = $state.get('index.service.general.place.built-in.bankid.submitted');
        state.data = {
          id: null
        };
      },
      views: {
        'content@index.service.general.place': {
          templateUrl: 'app/service/place/templates/built-in-bankid.submitted.html',
          controller: 'ServiceBuiltInBankIDController'
        }
      },
      resolve: {
        BankIDAccount: function(BankIDService) {
          return BankIDService.account();
        }
      }
    });
});