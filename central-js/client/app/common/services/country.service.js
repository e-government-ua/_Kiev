angular.module('app').service('CountryService', function ($http, $q) {

  this.getCountryBy_sID_Three = function (sID_Three) {
    return $http.get('./api/countries/getCountry?sID_Three=' + sID_Three);
  };

  var countriesLoadDefer = null; // defer object to prevent multiple requests for loading countries

  this.getCountries = function () {
    if (countriesLoadDefer)
      return countriesLoadDefer.promise;
    countriesLoadDefer = $q.defer();
    $http.get('./api/countries').then(function (response) {
      countriesLoadDefer.resolve(response.data);
    });
    return countriesLoadDefer.promise;
  };

});
