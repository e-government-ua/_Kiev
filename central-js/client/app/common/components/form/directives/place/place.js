/**

 Place.js - компонент, що допомогає вибирати місце - область та місто
 Використовує сервіс PlacesService
 
 FIXME: передбачити можливість вибору тільки міста, якщо його назва є унікальною — 
 тобто воно належить тільки одній області, і її можна взнати автоматично.

 */
angular.module('app')
  .directive('place', function($rootScope, $location, $state, $sce, AdminService, RegionListFactory, LocalityListFactory, PlacesService, ServiceService, serviceLocationParser) {

    var sControlMode = 'default';

    return {
      restrict: 'E',
      templateUrl: 'app/common/components/form/directives/place/place.html',
      link: function($scope, element, attrs) {

        $scope.getPlaceControlClass = function() {
          return PlacesService.getClassByState($state);
        };

        $scope.recallPlaceData = function() {
          var placeData = PlacesService.getPlaceData();
          // console.log('recall place data: ', placeData.region, placeData.city.sName);
          if ($scope.regionList) {
            $scope.regionList.select(placeData.region);
          }
          if ($scope.localityList) {
            $scope.localityList.select(placeData.city);
          }
        };

        $scope.resetPlaceData = function() {
          PlacesService.setPlaceData({
            region: null,
            city: null
          });
        };

        $scope.cityIsChosen = function() {
          var bCityIsChosen = PlacesService.cityIsChosen();
          // console.log('cityIsChosen:', bCityIsChosen);
          return bCityIsChosen;
        };

        $scope.regionIsChosen = function() {
          return PlacesService.regionIsChosen();
        };

        // TODO improve the logic
        $scope.authControlIsVisible = function() {
          return $scope.authControlIsNeeded() && sControlMode !== 'placeEditMode';
        };

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
          var bResult = true;

          bResult = $scope.placeControlIsNeeded();

          // console.log('placeControlIsVisible:', bResult );

          return bResult;
        };

        $scope.placeControlIsDisabled = function() {
          var bIsDisabled = false;
          bIsDisabled = $scope.placeControlIsComplete() && sControlMode !== 'placeEditMode';
          // console.info('placeControlIsDisabled:', $scope.placeControlIsComplete(), sControlMode !== 'placeEditMode', bIsDisabled);
          return bIsDisabled;
        };

        /**
         * Ця функція визначає, чи заповнені всі поля, які необхідно заповнити
         */
        $scope.placeControlIsComplete = function() {
          var bIsComplete = null;
          // FIXME: передбачити випадки, коли треба вибрати тільки регіон або місто, 
          // див. https://github.com/e-government-ua/i/issues/550
          var oAvail = PlacesService.getServiceAvailability();
          // var bAvail = PlacesService.serviceIsAvailableInPlace();

          // return false if no region or no city is chosen (usually on startup), but service is available somewhere
          if ((!$scope.regionIsChosen() || !$scope.cityIsChosen()) && (oAvail.isRegion || oAvail.isCity)) {
            bIsComplete = false;
          }

          // no region - no city, no need in choosing the place
          if (!oAvail.isRegion && !oAvail.isCity) {
            bIsComplete = true;
          }
          // ok region - no city
          if ((oAvail.isRegion && $scope.regionIsChosen()) && !oAvail.isCity) {
            bIsComplete = true;
          }
          // ok region - ok city
          if ((oAvail.isRegion && $scope.regionIsChosen()) && (oAvail.isCity && $scope.cityIsChosen())) {
            bIsComplete = true;
          }
          // no region - ok city
          if ((!oAvail.isRegion) && (oAvail.isCity && $scope.cityIsChosen())) {
            bIsComplete = true;
          }

          return bIsComplete;
        };

        // колись це було step1
        $scope.editPlace = function() {

          sControlMode = 'placeEditMode';
          $scope.resetPlaceData();

          // FIXME

          var regions = PlacesService.getRegionsForService(ServiceService.oService);
          $scope.regionList.reset();
          $scope.regionList.select(null);
          $scope.regionList.initialize(regions);

          $scope.localityList.reset();
          $scope.localityList.select(null);

          $scope.$emit('onEditPlace', {});
        };

        $scope.loadRegionList = function(search) {
          // console.info('loadRegionList, search =', search);
          return $scope.regionList.load(ServiceService.oService, search);
        };

        $scope.loadLocalityList = function(search) {
          var placeData = PlacesService.getPlaceData();
          // FIXME from service.controller
          // return $scope.localityList.load(null, placeData.region.nID, search);
          return $scope.localityList.load(ServiceService.oService, placeData.region.nID, search).then(function(cities) {
            // console.log('loadLocalityList:', search, cities);
            $scope.localityList.typeahead.defaultList = cities;
          });
        };

        $scope.processPlaceSelection = function() {
          var placeData = PlacesService.getPlaceData();

          console.log('processPlaceSelection');
          console.log('region is chosen:', $scope.regionIsChosen(), ', city is chosen:', $scope.cityIsChosen());
          console.log('Place controls is complete:', $scope.placeControlIsComplete());
          console.log('AuthControlIsVisible:', $scope.authControlIsVisible());

          PlacesService.setPlaceData(placeData);

          $scope.region = PlacesService.getPlaceData().region;
          $scope.city = PlacesService.getPlaceData().city;

          $scope.$emit('onPlaceChange', {
            serviceData: PlacesService.getServiceDataForSelectedPlace(),
            placeData: PlacesService.getPlaceData()
          });
        };

        $scope.initPlaceControls = function() {

          var regions = $scope.regions;
          var placeData = PlacesService.getPlaceData();

          // FIXME finalize re-selection of place
          // init place data by scope data if it's available
          $scope.region = $scope.data && $scope.data.region || placeData.region || $scope.region;
          $scope.city = $scope.data && $scope.data.city || placeData.city || $scope.city;

          PlacesService.setPlaceData({
            region: $scope.region,
            city: $scope.city
          });

          $scope.regionList = $scope.regionList || new RegionListFactory();
          $scope.localityList = $scope.localityList || new LocalityListFactory();

          $scope.regionList.initialize($scope.regions);

          console.log('[] [] initPlaceControls $scope.regions.length = ', $scope.regions.length, '$scope.region:', $scope.region, '$scope.city:', $scope.city, ' $scope.data:', $scope.data );

          // FIXME we need ro reset it to work with localstorage
          // and we do not need it when coming from step to step
          if (sControlMode === 'placeEditMode') {
            $scope.resetPlaceData();
          }

          $scope.recallPlaceData();

          // Якщо форма вже заповнена після відновлення даних з localStorage, то повідомити про це
          if ($scope.placeControlIsComplete()) {
            $scope.processPlaceSelection();
          }
        };

        $scope.onSelectRegionList = function($item, $model, $label) {
          PlacesService.setRegion($item);
          $scope.regionList.select($item, $model, $label);
          $scope.loadLocalityList(null);

          console.info('onSelectRegionList:', $item);

          // FIXME - from service.controller
          PlacesService.setCity(null);
          $scope.localityList.reset();
          // $scope.search();
          $scope.localityList.load(null, $item.nID, null).then(function(cities) {
            $scope.localityList.typeahead.defaultList = cities;
          });

          var serviceType = PlacesService.findServiceDataByRegion();

          // Service is unavailable in region - so let's load cities
          if (serviceType !== 1 && serviceType !== 4) {

            $scope.localityList.load(ServiceService.oService, $item.nID, null).then(function(cities) {
              $scope.localityList.typeahead.defaultList = cities;
              var initialCity = serviceLocationParser.getSelectedCity(cities);
              if (initialCity) {
                $scope.onSelectLocalityList(initialCity);
              }
            });
          } else {
            $scope.processPlaceSelection();
          }

        };

        $scope.onSelectLocalityList = function($item, $model, $label) {
          PlacesService.setCity($item);

          $scope.localityList.select($item, $model, $label);

          // FIXME - from service.controller
          // $scope.search();

          $scope.processPlaceSelection();
        };

        $scope.ngIfCity = function() {
          var placeData = PlacesService.getPlaceData();
          if ($state.current.name === 'index.service.general.placefix.built-in') {
            if (placeData.city) {
              return true;
            } else {
              return false;
            }
          }
          if ($state.current.name === 'index.service.general.placefix.built-in.bankid') {
            if (placeData.city) {
              return true;
            } else {
              return false;
            }
          }
          return placeData.region ? true : false;
        };

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