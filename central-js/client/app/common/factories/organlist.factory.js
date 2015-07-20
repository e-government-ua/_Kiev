angular.module('app').factory('OrganListFactory', function($http, $filter, TypeaheadFactory, DropdownFactory) {
  var organList = function() {
    this.typeahead = new TypeaheadFactory();
    this.dropdown = new DropdownFactory();
  };

  organList.prototype.initialize = function(list) {
    this.typeahead.initialize(list);
    this.dropdown.initialize(list);
  };

  organList.prototype.select = function($item, $model, $label) {
    this.typeahead.select($item, $model, $label);
    this.dropdown.select($item);
  };

  organList.prototype.load = function(oServiceData, search) {
    var self = this;

    var sID_UA = null;
    if (oServiceData.nID_City && oServiceData.nID_City.sID_UA)
      sID_UA = oServiceData.nID_City.sID_UA;
    else if (oServiceData.nID_City && oServiceData.nID_City.nID_Region && oServiceData.nID_City.nID_Region.sID_UA)
      sID_UA = oServiceData.nID_City.nID_Region.sID_UA;

    var data = {
      sID_UA: sID_UA
    };
    return this.typeahead.load('./api/organs/' + oServiceData.oSubject_Operator.oSubject.nID, search, data).then(function(organs) {
      if (search && search.length > 0 && search !== '[$empty$]')
        return $filter('filter')(organs, {sNameUa:search});
      else
        return organs;
    }).then(function(regions) {
      self.typeahead.list = regions;
      self.dropdown.list = regions;
      return regions;
    });
  };

  organList.prototype.reset = function() {
    this.typeahead.reset();
    this.dropdown.reset();
  };

  return organList;
});
