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
      url: '/built-in/region/{region:int}/city/{city:int}?formID&signedFileID',
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
        BankIDAccount: function($q, BankIDService) {
          return BankIDService.account().then(function(result){
            if(!result){
              return $q.reject(null);
            } else {
              return result;
            }
          });
        },
        processDefinitions: function(ServiceService, oServiceData) {
          return ServiceService.getProcessDefinitions(oServiceData, true);
        },
        processDefinitionId: function(oServiceData, processDefinitions) {
          //var sProcessDefinitionKeyWithVersion = oServiceData.oData.oParams.processDefinitionId;
          var sProcessDefinitionKeyWithVersion = oServiceData.oData.processDefinitionId;
          //console.log('[processDefinitionId]sProcessDefinitionKeyWithVersion='+sProcessDefinitionKeyWithVersion);
          var sProcessDefinitionKey = sProcessDefinitionKeyWithVersion.split(':')[0];
          //console.log('[processDefinitionId]sProcessDefinitionKey='+sProcessDefinitionKey);

          var sProcessDefinitionName = 'тест';

          angular.forEach(processDefinitions.data, function(value, key) {
            //console.log('[processDefinitionId]value.key='+value.key);
            //console.log('[processDefinitionId]key='+key);
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
        activitiForm: function($stateParams, ActivitiService, oServiceData, processDefinitionId) {
          if($stateParams.formID){
            return ActivitiService.loadForm(oServiceData, $stateParams.formID).then(function(savedForm){

              savedForm.activitiForm.formProperties.forEach(function(item){
                if(item.id === 'form_signed'){
                  item.value = $stateParams.signedFileID;
                } else {
                  item.value = savedForm.formData.params[item.id];
                }
              });

              return savedForm.activitiForm;
            });
          } else {
            return ActivitiService.getForm(oServiceData, processDefinitionId);
          }
        },
        countOrder: function ($stateParams, ServiceService, oService, oServiceData) {
          var nID_Service = oService.nID;
          var nLimit = oService.nOpenedLimit;
          var sID_UA = (oServiceData.oPlace && oServiceData.oPlace!==null) ? oServiceData.oPlace.sID_UA : null;
          //var sID_UA = oServiceData.oPlace.sID_UA;
          var bExcludeClosed = true;
          return ServiceService.getCountOrders(nID_Service, sID_UA, nLimit, bExcludeClosed);
        },
        allowOrder: function (oService, countOrder) {
         var nLimit = oService.nOpenedLimit;
         //console.log('[allowOrder]countOrder.nOpened='+countOrder.nOpened+",nLimit="+nLimit);
         if (nLimit === 0) { return true; }
 
          //console.log('[allowOrder]countOrder.nOpened='+countOrder.nOpened+",nLimit="+nLimit);
          return nLimit !== countOrder.nOpened;
        },
        selfOrdersCount: function(ServiceService, oService, oServiceData) {
          var sID_UA = (oServiceData.oPlace && oServiceData.oPlace!==null) ? oServiceData.oPlace.sID_UA : null;
          //console.log('[allowOrder]oService.nID='+oService.nID+",sID_UA="+sID_UA);
          return ServiceService.getCountOrders(oService.nID, sID_UA, 1, false);
          //return ServiceService.getCountOrders(oService.nID, oServiceData.oPlace.sID_UA, 1, false);
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
