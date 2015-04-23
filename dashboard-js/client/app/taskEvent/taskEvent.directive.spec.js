'use strict';

describe('Directive: taskEvent', function () {

  // load the directive's module and view
  beforeEach(module('dashboardJsApp'));
  beforeEach(module('app/taskEvent/taskEvent.html'));

  var element, scope;

  beforeEach(inject(function ($rootScope) {
    scope = $rootScope.$new();
  }));

  it('should make hidden element visible', inject(function ($compile) {
    element = angular.element('<task-event></task-event>');
    element = $compile(element)(scope);
    scope.$apply();
    expect(element.text()).toBe('this is the taskEvent directive');
  }));
});