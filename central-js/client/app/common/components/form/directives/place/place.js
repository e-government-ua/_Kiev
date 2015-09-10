/**
 * Place.js - компонент, що допомогає вибирати місце - область та місто 
 * - з controllers/serviceCity.controller.js
 * FIXME: передбачити можливість вибору тільки міста, якщо його назва є унікальною — 
 * тобто воно належить тільки одній області, і її можна взнати автоматично.
 */
angular.module('app')
  .directive('place', function($rootScope, $location, $state, $sce, AdminService, RegionListFactory, LocalityListFactory, PlacesService, ServiceService) {

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
          return PlacesService.regionIsChosen();
        };

        $scope.regionIsChosen = function() {
          return PlacesService.regionIsChosen();
        };

        /**
         * Ця функція визначає, чи заповнені всі поля, які необхідно заповнити
         */
        $scope.isComplete = function() {
          // FIXME: передбачити випадки, коли треба вибрати тільки регіон або місто, 
          // див. https://github.com/e-government-ua/i/issues/550
          var oServiceAvailable = PlacesService.getServiceAvailability();
          var result = false; //$scope.regionIsChosen() && $scope.cityIsChosen();
          // no region - no city
          // ok region - no city
          // ok region - ok city
          if (!oServiceAvailable.isRegion && !oServiceAvailable.isCity) {
            result = true;
          }
          if ((oServiceAvailable.isRegion && $scope.regionIsChosen()) && !oServiceAvailable.isCity) {
            result = true;
          }
          if ((oServiceAvailable.isRegion && $scope.regionIsChosen()) && (oServiceAvailable.isCity && $scope.cityIsChosen())) {
            result = true;
          }
          return result;
        };

        $scope.onEditPlace = function() {
          $scope.resetPlaceData();

          $scope.makeStep('editStep');

          $scope.regionList.select(null);
          $scope.localityList.select(null);
        };

        $scope.loadRegionList = function(search) {
          return $scope.regionList.load( ServiceService.oService, search);
        };

        $scope.loadLocalityList = function(search) {
          return $scope.localityList.load( ServiceService.oService, $scope.placeData.region.nID, search).then(function(cities) {
            $scope.localityList.typeahead.defaultList = cities;
          });
        };

        $scope.processPlaceSelection = function() {
          var oService = PlacesService.getServiceDataForSelectedPlace();
          // console.log('region is chosen: ', $scope.regionIsChosen(), ', city is chosen: ', $scope.cityIsChosen(), ' service Type:', s erviceType);

          $scope.serviceData = oService;
          if (oService.bNoteTrusted === false) {
            oService.sNote = $sce.trustAsHtml(oService.sNote);
            oService.sNoteTrusted = true;
          }

          PlacesService.setPlaceData($scope.placeData);

          $scope.$emit('onPlaceChange', {
            serviceData: oService,
            placeData: $scope.placeData
          });
        };

        $scope.initPlaceControls = function() {

          PlacesService.initPlacesByScopeAndState(this, $scope, $state, $rootScope, AdminService, $location, $sce);

          $scope.regionList = $scope.regionList || new RegionListFactory();
          $scope.localityList = $scope.localityList || new LocalityListFactory();
          $scope.regionList.initialize($scope.regions);
          $scope.resetPlaceData();
          $scope.recallPlaceData();

          // Якщо форма вже заповнена після відновлення даних з localStorage, то повідомити про це
          if ($scope.isComplete()) {
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