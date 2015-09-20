'use strict';

describe('Controller: EscalationsCtrl', function () {

  // load the controller's module
  beforeEach(module('dashboardJsApp'));

  var EscalationsCtrl, scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    EscalationsCtrl = $controller('EscalationsCtrl', {
      $scope: scope
    });
  }));
});
