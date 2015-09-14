// region = from service\region\built-in\script.js 
// FIXME merge with above into single logic
///////////////////
////////////////

angular.module('app').config(function($stateProvider) {
  $stateProvider
    .state('index.service.general.placefix.built-in.region', {
      url: '/{region:int}/region',
      data: {
        region: null,
        city: null
      },
      resolve: {
        region: function($state, $stateParams, PlacesService) {
          return PlacesService.getRegion($stateParams.region).then(function(response) {
            var currentState = $state.get('index.service.general.placefix.built-in.region');
            currentState.data.region = response.data;
            return response.data;
          });
        }
      },
      views: {
        'content@index.service.general.placefix': {
          // FIXME: REPLACE with Place Controller
          templateUrl: 'app/service/placefix/built-in/index.html',
          // controller: 'ServiceBuiltInController'
          controller: 'PlaceFixController'
        }
      }
    })
    .state('index.service.general.placefix.built-in.bankid', { // city - moved from app.states
      // url: '/built-in/?code', // country
      url: '/built-in/region/{region:int}/city/{city:int}/?code',
      parent: 'index.service.general.placefix',
      data: {
        region: null,
        city: null
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
          //,
          // service: function($stateParams, ServiceService) {
          //   return ServiceService.get($stateParams.id);
          // }
      },
      views: {
        // 'city-content': {
        //   templateUrl: 'app/service/placefix/built-in/bankid.html',
        //   //controller: 'PlaceFixController' // 'BuiltinCityController'
        //   controller: 'ServiceBuiltInBankIDController' // FIXME-0
        // },
        'content@index.service.general.placefix': {
          templateUrl: 'app/service/placefix/built-in/bankid.html',
          controller: 'ServiceBuiltInBankIDController'
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
        // FIXME resolve teh city-content
        // 'city-content@index.service.general.placefix': {
        //   templateUrl: 'app/service/placefix/built-in/bankid.submitted.html',
        //   controller: 'PlaceFixController' // function moved to Places' stateStartupFunction
        // }, // regions
        'content@index.service.general.placefix': {
          templateUrl: 'app/service/placefix/built-in/bankid.submitted.html',
          controller: 'PlaceFixController' // function moved to Places' stateStartupFunction

          // FIXME impl-t this functionality in PlaceFixController:
          // function($state, $scope, BankIDAccount) {
          //   $scope.state = $state.get('index.service.general.placefix.built-in.bankid.submitted');

          //   $scope.bankIDAccount = BankIDAccount;
          // }
        }
      }
    });
});

// country

angular.module('app').config(function($stateProvider) {
  // $stateProvider.state('index.service.general.placefix.built-in', {
  //     url: '/built-in',
  //     views: {
  //       'content@index.service.general.placefix': {
  //         templateUrl: 'app/service/placefix/built-in/index.html',
  //         controller: 'ServiceBuiltInController'
  //       }
  //     }
  //   })
    // .state('index.service.general.placefix.built-in.bankid', {
    //   url: '/built-in/?code',
    //   parent: 'index.service.general.placefix',
    //   data: {
    //     region: null,
    //     city: null
    //   },
    //   resolve: {
    //     oService: function($stateParams, service) {
    //       return service;
    //     },
    //     oServiceData: function($stateParams, service) {
    //       var aServiceData = service.aServiceData;
    //       return aServiceData[0];
    //     },
    //     BankIDLogin: function($q, $state, $location, $stateParams, BankIDService) {
    //       return BankIDService.isLoggedIn().then(function() {
    //         return {
    //           loggedIn: true
    //         };
    //       }).catch(function() {
    //         return $q.reject(null);
    //       });
    //     },
    //     BankIDAccount: function(BankIDService, BankIDLogin) {
    //       return BankIDService.account();
    //     },
    //     processDefinitions: function(ServiceService, oServiceData) {
    //       return ServiceService.getProcessDefinitions(oServiceData, true);
    //     },
    //     processDefinitionId: function(oServiceData, processDefinitions) {
    //       var sProcessDefinitionKeyWithVersion = oServiceData.oData.oParams.processDefinitionId;
    //       var sProcessDefinitionKey = sProcessDefinitionKeyWithVersion.split(':')[0];

    //       var sProcessDefinitionName = 'тест';
    //       angular.forEach(processDefinitions.data, function(value, key) {
    //         if (value.key === sProcessDefinitionKey) {
    //           sProcessDefinitionKeyWithVersion = value.id;
    //           sProcessDefinitionName = '(' + value.name + ')';
    //         }
    //       });

    //       return {
    //         sProcessDefinitionKeyWithVersion: sProcessDefinitionKeyWithVersion,
    //         sProcessDefinitionName: sProcessDefinitionName
    //       };
    //     },
    //     ActivitiForm: function(ActivitiService, oServiceData, processDefinitionId) {
    //       return ActivitiService.getForm(oServiceData, processDefinitionId);
    //     }
    //   },
    //   views: {
    //     'content@index.service.general.placefix': {
    //       templateUrl: 'app/service/placefix/built-in/bankid.html',
    //       controller: 'ServiceBuiltInBankIDController'
    //     }
    //   }
    // })
    // .state('index.service.general.placefix.built-in.bankid.submitted', {
    //   url: null,
    //   data: {
    //     id: null
    //   },
    //   onExit: function($state) {
    //     var state = $state.get('index.service.general.placefix.built-in.bankid.submitted');
    //     state.data = {
    //       id: null
    //     };
    //   },
    //   views: {
    //     'content@index.service.general.placefix': {
    //       templateUrl: 'app/service/placefix/built-in/bankid.submitted.html',
    //       controller: function($state, $scope, BankIDAccount) {
    //         $scope.state = $state.get('index.service.general.placefix.built-in.bankid.submitted');

  //         $scope.bankIdAccount = BankIDAccount;
  //       }
  //     }
  //   }
  // })
});

// end country