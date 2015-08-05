angular.module('app').service('CountryService', function ($http) {

  this.getCountryBy_sID_Three = function (sID_Three) {
    return $http.get('./api/countries/getCountry?sID_Three=' + sID_Three);
  }

});
