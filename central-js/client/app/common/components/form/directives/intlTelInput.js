angular.module('app').directive('intlTelInput', function() {
    return {
        restrict: 'A',
        link: function(scope, element, attrs) {
            angular.element('input[type=tel]').intlTelInput({
		        defaultCountry: 'auto',
		        autoFormat: true,
		        allowExtensions: false,
		        preferredCountries: ['ua'],
		        autoPlaceholder: false,
		        geoIpLookup: function(callback) {
		            $.get('http://ipinfo.io', function() {}, 'jsonp').always(function(resp) {
		                var countryCode = (resp && resp.country) ? resp.country : '';
		                callback(countryCode);
		            });
		        }
		    }).(scope.$eval(attrs.directiveName));
    };
}); 



/*

	$('input[type=tel]').intlTelInput({
          defaultCountry: 'auto',
          autoFormat: true,
          allowExtensions: false,
          preferredCountries: ['ua'],
          autoPlaceholder: false,
          geoIpLookup: function(callback) {
              $.get('http://ipinfo.io', function() {}, 'jsonp').always(function(resp) {
                  var countryCode = (resp && resp.country) ? resp.country : '';
                  callback(countryCode);
              });
          }
      });

*/