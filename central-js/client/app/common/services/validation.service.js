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
    if ( !markers ) {
      return;
    }

    // only apply the validator if ngModel (emailCtrl) is present and there's email validator
    if ( !emailCtrl || !emailCtrl.$validators.email ) {
      return;
    }

    // markers are here, so we can check if field is marked by it's name: 
    if (_.indexOf( markers.validate.Mail.aField_ID, emailCtrl.$name ) === -1) {
      return;
    }

    // overwrite the default Angular email validator
    emailCtrl.$validators.email = function(modelValue) {
      return emailCtrl.$isEmpty(modelValue) || EMAIL_REGEXP.test(modelValue);
    };

    // and revalidate it
    emailCtrl.$validate();
  };

});