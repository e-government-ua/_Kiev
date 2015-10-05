'use strict';

describe('ValidationService Tests', function() {

  // set it to true to enable pre-validation and tracing messages: 
  var isDebugMode = false;

  var $rootScope, $compile, $window, $filter, moment, amTimeAgoConfig, originalTimeAgoConfig, angularMomentConfig,
    originalAngularMomentConfig, amMoment;

  beforeEach(function() {
    module('app');
    module('angularMoment');
  });

  var validationService;
  // var angularMoment;

  beforeEach(inject(function($injector) {
    validationService = $injector.get('ValidationService');

    $rootScope = $injector.get('$rootScope');
    $compile = $injector.get('$compile');
    $window = $injector.get('$window');
    $filter = $injector.get('$filter');
    moment = $injector.get('moment');
    amMoment = $injector.get('amMoment');
    amTimeAgoConfig = $injector.get('amTimeAgoConfig');
    angularMomentConfig = $injector.get('angularMomentConfig');
    originalTimeAgoConfig = angular.copy(amTimeAgoConfig);
    originalAngularMomentConfig = angular.copy(angularMomentConfig);

    // Ensure the locale of moment.js is set to en by default
    // (moment.locale || moment.lang)('uk');
    // Add a sample timezones for tests
    // moment.tz.add('UTC|UTC|0|0|');
    // moment.tz.add('Pacific/Tahiti|LMT TAHT|9W.g a0|01|-2joe1.I');
  }));

  it('Should be defined: ValidationService and Moment', function() {
    expect(validationService).toBeDefined();
  });

  it('Mail validation:', function() {

    var self = this;

    this.validatorByName = {
      'Mail': 'email'
      ,'AutoVIN': 'autovin'
      ,'PhoneUA': 'tel'
      ,'TextUA': 'textua'
      ,'TextRU': 'textru'
      ,'DateFormat': 'dateformat'
      ,'DateElapsed': 'dateelapsed'
      ,'CodeKVED': 'CodeKVED'
      ,'CodeEDRPOU': 'CodeEDRPOU'
      ,'CodeMFO': 'CodeMFO'
    };

    function doValidate(validatorName, value, toBeOrNotToBe, options) {
      var bDebug = options ? options.bDebug : false;

      // запам'ятовуємо опції маркера - це важливо для того, щоб передати параметри, такі як, sFormat, bFuture, bLess, nDays ітн.

      var fValidator = validationService.getValidatorByName(validatorName, angular.copy(options));
      var fValidatorCall;
      if (typeof fValidator === 'function') {
        if (isDebugMode) {
          if (bDebug) {
            logHeader('DEBUG - THIS - DEBUG - THIS - DEBUG - THIS - DEBUG - THIS - DEBUG - THIS', 3);
          }
          var isValid = prevalidate(validatorName, value, toBeOrNotToBe, options);
          var sValid = isValid ? colr(' is valid', 'green') : colr(' is invalid', 'red');
          var sExpectedValid = toBeOrNotToBe ? colr('to be valid.', 'green') : colr('to be invalid', 'red');
          var validationFail = isValid === toBeOrNotToBe ? colr(' - validator OK', 'green') : colr(' - validator FAILED, FAILED, FAILED!', 'red');
          //, (options ? options : ''),
          console.log('' + validatorName + ', value of ' + value + sValid + ' - ' + sExpectedValid + validationFail);
          if (bDebug) {
            logHeader('DEBUG END - DEBUG END - DEBUG END - DEBUG END - DEBUG END', 3);
          }
        } else {
          fValidatorCall = fValidator.call(this, value, value, options);
          expect(fValidatorCall).toBe(toBeOrNotToBe);
        }
      }
    }

    function prevalidate(validatorName, value, toBeOrNotToBe, options) {
      var fValidator = validationService.getValidatorByName(validatorName, angular.copy(options));
      var result = null;
      if (typeof fValidator === 'function') {
        result = fValidator.call(self, value, value, options);
      }
      return result;
    }

    function validateArray(arrayToValidate, validatorName, toBeOrNotToBe, options) {
      var cl = colr(toBeOrNotToBe, toBeOrNotToBe ? 'green' : 'red');

      console.log(colr('\n=== Array Validation by ' + validatorName + ' validator' + ' to be ' + cl + ':', 'white'));

      for (var value in arrayToValidate) {

        doValidate(validatorName, arrayToValidate[value], toBeOrNotToBe, options);

      }
    }

    // Validate Emails:

    var emailRight = ['hello@email.org', 'a12@email.org'];
    var emailWrong = ['hello@email', '@email.org', 'asfasail.org', 'a', ''];

    // Validate Ukrainian Texts:

    var textUASamplesRight = [
      'Їжак чує грім',
      'Їжак чує - там грім',
      'ААБВГҐДЕЄЖЗИІЇЙКЛМНОПРСТУФХЦЧШЩЬЮЯабвгґдеєжзиіїйклмнопрстуфхцчшщьюя'
    ];

    var textUASamplesWrong = [
      'Ёжик слышит гром',
      'ЁёЪъЫыЭэ',
      'P.S. Сергій уже йде.', // english - не можна
      'Їжак то не Ёжик'
    ];

    // Validate Russian Texts:

    var textRUSamplesRight = [
      'Ёжик слышит гром - но не боится',
      'АаБбВвГгДдЕеЁёЖжЗзИиЙйКкЛлМмНнОоПпРрСсТтУуФфХхЦцЧчШшЩщЪъЫыЬьЭэЮюЯя'
    ];

    var textRUSamplesWrong = [
      'Ёжик Їжак',
      'ЄЁ',
      'Ёжик - hog',
      'Їжак чує грім',
      'ҐЄІЇґєії'
    ];

    // Validate Dates:
    var datesRight = [
      '2015-08-12',
      '2015-01-01',
      '1902-08-12',
      '1902-08-11'
    ];

    var datesWrong = [
      '2015-8-12',
      '2015-01-1',
      '0-2015',
      '-1-2015',
      'August 11 2015',
      '11 Aug 2015',
      '1902-0-1',
      '1902-1-0'
    ];

    // Validate Date Elapsed:

    validateArray( emailRight, 'Mail', true );
    validateArray( emailWrong, 'Mail', false );
    validateArray(textUASamplesRight, 'TextUA', true);
    validateArray(textUASamplesWrong, 'TextUA', false);
    validateArray(textRUSamplesRight, 'TextRU', true);
    validateArray(textRUSamplesWrong, 'TextRU', false);

    validateArray(datesRight, 'DateFormat', true, {
      sFormat: 'YYYY-MM-DD'
    });
    validateArray(datesWrong, 'DateFormat', false, {
      sFormat: 'YYYY-MM-DD'
    });

    //  function doValidate( validatorName, value, toBeOrNotToBe, options ) {
    //  Параметри:
    // *  bFuture: false  - якщо true, то дата modelValue має бути у майбутньому
    // *  bLess: true     - якщо true, то diff між modelValue та now  має бути 'менше ніж' вказано у нижніх параметрах:
    // *  nDays: 3
    // *  nMonths: 0
    // *  nYears: 1
    var now = moment();
    var oneDayAfter = moment().add(1, 'd');
    var weekAfter = moment().add(1, 'w');
    var monthAfter = moment().add(1, 'M');
    var yearAfter = moment().add(1, 'y');

    var oneDayBefore = moment().subtract(1, 'd');
    var weekBefore = moment().subtract(1, 'w');
    var monthBefore = moment().subtract(1, 'M');
    var yearBefore = moment().subtract(1, 'y');

    function momentToString(mValue) {
      return mValue.format('YYYY-MM-DD');
    }

    logHeader('BFUTURE === TRUE, NO BLESS');

    // Дата має бути у майбутньому, а ця - рік тому: 
    doValidate('DateElapsed', momentToString(yearBefore), false, {
      bFuture: true
    });

    // Дата має бути у майбутньому, а ця - учора: 
    doValidate('DateElapsed', momentToString(oneDayBefore), false, {
      bFuture: true
    });

    // Дата має бути у майбутньому, а ця - сьогодні: 
    doValidate('DateElapsed', momentToString(now), false, {
      bFuture: true
    });

    doValidate('DateElapsed', momentToString(oneDayAfter.add(1, 'd')), true, {
      sDebug: 'Дата має бути у майбутньому, і ця - післязавтра: ',
      bFuture: true
    });

    // Дата має бути у майбутньому, і ця - на місяць уперед: 
    doValidate('DateElapsed', momentToString(monthAfter), true, {
      bFuture: true
    });

    logHeader('BLESS === TRUE');

    // bLess:

    // Дата має бути у майбутньому, різниця - менш ніж 1 день.
    // monthAfter - на місяць уперед - ПРАВИЛЬНА 
    doValidate('DateElapsed', momentToString(monthAfter), false, {
      bFuture: true,
      bLess: true,
      nDays: 1,
      nMonths: 0,
      nYears: 0
    });

    // Дата має бути у майбутньому, різниця - менш ніж 1 місяць.
    // Дана weekAfter - на тиждень уперед - ПРАВИЛЬНА
    doValidate('DateElapsed', momentToString(weekAfter), true, {
      bFuture: true,
      bLess: true,
      nDays: 0,
      nMonths: 1,
      nYears: 0
    });

    // Дата має бути у майбутньому, різниця - менш ніж 60 днів.
    // Дана monthAfter - на місяць уперед: - ПРАВИЛЬНА
    doValidate('DateElapsed', momentToString(monthAfter), true, {
      bFuture: true,
      bLess: true,
      nDays: 60,
      nMonths: 0,
      nYears: 0
    });

    logHeader('BLESS === FALSE'); // --------------------------

    // Дата має бути у майбутньому (bFuture: true), різниця - більше, ніж 1 день (bLess: false).
    // monthAfter - на місяць уперед - ПРАВИЛЬНА:
    doValidate('DateElapsed', momentToString(monthAfter), true, {
      bFuture: true,
      bLess: false,
      nDays: 1,
      nMonths: 0,
      nYears: 0
    });

    logHeader('BFUTURE = FALSE, NO BLESS');

    // минуле:

    doValidate('DateElapsed', momentToString(yearBefore), true, {
      sDebug: 'Дата має бути у минулому, і ця - рік тому',
      bDebug: true,
      bFuture: false
    });

    // Дата має бути у минулому, і ця - учора: 
    doValidate('DateElapsed', momentToString(oneDayBefore), true, {
      bFuture: false
    });

    // Дата має бути у минулому, а ця - сьогодні: 
    doValidate('DateElapsed', momentToString(now), true, {
      bFuture: false
    });

    // Дата має бути у минулому, а ця - післязавтра: 
    doValidate('DateElapsed', momentToString(oneDayAfter.add(1, 'd')), false, {
      bFuture: false
    });

    // Дата має бути у минулому, а ця - на місяць уперед: 
    doValidate('DateElapsed', momentToString(monthAfter), false, {
      bFuture: false
    });

    logHeader('BFUTURE === FALSE, BLESS = TRUE');

    // Дата має бути у минулому, різниця - менш ніж 1 день.
    // Дана monthAfter - на місяць уперед, ПОМИЛКОВА:
    doValidate('DateElapsed', momentToString(monthAfter), false, {
      bFuture: false,
      bLess: true,
      nDays: 1,
      nMonths: 0,
      nYears: 0
    });

    // Дата має бути у минулому, різниця - менш ніж 1 місяць.
    // Дана weekAfter - на тиждень уперед: - ПОМИЛКОВА
    doValidate('DateElapsed', momentToString(weekAfter), false, {
      bFuture: false,
      bLess: true,
      nDays: 0,
      nMonths: 1,
      nYears: 0
    });

    // Дата має бути у минулому, різниця - менш ніж 60 днів.
    // Дана monthAfter - на місяць уперед: - ПОМИЛКОВА
    doValidate('DateElapsed', momentToString(monthAfter), false, {
      bFuture: false,
      bLess: true,
      nDays: 60,
      nMonths: 0,
      nYears: 0
    });

    logHeader('BFUTURE === FALSE, BLESS === FALSE'); // --------------------------

    // Дата має бути у минулому (bFuture: false), різниця - більше, ніж 1 день (bLess: false)
    // monthAfter - на місяць уперед - ПОМИЛКОВА:
    doValidate('DateElapsed', momentToString(monthAfter), false, {
      bFuture: false,
      bLess: false,
      nDays: 1,
      nMonths: 0,
      nYears: 0
    });

    // Дата має бути у минулому (bFuture: false), різниця - більше, ніж 1 день (bLess: false)
    // monthBefore - на місяць назад - ПРАВИЛЬНА:
    doValidate('DateElapsed', momentToString(monthBefore), true, {
      bFuture: false,
      bLess: false,
      nDays: 1,
      nMonths: 0,
      nYears: 0
    });

    doValidate('DateElapsed', momentToString(weekAfter.add(1, 'd')), true, {
      // sDebug: 'Уведіть дату у майбутньому, більш ніж на тиждень пізнішу за сьогодні.',
      // bDebug: true,
      bFuture: true,
      bLess: false,
      nDays: 7,
      nMonths: 0,
      nYears: 0
    });

    // TO ERROR:
    // Use case: "Уведіть дату у майбутньому, більш ніж на тиждень пізнішу за сьогодні":
    doValidate('DateElapsed', momentToString(weekBefore), false, {
      bFuture: true,
      bLess: false,
      nDays: 7,
      nMonths: 0,
      nYears: 0
    });

    // TO ERROR:
    // Use case: "Уведіть дату у минулому, більш ніж на тиждень ранішу за сьогодні":
    doValidate('DateElapsed', momentToString(oneDayAfter), false, {
      bFuture: false,
      bLess: false,
      nDays: 7,
      nMonths: 0,
      nYears: 0
    });

    // TO ERROR:
    // Use case: "Уведіть дату у минулому, менш ніж на рік ранішу від поточної":
    doValidate('DateElapsed', momentToString(yearBefore), false, {
      bFuture: false,
      bLess: true,
      nDays: 0,
      nMonths: 10,
      nYears: 0
    });

    // TO ERROR:
    doValidate('DateElapsed', momentToString(weekAfter), false, {
      sDebug: 'Уведіть заплановану дату шлюбу" (не раніше, ніж за місяць від сьогодні)',
      bFuture: true,
      bLess: false,
      nDays: 0,
      nMonths: 1,
      nYears: 0
    });

    // TO ERROR:
    doValidate('DateElapsed', momentToString(yearAfter), false, {
      sDebug: 'Уведіть заплановану дату візиту" (не пізніше, ніж за півроку від сьогодні)',
      bFuture: true,
      bLess: true,
      nDays: 2,
      nMonths: 6,
      nYears: 0
    });


    // Перевірити, як утворюється множина (pluralization) 
    console.log ( colr( validationService.pluralize( 0, 'days' ), 'green' )); 
    console.log ( colr( validationService.pluralize( 1, 'days' ), 'green' )); // день
    console.log ( colr( validationService.pluralize( 4, 'days' ), 'green' )); // дні
    console.log ( colr( validationService.pluralize( 5, 'days' ), 'green' )); // днів
    console.log ( colr( validationService.pluralize( 0, 'months' ), 'green' )); 
    console.log ( colr( validationService.pluralize( 1, 'months' ), 'green' )); // день
    console.log ( colr( validationService.pluralize( 4, 'months' ), 'green' )); // дні
    console.log ( colr( validationService.pluralize( 5, 'months' ), 'green' )); // днів
    console.log ( colr( validationService.pluralize( 0, 'years' ), 'green' )); 
    console.log ( colr( validationService.pluralize( 1, 'years' ), 'green' )); // день
    console.log ( colr( validationService.pluralize( 4, 'years' ), 'green' )); // дні
    console.log ( colr( validationService.pluralize( 5, 'years' ), 'green' )); // днів

    console.log ( colr( validationService.fromDateToDate( '12-08-2015', '12-08-2015' ), 'green' )); 
    console.log ( colr( validationService.fromDateToDate( '12-08-2015', '13-08-2015' ), 'green' )); 
    console.log ( colr( validationService.fromDateToDate( '12-08-2015', '14-08-2015' ), 'green' )); 
    console.log ( colr( validationService.fromDateToDate( '12-08-2015', '15-08-2015' ), 'green' )); 
    console.log ( colr( validationService.fromDateToDate( '12-08-2015', '16-08-2015' ), 'green' )); 
    console.log ( colr( validationService.fromDateToDate( '12-08-2015', '17-08-2015' ), 'green' )); 
    console.log ( colr( validationService.fromDateToDate( '12-08-2015', '18-08-2015' ), 'green' )); 
    console.log ( colr( validationService.fromDateToDate( '12-08-2015', '18-09-2015' ), 'green' )); 
    console.log ( colr( validationService.fromDateToDate( '12-08-2015', '18-09-2016' ), 'green' )); 
    console.log ( colr( validationService.fromDateToDate( '12-08-2015', '18-09-2026' ), 'green' ));

    // Допоміжня функція - розфарбовує консоль
    function colr(msg, sColor) {
      var colrs = {
        reset: '\u001B[0m',
        black: '\u001B[30m',
        red: '\u001B[31m', //\u001b[31;1m
        green: '\u001B[32m',
        yellow: '\u001B[33m',
        blue: '\u001B[34m',
        purple: '\u001B[35m',
        cyan: '\u001B[36m',
        white: '\u001B[37m'
      };
      var pattrn = colrs[sColor] + '\u001b{TEXT}\u001b[0m';
      return pattrn.replace('{TEXT}', msg);
    }

    // Допоміжня функція - виводить заголовок:
    function logHeader(str, level) {
      var l = str.length;
      var maxLength = 150;
      var spaceLength = Math.floor((maxLength - l) / 2);
      var spacer = '-';
      str = ' ' + str + ' ';
      for (var i = 0; i < spaceLength - 1; i += spacer.length) {
        str = spacer + str + spacer;
      }
      console.log(colr(str, 'white'));
    }

  });

});