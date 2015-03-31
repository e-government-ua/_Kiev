'use strict';

describe('Controller: ProcessFormCtrl', function () {

  // load the controller's module
  beforeEach(module('ui.bootstrap.demo'));
  beforeEach(module('portalDniproradaApp'));
  

  var DatepickerDemoCtrl;
  var ProcessFormCtrl, scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    ProcessFormCtrl = $controller('ProcessFormCtrl', {
      $scope: scope
    });
    DatepickerDemoCtrl = $controller('DatepickerDemoCtrl', {
      $scope: scope
    });
  }));

  it('should ...', function () {
    expect(1).toEqual(1);
  });
});
