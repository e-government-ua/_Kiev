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
 * Текст помилки: 'Текст може містити тількі російські літери або мінус че пробіл'
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
 *
 */
angular.module('app').service('ValidationService', function () {
  'use strict';

  this.validateByMarkers = function( form, $scope ) {
    var markers = $scope.markers;
    if ( !markers || !markers.validate || markers.validate.length < 1 ) {
      return;
    }
    // 'PhoneUA'
    // function validate( ctrlName ) {
    //   var vals = {};
    //   var ctrl = markers.validate[ctrlName];
    //   if ( ctrl ) {
    //     vals['aID_Field' + ctrlName] = ctrl['aField_ID'];
    //     console.log('ctrlName = ', ctrlName, 'ctrl = ', ctrl, vals['aID_Field' + ctrlName] );
    //   }
    //   return vals['aID_Field' + ctrlName];
    // }
    
    // var aID_FieldPhoneUA = getValidate('PhoneUA');
    // var aID_FieldMail = getValidate('Mail');
    // var aID_FieldAutoVIN = getValidate('AutoVIN');
    // var aID_FieldTextUA = getValidate('TextUA');
    // var aID_FieldTextRU = getValidate('TextRU');
    // var aID_FieldDateFormat = getValidate('DateFormat');
    // var aID_FieldDateElapsed = getValidate('DateElapsed');

    // var aID_FieldPhoneUA = markers.validate.PhoneUA.aField_ID;
    // var aID_FieldMail = markers.validate.Mail.aField_ID;
    // var aID_FieldAutoVIN = markers.validate.AutoVIN.aField_ID;
    // var aID_FieldTextUA = markers.validate.TextUA.aField_ID;
    // var aID_FieldTextRU = markers.validate.TextRU.aField_ID;
    // var aID_FieldDateFormat = markers.validate.DateFormat.aField_ID;
    // var aID_FieldDateElapsed = markers.validate.DateElapsed.aField_ID;

    var oValidators = {};

    angular.forEach( markers.validate, function ( validator, validatorName ) {

      // var aFieldsToValidate = validator['aField_ID'];

      console.log( 'validator: ', validatorName, ' = ', validator );

      // EMAIL
      // markers are here, so we can check if field is marked by it's name: 
      var validatorByName = { 
        'Mail': 'email'
      };

      function validateByName( validatorName, field ) {
        var validatorFunctionByName = {
          
          'Mail': function( modelValue ) {
            var EMAIL_REGEXP = /^[-a-z0-9~!$%^&*_=+}{\'?]+(\.[-a-z0-9~!$%^&*_=+}{\'?]+)*@([a-z0-9_][-a-z0-9_]*(\.[-a-z0-9_]+)*\.(aero|arpa|biz|com|coop|edu|gov|info|int|mil|museum|name|net|org|pro|travel|mobi|[a-z][a-z])|([0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}))(:[0-9]{1,5})?$/i;
            return field.$isEmpty(modelValue) || EMAIL_REGEXP.test(modelValue);
          },
          
          'AutoVIN': function(sValue) {
            // Логика: набор из 17 символов.
            // Разрешено использовать все арабские цифры и латинские буквы (А В C D F Е G Н J К L N М Р R S Т V W U X Y Z),
            // За исключением букв Q, O, I. (Эти буквы запрещены для использования, поскольку O и Q похожи между собой, а I и O можно спутать с 0 и 1.)
            var bValid = true;

            if( field.$isEmpty( sValue ) ) {
                return true;
            }

            bValid = bValid && (sValue !== null);
            bValid = bValid && (sValue.length === 17);
            bValid = bValid && (/^[a-zA-Z0-9]+$/.test(sValue));
            bValid = bValid && (sValue.indexOf('q') < 0 && sValue.indexOf('o') < 0 && sValue.indexOf('i') < 0);
            bValid = bValid && (sValue.indexOf('Q') < 0 && sValue.indexOf('O') < 0 && sValue.indexOf('I') < 0);

            return bValid;
          },

          'PhoneUA': function() {
            //field.$validate();
              return null;
            }
          };

        console.log('validatorFunctionByName = ', validatorName, validatorFunctionByName[validatorName]);
        return validatorFunctionByName[validatorName] ? validatorFunctionByName[validatorName] : null;
      }
      
      angular.forEach( form, function ( field ) {
        if ( field && field.$name && _.indexOf( markers.validate[validatorName]['aField_ID'], field.$name ) !== -1 ) {
          
          console.log( '\tfield:', field.$name );
          
          // overwrite the default Angular field validator
          if( !field.$validators[validatorByName[validatorName]] ) {

            var validatorFunction = validateByName(validatorName, field);
          
            field.$validators[validatorByName[validatorName]] = validatorFunction;
          
            // and validate it
            // field.$validate();
          }

        }
      });

      oValidators[validatorName] = validator;

    });

    // console.log(oValidators);

 /* - Усі українскі літери, без цифр, можливий мінус (дефіс) та пробіл
 * Текст помилки: 'Текст може містити тількі українські літери або мінус чи пробіл'
 */
  // validate('TextUA');
/**
 * 2) 'TextRU' - Усі російські літери, без цифр, можливий мінус (дефіс) та пробіл
 * Текст помилки: 'Текст може містити тількі російські літери або мінус че пробіл'
 */
 // validate('TextRU');
/*
 * 3) 'DateFormat' - Дата у заданому форматі DATE_FORMAT
 * Текст помилки: 'Дата може бути тільки формату DATE_FORMAT'
 */
 // validate('DateFormat');
/**
 * 4) 'DateElapsed' - З/до дати у полі з/після поточної, більше/менше днів/місяців/років
 * Текст помилки: 'З/до дати з/після сьогоднішньої, має бути більше/менше ніж х-днів, х-місяців, х-років.
 *
 * х-___        - підставляти тільки, якщо x не дорівнює 0
 * З/До         - в залежності від bFuture
 * більше/менше - в залежності від bLess
 */
 // validate('DateElapsed');

  };


  /**
   * Validate email if it can be found in the form by name given in the markers list:
   */
  // this.validateEmailByMarker = function( form, $scope ) {
  //   var markers = $scope.markers;
  //   if (!markers) {
  //     return;
  //   }

  //   // markers are here, so we can check if field is marked by it's name: 
  //   var EMAIL_REGEXP = /^[-a-z0-9~!$%^&*_=+}{\'?]+(\.[-a-z0-9~!$%^&*_=+}{\'?]+)*@([a-z0-9_][-a-z0-9_]*(\.[-a-z0-9_]+)*\.(aero|arpa|biz|com|coop|edu|gov|info|int|mil|museum|name|net|org|pro|travel|mobi|[a-z][a-z])|([0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}))(:[0-9]{1,5})?$/i;

  //   angular.forEach( form, function ( field ) {
  //     if ( field && field.$name && _.indexOf( markers.validate.Mail.aField_ID, field.$name ) !== -1 ) {
  //       // overwrite the default Angular email validator
  //       field.$validators.email = function( modelValue ) {
  //         return field.$isEmpty(modelValue) || EMAIL_REGEXP.test(modelValue);
  //       };
  //       // and validate it
  //       field.$validate();
  //     }
  //   });
  // };

  // this.validatePhoneByMarker = function( form, $scope ) {
  //   var markers = $scope.markers;
  //   if (!markers) {
  //     return;
  //   }

  //   // markers are here, so we can check if field is marked by it's name: 
  //   angular.forEach( form, function ( field ) {
  //     if ( field && field.$name && _.indexOf( markers.validate.PhoneUA.aField_ID, field.$name ) !== -1 ) {
  //       // validate it, the phone validator is set in the tel.js directive
  //       field.$validate();
  //     }
  //   });
  // };

  /**
   * Validate Auto VIN if it can be found in the form by name given in the markers list:
   */
  // this.validateAutoVINByMarker = function( form, $scope ) {
  //   var markers = $scope.markers;
  //   if (!markers) {
  //     return;
  //   }

  //   angular.forEach( form, function ( field ) {
  //     if ( field && field.$name && _.indexOf( markers.validate.AutoVIN.aField_ID, field.$name ) !== -1 ) {

  //       field.$validators.autovin = function(sValue) {
  //         // Логика: набор из 17 символов.
  //         // Разрешено использовать все арабские цифры и латинские буквы (А В C D F Е G Н J К L N М Р R S Т V W U X Y Z),
  //         // За исключением букв Q, O, I. (Эти буквы запрещены для использования, поскольку O и Q похожи между собой, а I и O можно спутать с 0 и 1.)
  //         var bValid = true;

  //         if(field.$isEmpty(sValue)){
  //             return true;
  //         }

  //         bValid = bValid && (sValue !== null);
  //         bValid = bValid && (sValue.length === 17);
  //         bValid = bValid && (/^[a-zA-Z0-9]+$/.test(sValue));
  //         bValid = bValid && (sValue.indexOf('q') < 0 && sValue.indexOf('o') < 0 && sValue.indexOf('i') < 0);
  //         bValid = bValid && (sValue.indexOf('Q') < 0 && sValue.indexOf('O') < 0 && sValue.indexOf('I') < 0);

  //         return bValid;
  //       };

  //       // and revalidate it
  //       field.$validate();
  //     }
  //   });
  // };

  /**
   * What is it? Check here: http://planetcalc.ru/2464/
   */
  this.getLunaValue = function ( id ) {
    // private static int sumDigitsByLuna(Long inputNumber) {
    //     int factor = 1;
    //     int sum = 0;
    //     int addend;
    //     while (inputNumber != 0){
    //         addend = (int) (factor * (inputNumber % 10));
    //         factor = (factor == 2) ? 1 : 2;
    //         addend = addend > 9 ? addend - 9 : addend;
    //         sum += addend;
    //         inputNumber /= 10;
    //     }
    //     return sum;
    // }
                
    //TODO: Fix Alhoritm Luna
    //Number 2187501 must give CRC=3
    //Check: http://planetcalc.ru/2464/
    //var inputNumber = 3;

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

});