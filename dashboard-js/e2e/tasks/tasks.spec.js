'use strict';

describe('Tasks View', function() {
  var page;

  beforeEach(function() {
    browser.get('/tasks');
    page = require('./tasks.po');
  });

  it('should load list of unassigned tasks by click', function() {
    page.unassignedMenu.click();
  });
});