/**
 place.js - компонент, що допомогає вибирати місце - область та місто 
 *
 */

/* initially controllers/serviceCity.controller.js */

angular.module('app')
  .directive('place', function($location, $state, $sce, RegionListFactory, LocalityListFactory) {
    return {
      restrict: 'E',
      templateUrl: 'app/common/components/form/directives/place/place.html',
      require: 'ngModel',
      link: function($scope, element, attrs, ngModel) {

        $scope.regionList = new RegionListFactory();
        $scope.regionList.initialize($scope.regions);

        $scope.localityList = new LocalityListFactory();

        $scope.data = {
          region: null,
          city: null
        };

        console.log( '$scope.data = ', $scope.data );

        $scope.loadRegionList = function(search) {
          return $scope.regionList.load($scope.service, search);
        };

        $scope.onSelectRegionList = function($item, $model, $label) {
          $scope.data.region = $item;
          $scope.regionList.select($item, $model, $label);

          var serviceType = $scope.findServiceDataByRegion();
          switch (serviceType.nID) {
            case 1:
              $state.go('index.service.general.city.link', {
                id: $scope.service.nID
              }, {
                location: false
              }).then(function() {
                // FIXME
                // isStep2 = true;
              });
              break;
            case 4:
              $state.go('index.service.general.city.built-in', {
                id: $scope.service.nID
              }, {
                location: false
              }).then(function() {
                // FIXME
                // isStep2 = true;
              });
              break;
            default:
              $scope.localityList.load($scope.service, $item.nID, null).then(function(cities) {
                $scope.localityList.typeahead.defaultList = cities;
              });
          }
        };

        $scope.loadLocalityList = function(search) {
          return $scope.localityList.load($scope.service, $scope.data.region.nID, search);
        };

        $scope.onSelectLocalityList = function($item, $model, $label) {
          $scope.data.city = $item;
          $scope.localityList.select($item, $model, $label);

          var serviceType = $scope.findServiceDataByCity();
          switch (serviceType.nID) {
            case 1:
              $state.go('index.service.general.city.link', {
                id: $scope.service.nID
              }, {
                location: false
              }).then(function() {
                // FIXME
                // isStep2 = true;
              });
              break;
            case 4:
              $state.go('index.service.general.city.built-in', {
                id: $scope.service.nID
              }, {
                location: false
              }).then(function() {
                // FIXME
                // isStep2 = true;
              });
              break;
            default:
              $state.go('index.service.general.city.error', {
                id: $scope.service.nID
              }, {
                location: false
              });
          }
        };

        $scope.ngIfCityOrRegion = function() {
          var bResult = $scope.data.city ? true : $scope.data.region ? true : false;
          console.log('is city: ', bResult);
          return bResult;
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

        //scope.onSelectRegionList = $scope.onSelectRegionList;
        //   // this parser run before control's parser
        //   // modelCtrl.$parsers.unshift(function(inputValue) {});
        //   // this parser run after controls's parser
        //   // modelCtrl.$parsers.push(function(inputValue) {});
      }
    };
  });