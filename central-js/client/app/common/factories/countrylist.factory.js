angular.module('app').factory('CountryListFactory', function($http, $filter, TypeaheadFactory, DropdownFactory) {
  var countryList = function() {
    this.typeahead = new TypeaheadFactory();
    this.dropdown = new DropdownFactory();
  };

  countryList.prototype.initialize = function(list) {
    this.typeahead.initialize(list);
    this.dropdown.initialize(list);
  };

  countryList.prototype.select = function($item, $model, $label) {
    this.typeahead.select($item, $model, $label);
    this.dropdown.select($item);
  };

  countryList.prototype.load = function(oServiceData, search) {
    var self = this;

    var data = {
      resident: oServiceData.resident
    };
    return this.typeahead.load('./api/countries/', search, data).then(function(countryList) {
      if (search && search.length > 0 && search !== '[$empty$]')
        return $filter('filter')(countryList, {sNameShort_UA:search});
      else
        return countryList;
    }).then(function(countryList) {
      self.typeahead.list = countryList;
      self.dropdown.list = countryList;
      return countryList;
    });
  };

  countryList.prototype.reset = function() {
    this.typeahead.reset();
    this.dropdown.reset();
  };

  return countryList;
});
