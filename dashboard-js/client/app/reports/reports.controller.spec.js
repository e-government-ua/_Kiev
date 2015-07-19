'use strict';

describe('Controller: ReportsCtrl', function () {

  // load the controller's module
  beforeEach(module('dashboardJsApp'));

  var ReportsCtrl, scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    ReportsCtrl = $controller('ReportsCtrl', {
      $scope: scope
    });
  }));

  it('should ...', function () {
    expect(1).toEqual(1);
  });
});
