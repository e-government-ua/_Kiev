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

  function mockTasksService () {
    tasks = jasmine.createSpyObj('task', ['list']);
    tasks.list.andReturn(q.when({}));
    tasks.filterTypes = {};
  }

  function mockProcessesService () {
    processes = jasmine.createSpyObj('processes', ['list']);
    processes.list.andReturn(q.when({}));
  }

  function createController ($controller) {
    $controller('TasksCtrl', {
      $scope: scope,
      tasks: tasks,
      processes: processes
    });
  }
});
