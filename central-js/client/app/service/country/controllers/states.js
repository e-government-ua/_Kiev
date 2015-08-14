'use strict';
angular.module('app').controller('ServiceCountryController', function($state, $rootScope, $scope, $sce, service, AdminService) {


  $scope.service = service;

  $scope.bAdmin = AdminService.isAdmin();

  $scope.data = {
    region: null,
    city: null
  };

  $scope.step1 = function() {
    var aServiceData = $scope.service.aServiceData;
    var serviceType = {nID: 0};
    angular.forEach(aServiceData, function(value, key) {
      serviceType = value.nID_ServiceType;
      $scope.serviceData = value;
      $scope.serviceData.sNote = $sce.trustAsHtml($scope.serviceData.sNote);
    });

    switch (serviceType.nID) {
      case 1:
        return $state.go('index.service.general.country.link', {id: $scope.service.nID}, {location: false});
      case 4:
        return $state.go('index.service.general.country.built-in', {id: $scope.service.nID}, {location: false});
      default:
        return $state.go('index.service.general.country.error', {id: $scope.service.nID}, {location: false});
    }
  };

  if ($state.current.name === 'service.general.country.built-in.bankid') {
    return true;
  }

  $scope.step1();
});

angular.module('app').controller('ServiceCountryAbsentController', function($state,
                                                                            $rootScope,
                                                                            $scope,
                                                                            service,
                                                                            MessagesService,
                                                                            ValidationService) {
  $scope.service = service;
  $scope.hiddenCtrls = true;
  (function() {
    if (window.pluso && typeof window.pluso.start === 'function') { return; }
    if (window.ifpluso === undefined) {
      window.ifpluso = 1;
      var d = document, s = d.createElement('script'), g = 'getElementsByTagName';
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

  $scope.emailKeydown = function( e, absentMessageForm, absentMessage ) {
    $scope.absentMessage.showErrors = false;
    // If key is Enter (has 13 keyCode), try to submit the form:
    if ( e.keyCode === 13 ) {
      $scope.sendAbsentMessage( absentMessageForm, absentMessage );
    }    
  };

  $scope.sendAbsentMessage = function(absentMessageForm, absentMessage) {

    // TODO Test it here
    // ValidationService.validateByMarkers( absentMessageForm );

    if (false === absentMessageForm.$valid) {
      console.log( 'states absentMessageForm', absentMessageForm );
      $scope.absentMessage.showErrors = true;
      return false;
    }

    // @todo Fix hardcoded city name, we should pass it into state
    var data = {
      sMail: absentMessage.email,
      sHead: 'Закликаю владу перевести цю послугу в електронну форму!',
      sBody: 'Україна - ' + service.sName
    };
    MessagesService.setMessage(data, 'Дякуємо! Ви будете поінформовані, коли ця послуга буде доступна через Інтернет');
  };
});
