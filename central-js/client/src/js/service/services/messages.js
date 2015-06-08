define('messages/service', ['angularAMD'], function (angularAMD) {
	angularAMD.service('MessagesService', ['$http', function($http) {
		this.setMessage = function(message, userMessage) {
			var data = {
                "sMail": message.sMail,
                "sHead": message.sHead,
                "sBody": message.sBody
            };

            return $http.post('./api/messages', data).then(function(response) {
                // @todo Better notification and error processing.
                alert(userMessage);
				return response.data;
			});
		};
	}]);
});