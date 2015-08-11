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
      // expect(validationService.validateByMarkers).toBeDefined();
      //expect(moment).toBeDefined();
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

      function validate( validatorName, value, toBeOrNotToBe, format, a, b, c, d, e, f ) {
        var fValidator = validationService.getValidatorByName( validatorName );
        if ( typeof fValidator === 'function' ) {
          if ( toBeOrNotToBe !== undefined ) {
            expect( fValidator.call( this, value, format, a, b, c, d, e, f )).toBe( toBeOrNotToBe );
          } else {
            expect( fValidator.call( this, value, format, a, b, c, d, e, f )).toBeDefined();
          }
        }
      }

      function prevalidate( validatorName, value, format, a, b, c, d, e, f ) {
        var fValidator = validationService.getValidatorByName( validatorName );
        var result = null;
        if ( typeof fValidator === 'function' ) {
          result = fValidator.call( self, value, format, a, b, c, d, e, f );
        }
        return result;
      }

      function validateArray( arrayToValidate, validatorName, toBeOrNotToBe, format, a, b, c, d, e, f ) {
        console.log( colorlog('\n=== Array Validation by ' + validatorName + ' validator' + ' to be ' + toBeOrNotToBe + ':', 'green' ) );
        for ( var value in arrayToValidate ) {
          validate( validatorName, arrayToValidate[value], toBeOrNotToBe, format, a, b, c, d, e, f );
          console.log(' ' + validatorName + ', ' + arrayToValidate[value] + ' is valid: ' + prevalidate( validatorName, arrayToValidate[value], format, a, b, c, d, e, f ) );
        }
      }

      // Validate Emails:

      var emailRight = [ 'hello@email.org', 'a12@email.org' ];
      var emailWrong = [ 'hello@email', '@email.org', 'asfasail.org', 'a', ''];

      validateArray( emailRight, 'Mail', true );
      validateArray( emailWrong, 'Mail', false );

      // Validate Ukrainian Texts:

      var textUASamplesRight = [
        'Їжак чує грім', 
        'ААБВГҐДЕЄЖЗИІЇЙКЛМНОПРСТУФХЦЧШЩЬЮЯабвгґдеєжзиіїйклмнопрстуфхцчшщьюя'
      ];

      var textUASamplesWrong = [
        'Ёжик слышит гром', 
        'ЁёЪъЫыЭэ'
      ];

      validateArray( textUASamplesRight, 'TextUA', true );
      validateArray( textUASamplesWrong, 'TextUA', false );

      // Validate Russian Texts:

      var textRUSamplesRight = [
        'Ёжик слышит гром', 
        'АаБбВвГгДдЕеЁёЖжЗзИиЙйКкЛлМмНнОоПпРрСсТтУуФфХхЦцЧчШшЩщЪъЫыЬьЭэЮюЯя'
      ];

      var textRUSamplesWrong = [
        'Їжак чує грім', 
        'ҐЄІЇґєії'
      ];

      validateArray( textRUSamplesRight, 'TextRU', true );
      validateArray( textRUSamplesWrong, 'TextRU', false );

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

      validateArray( datesRight, 'DateFormat', true, 'DD-MM-YYYY' );
      validateArray( datesWrong, 'DateFormat', false, 'DD-MM-YYYY' );

      // Validate Date Elapsed:

      var eDatesRight = [
        '08-08-2015', 
        '12-08-2015',
        '15-08-2015'
      ];

      function colorlog ( msg, sColor, toLog ) {
        var color = sColor || 'red';
        var colors = {
          red: '\u001b[31;1m\u001b {TEXT} \u001b[0m',
          blue: '\u001b[34;1m\u001b {TEXT} \u001b[0m',
          purple: '\u001b[35;1m\u001b {TEXT} \u001b[0m',
          green: '\u001b[32;1m\u001b {TEXT} \u001b[0m'
        };
        msg = colors[color].replace( '{TEXT}', msg );
        if ( toLog ) {
          console.log( msg );
        }
        return msg;
      }


      // validateArray( eDatesRight, 'DateElapsed', toBe: true, bFuture, bLess, days, months, years );
      
      validateArray( eDatesRight, 'DateElapsed', true, false, true, 0, 0, 0 );
      // validateArray( eDatesRight, 'DateElapsed', true, false, false, 1, 0, 0 );
      // validateArray( eDatesRight, 'DateElapsed', true, true, false, 1, 0, 0 );
      // validateArray( eDatesRight, 'DateElapsed', true, true, true, 1, 0, 0 );
     
    });

  // });
});