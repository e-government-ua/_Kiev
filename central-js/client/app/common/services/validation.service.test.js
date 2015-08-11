'use strict';

describe('ValidationService Tests', function() {

  var validationService;
  var moment;

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
  });

  beforeEach(inject(function (_ValidationService_) {
    validationService = _ValidationService_;
    //console.log('ARGS: ', arguments );
    //moment = _moment_;
  }));

  // describe('ValidationService', function(){

    it('Should be defined: ValidationService and Moment', function() {
      // ValidationService.validateByMarkers( form, mockScope.markers );
      expect(validationService).toBeDefined();
      // expect(validationService.validateByMarkers).toBeDefined();
      //expect(moment).toBeDefined();
    });

    return;

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
        console.log('\n=== Array Validation by ' + validatorName + ' validator' + ' to be ' + toBeOrNotToBe + ':' );
        for ( var value in arrayToValidate ) {
          validate( validatorName, arrayToValidate[value], toBeOrNotToBe, format, a, b, c, d, e, f );
          console.log(' ' + validatorName + ', ' + arrayToValidate[value] + ' is valid: ' + prevalidate( validatorName, arrayToValidate[value], format, a, b, c, d, e, f ) );
        }
      }

      // Validate Emails:

      var emailRight = [ 'hello@email.org', 'a12@email.org' ];
      var emailWrong = [ 'hello@email', '@email.org', 'asfasail.org', 'a', ''];

      // validateArray( emailRight, 'Mail', true );
      // validateArray( emailWrong, 'Mail', false );

      // Validate Ukrainian Texts:

      var textUASamplesRight = [
        'Їжак чує грім', 
        'ААБВГҐДЕЄЖЗИІЇЙКЛМНОПРСТУФХЦЧШЩЬЮЯабвгґдеєжзиіїйклмнопрстуфхцчшщьюя'
      ];

      var textUASamplesWrong = [
        'Ёжик слышит гром', 
        'ЁёЪъЫыЭэ'
      ];

      // validateArray( textUASamplesRight, 'TextUA', true );
      // validateArray( textUASamplesWrong, 'TextUA', false );

      // Validate Russian Texts:

      var textRUSamplesRight = [
        'Ёжик слышит гром', 
        'АаБбВвГгДдЕеЁёЖжЗзИиЙйКкЛлМмНнОоПпРрСсТтУуФфХхЦцЧчШшЩщЪъЫыЬьЭэЮюЯя'
      ];

      var textRUSamplesWrong = [
        'Їжак чує грім', 
        'ҐЄІЇґєії'
      ];

      // validateArray( textRUSamplesRight, 'TextRU', true );
      // validateArray( textRUSamplesWrong, 'TextRU', false );

      // Validate Dates:

      var datesRight = [
        '10-10-2015', 
        '1-1-2015'
      ];

      var datesWrong = [
        '0-2015', 
        '-1-2015'
      ];

      validateArray( datesRight, 'DateFormat', true, 'DD-MM-YYYY' );
      // FIXME 
      validateArray( datesWrong, 'DateFormat', false, 'DD-MM-YYYY' );

      // Validate Date Elapsed:

      var eDatesRight = [
        '10-10-2015', 
        '1-1-2015'
      ];

      var eDatesWrong = [
        '0-0-2015', 
        '-1-2015'
      ];

      validateArray( eDatesRight, 'DateElapsed', true, false, true, 3, 0, 1 );
      // FIXME validateArray( eDatesWrong, 'DateElapsed', true, false, true, 3, 0, 1 );
     
    });

  // });
});