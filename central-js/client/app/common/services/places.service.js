angular.module('app')
  .service('PlacesService', function($http) {

    var self = this;

    var sequencer = {};

    // Зберігаємо savedPlaceData у localStorage і потім відновлюємо
    // Формат даних:
    // {"region":{"sID_UA:"1200000000","nID":1,"sName":"Дніпропетровська","aCity":[{"sID_UA":"1220310100","nID":260,"sName":"Апостолове"},{"sID_UA":"1221010300","nID":369,"sName":"Верхівцеве"},{"sID_UA":"1221010100","nID":251,"sName":"Верхньодніпровськ"},{"sID_UA":"1210200000","nID":182,"sName":"Вільногірськ"},{"sID_UA":"1210400000","nID":28,"sName":"Дніпродзержинськ"},{"sID_UA":"1210100000","nID":1,"sName":"Дніпропетровськ"},{"sID_UA":"1210700000","nID":92,"sName":"Жовті Води"},{"sID_UA":"1220310300","nID":284,"sName":"Зеленодольськ"},{"sID_UA":"1211000000","nID":2,"sName":"Кривий Ріг"},{"sID_UA":"1211300000","nID":102,"sName":"Марганець"},{"sID_UA":"1211600000","nID":37,"sName":"Нікополь"},{"sID_UA":"1211900000","nID":65,"sName":"Новомосковськ"},{"sID_UA":"1212100000","nID":104,"sName":"Орджонікідзе"},{"sID_UA":"1212400000","nID":42,"sName":"Павлоград"},{"sID_UA":"1223210500","nID":373,"sName":"Перещепине"},{"sID_UA":"1212600000","nID":149,"sName":"Першотравенськ"},{"sID_UA":"1221411000","nID":234,"sName":"Підгородне"},{"sID_UA":"1224510100","nID":208,"sName":"Пятихатки"},{"sID_UA":"1213000000","nID":137,"sName":"Синельникове"},{"sID_UA":"1213500000","nID":148,"sName":"Тернівка"}],"color":"green","$$hashKey":"object:20"},"city":{"sID_UA":"1210100000","nID":1,"sName":"Дніпропетровськ","color":"green","$$hashKey":"object:87"}};

    var savedPlaceData = null;

    var statesMap = {
      'index.service.general.city.built-in': {
        startupFunction: function(sequencer, $location, $state, $rootScope, $scope, placeCtrl) {
          $scope.$location = $location;
          $scope.$state = $state;
          sequencer.isStep2 = true;
        },
        viewClass: 'state-disabled'
      },
      'index.service.general.city.built-in.bankid.submitted': {
        startupFunction: function(sequencer, $location, $state, $rootScope, $scope, placeCtrl) {
          $scope.collapse();
          $scope.state = $state; //.get('index.service.general.city.built-in.bankid.submitted');
        },
        viewClass: 'state-collapsed'
      }
    };

    // options: self, regions, service
    self.setController = function(ctrl) {
      self.iPlaceController = ctrl;
    };

    self.getClassByState = function($state) {
      return statesMap[$state.current.name] && statesMap[$state.current.name].viewClass || '';
    };

    self.saveLocal = function(oSavedPlaceData) {
      localStorage.setItem('igSavedPlaceData', JSON.stringify(oSavedPlaceData));
    };

    self.setPlace = function(oSavedPlaceData) {
      savedPlaceData = oSavedPlaceData;
      self.saveLocal(savedPlaceData);
      // console.log('set place data:', JSON.stringify(savedPlaceData));
    };

    self.getPlace = function() {
      savedPlaceData = JSON.parse(localStorage.getItem('igSavedPlaceData')) || savedPlaceData;
      // console.log('get place data:', JSON.stringify(savedPlaceData));
      return savedPlaceData;
    };

    self.initPlacesByScopeAndState = function(placeCtrl, $scope, $state, $rootScope, AdminService, $location, $sce) {

      // wizard controller
      var ctrl = self.iPlaceController;
      sequencer = ctrl.controller;
      sequencer.isStep2 = sequencer.isStep2 || false;

      $scope.service = ctrl.service;
      $scope.bAdmin = AdminService.isAdmin();
      $scope.regions = ctrl.regions;

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

      console.log('state href: ', $state.href('index.service.general.city.built-in.bankid', {
        'id': ctrl.service.nID,
        'region': $scope.getRegionId(),
        'city': $scope.getCityId()
      }));

      var curState = $scope.getStateName();

      console.log('Places (wizard), state = ', curState);

      if (statesMap[curState] && statesMap[curState].startupFunction) {
        statesMap[curState].startupFunction.call(sequencer, $location, $state, $rootScope, $scope, placeCtrl);
      } else {
        // default startup
        $scope.$location = $location;
      }

      $scope.$state = $state;

      $scope.getHtml = function(html) {
        return $sce.trustAsHtml(html);
      };

      $scope.step1 = function() {
        sequencer.isStep2 = false;
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
        sequencer.isStep2 = true;
      };

      $scope.makeStep = function(stepId) {
        if (stepId) {
          if (stepId === 'editStep') {
            sequencer.isStep2 = false;
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

        console.log('on Place сhange:', state, params, ', curState = ', curState);

        if (curState === 'index.service.general.city.built-in' || curState === 'index.service.general.city') {
          if (state && params.placeData.city) {
            sequencer.isStep2 = true;
            // console.log('go state:', state);
            $state.go(state, {
              id: $scope.service.nID
            }, {
              location: false
            }).then(function() {
              sequencer.isStep2 = true;
            });
          }
        }
      });

      $scope.ngIfStep2 = function() {
        return sequencer.isStep2;
      };
    };
    // end of init Places By Scope

    self.getRegionsForService = function(service) {
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

    self.getRegions = function() {
      return $http.get('./api/places/regions');
    };

    self.getRegion = function(region) {
      return $http.get('./api/places/region/' + region);
    };

    self.getCities = function(region, search) {
      var data = {
        sFind: search
      };
      return $http.get('./api/places/region/' + region + '/cities', {
        params: data,
        data: data
      });
    };

    self.getCity = function(region, city) {
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