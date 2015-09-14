/**
  Послуги для тестування:
  
  region only, built-in:

  655 NULL  1 4 {sPath:service/form/form-data,oParams:{processDefinitionId:dnepr_spravka_o_doxodax:1:1}}  https://test.region.igov.org.ua/wf-region/  FALSE 1 FALSE   BankID,EDS                            
  Отримання довідки про доходи фізичних осіб (тільки регіон - Дніпро, 1)
  https://test.igov.org.ua/service/655/general
  
  country only, external: 

  101 NULL  NULL  1 {}  http://map.land.gov.ua/kadastrova-karta FALSE 1 FALSE Ви можете отримати послугу на сайті Публічної кадастрової карти України BankID,EDS                              Надання відомостей з Державного земельного кадастру у формі витягу з Державного земельного кадастру про земельну ділянку
  https://test.igov.org.ua/service/101/general

  city:
  
  159 2 NULL  1 {}  https://dniprorada.igov.org.ua  FALSE 1 TRUE    BankID,EDS
  159 1 NULL  4 {   sPath:service/form/form-data,oParams:{processDefinitionId:dnepr_mreo_1:1:58}}  https://test.region.igov.org.ua/wf-region/  FALSE 1 FALSE   BankID,EDS                              
  702 3 NULL  1 {}  https://egov.city-adm.lviv.ua/  FALSE 1 FALSE Скористатися послугою можна на сайті порталу міста Львів. Для користування послугою треба будет увійти в систему через BankID або електронно-цифровий підпис  BankID,EDS                            
  159 6 NULL  1 {}  FALSE 1 FALSE Будь ласка, оберіть адресу МРЄВ,за якою Ви бажаєте отримати послугу: <br> <a href=/service/726/general?sID_UA_Region=8000000000&sID_UA_City=8000000000> Петропавлівська Борщагівка, вул.Кільцева 4</a> <br> <a href=/service/727/general?sID_UA_Region=8000000000&sID_UA_City=8000000000> вул. Туполєва, 19</a> <br /><a href=/service/728/general?sID_UA_Region=8000000000&sID_UA_City=8000000000> вул. Велика кільцева дорога. 22-А</a> <br /><a href=/service/729/general?sID_UA_Region=8000000000&sID_UA_City=8000000000> вул. Братиславська, 52</a> <br /><a href=/service/730/general?sID_UA_Region=8000000000&sID_UA_City=8000000000> вул. Столичне шосе, 104</a> <br /><a href=/service/731/general?sID_UA_Region=8000000000&sID_UA_City=8000000000> вул. Павла Усенка, 8</a> <br /><a href=/service/732/general?sID_UA_Region=8000000000&sID_UA_City=8000000000> вул. Новокостянтинівська, 8</a> <br /><a href=/service/733/general?sID_UA_Region=8000000000&sID_UA_City=8000000000> пров. Балтійський, 20</a> <br />                              

  107 4 8 NULL  4 {sPath:service/form/form-data,oParams:{processDefinitionId:kuznetsovsk_mvk_4:1:1}}  https://test.region.igov.org.ua/wf-region/  FALSE 1 FALSE   BankID,EDS                            
 *
 */

