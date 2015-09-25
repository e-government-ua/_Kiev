/**

  Сервіс для вибору регіона та міста.

  Метод getPlaceData надає контроллерам інформацію про вибраний користувачем регіон та / або місто
  Метод setPlaceData дозволяє контроллерам змінювати цю інформацию в сервісі. 

  Сервіс може запам'ятовувати у localStorage вибране міце.


  Сервіс може ініціюватися з даних URL, якщо там вказано область / місто (FIXME: потестувати це). 
  
  Користувачами сервіса є директиви (див. place.js), які дозволяють вибирати область / місто та будь-які контроллери, які хочуть взнати про вибране місце.
  
  Обговорення: https://github.com/e-government-ua/i/issues/550#issuecomment-128641486
  
  TODO: броадкаст повідомлення про зміну свого стану, щоб підписані контроллери та директиви могли отримати актуальні дані.

  // WIP

  Послуги для тестування розробником:
  
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

  "Должны быть такие тесткейсы:

    1) Услуга где есть только города - позволяет выбрать область, потом дает выбрать город и переходит на шаг 2
    2) Услуга где есть только области - позволяет выбрать область, и переходит на шаг 2
    3) Услуга где есть города, идущие вначале и области - позволяет выбрать область, потом, если по данной области есть еще и города дает выбрать город и переходит на шаг 2, если города по области нет - сразу переходит на шаг 2
    4) Услуга где есть области, идущие вначале и города - позволяет выбрать область, потом, если по данной области есть еще и города дает выбрать город и переходит на шаг 2, если города по области нет - сразу переходит на шаг 2
    5) Услуга где нет ни областей ни городов (уровень страны) - сразу переходит на шаг 2

  если все эти кейсы отрабатываются корректно - все гуд 
  (п.1, п.3 и п.4. обеспечиваются в подкаталоге city, п.2 - в region, 
  а п.5. - в country. т.е. стоит это все реализовать в единой папке place, и там разместить унифицированную логику).

  Файл ServiceData.csv - там визначено, де є послуга.

  В колонках nID_City і nID_Region как раз и — прив'язка до області / міста / країни ( якщо обидва значення === null ).

  В колонці nID_Service - ИД послуги

  Приклад багу: ("Тернопольская область" и "город Кривой рог" одновременно): https://test.igov.org.ua/wf-central/service/services/getService?nID=628  

*/

angular.module('app').service('PlacesService', function($http, $state, ServiceService) {

  var self = this;

  self.rememberMyData = false;

  self.isStep2 = false;

  // Зберігаємо savedPlaceData у localStorage і потім відновлюємо, приклад формату даних:
  var placeDataExample = {
    region: {
      sID_UA: '1200000000',
      'nID': 1,
      'sName': 'Дніпропетровська',
      'aCity': [{
          'sID_UA': '1220310100',
          'nID': 260,
          'sName': 'Апостолове'
        },
        //
        {
          'sID_UA': '1210100000',
          'nID': 1,
          'sName': 'Дніпропетровськ'
        }
      ],
      'color': 'green',
      '$$hashKey': 'object:20'
    },
    'city': {
      'sID_UA': '1210100000',
      'nID': 1,
      'sName': 'Дніпропетровськ',
      'color': 'green',
      '$$hashKey': 'object:87'
    }
  };

  var savedPlaceData = {};

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

    var localData = JSON.parse(localStorage.getItem('igSavedPlaceData'));

    if (self.rememberMyData && localData) {
      savedPlaceData = localData;
    }

    // console.log('get place data:', JSON.stringify(savedPlaceData));
    return savedPlaceData;
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

  self.setRegion = function(region) {
    savedPlaceData.region = _.clone(region);
  };

  self.setCity = function(city) {
    savedPlaceData.city = _.clone(city);
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
    return bResult;
  };

  self.cityIsChosen = function() {
    var bResult = savedPlaceData && (savedPlaceData.city ? true : false);
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
    return {
      nID_ServiceType: {
        nID: 1
      }
    };
  };

  self.serviceIsAvailableInRegion = function() {
    return self.regionIsChosen() && self.findServiceDataByRegion() !== null;
  };

  self.serviceIsAvailableInCity = function() {
    return self.cityIsChosen() && self.findServiceDataByCity() !== null;
  };

  /**
   * Визначає доступність сервісу взагалі та у вибраному місці
   * Повертає об'єкт типу:
   * {
   *   isRegion: false,    // сервіс доступний у якомусь із регіонів
   *   isCity: false,      // сервіс доступний у якомусь із міст
   *   thisRegion: false,  // доступний у вибраному регіоні
   *   thisCity: false    // ...і доступний у вибраному місті
   * }
   */
  self.getServiceAvailability = function() {
    var result = {
      isRegion: false,
      isCity: false,
      thisRegion: false,
      thisCity: false
    };
    var oService = ServiceService.oService;
    var oPlace = self.getPlaceData();

    angular.forEach(oService.aServiceData, function(srv) {
      // сервіс доступний у якомусь із регіонів
      if (srv.hasOwnProperty('nID_Region') && srv.nID_Region.nID !== null) {
        result.isRegion = true;
        // сервіс доступний у вибраному регіоні
        if (oPlace && oPlace.region && oPlace.region.nID === srv.nID_Region.nID) {
          result.thisRegion = true;
        }
      }
      // сервіс доступний у якомусь із міст
      if (srv.hasOwnProperty('nID_City') && srv.nID_City.nID !== null) {
        result.isCity = true;
        // ...і доступний у вибраному місті
        if (oPlace && oPlace.city && oPlace.city.nID === srv.nID_City.nID) {
          result.thisCity = true;
        }
      }
    });
    return result;
  };

  // self.serviceIsAvailableInPlace = function() {
  //   var oAvail = self.getServiceAvailability();
  //   return (oAvail.thisRegion || oAvail.thisCity);
  // };

  // TODO check it again and again
  self.getServiceDataForSelectedPlace = function() {
    var result = self.getServiceDataForCountry();

    if (self.serviceIsAvailableInRegion()) {
      result = self.findServiceDataByRegion();
    }
    if (self.serviceIsAvailableInCity()) {
      result = self.findServiceDataByCity();
    }
    return result;
  };

  self.getPlaceData();

});