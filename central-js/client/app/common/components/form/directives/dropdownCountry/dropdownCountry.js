angular.module('app').directive('dropdownCountry', function (CountryListFactory) {
  return {
    restrict: 'EA',
    templateUrl: 'app/common/components/form/directives/dropdownCountry/dropdownCountry.html',
    scope: {
      ngModel: "=",
      serviceData: "=",
      ngRequired: "="
    },
    link: function (scope) {
      // init country list for country select
      scope.countryList = new CountryListFactory();
      scope.loadCountryList = function (search) {
        return scope.countryList.load(scope.serviceData, search);
      };
      scope.onSelectCountryList = function (country) {
        scope.ngModel = country.sNameShort_UA;
        scope.countryList.typeahead.model = country.sNameShort_UA;
      };
      scope.countryList.reset();
      scope.countryList.initialize();
      scope.countryList.load(scope.serviceData, null).then(function (countryList) {
        scope.countryList.initialize(countryList);
        scope.countryList.typeahead.model = scope.ngModel;
      });
    }
  };
});
