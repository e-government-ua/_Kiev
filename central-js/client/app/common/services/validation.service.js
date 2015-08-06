/**
 * Если в поле типа 'markers' содержится объект, в элементах которого фигурирует ссылка на какие-то поля, как требующие валидации 'по формату телефона' - проводить их валидацию после ввода содержимого этого поля, и отображать справа текст об ошибке формата, если ошибка будет.
 * Пример объекта:
 * markers: {
 *  'validate':{
 *    'PhoneUA':{
 *      'aField_ID':['privatePhone','workPhone']
 *    }, 'Mail':{
 *      'aField_ID':['privateMail']
 *    }
 * }
 *
 * Где 'privatePhone' и 'workPhone' - это ИД-шники тех полей, которые нужно валидировать
 * См.: /i/issues/375
 */
angular.module('app').service('ValidationService', function () {

  'use strict';

  /**
   * Validate email if it can be found in the form by name given in the markers list:
   */
  this.validateEmailByMarker = function( form, $scope ) {
    var markers = $scope.markers;
    if (!markers) {
      return;
    }

    // markers are here, so we can check if field is marked by it's name: 
    var EMAIL_REGEXP = /^[-a-z0-9~!$%^&*_=+}{\'?]+(\.[-a-z0-9~!$%^&*_=+}{\'?]+)*@([a-z0-9_][-a-z0-9_]*(\.[-a-z0-9_]+)*\.(aero|arpa|biz|com|coop|edu|gov|info|int|mil|museum|name|net|org|pro|travel|mobi|[a-z][a-z])|([0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}))(:[0-9]{1,5})?$/i;

    angular.forEach( form, function ( field ) {
      if ( field && field.$name && _.indexOf( markers.validate.Mail.aField_ID, field.$name ) !== -1 ) {
        // overwrite the default Angular email validator
        field.$validators.email = function( modelValue ) {
          return field.$isEmpty(modelValue) || EMAIL_REGEXP.test(modelValue);
        };
        // and validate it
        field.$validate();
      }
    });
  };

  this.validatePhoneByMarker = function( form, $scope ) {
    var markers = $scope.markers;
    if (!markers) {
      return;
    }

    // markers are here, so we can check if field is marked by it's name: 
    angular.forEach( form, function ( field ) {
      if ( field && field.$name && _.indexOf( markers.validate.PhoneUA.aField_ID, field.$name ) !== -1 ) {
        // validate it, the phone validator is set in the tel.js directive
        field.$validate();
      }
    });
  };

  /**
   * Validate Auto VIN if it can be found in the form by name given in the markers list:
   */
  this.validateAutoVINByMarker = function( form, $scope ) {
    var markers = $scope.markers;
    if (!markers) {
      return;
    }

    angular.forEach( form, function ( field ) {
      if ( field && field.$name && _.indexOf( markers.validate.AutoVIN.aField_ID, field.$name ) !== -1 ) {

        field.$validators.autovin = function(sValue) {
          // Логика: набор из 17 символов.
          // Разрешено использовать все арабские цифры и латинские буквы (А В C D F Е G Н J К L N М Р R S Т V W U X Y Z),
          // За исключением букв Q, O, I. (Эти буквы запрещены для использования, поскольку O и Q похожи между собой, а I и O можно спутать с 0 и 1.)
          var bValid = true;

          if(field.$isEmpty(sValue)){
              return true;
          }

          bValid = bValid && (sValue !== null);
          bValid = bValid && (sValue.length === 17);
          bValid = bValid && (/^[a-zA-Z0-9]+$/.test(sValue));
          bValid = bValid && (sValue.indexOf('q') < 0 && sValue.indexOf('o') < 0 && sValue.indexOf('i') < 0);
          bValid = bValid && (sValue.indexOf('Q') < 0 && sValue.indexOf('O') < 0 && sValue.indexOf('I') < 0);

          return bValid;
        };

        // and revalidate it
        field.$validate();
      }
    });
  };

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