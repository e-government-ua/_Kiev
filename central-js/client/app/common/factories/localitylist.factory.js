angular.module('app').factory('LocalityListFactory', function($http, PlacesService, TypeaheadFactory, DropdownFactory) {
  var localityList = function() {
    this.typeahead = new TypeaheadFactory();
    this.dropdown = new DropdownFactory();
  };

  localityList.prototype.initialize = function(list) {
    this.typeahead.initialize(list);
    this.dropdown.initialize(list);
  };

  localityList.prototype.select = function($item, $model, $label) {
    this.typeahead.select($item, $model, $label);
    this.dropdown.select($item);
  };

  localityList.prototype.load = function(service, region, search) {
    var self = this;
    var data = {
      sFind: search
    };
    return this.typeahead.load('./api/places/region/' + region + '/cities', search, data).then(function(cities) {
      if (service === null) {
        return cities;
      }
      return PlacesService.colorizeCitiesForService(cities, service);
    }).then(function(cities) {
      self.typeahead.list = cities;
      self.dropdown.list = cities;
      return cities;
    });
  };

  localityList.prototype.reset = function() {
    this.typeahead.reset();
    this.dropdown.reset();
  };

  return localityList;
});