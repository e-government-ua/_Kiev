/*
a simple service for storing some state
Usage:
stateStorageService.setState('myState', {name: 'John', age: 21});
var state = stateStorageService.getState('myState'); // state = {name: 'John', age: 21};
stateStorageService.clearState('myState');
*/

angular.module('app').service('stateStorageService', [function() {
  var service = {
    state: {},
    setState: function(name, data) {
      console.log('setState', name, data);
      this.state[name] = data;
    },
    getState: function(name) {
      if (typeof (this.state[name]) != 'undefined') {
        return this.state[name];
      } else {
        console.warn('stateStorageService: no state with name %o in state %o', name, this.state);
      }
    },
    clearState: function(name) {
      if (typeof (this.state[name]) != 'undefined' && name) {
        delete this.state[name];
      }
    }
  };
  return service;
}]);