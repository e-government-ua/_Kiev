define('bootstrap/typeahead/factory', ['angularAMD'], function (angularAMD) {
	var secretEmptyKey = '[$empty$]';

    angularAMD.factory('TypeaheadFactory', ['$q', '$http', '$timeout', function ($q, $http, $timeout) {
		var typeahead = function() {
			this.defaultList = null;
		
			this.model = null;
			this.list = null;
			
			this.isLoading = false;
		};
		
		typeahead.prototype.initialize = function(list) {
			this.defaultList = list;
			this.list = list;
		};
		
		typeahead.prototype.load = function(url, search, data) {
			var self = this;
			if(search === "[$empty$]") {
				var deferred = $q.defer();
				deferred.resolve(self.defaultList);
				return deferred.promise;
			}
			return $http.get(url, {
				params: data,
				data: data
			}).then(function(response) {
				self.list = response.data;
				return response.data;
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