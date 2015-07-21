angular.module('app').controller('ServiceCityAbsentController', function($state,
                                                      $rootScope,
                                                      $scope,
                                                      service,
                                                      MessagesService,
                                                      AdminService) {
  $scope.service = service;
  $scope.bAdmin = AdminService.isAdmin();
  (function() {
    if (window.pluso)if (typeof window.pluso.start == "function") return;
    if (window.ifpluso == undefined) {
      window.ifpluso = 1;
      var d = document, s = d.createElement('script'), g = 'getElementsByTagName';
      s.type = 'text/javascript';
      s.charset = 'UTF-8';
      s.async = true;
      s.src = ('https:' == window.location.protocol ? 'https' : 'http') + '://share.pluso.ru/pluso-like.js';
      var h = d[g]('body')[0];
      h.appendChild(s);
    }
  })();

  if (!!window.pluso) {
    window.pluso.build(document.getElementsByClassName("pluso")[0], false);
  }

  $scope.absentMessage = {
    email: "",
    showErrors: false
  };

  $scope.emailKeydown = function() {
    $scope.absentMessage.showErrors = false;
  };

  $scope.sendAbsentMessage = function(absentMessageForm, absentMessage) {

    if (false === absentMessageForm.$valid) {
      console.log( 'city absentMessageForm', absentMessageForm );
      absentMessageForm.$setValidity('email', false);
      $scope.absentMessage.showErrors = true;
      return false;
    }

    // @todo Fix hardcoded city name, we should pass it into state
    var data = {
      sMail: absentMessage.email,
      sHead: "Закликаю владу перевести цю послугу в електронну форму!",
      sBody: $scope.$parent.$parent.data.city.sName + " - " + service.sName
    };
    MessagesService.setMessage(data, 'Дякуємо! Ви будете поінформовані, коли ця послуга буде доступна через Інтернет');
  }
});
