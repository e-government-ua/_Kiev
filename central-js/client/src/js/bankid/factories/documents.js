define('bankid/documents/factory', ['angularAMD', 'bankid/documents/passport/factory'], function (angularAMD) {
	angularAMD.factory('BankIDDocumentsFactory', [
		'BankIDDocumentsPassportFactory',
		function(PassportFactory) {
			var documents = function() {
				this.list = [];
			};
			
			documents.prototype.initialize = function(list) {
				angular.forEach(list, function(value, key) {
					switch(value.type) {
						case 'passport':
							var element = new PassportFactory();
							element.initialize(value);
							
							this.list.push(element);
							break;
						default:
							break;
					}
				}, this);
			};
			
			documents.prototype.getPassport = function() {
				for(var i = 0; i < this.list.length; i++) {
					var element = this.list[i];
					if(element instanceof PassportFactory) {
						return element.toString();
					}
				}
				return null;
			};
			
			return documents;
		}
	]);
});