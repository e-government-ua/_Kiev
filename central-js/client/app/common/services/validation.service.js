/**
 * Поле 'markers' має посиланнями на поля, що потребують валідації по певному формату.
 * Важливо: різні маркери можуть призначатися одним і тим же полям. 
 * Див. i/issues/375, 654
 *
 * Валідатори:
 *
 * 1) 'TextUA' - Усі українскі літери, без цифр, можливий мінус (дефіс) та пробіл
 * Текст помилки: 'Текст може містити тількі українські літери або мінус чи пробіл'
 *
 * 2) 'TextRU' - Усі російські літери, без цифр, можливий мінус (дефіс) та пробіл
 * Текст помилки: 'Текст може містити тількі російські літери або мінус чи пробіл'
 *
 * 3) 'DateFormat' - Дата у заданому форматі DATE_FORMAT
 * Текст помилки: 'Дата може бути тільки формату DATE_FORMAT'
 *
 * 4) 'DateElapsed' - З/до дати у полі з/після поточної, більше/менше днів/місяців/років
 * Текст помилки: 'З/до дати з/після сьогоднішньої, має бути більше/менше ніж х-днів, х-місяців, х-років.
 *
 * х-___        - підставляти тільки, якщо x не дорівнює 0
 * З/До         - в залежності від bFuture
 * більше/менше - в залежності від bLess
 *
 * Приклад об'єкту markers:
 *
 * var markers = {
 *  'validate': {
 *    'PhoneUA': {
 *      'aField_ID': ['privatePhone','workPhone']
 *    }, 'Mail': {
 *      'aField_ID': ['privateMail', 'someMail']
 *    }, 'TextUA': { 
 *      'aField_ID': ['fio']
 *    }, 'TextRU': {
 *      'aField_ID': ['fio_RU']
 *    }, 'DateFormat': {
 *      'aField_ID': ['fio'],
 *      'sFormat': 'YYYY-MM-DD' //
 *    }, 'DateElapsed': {
 *      'aField_ID': ['dateOrder'],
 *      'bFuture': false, //если true то дата должна быть в будущем
 *      'bLess': true, //если true то 'дельта' между датами должна быть 'менее чем' (указана нижними параметрами)
 *      'nDays': 3,
 *      'nMounths': 0,
 *      'nYears': 1
 *    }
 *  }
 * };
 *
 * Де 'privatePhone' і 'workPhone' - це назви полів, яку треба валідувати.
 */

'use strict';

angular.module('app').service( 'ValidationService', [ 'moment', 'angularMomentConfig', ValidationService ] );

