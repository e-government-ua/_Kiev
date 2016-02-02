/*
  PlacesService - cервіс для вибору регіона та міста.

  Метод getPlaceData надає інформацію про вибраний користувачем регіон та / або місто
  Метод setPlaceData дозволяє контроллерам зберігати цю інформацию в сервісі.
  Може зберігати вибране міце у localStorage і читати його звідти ж.
  Сервіс ініціюється з даних URL, якщо там вказано область / місто (TODO: перевірити це).
  Користувачами сервіса є директиви і контроллери (див. place.js) для вибору місця.

  Обговорення: https://github.com/e-government-ua/i/issues/550#issuecomment-128641486
*/
angular.module('app').service('PlacesService', function($http, $state, ServiceService) {

  var self = this;

  self.rememberMyData = false;

  // Зберігаємо savedPlaceData у сервісі (і localStorage)
  var savedPlaceData = {};

    // UTF-8 encode / decode by Johan Sundstr?m
  self.encode_utf8 = function ( s ) {
      return unescape( encodeURIComponent( s ) );
    }

  self.decode_utf8 = function ( s ) {
      return decodeURIComponent( escape( s ) );
    }
    
  self.saveLocal = function(oSavedPlaceData) {
    if (self.rememberMyData) {
      localStorage.setItem('igSavedPlaceData', JSON.stringify(oSavedPlaceData));
      localStorage.setItem('igSavedPlaceData0', self.encode_utf8(JSON.stringify(oSavedPlaceData)));
    }
  };

  self.setPlaceData = function(oSavedPlaceData) {
    savedPlaceData = oSavedPlaceData;
    self.saveLocal(savedPlaceData);
    // console.log('set place data:', JSON.stringify(savedPlaceData));
  };

  self.resetPlaceData = function(oSavedPlaceData) {
    self.setPlaceData({
      region: null,
      city: null
    });
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
  self.getPlaceData0 = function() {
    var localData = JSON.parse(self.decode_utf8(localStorage.getItem('igSavedPlaceData0')));
    if (self.rememberMyData && localData) {
      savedPlaceData = localData;
    }
    // console.log('get place data:', JSON.stringify(savedPlaceData));
    return savedPlaceData;
  };

  self.colorizeRegionsForService = function(regions, service) {
    var aServiceData = service.aServiceData;
    angular.forEach(regions, function(region) {
      var color = 'red';
      angular.forEach(aServiceData, function(oServiceData) {
        if (oServiceData.hasOwnProperty('nID_Region')) {
          if (oServiceData.nID_Region.nID === region.nID) {
            color = 'green';
          }
          return;
        }
        if (oServiceData.hasOwnProperty('nID_City')) {
          if (oServiceData.nID_City.nID_Region.nID === region.nID) {
            color = 'green';
          }
          return;
        }
      });
      region.color = color;
    });
    return regions;
  };

  self.colorizeCitiesForService = function(cities, service) {
    var aServiceData = service.aServiceData;
    angular.forEach(cities, function(oCity) {
      var color = 'red';
      angular.forEach(aServiceData, function(oServiceData) {
        if (oServiceData.hasOwnProperty('nID_City') === false) {
          return;
        }
        if (oServiceData.nID_City.nID === oCity.nID) {
          color = 'green';
        }
      });
      oCity.color = color;
    });
    return cities;
  };

  self.getRegionsForService = function(service) {
    return $http.get('./api/places/regions').then(function(response) {
      return self.colorizeRegionsForService(response.data, service);
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
    return savedPlaceData && (savedPlaceData.region ? true : false);
  };

  self.cityIsChosen = function() {
    return savedPlaceData && (savedPlaceData.city ? true : false);
  };

  self.findServiceDataByCountry = function() {
    var aServiceData = ServiceService.oService.aServiceData;
    var result = null;
    angular.forEach(aServiceData, function(oService, key) {
      if (!oService.nID_Region && !oService.nID_City && oService.nID_ServiceType && oService.nID_ServiceType.nID) {
        result = oService;
      }
    });
    return result;
  };

  self.findServiceDataByRegion = function() {
    var aServiceData = ServiceService.oService.aServiceData;
    var result = null;
    angular.forEach(aServiceData, function(oService, key) {
      // if service is available in region
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

  /**
   * Визначає доступність сервісу взагалі та у вибраному місці
   * Повертає об'єкт типу:
   * {
   *   thisCountry: false,    // сервіс доступний у країні
   *   someRegion: false,    // сервіс доступний у якомусь із регіонів
   *   someCity: false,      // сервіс доступний у якомусь із міст
   *   thisRegion: false,  // доступний у вибраному регіоні
   *   thisCity: false    // ...і доступний у вибраному місті
   * }
   */
  self.serviceAvailableIn = function() {
    var result = {
      thisCountry: false,
      someRegion: false,
      someCity: false,
      thisRegion: false,
      thisCity: false,
      someCityInThisRegion: false,
      thisCityInThisRegion: false
    };
    var oService = ServiceService.oService;
    var oPlace = self.getPlaceData();

    angular.forEach(oService.aServiceData, function(srv) {
      // сервіс доступний у країні
      if (self.findServiceDataByCountry() !== null) {
        result.thisCountry = true;
      }

      // сервіс доступний у якомусь із регіонів
      if (srv.hasOwnProperty('nID_Region') && srv.nID_Region.nID !== null) {
        result.someRegion = true;
        // сервіс доступний у вибраному регіоні
        if (oPlace && oPlace.region && oPlace.region.nID === srv.nID_Region.nID) {
          // сервіс доступний у якомусь із міст вибраного регіону
          result.thisRegion = true;
        }
      }

      // сервіс доступний у якомусь із міст
      if (srv.hasOwnProperty('nID_City') && srv.nID_City.nID !== null){
        result.someCity = true;
        // ...і доступний у вибраному місті
        if (oPlace && oPlace.city && oPlace.city.nID === srv.nID_City.nID) {
          result.thisCity = true;
        }

        if(oPlace && oPlace.region && oPlace.region.nID === srv.nID_City.nID_Region.nID) {
          result.someCityInThisRegion = true;

          // ...і у вибраному місті, що знаходиться у вибраній області
          if (oPlace && oPlace.city && oPlace.city.nID === srv.nID_City.nID) {
            result.thisCityInThisRegion = true;
          }
        }
      }
    });
    return result;
  };

  self.getServiceStateForPlace = function() {
    var serviceType = {
      nID_ServiceType: {
        nID: 0
      }
    };

    serviceType = self.findServiceDataByCountry() || serviceType;

    if (self.regionIsChosen() && self.findServiceDataByRegion() !== null) {
      // serviceType = self.findServiceDataByRegion() || serviceType;
      serviceType = self.findServiceDataByRegion();
    }
    if (self.cityIsChosen() && self.findServiceDataByCity() !== null) {
      // serviceType = self.findServiceDataByCity() || serviceType;
      serviceType = self.findServiceDataByCity();
    }

    var stateByServiceType = {
      // Сервіс за посиланням
      1: 'index.service.general.place.link',
      // Вбудований сервіс
      4: 'index.service.general.place.built-in',
      // Помилка - сервіс відсутній
      0: 'index.service.general.place.error'
    };

    return stateByServiceType[serviceType.nID_ServiceType.nID];
  };

  // TODO
  self.getClassByState = function($state) {
    // var curState = statesMap[$state.current.name];
    // var statesMap = {
    //   'index.service.general.place.built-in': {
    //     viewClass: 'state-disabled'
    //   },
    //   'index.service.general.place.built-in.bankid.submitted': {
    //     viewClass: 'state-collapsed'
    //   }
    // };
    // return curState && curState.viewClass || '';
    return '';
  };

  self.getPlaceData();
});
