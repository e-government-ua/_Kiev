/**
 * Place.js - компонент, що допомогає вибирати місце - область та місто 
 * - з controllers/serviceCity.controller.js
 * FIXME: передбачити можливість вибору тільки міста, якщо його назва є унікальною — 
 * тобто воно належить тільки одній області, і її можна взнати автоматично.
 */
angular.module('app')
  .directive('place', function($rootScope, $location, $state, $sce, AdminService, RegionListFactory, LocalityListFactory, PlacesService, ServiceService) {

    var mode = 'default';

    return {
      restrict: 'E',
      templateUrl: 'app/common/components/form/directives/place/place.html',
      link: function($scope, element, attrs) {

        $scope.getPlaceControlClass = function() {
          return PlacesService.getClassByState($state);
        };

        $scope.recallPlaceData = function() {
          $scope.placeData = PlacesService.getPlaceData() || $scope.placeData;
          // console.log('recall place data: ', $scope.placeData.region, $scope.placeData.city.sName);
          if ($scope.regionList) {
            $scope.regionList.select($scope.placeData.region);
          }
          if ($scope.localityList) {
            $scope.localityList.select($scope.placeData.city);
          }
        };

        $scope.resetPlaceData = function() {
          $scope.placeData = {
            region: null,
            city: null
          };
        };

        $scope.cityIsChosen = function() {
          var is = PlacesService.cityIsChosen();
          // console.log('cityIsChosen:', is);
          return is;
        };

        $scope.regionIsChosen = function() {
          return PlacesService.regionIsChosen();
        };

        // TODO improve the logic
        $scope.authControlIsNeeded = function() {
          var bNeeded = true;
          var oAvail = PlacesService.getServiceAvailability();
          bNeeded = bNeeded && (oAvail.thisRegion || oAvail.thisCity) && $scope.placeControlIsComplete();

          return bNeeded;
        };

        // TODO improve the logic
        $scope.authControlIsComplete = function() {
          var bComplete = false;
          return bComplete;
        };

        $scope.placeControlIsNeeded = function() {
          var bNeeded = false;
          var oAvail = PlacesService.getServiceAvailability();
          // var bAvailForPlace = PlacesService.serviceIsAvailableInPlace();

          // needed because service is available for some place
          if (oAvail.isRegion || oAvail.isCity) {
            bNeeded = true;
          }

          // needed because is available for selected place
          // if (bAvailForPlace === true && $scope.placeControlIsComplete()) {
          //   bNeeded = true;
          // }

          // console.info('place control is needed:', bNeeded, ', availability:', oAvail);

          return bNeeded;
        };

        $scope.placeControlIsVisible = function() {
          var result = true;

          result = $scope.placeControlIsNeeded();

          // console.log('placeControlIsVisible:', result );

          return result;
        };

        $scope.placeControlIsDisabled = function() {
          var isDisabled = false;
          isDisabled = $scope.placeControlIsComplete() && mode !== 'editMode';
          // console.info('placeControlIsDisabled:', $scope.placeControlIsComplete(), mode !== 'editMode', isDisabled);
          return isDisabled;
          // return false;
        };

        /**
         * Ця функція визначає, чи заповнені всі поля, які необхідно заповнити
         */
        $scope.placeControlIsComplete = function() {
          // FIXME: передбачити випадки, коли треба вибрати тільки регіон або місто, 
          // див. https://github.com/e-government-ua/i/issues/550
          var oAvail = PlacesService.getServiceAvailability();
          // var bAvail = PlacesService.serviceIsAvailableInPlace();

          // return false if no region or no city is chosen (usually on startup), but service is available somewhere
          if ((!$scope.regionIsChosen() || !$scope.cityIsChosen()) && (oAvail.isRegion || oAvail.isCity)) {
            return false;
          }

          // no region - no city, no need in choosing the place
          if (!oAvail.isRegion && !oAvail.isCity) {
            return true;
          }
          // ok region - no city
          if ((oAvail.isRegion && $scope.regionIsChosen()) && !oAvail.isCity) {
            return true;
          }
          // ok region - ok city
          if ((oAvail.isRegion && $scope.regionIsChosen()) && (oAvail.isCity && $scope.cityIsChosen())) {
            return true;
          }
          // no region - ok city
          if ((!oAvail.isRegion) && (oAvail.isCity && $scope.cityIsChosen())) {
            return true;
          }

          // console.log('Place controls is complete:', result);
          return false;
        };

        $scope.onEditPlace = function() {

          mode = 'editMode';

          $scope.resetPlaceData();

          // $scope.makeStep('editStep');

          $scope.$emit('onEditPlace', {});

          $scope.regionList.select(null);
          $scope.localityList.select(null);
        };

        $scope.loadRegionList = function(search) {
          console.log('loadRegionList, search =', search);
          return $scope.regionList.load(ServiceService.oService, search);
        };

        $scope.loadLocalityList = function(search) {
          return $scope.localityList.load(ServiceService.oService, $scope.placeData.region.nID, search).then(function(cities) {
            console.log('loadLocalityList:', search, cities);
            $scope.localityList.typeahead.defaultList = cities;
          });
        };

        // city - from ServiceCityController
        // $scope.loadLocalityList = function(search) {
        //   return $scope.localityList.load(ServiceService.oService, $scope.data.region.nID, search);
        // };

        $scope.processPlaceSelection = function() {
          // console.log('region is chosen: ', $scope.regionIsChosen(), ', city is chosen: ', $scope.cityIsChosen(), ' service Type:', s erviceType);

          PlacesService.setPlaceData($scope.placeData);

          $scope.$emit('onPlaceChange', {
            serviceData: PlacesService.getServiceDataForSelectedPlace(),
            placeData: $scope.placeData
          });
        };

        $scope.initPlaceControls = function() {

          PlacesService.initPlacesByScopeAndState(this, $scope, $state, $rootScope, AdminService, $location, $sce);

          $scope.regionList = $scope.regionList || new RegionListFactory();
          $scope.localityList = $scope.localityList || new LocalityListFactory();

          $scope.regionList.initialize($scope.regions);

          console.log('initPlaceControls, $scope.regions.length = ', $scope.regions.length);

          $scope.resetPlaceData();
          $scope.recallPlaceData();

          // Якщо форма вже заповнена після відновлення даних з localStorage, то повідомити про це
          if ($scope.placeControlIsComplete()) {
            $scope.processPlaceSelection();
          }
        };

        $scope.onSelectRegionList = function($item, $model, $label) {
          $scope.placeData.region = $item;
          $scope.regionList.select($item, $model, $label);
          $scope.loadLocalityList(null);
          $scope.processPlaceSelection();
        };

        // region - from ServiceRegionController
        //   $scope.onSelectRegionList = function($item, $model, $label) {
        //     $scope.data.region = $item;
        //     $scope.regionList.select($item, $model, $label);
        //   };

        // city - from ServiceCityController
        //  $scope.onSelectRegionList = function($item) {
        //    $scope.data.region = $item;
        //    $scope.regionList.select($item);

        // var serviceType = $scope.findServiceDataByRegion();

        //    switch (serviceType.nID) {
        //      case 1:
        //        $state.go('index.service.general.placefix.link', {id: ServiceService.oService.nID}, {location: false}).then(function() {
        //    isStep2 = true;
        //  });
        //  break;
        //      case 4:
        //        $state.go('index.service.general.placefix.built-in', {id: ServiceService.oService.nID}, {location: false}).then(function() {
        //    isStep2 = true;
        //  });
        //  break;
        //      default:
        //     $scope.localityList.load(ServiceService.oService, $item.nID, null).then(function(cities) {
        //          $scope.localityList.typeahead.defaultList = cities;
        //          var initialCity = serviceLocationParser.getSelectedCity(cities);
        //          if (initialCity)
        //            $scope.onSelectLocalityList(initialCity);
        //        });
        //    }
        //  };

        $scope.onSelectLocalityList = function($item, $model, $label) {
          $scope.placeData.city = $item;
          $scope.localityList.select($item, $model, $label);
          $scope.processPlaceSelection();
        };

        // city - from ServiceCityController

        //  $scope.onSelectLocalityList = function($item, $model, $label) {
        //    $scope.data.city = $item;
        //    $scope.localityList.select($item, $model, $label);
        // var serviceType = $scope.findServiceDataByCity();
        //    switch (serviceType.nID) {
        //      case 1:
        //        $state.go('index.service.general.placefix.link', {id: ServiceService.oService.nID}, {location: false}).then(function() {
        //    isStep2 = true;
        //  });
        //  break;
        //      case 4:
        //        $state.go('index.service.general.placefix.built-in', {id: ServiceService.oService.nID}, {location: false}).then(function() {
        //    isStep2 = true;
        //  });
        //  break;
        //      default:
        //        $state.go('index.service.general.placefix.error', {id: ServiceService.oService.nID}, {location: false}).then(function() {
        //    isStep2 = true;
        //  });
        //    }
        //  };

        // city - from ServiceCityController

        //  $scope.data = {
        //    region: null,
        //    city: null
        //  };


        //  $scope.ngIfCity = function() {
        // if($state.current.name === 'index.service.general.placefix.built-in') {
        //  if($scope.data.city) {
        //    return true;
        //  } else {
        //    return false;
        //  }
        // }
        // if($state.current.name === 'index.service.general.placefix.built-in.bankid') {
        //  if($scope.data.city) {
        //    return true;
        //  } else {
        //    return false;
        //  }
        // }
        // return $scope.data.region ? true: false;
        //  };

        //  $scope.getRegionId = function() {
        // var region = $scope.data.region;
        // return region ? region.nID: 0;
        //  };

        //  $scope.getCityId = function() {
        // var city = $scope.data.city;
        // return city ? city.nID: 0;
        //  };


        $scope.initPlaceControls();

        // TODO
        // ngModel.$render = function() {
        //   console.log('render a: ', arguments);
        //   // body...
        // };

        // this parser run before control's parser
        // modelCtrl.$parsers.unshift(function(inputValue) {});
        // this parser run after controls's parser
        // modelCtrl.$parsers.push(function(inputValue) {});
      }
    };
  });