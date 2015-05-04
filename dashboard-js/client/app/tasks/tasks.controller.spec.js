'use strict';
describe('Controller: TasksCtrl', function() {
  var scope, tasks, q, processes;

  beforeEach(module('dashboardJsApp'));
  beforeEach(inject(function($controller, $rootScope, $q) {
    scope = $rootScope.$new();
    q = $q;
    mockTasksService();
    mockProcessesService();
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

  function thenSelfAssignedMenuProvided () {
    expect(scope.menus[0].title).toBe('В роботі');
    expect(scope.menus[0].type).toBe('selfAssigned');
  }

  function thenUnassignedMenuProvided () {
    expect(scope.menus[1].title).toBe('Необроблені');
    expect(scope.menus[1].type).toBe('unassigned');
  }

  function thenFinishedMenuProvided () {
    expect(scope.menus[2].title).toBe('Оброблені');
    expect(scope.menus[2].type).toBe('finished');
  }

  function mockTasksService () {
    tasks = jasmine.createSpyObj('task', ['list']);
    tasks.list.and.returnValue(q.when({}));
    tasks.filterTypes = {
      selfAssigned: 'selfAssigned',
      unassigned: 'unassigned',
      finished: 'finished'
    }
  }

  function mockProcessesService () {
    processes = jasmine.createSpyObj('processes', ['list']);
    processes.list.and.returnValue(q.when({}));
  }

  function createController ($controller) {
    $controller('TasksCtrl', {
      $scope: scope,
      tasks: tasks,
      processes: processes
    });
  }
});
