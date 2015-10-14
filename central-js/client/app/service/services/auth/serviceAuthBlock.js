angular.module('app').directive('serviceAuthBlock', function() {
  return {
    restrict: 'E',
    templateUrl: 'app/service/auth/serviceAuthBlock.html',
    link: function(scope, element, attrs) {

      scope.authMethods = false;

      if (scope.serviceData) {
        if (scope.serviceData.asAuth && scope.serviceData.asAuth.length > 0) {
          if (typeof(scope.serviceData.asAuth) === 'string') {
            scope.authMethods = scope.serviceData.asAuth.split(',');
          } else {
            scope.authMethods = scope.serviceData.asAuth;
          }
        }
      }

      scope.redirectUri = attrs.redirectUri;
      scope.nStep = attrs.nStep;
    }
  };
});