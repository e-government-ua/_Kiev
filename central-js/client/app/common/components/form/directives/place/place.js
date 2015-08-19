/**
 * Place.js - компонент, що допомогає вибирати місце - область та місто 
 * - з controllers/serviceCity.controller.js
 *
 */
angular.module('app')
  .directive('place', function($rootScope, $location, $state, $sce, AdminService, RegionListFactory, LocalityListFactory, PlacesService) {
    return {
      restrict: 'E',
      templateUrl: 'app/common/components/form/directives/place/place.html',
      require: 'ngModel',
      link: function($scope, element, attrs, ngModel) {

        $scope.recallPlaceData = function() {
          $scope.data = PlacesService.getPlace() || $scope.data;

          // console.log('recall place data: ', $scope.data.region, $scope.data.city.sName);

          if ($scope.regionList) {
            $scope.regionList.select($scope.data.region);
          }

          if ($scope.localityList) {
            $scope.localityList.select($scope.data.city);
          }
        };

        $scope.resetPlaceData = function() {
          $scope.data = {
            region: null,
            city: null
          };
        };

        $scope.cityIsChosen = function() {
          var r = $scope.data && ($scope.data.city ? true : false);
          // console.log('city is chosen: ', r);
          return r;
        };

        $scope.regionIsChosen = function() {
          var bResult = $scope.data && ($scope.data.region ? true : false);
          // console.log('region is chosen: ', bResult);
          return bResult;
        };

        $scope.initPlaceControls = function() {
          PlacesService.initPlacesByScope(this, $scope, $state, $rootScope, AdminService, $location);

          // не ініціюємо, якщо контроли вже є
          if ($scope.regionIsChosen() && $scope.cityIsChosen()) {
            return;
          }

          $scope.regionList = new RegionListFactory();
          $scope.localityList = new LocalityListFactory();
          $scope.regionList.initialize($scope.regions);
          $scope.resetPlaceData();

          $scope.recallPlaceData();

          console.log('init place controls: ', $scope.data);
        };

        $scope.initPlaceControls();

        $scope.edit = function() {
          $scope.resetPlaceData();

          $scope.makeStep('editStep');

          $scope.regionList.select(null);
          $scope.localityList.select(null);
        };

        $scope.onSelectLocalityList = function($item, $model, $label) {
          $scope.data.city = $item;
          $scope.localityList.select($item, $model, $label);
        };

        $scope.loadRegionList = function(search) {
          return $scope.regionList.load($scope.service, search);
        };

        $scope.loadLocalityList = function(search) {
          return $scope.localityList.load($scope.service, $scope.data.region.nID, search).then(function(cities) {
            $scope.localityList.typeahead.defaultList = cities;
          });
        };

        $scope.processPlaceSelection = function() {
          var serviceType = $scope.cityIsChosen() ? $scope.findServiceDataByCity() : $scope.findServiceDataByRegion();
          console.log('region is chosen: ', $scope.regionIsChosen(), ', city is chosen: ', $scope.cityIsChosen(), ' serviceType:', serviceType);
          PlacesService.setPlace($scope.data);

          // FIXME event must be here
          $scope.onPlaceChange(serviceType, $scope.data);
        };

        $scope.onSelectRegionList = function($item, $model, $label) {
          $scope.data.region = $item;
          $scope.regionList.select($item, $model, $label);
          $scope.loadLocalityList(null);
          $scope.processPlaceSelection();
        };

        $scope.onSelectLocalityList = function($item, $model, $label) {
          $scope.data.city = $item;
          $scope.localityList.select($item, $model, $label);
          $scope.processPlaceSelection();
        };

        $scope.getRegionId = function() {
          var region = $scope.data.region;
          return region ? region.nID : 0;
        };

        $scope.getCityId = function() {
          var city = $scope.data.city;
          return city ? city.nID : 0;
        };

        $scope.findServiceDataByRegion = function() {
          var aServiceData = $scope.service.aServiceData;
          var serviceType = {
            nID: 0
          };
          angular.forEach(aServiceData, function(value, key) {
            if (value.nID_Region && value.nID_Region.nID === $scope.data.region.nID) {
              serviceType = value.nID_ServiceType;
              $scope.serviceData = value;
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
          angular.forEach(aServiceData, function(value, key) {
            if (value.nID_City && value.nID_City.nID === ($scope.data.city && $scope.data.city.nID)) {
              serviceType = value.nID_ServiceType;
              $scope.serviceData = value;
              if ($scope.serviceData.bNoteTrusted === false) {
                $scope.serviceData.sNote = $sce.trustAsHtml($scope.serviceData.sNote);
                $scope.serviceData.sNoteTrusted = true;
              }
            }
          });
          return serviceType;
        };

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