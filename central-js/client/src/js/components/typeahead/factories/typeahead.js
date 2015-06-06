define('bootstrap/typeahead/factory', ['angularAMD'], function (angularAMD) {
    angularAMD.factory('TypeaheadFactory', ['$http', function ($http) {
		var typeahead = function() {
			this.model = null;
			this.list = null;
			
			this.isLoading = false;
		};
		
		typeahead.prototype.initialize = function(list) {
			this.list = list;
		};
		
		typeahead.prototype.load = function(url, data) {
			var self = this;
			return $http.get(url, {
				params: data,
				data: data
			}).then(function(response) {
				self.list = response.data;
				return response;
			});
		};
		
		typeahead.prototype.select = function($item, $model, $label) {
			this.model = $item;
		};
		
		typeahead.prototype.reset = function() {
			this.model = null;
			this.list = null;
		};
		
		return typeahead;
    }]);
});