// angularMomentConfig
function ValidationService ( moment ) {

  var self = this;

  self.validateByMarkers = function( form, $scope ) {

    var markers = $scope.markers;

    if (!markers || !markers.validate || markers.validate.length < 1) {
      return;
    }

    angular.forEach( markers.validate, function ( validator, validatorName ) {
      var fieldByName = self.validatorByName[validatorName];
      var marked = markers.validate[validatorName];
      function fieldByFieldId( field ) {
        return field && field.$name && _.indexOf( marked['aField_ID'], field.$name ) !== -1;
      }
      angular.forEach(form, function (formField) {
        if ( fieldByFieldId(formField) ) {
          var ffield = formField.$validators[fieldByName];
          // overwrite the default Angular field validator
          if( !ffield ) {
            ffield = self.getValidatorByName(validatorName, formField );

            // switch ( validatorName ) {

            //   case 'DateFormat': 
            //     // ffield.sFormat = marked['sFormat'];

            //     break;
            //   case 'DateElapsed':

            //     // ffield.bFuture = marked['bFuture']; // 'bFuture': false, //если true то дата должна быть в будущем
            //     // ffield.bLess = marked['bLess']; // : true, //если true то 'дельта' между датами должна быть 'менее чем' (указана нижними параметрами)
            //     // ffield.nDays = marked['nDays'];  // 3,
            //     // ffield.nMounths = marked['nMounths']; // : 0,
            //     // ffield.nYears = marked['nYears']; //: 1
            //     break;

            // }
            // and validate it
            formField.$validate();
          }
        }
      });
    });
    };

    self.validatorByName = { 
      'Mail': 'email',
      'AutoVIN': 'autovin',
      'PhoneUA': 'tel',
      'TextUA': 'textua',
      'TextRU': 'textru',
      'DateFormat': 'dateformat',
      'DateElapsed': 'dateelapsed'
    };

    /**
     * formField - додатковий параметр.
     * Повертає null для неіснуючого валідатора.
     */
    self.getValidatorByName = function( validatorName ) {

      // console.log( 'validationService.getValidatorByName: ', validatorName, ' = ', fValidator );
      var fValidator = self.validatorFunctionsByFieldId[validatorName];

      var fValidatorModule = function( modelValue, sFormat, a, b, c, d, e, f ) {
        var result = null;
        if ( fValidator ) {
          result = fValidator.call( self, modelValue, sFormat, a, b, c, d, e, f );
        }
        return result;
      };

      return fValidatorModule;
    };

    self.validatorFunctionsByFieldId = {
          
      'Mail': function( modelValue ) {
        var bValid = true;
        var EMAIL_REGEXP = /^[-a-z0-9~!$%^&*_=+}{\'?]+(\.[-a-z0-9~!$%^&*_=+}{\'?]+)*@([a-z0-9_][-a-z0-9_]*(\.[-a-z0-9_]+)*\.(aero|arpa|biz|com|coop|edu|gov|info|int|mil|museum|name|net|org|pro|travel|mobi|[a-z][a-z])|([0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}))(:[0-9]{1,5})?$/i;
        bValid = bValid && EMAIL_REGEXP.test( modelValue );
        // console.log('Validate Email: ' + modelValue + ' is valid email: ' + bValid );
        return bValid;
      },
      
      /**
       * 'AutoVIN' - Логика: набор из 17 символов.
       * Разрешено использовать все арабские цифры и латинские буквы (А В C D F Е G Н J К L N М Р R S Т V W U X Y Z),
       * За исключением букв Q, O, I. (Эти буквы запрещены для использования, поскольку O и Q похожи между собой, а I и O можно спутать с 0 и 1.)
       */
      'AutoVIN': function( sValue ) {
        
        var bValid = true;
        bValid = bValid && (sValue !== null);
        bValid = bValid && (sValue.length === 17);
        bValid = bValid && (/^[a-zA-Z0-9]+$/.test(sValue));
        bValid = bValid && (sValue.indexOf('q') < 0 && sValue.indexOf('o') < 0 && sValue.indexOf('i') < 0);
        bValid = bValid && (sValue.indexOf('Q') < 0 && sValue.indexOf('O') < 0 && sValue.indexOf('I') < 0);
        // console.log('Validate AutoVIN: ', sValue, ' is valid: ' + bValid );
        return bValid;
      },

      'PhoneUA': null,

      /**
       * 'TextUA' - Усі українскі літери, без цифр, можливий мінус (дефіс) та пробіл
       * Текст помилки: 'Текст може містити тількі українські літери або мінус чи пробіл'
       */
      'TextUA': function( modelValue ) {
        var TEXTUA_REGEXP = /[ААБВГҐДЕЄЖЗИІЇЙКЛМНОПРСТУФХЦЧШЩЬЮЯабвгґдеєжзиіїйклмнопрстуфхцчшщьюя]|-|\s+/g;
        var TEXTRU_ONLY = /[ЁёЪъЫыЭэ]+/g;
        var bValid = TEXTUA_REGEXP.test( modelValue ) && !TEXTRU_ONLY.test( modelValue );
        //console.log('Validate TextUA: ' + modelValue + ' is valid: ' + bValid );
        return bValid;
      },

      /**
       * 'TextRU' - Усі російські літери, без цифр, можливий мінус (дефіс) та пробіл
       * Текст помилки: 'Текст може містити тількі російські літери або мінус че пробіл'
       */
      'TextRU': function( modelValue ) {
        var TEXTRU_REGEXP = /[АаБбВвГгДдЕеЁёЖжЗзИиЙйКкЛлМмНнОоПпРрСсТтУуФфХхЦцЧчШшЩщЪъЫыЬьЭэЮюЯя]|-|\s+/g;
        var TEXTUA_ONLY = /[ҐЄІЇґєії]+/g;
        var bValid = TEXTRU_REGEXP.test( modelValue ) && !TEXTUA_ONLY.test( modelValue );
        // console.log('Validate TextRU: ' + modelValue + ' is valid: ' + bValid );
        return bValid;
      },

      /**
       * 'DateFormat' - Дата у заданому форматі DATE_FORMAT
       * Текст помилки: 'Дата може бути тільки формату DATE_FORMAT'
       * Для валідації формату використовується  moment.js
       */
      'DateFormat': function( modelValue, sFormat ) {
        var bValid = moment( modelValue, sFormat ).isValid();

        // console.log('Validate DateFomat: ' + modelValue + ' is valid Date: ' + bValid + ' in ' + sFormat, ' format' );
      
        return bValid;
      },

      /**
       * 'DateElapsed' - З/до дати у полі з/після поточної, більше/менше днів/місяців/років
       *
       *  Параметри:
       *  bFuture: false,  // якщо true, то дата modelValue має бути у майбутньому
       *  bLess: true,     // якщо true, то 'дельта' між modelValue та зараз має бути 'менше ніж' вказана нижніми параметрами
       *  nDays: 3,
       *  nMounths: 0,
       *  nYears: 1
       *       
       * Текст помилки: 'Від/до дати до/після сьогоднішньої має бути більше/менше ніж х-днів, х-місяців, х-років.
       * х-___        - підставляти тільки, якщо x не дорівнює 0
       * З/До         - в залежності від bFuture
       * більше/менше - в залежності від bLess
       */
      'DateElapsed': function( modelValue, bFuture, bLess, nDays, nMounths, nYears ) {
        var bValid = true;

        var fmt = 'DD-MM-YYYY';

        // FIXME Check Date Validity First
        
        var now = moment();
        var modelMoment = moment( modelValue, fmt );

        //var timeFromNow = now.from( modelMoment );
        var diffDays = now.diff( modelMoment, 'days' );
        var diffMonthes = now.diff( modelMoment, 'months' );
        var diffYears = now.diff( modelMoment, 'years' );

        var message = '{VID_DO} дати у полі {DO_PISLYA} поточної має бути {BIL_MEN} ніж {N_DAYS} днів, {N_MONTHES} місяців, {N_YEARS} років.';

        // VID_DO: Від/до
        var sVidDo = bFuture ? 'До' : 'Від';

        // DO_PISLYA: до/після
        var sDoPislya = bFuture ? 'після' : 'до';

        // BIL_MEN: більше/менше
        var sBilMen = bLess ? 'менше' : 'більше';

        if ( diffDays > nDays ){
          // diffMonthes > nMounths && diffYears > nYears;
          message = message
            .replace( '{VID_DO}', sVidDo )
            .replace( '{DO_PISLYA}', sDoPislya )
            .replace( '{BIL_MEN}', sBilMen )
            .replace( 'N_DAYS', nDays )
            .replace( 'N_MONTHES', nMounths )
            .replace( 'N_YEARS', nYears );
        } else {
          message = 'OK';
        }

        console.log('DateElapsed. Now: ' + now.format( fmt ) + ' vs. ' + modelMoment.format( fmt ) + ', diff D:' + 
          diffDays + '|' + nDays + ', M: ' + diffMonthes + '|' + nMounths + ', Y: ' + diffYears + '|' + nYears + ', message: \n' +
          message );

        return bValid;
      }
    };    

  /**
   * What is it? Check here: http://planetcalc.ru/2464/
   */
  this.getLunaValue = function ( id ) {

    // TODO: Fix Alhoritm Luna
    // Number 2187501 must give CRC=3
    // Check: http://planetcalc.ru/2464/
    // var inputNumber = 3;

    var n = parseInt(id);

    //var n = parseInt(2187501);

    var nFactor = 1;
    var nCRC = 0;
    var nAddend;

    while (n !== 0){
        nAddend = Math.round(nFactor * (n % 10));
        nFactor = (nFactor === 2) ? 1 : 2;
        nAddend = nAddend > 9 ? nAddend - 9 : nAddend;
        nCRC += nAddend;
        n = parseInt(n / 10);
    }

    nCRC = nCRC%10;

    // alert(nCRC%10);
    // nCRC=Math.round(nCRC/10)
    // alert(nCRC%10);
    // alert(nCRC);

    return nCRC;

  };

}