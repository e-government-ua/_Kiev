'use strict';

describe('Controller: ProcessFormCtrl', function () {

  // load the controller's module
  beforeEach(module('portalDniproradaApp'));

  var ProcessFormCtrl, scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    ProcessFormCtrl = $controller('ProcessFormCtrl', {
      $scope: scope
    });
  }));

  it('should ...', function () {
    expect(1).toEqual(1);
  });
});
