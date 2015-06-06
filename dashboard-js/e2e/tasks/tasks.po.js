'use strict';

var TasksPage = function() {
	this.selfAssignedMenu = element(by.id('selfAssigned'));
	this.unassignedMenu = element(by.id('unassigned'));
	this.finishedMenu = element(by.id('finished'));
};

module.exports = new TasksPage();