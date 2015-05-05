'use strict';
describe('Controller: TasksCtrl', function() {
  var scope, tasks, processes, taskDeferred, processesDeferred;

  beforeEach(module('dashboardJsApp'));
  beforeEach(inject(function($controller, $rootScope, $q) {
    scope = $rootScope.$new();
    mockTasksService($q);
    mockProcessesService($q);
    createController($controller);
  }));

  it('provides selected task', function() {
    scope.selectedTask = {id: '666'};
    expect(scope.isTaskSelected({id: '666'})).toBeTruthy();
    expect(scope.isTaskSelected({id: 666})).toBeFalsy();
  });

  it('provides tasks menu', function() {
    thenSelfAssignedMenuProvided();
    thenUnassignedMenuProvided();
    thenFinishedMenuProvided();
  });

  it('loads task counters', function() {
    scope.init();
    thenLoadCounts();
  });

  it('parses task counters', function() {
    whenCountsReceived();
    thenCountsParsed();
  });

  function thenLoadCounts () {
    expect(tasks.list.calls.count()).toBe(4);
    expect(tasks.list.calls.argsFor(0)[0]).toBe('selfAssigned');
    expect(tasks.list.calls.argsFor(1)[0]).toBe('unassigned');
    expect(tasks.list.calls.argsFor(2)[0]).toBe('finished');
  }

  function whenCountsReceived () {
    scope.init();
    taskDeferred.resolve('{"data":[{"id":"1"}, {"id":"2"}]}');
    scope.$apply();
  }

  function thenCountsParsed () {
    expect(scope.menus[0].count).toBe(2);
    expect(scope.menus[1].count).toBe(2);
    expect(scope.menus[2].count).toBe(2);
  }

  function thenSelfAssignedMenuProvided () {
    expect(scope.menus[0].title).toBe('В роботі');
    expect(scope.menus[0].type).toBe('selfAssigned');
    expect(scope.menus[0].count).toBe(0);
  }

  function thenUnassignedMenuProvided () {
    expect(scope.menus[1].title).toBe('Необроблені');
    expect(scope.menus[1].type).toBe('unassigned');
    expect(scope.menus[1].count).toBe(0);
  }

  function thenFinishedMenuProvided () {
    expect(scope.menus[2].title).toBe('Оброблені');
    expect(scope.menus[2].type).toBe('finished');
    expect(scope.menus[2].count).toBe(0);
  }

  function mockTasksService ($q) {
    taskDeferred = $q.defer();
    tasks = jasmine.createSpyObj('task', ['list']);
    tasks.list.and.returnValue(taskDeferred.promise);
    tasks.filterTypes = {
      selfAssigned: 'selfAssigned',
      unassigned: 'unassigned',
      finished: 'finished'
    }
  }

  function mockProcessesService ($q) {
    processesDeferred = $q.defer();
    processes = jasmine.createSpyObj('processes', ['list']);
    processes.list.and.returnValue(processesDeferred.promise);
  }

  function createController ($controller) {
    $controller('TasksCtrl', {
      $scope: scope,
      tasks: tasks,
      processes: processes
    });
  }
});
