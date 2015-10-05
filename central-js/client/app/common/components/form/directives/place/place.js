/**
 Place.js - компонент для вибору місця - області та міста.
 Використовує сервіс PlacesService.
 
 FIXME: передбачити можливість вибору тільки міста, якщо його назва є унікальною — тобто воно належить тільки одній області, і її можна взнати автоматично.
 
 FIXME: був баг: при виборі області інтерфейс дає вибрати ще й місто, хоча область у данноу випадку є кінцевоб точкою: https://github.com/e-government-ua/i/issues/540 

 FIXME: наявність обласної послуги не має блокувати можливість розміщення там же і міської послуги, див https://github.com/e-government-ua/i/issues/443
 Правильно: вибрати "Миколіївську", а потім мати можливість вибрати "Миколаїв"

 Див. https://github.com/e-government-ua/i/issues/550
**/
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

          var regions = PlacesService.getRegionsForService(ServiceService.oService);
          var initialRegion = serviceLocationParser.getSelectedRegion(regions);

          if ($scope.regionList) {
            $scope.regionList.select(initialRegion || placeData.region);
          }
          if ($scope.localityList) {
            $scope.localityList.select(placeData.city);
          }
          if (initialRegion) {
            // TODO debug it
            // console.log('initialRegion: ', initialRegion);
            $scope.onSelectRegionList(initialRegion);
          }
        };

        $scope.resetPlaceData = function() {
          PlacesService.setPlaceData({
            region: null,
            city: null
          });
        };

        $scope.serviceAvailableIn = function() {
          return PlacesService.serviceAvailableIn();
        };

        $scope.cityIsChosen = function() {
          return PlacesService.cityIsChosen();
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
          var sa = PlacesService.serviceAvailableIn();
          bNeeded = bNeeded && (sa.thisRegion || sa.thisCity) && $scope.placeControlIsComplete();

          return bNeeded;
        };

        // TODO do the logic
        $scope.authControlIsComplete = function() {
          var bComplete = false;
          return bComplete;
        };

        $scope.placeControlIsNeeded = function() {
          var bNeeded = false;
          var sa = PlacesService.serviceAvailableIn();

          // needed because service is available for some place
          if (sa.someRegion || sa.someCity) {
            bNeeded = true;
          }

          return bNeeded;
        };

        $scope.placeControlIsVisible = function() {
          var bResult = true;

          bResult = $scope.placeControlIsNeeded();

          return bResult;
        };

        $scope.placeControlIsDisabled = function() {
          var bIsDisabled = false;
          bIsDisabled = $scope.placeControlIsComplete() && sControlMode !== 'placeEditMode';
          console.log('placeControlIsDisabled:', bIsDisabled);
          return bIsDisabled;
        };

        /**
         * Ця функція визначає, чи заповнені всі поля, які необхідно заповнити
         */
        $scope.placeControlIsComplete = function() {
          var bIsComplete = false;
          var sa = PlacesService.serviceAvailableIn();
          var regionIsChosen = $scope.regionIsChosen();
          var cityIsChosen = $scope.cityIsChosen();

          // return false if no region or no city is chosen (usually on startup), but service is available somewhere
          // if ((!regionIsChosen && sa.someRegion) || (!cityIsChosen && sa.someCity)) {
          //   bIsComplete = false;
          // }
          // no region - no city, no need in choosing the place
          if (!sa.someRegion && !sa.someCity) {
            bIsComplete = true;
          }
          // this region only and no city in this region (!)
          if (sa.thisRegion && !sa.someCityInThisRegion) {
            bIsComplete = true;
          }
          // this city
          if (sa.thisCity) {
            bIsComplete = true;
          }
          // not this region, but maybe another (but not another city)
          if (regionIsChosen && !sa.thisRegion && sa.someRegion && !sa.someCity) {
            bIsComplete = true;
          }

          return bIsComplete;
        };

        $scope.editPlace = function() {
          sControlMode = 'placeEditMode';

          $scope.resetPlaceData();
          var regions = PlacesService.getRegionsForService(ServiceService.oService);

          $scope.regionList.reset();
          $scope.regionList.select(null);
          $scope.regionList.initialize(regions);

          $scope.localityList.reset();
          $scope.localityList.select(null);

          $scope.$emit('onEditPlace', {});
        };

        $scope.loadRegionList = function(search) {
          return $scope.regionList.load(ServiceService.oService, search);
        };

        $scope.loadLocalityList = function(search) {
          return $scope.localityList.load(ServiceService.oService, PlacesService.getPlaceData().region.nID, search);
        };

        $scope.processPlaceSelection = function() {
          var placeData = PlacesService.getPlaceData();

          // console.log('Process Place selection.');
          // console.log('1. Region is chosen:', $scope.regionIsChosen(), ', city is chosen:', $scope.cityIsChosen());
          // console.log('2. Place controls is complete:', $scope.placeControlIsComplete());
          // console.log('3. Auth control is visible:', $scope.authControlIsVisible());
          // console.log('4. Service Availability:', JSON.stringify(PlacesService.serviceAvailableIn(), null, ''));

          PlacesService.setPlaceData(placeData);

          $scope.region = PlacesService.getPlaceData().region;
          $scope.city = PlacesService.getPlaceData().city;

          $scope.$emit('onPlaceChange');
        };

        // FIXME дебажити повторний вибір місця
        $scope.initPlaceControls = function() {

          var regions = $scope.regions;
          var placeData = PlacesService.getPlaceData();

          // ініціюємо дані зі scope, якщо вони там є:
          $scope.region = $scope.data && $scope.data.region || placeData.region || $scope.region;
          $scope.city = $scope.data && $scope.data.city || placeData.city || $scope.city;

          PlacesService.setPlaceData({
            region: $scope.region,
            city: $scope.city
          });

          $scope.regionList = $scope.regionList || new RegionListFactory();
          $scope.localityList = $scope.localityList || new LocalityListFactory();

          $scope.regionList.initialize($scope.regions);

          // console.log('initPlaceControls $scope.regions.length = ', $scope.regions.length, '$scope.region:', $scope.region, '$scope.city:', $scope.city, ' $scope.data:', $scope.data);

          // TODO обнуляти дані, коли працюємо з localstorage, але не коли йдемо "крок за кроком":
          if (sControlMode === 'placeEditMode') {
            $scope.resetPlaceData();
          }

          $scope.recallPlaceData();

          // Якщо форма вже заповнена після відновлення даних з localStorage, то перейти до наступного кроку:
          if ($scope.placeControlIsComplete()) {
            $scope.processPlaceSelection();
          }
        };

        $scope.onSelectRegionList = function($item, $model, $label) {

          console.info('onSelectRegionList, Service Availability:', JSON.stringify(PlacesService.serviceAvailableIn(), null, ''), $item);

          PlacesService.setRegion($item);
          $scope.regionList.select($item, $model, $label);

          $scope.loadLocalityList(null);
          PlacesService.setCity(null);
          $scope.localityList.reset();

          $scope.localityList.load(null, $item.nID, null).then(function(cities) {
            $scope.localityList.typeahead.defaultList = cities;
          });

          var serviceType = PlacesService.findServiceDataByRegion();

          // Сервіс недоступний у області — значить, варто завантажити міста, інакше вважати місце вибраним:
          if (serviceType !== 1 && serviceType !== 4) {
            $scope.localityList.load(ServiceService.oService, $item.nID, null).then(function(cities) {
              $scope.localityList.typeahead.defaultList = cities;
              var initialCity = serviceLocationParser.getSelectedCity(cities);
              if (initialCity) {
                $scope.onSelectLocalityList(initialCity);
              }
            });
          }

          if ($scope.placeControlIsComplete()) {
            $scope.processPlaceSelection();
          }
        };

        $scope.showCityDropdown = function() {
          var sa = $scope.serviceAvailableIn();
          return $scope.regionIsChosen() && (sa.someCityInThisRegion || !sa.thisRegion && sa.someCity);
        };

        $scope.onSelectLocalityList = function($item, $model, $label) {
          PlacesService.setCity($item);
          $scope.localityList.select($item, $model, $label);
          $scope.processPlaceSelection();
        };

        $scope.initPlaceControls();
      }
    };
  });