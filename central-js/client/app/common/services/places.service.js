angular.module('app').service('PlacesService', function($http) {

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
  var mockError = {
    'sSubjectOperatorName': 'Обласна державна адміністрація',
    'subjectOperatorName': 'Обласна державна адміністрація',
    'nID': 628,
    'sName': 'Надання одноразової матеріальної допомоги громадянам',
    'nOrder': 21,
    'aServiceData': [{
      'bTest': true,
      'sNote': '',
      'asAuth': 'BankID,EDS',
      'test': true,
      'note': '',
      'nID': 74,
      'nID_City': {
        'sID_UA': '1211000000',
        'nID': 2,
        'sName': 'Кривий Ріг',
        'nID_Region': {
          'sID_UA': '1200000000',
          'nID': 1,
          'sName': 'Дніпропетровська'
        }
      },
      'nID_ServiceType': {
        'nID': 1,
        'sName': 'Внешняя',
        'sNote': 'Пользователь переходит по ссылке на услугу, реализованную на сторонней платформе'
      },
      'oSubject_Operator': {
        'oSubject': {
          'sID': 'ПАО',
          'sLabel': 'ПАО ПриватБанк',
          'sLabelShort': 'ПриватБанк',
          'nID': 1
        },
        'sOKPO': '093205',
        'sFormPrivacy': 'ПАО',
        'sNameFull': 'Банк ПриватБанк',
        'nID': 1,
        'sName': 'ПриватБанк'
      },
      'oData': '{}',
      'sURL': 'https://dniprorada.igov.org.ua',
      'bHidden': false
    }, {
      'bTest': false,
      'sNote': 'Ви можете отримати послугу на порталі адміністративних послуг м.Луцьк',
      'asAuth': 'BankID,EDS',
      'test': false,
      'note': 'Ви можете отримати послугу на порталі адміністративних послуг м.Луцьк',
      'nID': 140,
      'nID_City': {
        'sID_UA': '710100000',
        'nID': 9,
        'sName': 'Луцьк',
        'nID_Region': {
          'sID_UA': '700000000',
          'nID': 8,
          'sName': 'Волинська'
        }
      },
      'nID_ServiceType': {
        'nID': 1,
        'sName': 'Внешняя',
        'sNote': 'Пользователь переходит по ссылке на услугу, реализованную на сторонней платформе'
      },
      'oSubject_Operator': {
        'oSubject': {
          'sID': 'ПАО',
          'sLabel': 'ПАО ПриватБанк',
          'sLabelShort': 'ПриватБанк',
          'nID': 1
        },
        'sOKPO': '093205',
        'sFormPrivacy': 'ПАО',
        'sNameFull': 'Банк ПриватБанк',
        'nID': 1,
        'sName': 'ПриватБанк'
      },
      'oData': '{}',
      'sURL': 'http://www.ap.lutsk.ua/uk/info/service/2505/details',
      'bHidden': false
    }, {
      'bTest': true,
      'sNote': '',
      'asAuth': 'BankID,EDS',
      'test': true,
      'note': '',
      'nID': 112,
      'nID_City': {
        'sID_UA': '6110100000',
        'nID': 10,
        'sName': 'Тернопіль',
        'nID_Region': {
          'sID_UA': '6100000000',
          'nID': 9,
          'sName': 'Тернопільська'
        }
      },
      'nID_ServiceType': {
        'nID': 4,
        'sName': 'Встроенная',
        'sNote': 'Все этапы услуги реализованы на платформе Портала'
      },
      'oSubject_Operator': {
        'oSubject': {
          'sID': 'ПАО',
          'sLabel': 'ПАО ПриватБанк',
          'sLabelShort': 'ПриватБанк',
          'nID': 1
        },
        'sOKPO': '093205',
        'sFormPrivacy': 'ПАО',
        'sNameFull': 'Банк ПриватБанк',
        'nID': 1,
        'sName': 'ПриватБанк'
      },
      'oData': '{\'sPath\':\'service/form/form-data\',\'oParams\':{\'processDefinitionId\':\'ternopol_2_long2:1:1\'}}',
      'sURL': 'https://test.region.igov.org.ua/wf-region/',
      'bHidden': false
    }],
    'sInfo': '',
    'sFAQ': '',
    'sLaw': '',
    'nSub': 0,
    'nID_Status': 1,
    'nStatus': 0
  };

  // FIXME зберігати placeData у localStorage і відновлювати для юзера
  this.placeData = null;

  this.saveLocal = function(placeData) {
    localStorage.setItem('igPlaceData', JSON.stringify(placeData));
  }

  this.setPlace = function(placeData) {
    this.placeData = placeData;
    this.saveLocal(placeData);
    console.log('set place data:', JSON.stringify(placeData));
  }

  this.getPlace = function() {
    this.placeData = JSON.parse(localStorage.getItem('igPlaceData')) || this.placeData;
    console.log('get place data:', this.placeData);
    return this.placeData;
  }

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