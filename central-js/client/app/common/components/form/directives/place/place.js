/**
 * Place.js - компонент, що допомогає вибирати місце - область та місто 
 * - з controllers/serviceCity.controller.js
 * FIXME: передбачити можливість вибору тільки міста, якщо його назва є унікальною — 
 * тобто воно належить тільки одній області, і її можна взнати автоматично.
 */
angular.module('app')
  .directive('place', function($rootScope, $location, $state, $sce, AdminService, RegionListFactory, LocalityListFactory, PlacesService) {

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
          $scope.placeData = PlacesService.getPlace() || $scope.placeData;
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
          //var isAvailableInTheCity = $scope.getServiceAvailability();
          var r = $scope.placeData && ($scope.placeData.city ? true : false);
          var s = $scope.service.aServiceData;
          // console.log('city is chosen: ', r);
          return r;
        };

        /**
         * Ця функція визначає, чи заповнені всі поля, які необхідно заповнити
         */
        $scope.isComplete = function() {
          // FIXME: передбачити випадки, коли треба вибрати тільки регіон або місто, 
          // див. https://github.com/e-government-ua/i/issues/550
          var oServiceAvailable = $scope.getServiceAvailability();
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

        $scope.regionIsChosen = function() {
          var bResult = $scope.placeData && ($scope.placeData.region ? true : false);
          // console.log('region is chosen: ', bResult);
          return bResult;
        };

        $scope.onEditPlace = function() {
          $scope.resetPlaceData();

          $scope.makeStep('editStep');

          $scope.regionList.select(null);
          $scope.localityList.select(null);
        };

        $scope.loadRegionList = function(search) {
          return $scope.regionList.load($scope.service, search);
        };

        $scope.loadLocalityList = function(search) {
          return $scope.localityList.load($scope.service, $scope.placeData.region.nID, search).then(function(cities) {
            $scope.localityList.typeahead.defaultList = cities;
          });
        };

        $scope.processPlaceSelection = function() {
          var serviceType = $scope.cityIsChosen() ? $scope.findServiceDataByCity() : $scope.findServiceDataByRegion();
          // console.log('region is chosen: ', $scope.regionIsChosen(), ', city is chosen: ', $scope.cityIsChosen(), ' serviceType:', serviceType);
          PlacesService.setPlace($scope.placeData);

          $scope.$emit('onPlaceChange', {
            serviceType: serviceType,
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

        $scope.findServiceDataByRegion = function() {
          var aServiceData = $scope.service.aServiceData;
          var serviceType = {
            nID: 0
          };
          angular.forEach(aServiceData, function(oService, key) {
            // if service is available in 
            if (oService.nID_Region && oService.nID_Region.nID === $scope.placeData.region.nID) {
              serviceType = oService.nID_ServiceType;
              $scope.serviceData = oService;
              if ($scope.serviceData.bNoteTrusted === false) {
                $scope.serviceData.sNote = $sce.trustAsHtml($scope.serviceData.sNote);
                $scope.serviceData.sNoteTrusted = true;
              }
            }
          });
          return serviceType;
        };

        $scope.findServiceDataByCity = function() {
          var aServiceData = $scope.service.aServiceData;
          var serviceType = {
            nID: 0
          };
          angular.forEach(aServiceData, function(oService, key) {
            if (oService.nID_City && oService.nID_City.nID === ($scope.placeData.city && $scope.placeData.city.nID)) {
              serviceType = oService.nID_ServiceType;
              $scope.serviceData = oService;
              if ($scope.serviceData.bNoteTrusted === false) {
                $scope.serviceData.sNote = $sce.trustAsHtml($scope.serviceData.sNote);
                $scope.serviceData.sNoteTrusted = true;
              }
            }
          });
          return serviceType;
        };

        // FIXME-2
        $scope.getServiceAvailability = function() {
          var oService = $scope.service;
          var result = {
            isCity: false,
            isRegion: false
          };
          angular.forEach(oService.aServiceData, function(oServiceData) {
            if (oServiceData.nID_City && oServiceData.nID_City.nID !== null) {
              result.isCity = true;
            }
            if (oServiceData.nID_Region && oServiceData.nID_Region.nID !== null) {
              result.isRegion = true;
            }
          });
          console.log('getServiceAvailability, iD:', oService.nID, result);
          return result;
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