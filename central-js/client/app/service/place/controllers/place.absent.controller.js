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

  // mock markers
  $scope.markers = ValidationService.getValidationMarkers($scope);

  $scope.emailKeydown = function(e, absentMessageForm, absentMessage) {
    $scope.absentMessage.showErrors = false;
    // If key is Enter (has 13 keyCode), try to submit the form:
    if (e.keyCode === 13) {
      $scope.sendAbsentMessage(absentMessageForm, absentMessage);
    }
  };

  $scope.sendAbsentMessage = function(absentMessageForm, absentMessage) {

    // ValidationService.validateByMarkers( absentMessageForm );

    if (false === absentMessageForm.$valid) {
      $scope.absentMessage.showErrors = true;
      return false;
    }

    var placeData = PlacesService.getPlaceData();
    var selectedRegion = placeData && placeData.region ? placeData.region.sName + ' - ' : '';
    var selectedCity = placeData && placeData.city ? placeData.city.sName + ' - ' : '';

    var data = {
      sMail: absentMessage.email,
      sHead: 'Закликаю владу перевести цю послугу в електронну форму!',
      sBody: selectedRegion + selectedCity + service.sName
    };

    var messageText = 'Дякуємо! Ви будете поінформовані, коли ця послуга буде доступна через Інтернет.';
    
    MessagesService.setMessage(data, messageText);

    ErrorsFactory.push({type: 'success', text: messageText});
  };

});