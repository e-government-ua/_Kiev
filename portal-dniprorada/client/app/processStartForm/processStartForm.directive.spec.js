'use strict';

describe('Directive: processStartForm', function () {

  // load the directive's module and view
  beforeEach(module('portalDniproradaApp'));
  beforeEach(module('app/processStartForm/processStartForm.html'));

  var element, scope;

  beforeEach(inject(function ($rootScope) {
    scope = $rootScope.$new();
  }));

  it('should make hidden element visible', inject(function ($compile) {
    element = angular.element('<process-start-form></process-start-form>');
    element = $compile(element)(scope);
    scope.$apply();
    expect(element.text()).toBe('this is the processStartForm directive');
  }));
});