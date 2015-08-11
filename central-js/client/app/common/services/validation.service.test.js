'use strict';

describe('ValidationService Tests', function() {

  var $rootScope, $compile, $window, $filter, moment, amTimeAgoConfig, originalTimeAgoConfig, angularMomentConfig,
    originalAngularMomentConfig, amMoment;

  // var mockScope =  {
  //   markers: {
  //     validate:{
  //         PhoneUA:{
  //             aField_ID:['privatePhone','workPhone', 'phone', 'tel']
  //         }, Mail:{
  //             aField_ID:['privateMail','email']
  //         }, AutoVIN:{
  //             aField_ID:['vin_code', 'vin_code1', 'vin']
  //         }, TextUA: { 
  //           aField_ID: ['bankIdaddress']
  //         }, TextRU: {
  //           aField_ID: []
  //         }, DateFormat: {
  //           aField_ID: ['bankIdaddress'],
  //           sFormat: 'YYYY-MM-DD' //
  //         }, DateElapsed: {
  //           aField_ID: ['dateOrder'],
  //           bFuture: false, //если true то дата должна быть в будущем
  //           bLess: true, //если true то 'дельта' между датами должна быть 'менее чем' (указана нижними параметрами)
  //           nDays: 3,
  //           nMounths: 0,
  //           nYears: 1
  //         }
  //     }
  //   }
  // };

  beforeEach(function(){
    module('app');
    module('angularMoment');
  });

  var validationService;
  // var angularMoment;

  beforeEach(inject(function ( $injector ) {
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
    (moment.locale || moment.lang)('en');
    // Add a sample timezones for tests
    // moment.tz.add('UTC|UTC|0|0|');
    // moment.tz.add('Pacific/Tahiti|LMT TAHT|9W.g a0|01|-2joe1.I');

    //console.log('angularMomentConfig = ', angularMomentConfig );
  }));

  // describe('ValidationService', function(){

    it('Should be defined: ValidationService and Moment', function() {
      // ValidationService.validateByMarkers( form, mockScope.markers );
      expect(validationService).toBeDefined();
    });

    it('Mail validation:', function() {

      var self = this;

      this.validatorByName = { 
        'Mail': 'email',
        'AutoVIN': 'autovin',
        'PhoneUA': 'tel',
        'TextUA': 'textua',
        'TextRU': 'textru',
        'DateFormat': 'dateformat',
        'DateElapsed': 'dateelapsed'
      };

      //field.$validators[fieldByName] = self.getValidatorByName(validatorName, field);

      function validate( validatorName, value, toBeOrNotToBe, options ) {
        var fValidator = validationService.getValidatorByName( validatorName );
        var fValidatorCall;
        if ( typeof fValidator === 'function' ) {
          fValidatorCall = fValidator.call( this, value, options );
          if ( toBeOrNotToBe !== undefined ) {
            expect( fValidatorCall ).toBe( toBeOrNotToBe );
          } else {
            expect( fValidatorCall ).toBeDefined();
          }
        }
      }

      function prevalidate( validatorName, value, toBeOrNotToBe, options ) {
        var fValidator = validationService.getValidatorByName( validatorName );
        var result = null;
        if ( typeof fValidator === 'function' ) {
          result = fValidator.call( self, value, options );
        }
        return result;
      }

      function validateArray( arrayToValidate, validatorName, toBeOrNotToBe, options ) {
        var cl = colr( toBeOrNotToBe, toBeOrNotToBe ? 'green' : 'red' );

        console.log( colr('\n=== Array Validation by ' + validatorName + ' validator' + ' to be ' + cl + ':', 'white' ) );

        for ( var value in arrayToValidate ) {
          validate( validatorName, arrayToValidate[value], toBeOrNotToBe, options );

          var isValid = prevalidate( validatorName, arrayToValidate[value], null, options );
          var sValid = isValid ? colr(  ' is valid.', 'green' ) : colr( ' is invalid', 'red' );

          console.log( '' + validatorName + ' ' + arrayToValidate[value] + sValid );
        }
      }

      // Validate Emails:

      var emailRight = [ 'hello@email.org', 'a12@email.org' ];
      var emailWrong = [ 'hello@email', '@email.org', 'asfasail.org', 'a', ''];

      // Validate Ukrainian Texts:

      var textUASamplesRight = [
        'Їжак чує грім', 
        'ААБВГҐДЕЄЖЗИІЇЙКЛМНОПРСТУФХЦЧШЩЬЮЯабвгґдеєжзиіїйклмнопрстуфхцчшщьюя'
      ];

      var textUASamplesWrong = [
        'Ёжик слышит гром', 
        'ЁёЪъЫыЭэ'
      ];

      // Validate Russian Texts:

      var textRUSamplesRight = [
        'Ёжик слышит гром', 
        'АаБбВвГгДдЕеЁёЖжЗзИиЙйКкЛлМмНнОоПпРрСсТтУуФфХхЦцЧчШшЩщЪъЫыЬьЭэЮюЯя'
      ];

      var textRUSamplesWrong = [
        'Їжак чує грім', 
        'ҐЄІЇґєії'
      ];

      // Validate Dates:

      var datesRight = [
        '10-10-2015', 
        '1-1-2015'
      ];

      var datesWrong = [
        '0-2015', 
        '-1-2015',
        'August 11 2015',
        '11 Aug 2015'
      ];

      // Validate Date Elapsed:

      var eDatesRight = [
        '08-08-2015', 
        '07-08-2015'
        // '15-08-2015'
      ];

      validateArray( emailRight, 'Mail', true );
      validateArray( emailWrong, 'Mail', false );
      validateArray( textUASamplesRight, 'TextUA', true );
      validateArray( textUASamplesWrong, 'TextUA', false );
      validateArray( textRUSamplesRight, 'TextRU', true );
      validateArray( textRUSamplesWrong, 'TextRU', false );
      
      validateArray( datesRight, 'DateFormat', true, { sFormat: 'DD-MM-YYYY' } );
      validateArray( datesWrong, 'DateFormat', false, { sFormat:'DD-MM-YYYY' } );

      // SAMPLE: validateArray( eDatesRight, 'DateElapsed', toBe: true, bFuture, bLess, days, months, years );
      

      //  function validate( validatorName, value, toBeOrNotToBe, options ) {
      //  Параметри:
      // *  bFuture: false  - якщо true, то дата modelValue має бути у майбутньому
      // *  bLess: true     - якщо true, то diff між modelValue та now  має бути 'менше ніж' вказано у нижніх параметрах:
      // *  nDays: 3
      // *  nMonths: 0
      // *  nYears: 1
      validate( 'DateElapsed', 
                '12-08-2015', 
                true, 
                { 
                  bFuture: false,
                  bLess: true,
                  nDays: 0,
                  nMonths: 0,
                  nYears: 0
                });
      
      prevalidate( 'DateElapsed', 
                '20-08-2015', 
                null, 
                { 
                  bFuture: false,
                  bLess: true, 
                  nDays: 0, 
                  nMonths: 0, 
                  nYears: 0
                });

      prevalidate( 'DateElapsed', 
                '20-08-2015', 
                null, 
                { 
                  bFuture: true,
                  bLess: false, 
                  nDays: 0, 
                  nMonths: 0, 
                  nYears: 0
                });

      // validate( 'DateElapsed', '17-08-2015', false, { bFuture: false, bLess: true, nDays: 0, nMonths: 0, nYears: 0 } );
      // validateArray( eDatesRight, 'DateElapsed', true, false, false, 1, 0, 0 );
      // validateArray( eDatesRight, 'DateElapsed', true, true, false, 1, 0, 0 );
      // validateArray( eDatesRight, 'DateElapsed', true, true, true, 1, 0, 0 );

      function colr ( msg, sColor ) {
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
        return pattrn.replace( '{TEXT}', msg );
      }

    });

  // });
});