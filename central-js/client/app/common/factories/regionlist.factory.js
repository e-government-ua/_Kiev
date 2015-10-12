angular.module('app').factory('RegionListFactory', function($http, PlacesService, TypeaheadFactory, DropdownFactory) {
  var regionList = function() {
    this.typeahead = new TypeaheadFactory();
    this.dropdown = new DropdownFactory();
  };

  regionList.prototype.initialize = function(list) {
    this.typeahead.initialize(list);
    this.dropdown.initialize(list);
  };

  regionList.prototype.select = function($item, $model, $label) {
    this.typeahead.select($item, $model, $label);
    this.dropdown.select($item);
  };

  regionList.prototype.load = function(service, search) {
    var self = this;
    var data = {
      sFind: search
    };
    return this.typeahead.load('./api/places/regions', search, data).then(function(regions) {
      if (service === null) {
        return regions;
      }
      return PlacesService.colorizeRegionsForService(regions, service);
    }).then(function(regions) {
      self.typeahead.list = regions;
      self.dropdown.list = regions;
      return regions;
    });
  };

  regionList.prototype.reset = function() {
    this.typeahead.reset();
    this.dropdown.reset();
  };

  return regionList;
});