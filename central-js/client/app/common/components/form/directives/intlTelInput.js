angular.module('app').directive('intlTelInput', function() {
	console.log( 'this: ', this );
	console.log( 'this: ', this );

	function link( scope, element, attrs ) {
		//$(element).'pluginActivationFunction'(scope.$eval(attrs.directiveName));
		console.log( 'scope: ', scope );
		console.log( 'element: ', element );
		console.log( 'attrs: ', attrs );
		//$('input[type=tel]').intlTelInput(scope.$eval(attrs.intlTelInput));
		// {
	 //        defaultCountry: 'auto',
	 //        autoFormat: true,
	 //        allowExtensions: false,
	 //        preferredCountries: ['ua'],
	 //        autoPlaceholder: false,
	 //        geoIpLookup: function(callback) {
	 //            $.get('http://ipinfo.io', function() {}, 'jsonp').always(function(resp) {
	 //                var countryCode = (resp && resp.country) ? resp.country : '';
	 //                callback(countryCode);
	 //            });
	 //        }
	 //    });
	};

	return {
    	template: 'Name: '
  	};

/* $('input[type=tel]').intlTelInput({
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
  }); */
    // return {
    //     restrict: 'A',
    //     link: link
    // };	
});