angular.module('app').service('PlacesService', function($http) {

  // options: self, regions, service
  this.setController = function(options) {
    this.iPlaceControllerOptions = options;
  };

  this.initPlacesByScope = function(placeCtrl, $scope, $state, $rootScope, AdminService, $location) {

    var o = this.iPlaceControllerOptions;
    var viewCtrl = o.self;

    // з wizard.controller:
    viewCtrl.isStep2 = viewCtrl.isStep2 || false;

    $scope.service = o.service;
    $scope.bAdmin = AdminService.isAdmin();
    $scope.regions = o.regions;

    $scope.getStateName = function() {
      return $state.current.name;
    };

    var curState = $scope.getStateName();

    console.log('Places (wizard), state = ', curState);

    var stateStartupFunction = {
      'index.service.general.city.built-in': function($location, $state, $rootScope, $scope) {
        $scope.$location = $location;
        $scope.$state = $state;
        viewCtrl.isStep2 = true;
      }
    };

    if (stateStartupFunction[curState]) {
      stateStartupFunction[curState].call(viewCtrl, $location, $state, $rootScope, $scope);
    } else {
      // default startup
      $scope.$location = $location;
      $scope.$state = $state;
    }

    $scope.step1 = function() {
      viewCtrl.isStep2 = false;
      // FIXME
      // if (byState('index.service.general.city')) {
      //   return $state.go('index.service.general.city', {
      //     id: $scope.service.nID
      //   });
      // }
    };

    $scope.step2 = function() {
      var aServiceData = $scope.service.aServiceData;

      // console.log('step 2:');
      viewCtrl.isStep2 = true;
    };

    $scope.makeStep = function(stepId) {
      if (stepId) {
        if (stepId === 'editStep') {
          viewCtrl.isStep2 = false;
        }
      }
    };

    /**
     * params: serviceType, placeData
     */
    $scope.$on('onPlaceChange', function(evt, params) {
      var stateByServiceType = {
        // Сервіс за посиланням
        1: 'index.service.general.city.link',
        // Вбудований сервіс
        4: 'index.service.general.city.built-in',
        // Помилка - сервіс відсутній
        0: 'index.service.general.city.error'
      };

      var state = stateByServiceType[params.serviceType.nID];

      console.log('on Place сhange:', state, params, ', curState = ', curState );

      if ( curState === 'index.service.general.city.built-in' || curState === 'index.service.general.city' ) {
        if (state && params.placeData.city) {
          viewCtrl.isStep2 = true;
          // console.log('go state:', state);
          $state.go(state, {
            id: $scope.service.nID
          }, {
            location: false
          }).then(function() {
            viewCtrl.isStep2 = true;
          });
        }
      }
    });

    $scope.ngIfStep2 = function() {
      return viewCtrl.isStep2;
    };

  }; // initPlacesByScope

  // FIXME зберігати placeData у localStorage і відновлювати для юзера
  this.placeData = null;

  this.saveLocal = function(placeData) {
    localStorage.setItem('igPlaceData', JSON.stringify(placeData));
  };

  this.setPlace = function(placeData) {
    this.placeData = placeData;
    this.saveLocal(placeData);
    // console.log('set place data:', JSON.stringify(placeData));
  };

  this.getPlace = function() {
    this.placeData = JSON.parse(localStorage.getItem('igPlaceData')) || this.placeData;
    // console.log('get place data:', this.placeData);
    return this.placeData;
  };

  this.getRegionsForService = function(service) {
    return $http.get('./api/places/regions').then(function(response) {
      var regions = response.data;
      var aServiceData = service.aServiceData;

      angular.forEach(regions, function(region) {
        var color = 'red';
        angular.forEach(aServiceData, function(oServiceData) {
          if (oServiceData.hasOwnProperty('nID_City') === false) {
            return;
          }
          var oCity = oServiceData.nID_City;
          var oRegion = oCity.nID_Region;
          if (oRegion.nID === region.nID) {
            color = 'green';
          }
        });
        region.color = color;
      });

      return regions;
    });
  };

  this.getRegions = function() {
    return $http.get('./api/places/regions');
  };

  this.getRegion = function(region) {
    return $http.get('./api/places/region/' + region);
  };

  this.getCities = function(region, search) {
    var data = {
      sFind: search
    };
    return $http.get('./api/places/region/' + region + '/cities', {
      params: data,
      data: data
    });
  };

  this.getCity = function(region, city) {
    return $http.get('./api/places/region/' + region + '/city/' + city);
  };
});

/**
  @uazure:

  Выбор региона осуществляется через ангуляровский сервис.

  Задача сервиса - предоставлять контроллерам информацию о том, какой регион или город выбрал пользователь, 
  и позволять контроллерам обновлять эту информацию в сервисе. 

  Сервис делает броадкаст сообщения при изменении своего состояния, 
  что позволяет подписчикам (контроллерам/директивам) запросить у сервиса актуальные данные.

  Сервис может инициализироваться из данных урл, если в нем указаны регион\город. 
  Сервис может запоминать в localStorage (или sessionStorage - как договоримся) выбранный город.

  Пользователями сервиса должны являться директивы, которые позволяют выбирать регион\город и любые контроллеры, которым нужно знать регион\город.
  https://github.com/e-government-ua/i/issues/550#issuecomment-128641486

  @bvv4ik:

  По сути должны быть такие тесткейсы:

    1) Услуга где есть только города - позволяет выбрать область, потом дает выбрать город и переходит на шаг 2
    2) Услуга где есть только области - позволяет выбрать область, и переходит на шаг 2
    3) Услуга где есть города, идущие вначале и области - позволяет выбрать область, потом, если по данной области есть еще и города дает выбрать город и переходит на шаг 2, если города по области нет - сразу переходит на шаг 2
    4) Услуга где есть области, идущие вначале и города - позволяет выбрать область, потом, если по данной области есть еще и города дает выбрать город и переходит на шаг 2, если города по области нет - сразу переходит на шаг 2
    5) Услуга где нет ни областей ни городов (уровень страны) - сразу переходит на шаг 2

  если все эти кейсы отрабатываются корректно - все гуд 
  (п.1, п.3 и п.4. обеспечиваются в подкаталоге city, п.2 - в region, 
  а п.5. - в country. т.е. стоит это все реализовать в единой папке place, и там разместить унифицированную логику).

  ВАЖНО:
  В проекте есть файл ServiceData.csv - именно на базе его данных при заходе в услугу определяется - в каких городах/регионах она есть.

  Там в колонках nID_City и nID_Region как раз и определяется привязка к региону или городу или стране (если оба налл).

  а в колонке nID_Service - ИД услуги

  т.е. по нему можно подобрать реальные услуги под описанные выше тест-кейсы...

  @VadymVolos:

  Можно сделать как в интернет магазинах, там есть фильтр, можно фильтровать по брендам, 
  можно по товарам, можно по цвету. У нас можно отсортировать по ОДА например.

*/

// ("Тернопольская область" и "город Кривой рог" одновременно): https://test.igov.org.ua/wf-central/service/services/getService?nID=628