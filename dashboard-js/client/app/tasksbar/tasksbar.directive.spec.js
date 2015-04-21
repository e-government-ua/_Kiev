'use strict';

describe('Directive: tasksbar', function () {

  // load the directive's module and view
  beforeEach(module('dashboardJsApp'));
  beforeEach(module('app/tasksbar/tasksbar.html'));

  var element, scope;

  beforeEach(inject(function ($rootScope) {
    scope = $rootScope.$new();
  }));

  it('should make hidden element visible', inject(function ($compile) {
    element = angular.element('<tasksbar></tasksbar>');
    element = $compile(element)(scope);
    scope.$apply();
    expect(element.text()).toBe('this is the tasksbar directive');
  }));
});