angular.module('app').directive('validateEmail', function() {

  'use strict';

  var EMAIL_REGEXP = /^([\w-]+(?:.[\w-]+))@((?:[\w-]+.)\w[\w-]{0,66}).([a-z]{2,6}(?:.[a-z]{2})?)$/i;

  return {
    require: 'ngModel',
    restrict: '',
    link: function(scope, elm, attrs, ctrl) {

      //mock markers
      scope.data.formData = {
          'params':{
            'markers':{
              'validate':{
                  'PhoneUA':{
                      'aField_ID':['privatePhone','workPhone', 'phone1']
                  }, 'Mail':{
                      'aField_ID':['privateMail','email1', 'validateMe']
                  }            
              }
          }
        }
      };

      var validateMailIds = scope.data.formData.params.markers.validate.Mail.aField_ID;

      // only apply the validator if ngModel is present and Angular has added the email validator

      if (ctrl && ctrl.$validators.email ) {
        var id = attrs.id;
        var marker = attrs.marker;
        var emailIsMarkedForValidation = validateMailIds.indexOf( id ) !== -1 || validateMailIds.indexOf( marker ) !== -1;

        if ( !emailIsMarkedForValidation ) {
          return;
        }

        // this will overwrite the default Angular email validator
        ctrl.$validators.email = function(modelValue) {
          return ctrl.$isEmpty(modelValue) || EMAIL_REGEXP.test(modelValue);
        };
      }
    }
  };
});