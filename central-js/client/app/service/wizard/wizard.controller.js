angular.module('app').controller('WizardController', function(
  $state, AdminService, $rootScope, $scope, $location, $sce, RegionListFactory, LocalityListFactory, PlacesService, ServiceService) {

  // Each controller which uses Places Control should tell it:

  // FIXME: preload regions and service and provide them as part of the locations service
  console.log('WizardController');

  PlacesService.setController(this);

  var self = this;

    var statesMap = {
      'index.service.general.city.built-in': {
        startupFunction: function(iPlaceController, $location, $state, $rootScope, $scope, placeCtrl) {
          $scope.$location = $location;
          $scope.$state = $state;
          iPlaceController.isStep2 = true;
        },
        viewClass: 'state-disabled'
      },
      'index.service.general.city.built-in.bankid.submitted': {
        startupFunction: function(iPlaceController, $location, $state, $rootScope, $scope, placeCtrl) {
          $scope.collapse();
          $scope.state = $state; //.get('index.service.general.placefix.built-in.bankid.submitted');
        },
        viewClass: 'state-collapsed'
      }
    };


  self.processPlaceChange = function(oParams) {
    var oService = ServiceService.oService;
    var stateByServiceType = {
      // Сервіс за посиланням
      1: 'index.service.general.placefix.link',
      // Вбудований сервіс
      4: 'index.service.general.placefix.built-in',
      // Помилка - сервіс відсутній
      0: 'index.service.general.placefix.error'
    };

    var state = stateByServiceType[oParams.serviceType.nID];

    console.log('Places Service. On Place сhange, oParams =', oParams);

    // FROM COUNTRY CTRL

    // var aServiceData = $scope.service.aServiceData;

    // obtain service data and it's notes
    // angular.forEach(aServiceData, function(value, key) {
    //   $scope.serviceData = value;
    //   $scope.serviceData.sNote = $sce.trustAsHtml($scope.serviceData.sNote);
    // });

    // switch (serviceType.nID) {
    //   case 1:
    //     return $state.go('index.service.general.placefix.link', {
    //       id: $scope.service.nID
    //     }, {
    //       location: false
    //     });
    //   case 4:
    //     return $state.go('index.service.general.placefix.built-in', {
    //       id: $scope.service.nID
    //     }, {
    //       location: false
    //     });
    //   default:
    //     return $state.go('index.service.general.placefix.error', {
    //       id: $scope.service.nID
    //     }, {
    //       location: false
    //     });
    // }

    // FROM COUNTRY

    // FIXME generalize it: if cur state is step 1 and it's complete, go to step 2 and change state
    if (state && oParams.placeData.city) {
      self.isStep2 = true;
      // console.log('go state:', state);
      $state.go(state, {
        id: oService.nID
      }, {
        location: false
      }).then(function() {
        self.isStep2 = true;
      });
    }
  };

      // wizard controller
      // FIXME create sequencer 
      self.isStep2 = self.isStep2 || false;

      $scope.bAdmin = AdminService.isAdmin();
      // $scope.regions = iPlaceController.regions;

      $scope.getStateName = function() {
        return $state.current.name;
      };

      $scope.getRegionId = function() {
        var place = self.getPlace();
        var region = place ? place.region || null : null;
        return region ? region.nID : 0;
      };

      $scope.getCityId = function() {
        var place = self.getPlace();
        var city = place ? place.city || null : null;
        return city ? city.nID : 0;
      };

      // FIXME remove state dependency
      var curState = $scope.getStateName();

      console.log('Places Service. $state =', $state);

      if (statesMap[curState] && statesMap[curState].startupFunction) {
        statesMap[curState].startupFunction.call(self, $location, $state, $rootScope, $scope, self.placeCtrl);
      } else {
        // default startup
        $scope.$location = $location;
      }

      $scope.$state = $state;

      $scope.getHtml = function(html) {
        return $sce.trustAsHtml(html);
      };

      // STOPPEDHERE 
      
      $scope.step1 = function() {
        self.isStep2 = false;
        // FIXME
        // if (byState('index.service.general.placefix')) {
        //   return $state.go('index.service.general.placefix', {
        //     id: $scope.service.nID
        //   });
        // }
      };

      $scope.step2 = function() {
        var aServiceData = $scope.service.aServiceData;

        // console.log('step 2:');
        self.isStep2 = true;
      };

      $scope.makeStep = function(stepId) {
        if (stepId) {
          if (stepId === 'editStep') {
            self.isStep2 = false;
          }
        }
      };

      /**
       * params: serviceType, placeData
       */
      $scope.$on('onPlaceChange', function(evt, oParams) {
        self.processPlaceChange(oParams);
      });

      $scope.ngIfStep2 = function() {
        return self.isStep2;
      };

  self.processPlaceChange( null );

  /*
  angular.module('app').controller('ServiceBuiltInController', function($location, $state, $rootScope, $scope) {
    $scope.$location = $location;
    $scope.$state = $state;
  });
  */

});

/*
angular.module('app').controller('ServiceRegionController', function($state, $rootScope, $scope, $sce, RegionListFactory, PlacesService, ServiceService, service, AdminService) {
  
  // FIXME - code moved to place.js
  $scope.onSelectRegionList = function($item, $model, $label) {
    $scope.data.region = $item;
    $scope.regionList.select($item, $model, $label);
  };
});
*/