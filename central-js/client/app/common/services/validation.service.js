/**
 * Если в поле типа "markers" содержится объект, в элементах которого фигурирует ссылка на какие-то поля, как требующие валидации "по формату телефона" - проводить их валидацию после ввода содержимого этого поля, и отображать справа текст об ошибке формата, если ошибка будет.
 * Пример объекта:
 * markers: {
 *  "validate":{
 *    "PhoneUA":{
 *      "aField_ID":["privatePhone","workPhone"]
 *    }, "Mail":{
 *      "aField_ID":["privateMail"]
 *    }
 * }
 *
 * Где "privatePhone" и "workPhone" - это ИД-шники тех полей, которые нужно валидировать
 * См.: /i/issues/375
 */
angular.module('app').service('ValidationService', function () {

  'use strict';

  /**
   * Validate email if it can be found in markers list by name:
   */
  this.validateEmailByMarker = function( emailCtrl, markers ) {

    var EMAIL_REGEXP = /^[-a-z0-9~!$%^&*_=+}{\'?]+(\.[-a-z0-9~!$%^&*_=+}{\'?]+)*@([a-z0-9_][-a-z0-9_]*(\.[-a-z0-9_]+)*\.(aero|arpa|biz|com|coop|edu|gov|info|int|mil|museum|name|net|org|pro|travel|mobi|[a-z][a-z])|([0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}))(:[0-9]{1,5})?$/i;
    // alternative re: /^([\w-]+(?:.[\w-]+))@((?:[\w-]+.)\w[\w-]{0,66}).([a-z]{2,6}(?:.[a-z]{2})?)$/i;

    // return true if there's no markers set, so it won't prevent validation
    if ( !markers || !emailCtrl ) {
      return;
    }

    // markers are here, so we can check if field is marked by it's name: 
    if (_.indexOf( markers.validate.Mail.aField_ID, emailCtrl.$name ) === -1) {
      return;
    }

    // only apply the validator if ngModel (emailCtrl) is present and there's email validator
    if ( !emailCtrl || !emailCtrl.$validators ) {
      return;
    }

    // overwrite the default Angular email validator
    emailCtrl.$validators.email = function(modelValue) {
      return emailCtrl.$isEmpty(modelValue) || EMAIL_REGEXP.test(modelValue);
    };

    // and revalidate it
    emailCtrl.$validate();
  };

  this.validateTelephoneByMarker = function( telCtrl, markers ) {
    if ( !markers || !telCtrl ) {
      return;
    }    
    // validate it, the phone validator is set in the tel.js directive
    telCtrl.$validate();
  };

  /**
   * Validate email if it can be found in markers list by name:
   */
  
  this.validateAutoVIN = function( field, markers ) {

    // alternative re: /^([\w-]+(?:.[\w-]+))@((?:[\w-]+.)\w[\w-]{0,66}).([a-z]{2,6}(?:.[a-z]{2})?)$/i;

    // return true if there's no markers set, so it won't prevent validation
    if ( !markers || !field ) {
      return;
    }

    // markers are here, so we can check if field is marked by it's name: 
    if (_.indexOf( markers.validate.AutoVIN.aField_ID, field.$name ) === -1) {
      return;
    }

    // only apply the validator if ngModel (emailCtrl) is present and there's email validator
    if ( !field || !field.$validators ) {
      return;
    }

    // overwrite the default Angular email validator
    field.$validators.AutoVIN = function(sValue) {
        
      //Логика: набор из 17 символов.
      //Разрешено использовать все арабские цифры и латинские буквы (А В C D F Е G Н J К L N М Р R S Т V W U X Y Z),
      //за исключением букв Q, O, I. (Эти буквы запрещены для использования, поскольку O и Q похожи между собой, а I и O можно спутать с 0 и 1.)
      var bValid=true;  
      if(field.$isEmpty(sValue)){
          return true;
      }
      if(sValue==null){
          return false;
      }
      
      bValid = bValid && (sValue.length=17);
      bValid = bValid && (/^[a-zA-Z0-9]+$/.test(sValue));
      bValid = bValid && (sValue.indexOf("Q")<0 && sValue.indexOf("O")<0 && sValue.indexOf("I")<0);
      return bValid;
      
      //var oRegExp = /^[a-zA-Z0-9]+$/;
      //var sRegExp = /^[-a-z0-9~!$%^&*_=+}{\'?]+(\.[-a-z0-9~!$%^&*_=+}{\'?]+)*@([a-z0-9_][-a-z0-9_]*(\.[-a-z0-9_]+)*\.(aero|arpa|biz|com|coop|edu|gov|info|int|mil|museum|name|net|org|pro|travel|mobi|[a-z][a-z])|([0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}))(:[0-9]{1,5})?$/i;
      //var sRegExp = /^[-a-z0-9~!QOI?]+(\.[-a-z0-9~!$%^&*_=+}{\'?]+)*@([a-z0-9_][-a-z0-9_]*(\.[-a-z0-9_]+)*\.(aero|arpa|biz|com|coop|edu|gov|info|int|mil|museum|name|net|org|pro|travel|mobi|[a-z][a-z])|([0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}))(:[0-9]{1,5})?$/i;
      
      //oRegExp.test(modelValue);
      //oRegExp.test("ddg0dsfh");      
      //var sRegExp = /^[-a-z0-9?]/i;
      //bValid = bValid && (sRegExp.test(modelValue));
      //return field.$isEmpty(modelValue) || EMAIL_REGEXP.test(modelValue);
    };

    // and revalidate it
    field.$validate();
  };


});