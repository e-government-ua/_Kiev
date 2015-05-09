'use strict';
describe('Controller: MainCtrl', function() {
  var scope;

  beforeEach(module('dashboardJsApp'));
  beforeEach(inject(function($controller, $rootScope) {
    scope = $rootScope.$new();
    $controller('MainCtrl', {
      $scope: scope
    });
  }));
});
