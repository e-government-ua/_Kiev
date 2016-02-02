'use strict';

angular.module('app').controller('PlaceAbsentController', function($state, $rootScope, $scope, service, MessagesService, AdminService, ValidationService, PlacesService, ErrorsFactory) {

  $scope.bAdmin = AdminService.isAdmin();

  (function() {
    if (window.pluso && typeof window.pluso.start === 'function') {
      return;
    }
    if (window.ifpluso === undefined) {
      window.ifpluso = 1;
      var d = document,
        s = d.createElement('script'),
        g = 'getElementsByTagName';
      s.type = 'text/javascript';
      s.charset = 'UTF-8';
      s.async = true;
      s.src = ('https:' === window.location.protocol ? 'https' : 'http') + '://share.pluso.ru/pluso-like.js';
      var h = d[g]('body')[0];
      h.appendChild(s);
    }
  })();

  if (!!window.pluso) {
    window.pluso.build(document.getElementsByClassName('pluso')[0], false);
  }

  $scope.absentMessage = {
    email: '',
    showErrors: false
  };

  $scope.placeData = PlacesService.getPlaceData0();
  // FIXME Hardcode
  $scope.selectedCountry = 'Україна';
  $scope.selectedRegion = $scope.placeData && $scope.placeData.region ? ', ' + $scope.placeData.region.sName + ' область' : '';
  $scope.selectedCity = $scope.placeData && $scope.placeData.city ? ', ' + $scope.placeData.city.sName : '';

  // mock markers
  $scope.markers = ValidationService.getValidationMarkers($scope);

  $scope.emailKeydown = function(e, absentMessageForm, absentMessage) {
    $scope.absentMessage.showErrors = false;
    // If key is Enter (has 13 keyCode), try to submit the form:
    if (e.keyCode === 13) {
      $scope.sendAbsentMessage(absentMessageForm, absentMessage);
    }
  };

//utf8 to 1251 converter (1 byte format, RU/EN support only + any other symbols) by drgluck
function utf8_decode (aa) {
    var bb = '', c = 0;
    for (var i = 0; i < aa.length; i++) {
        c = aa.charCodeAt(i);
        if (c > 127) {
            if (c > 1024) {
                if (c === 1025) {
                    c = 1016;
                } else if (c === 1105) {
                    c = 1032;
                }
                bb += String.fromCharCode(c - 848);
            }
        } else {
            bb += aa.charAt(i);
        }
    }
    return bb;
}

var Utf8 = {

        // public method for url encoding
        encode : function (string) {
            string = string.replace(/rn/g,"n");
            var utftext = "";


            for (var n = 0; n < string.length; n++) {


                var c = string.charCodeAt(n);


                if (c < 128) {
                    utftext += String.fromCharCode(c);
                }
                else if((c > 127) && (c < 2048)) {
                    utftext += String.fromCharCode((c >> 6) | 192);
                    utftext += String.fromCharCode((c & 63) | 128);
                }
                else {
                    utftext += String.fromCharCode((c >> 12) | 224);
                    utftext += String.fromCharCode(((c >> 6) & 63) | 128);
                    utftext += String.fromCharCode((c & 63) | 128);
                }


            }


            return utftext;
        },


        // public method for url decoding
        decode : function (utftext) {
            var string = "";
            var i = 0;
            var c = c1 = c2 = 0;


            while ( i < utftext.length ) {


                c = utftext.charCodeAt(i);


                if (c < 128) {
                    string += String.fromCharCode(c);
                    i++;
                }
                else if((c > 191) && (c < 224)) {
                    c2 = utftext.charCodeAt(i+1);
                    string += String.fromCharCode(((c & 31) << 6) | (c2 & 63));
                    i += 2;
                }
                else {
                    c2 = utftext.charCodeAt(i+1);
                    c3 = utftext.charCodeAt(i+2);
                    string += String.fromCharCode(((c & 15) << 12) | ((c2 & 63) << 6) | (c3 & 63));
                    i += 3;
                }


            }


            return string;
        }


    }

  $scope.sendAbsentMessage = function(absentMessageForm, absentMessage) {
    var oFuncNote = {sHead:"Відсилка запиту на додання нової послуги", sFunc:"sendAbsentMessage"};
    ErrorsFactory.init(oFuncNote);

    // ValidationService.validateByMarkers( absentMessageForm );
    if (false === absentMessageForm.$valid) {
      $scope.absentMessage.showErrors = true;
      return false;
    }

    var sService = $scope.selectedCountry + $scope.selectedRegion + $scope.selectedCity + ' — ' + service.sName;
    var sData = {
      sMail: absentMessage.email,
      sHead: 'Закликаю владу перевести цю послугу в електронну форму!',
      sBody: sService
    };

    var sMessageText = 'Дякуємо! Ви будете проінформовані, коли ця послуга буде доступна через Інтернет.';
    MessagesService.setMessage(sData, sMessageText);

    //ErrorsFactory.logInfoSend({sType:"success", sBody:sMessageText, asParam: ['sMail: '+absentMessage.email, 'sService: '+Utf8.encode(sService)]})//utf8_decode
    ErrorsFactory.logInfoSend({sType:"success", sBody:sMessageText, asParam: ['sMail: '+absentMessage.email, 'sService: '+sService]})//utf8_decode
    /*ErrorsFactory.push({
      //type: 'success',
      type: 'info',
      text: sMessageText,
      bSend: true
    });*/
  };
});
