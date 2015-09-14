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

        $scope.getClass = function() {
          return PlacesService.getClassByState($state);
        };

        $scope.collapse = function() {
          console.log('collapse: ', this, $scope);
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
          var bUnavail = PlacesService.serviceIsUnavailableInPlace();
          bNeeded = bNeeded && $scope.placeControlIsComplete() && !bUnavail;

          bNeeded = bNeeded && bUnavail === false;

          console.log('auth control is needed:', bNeeded, ', unavailable in Place:', bUnavail);

          // STOPPEDHERE

          return bNeeded;
        };

        // TODO improve the logic
        $scope.authControlIsComplete = function() {
          var bComplete = false;
          return bComplete;
        };

        $scope.placeControlIsVisible = function() {

          var result = true;

          // if ($scope.serviceIsUnavailableInPlace()) {
          //   result = false;
          // }

          // console.log('placeControlIsVisible:', result );

          return result;
        };

        $scope.placeControlIsDisabled = function() {
          var isDisabled = $scope.placeControlIsComplete() && mode !== 'editMode';
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
          var oAvail = PlacesService.getServiceAvailabilityForSelectedPlace();
          var result = false; //$scope.regionIsChosen() && $scope.cityIsChosen();
          // no region - no city
          // ok region - no city
          // ok region - ok city
          if (!oAvail.isRegion && !oAvail.isCity) {
            result = true;
          }
          if ((oAvail.isRegion && $scope.regionIsChosen()) && !oAvail.isCity) {
            result = true;
          }
          if ((oAvail.isRegion && $scope.regionIsChosen()) && (oAvail.isCity && $scope.cityIsChosen())) {
            result = true;
          }
          if ((!oAvail.isRegion) && (oAvail.isCity && $scope.cityIsChosen())) {
            result = true;
          }
          // console.log('Place controls is complete:', result);
          return result;
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

        $scope.onSelectLocalityList = function($item, $model, $label) {
          $scope.placeData.city = $item;
          $scope.localityList.select($item, $model, $label);
          $scope.processPlaceSelection();
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