angular.module('app')
  .service('PlacesService', function($http, $state, ServiceService) {

    var self = this;

    var rememberMyData = false;

    // Зберігаємо savedPlaceData у localStorage і потім відновлюємо
    // Формат даних:
    // {"region":{"sID_UA:"1200000000","nID":1,"sName":"Дніпропетровська","aCity":[{"sID_UA":"1220310100","nID":260,"sName":"Апостолове"},{"sID_UA":"1221010300","nID":369,"sName":"Верхівцеве"},{"sID_UA":"1221010100","nID":251,"sName":"Верхньодніпровськ"},{"sID_UA":"1210200000","nID":182,"sName":"Вільногірськ"},{"sID_UA":"1210400000","nID":28,"sName":"Дніпродзержинськ"},{"sID_UA":"1210100000","nID":1,"sName":"Дніпропетровськ"},{"sID_UA":"1210700000","nID":92,"sName":"Жовті Води"},{"sID_UA":"1220310300","nID":284,"sName":"Зеленодольськ"},{"sID_UA":"1211000000","nID":2,"sName":"Кривий Ріг"},{"sID_UA":"1211300000","nID":102,"sName":"Марганець"},{"sID_UA":"1211600000","nID":37,"sName":"Нікополь"},{"sID_UA":"1211900000","nID":65,"sName":"Новомосковськ"},{"sID_UA":"1212100000","nID":104,"sName":"Орджонікідзе"},{"sID_UA":"1212400000","nID":42,"sName":"Павлоград"},{"sID_UA":"1223210500","nID":373,"sName":"Перещепине"},{"sID_UA":"1212600000","nID":149,"sName":"Першотравенськ"},{"sID_UA":"1221411000","nID":234,"sName":"Підгородне"},{"sID_UA":"1224510100","nID":208,"sName":"Пятихатки"},{"sID_UA":"1213000000","nID":137,"sName":"Синельникове"},{"sID_UA":"1213500000","nID":148,"sName":"Тернівка"}],"color":"green","$$hashKey":"object:20"},"city":{"sID_UA":"1210100000","nID":1,"sName":"Дніпропетровськ","color":"green","$$hashKey":"object:87"}};

    var savedPlaceData = null;

    self.iPlaceController = null;

    // options: self
    self.setController = function(iPlaceController) {
      self.iPlaceController = iPlaceController;
    };

    self.getClassByState = function($state) {
      // FIXME 
      // return statesMap[$state.current.name] && statesMap[$state.current.name].viewClass || '';
      return '';
    };

    self.saveLocal = function(oSavedPlaceData) {
      localStorage.setItem('igSavedPlaceData', JSON.stringify(oSavedPlaceData));
    };

    self.setPlaceData = function(oSavedPlaceData) {
      savedPlaceData = oSavedPlaceData;
      self.saveLocal(savedPlaceData);
      // console.log('set place data:', JSON.stringify(savedPlaceData));
    };

    /**
     * returns saved place data
     */
    self.getPlaceData = function() {
      savedPlaceData = JSON.parse(localStorage.getItem('igSavedPlaceData')) || savedPlaceData;
      // savedPlaceData = rememberMyData ? JSON.parse(localStorage.getItem('igSavedPlaceData')) || savedPlaceData : null;
      // console.log('get place data:', JSON.stringify(savedPlaceData));
      return savedPlaceData;
    };

    self.initPlacesByScopeAndState = function(placeCtrl, $scope, $state, $rootScope, AdminService, $location, $sce) {

      // tell controller about Place Control
      self.iPlaceController.placeCtrl = placeCtrl;
      // ctrllr methods moved back to PlaceFixController
    };

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

    self.regionIsChosen = function() {
      var bResult = savedPlaceData && (savedPlaceData.region ? true : false);
      // console.log('region is chosen: ', bResult);
      return bResult;
    };

    self.cityIsChosen = function() {
      var bResult = savedPlaceData && (savedPlaceData.city ? true : false);
      // console.log('city is chosen: ', r);
      return bResult;
    };

    self.findServiceDataByRegion = function() {
      var aServiceData = ServiceService.oService.aServiceData;
      var result = null;
      angular.forEach(aServiceData, function(oService, key) {
        // if service is available in r
        if (oService.nID_Region && oService.nID_Region.nID === savedPlaceData.region.nID) {
          result = oService;
        }
      });
      return result;
    };

    self.findServiceDataByCity = function() {
      var aServiceData = ServiceService.oService.aServiceData;
      var result = null;
      angular.forEach(aServiceData, function(oService, key) {
        if (oService.nID_City && oService.nID_City.nID === (savedPlaceData.city && savedPlaceData.city.nID)) {
          result = oService;
        }
      });
      return result;
    };

    self.getServiceDataForCountry = function() {
      return ServiceService.oService.aServiceData[0];
    };

    self.serviceIsAvailableInRegion = function() {
      return self.regionIsChosen() && self.findServiceDataByRegion() !== null;
    };

    self.serviceIsAvailableInCity = function() {
      return self.cityIsChosen() && self.findServiceDataByCity() !== null;
    };

    self.getServiceAvailability = function(service) {
      // FIXME move to use ServiceService.oService everywhere instead of $scope.service
      var oService = service || ServiceService.oService;
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
      console.log('getServiceAvailability, iD:', oService.nID, result.isCity, result.isRegion);
      return result;
    };

    // TODO check it again and again
    self.getServiceDataForSelectedPlace = function() {
      return self.serviceIsAvailableInCity() ? self.findServiceDataByCity() : self.serviceIsAvailableInRegion() ? self.findServiceDataByRegion() : self.getServiceDataForCountry();
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