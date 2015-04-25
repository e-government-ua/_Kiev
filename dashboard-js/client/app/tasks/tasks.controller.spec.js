'use strict';
describe('Controller: TasksCtrl', function() {
	var scope, tasks, q;

	beforeEach(module('dashboardJsApp'));

	beforeEach(inject(function($controller, $rootScope, $q) {
		scope = $rootScope.$new();
		q = $q;
		mockTasksService();
		$controller('TasksCtrl', {
			$scope: scope,
			tasks: tasks
		});
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
});
