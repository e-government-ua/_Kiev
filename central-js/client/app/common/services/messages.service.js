angular.module('app').service('MessagesService', function($http) {
  this.setMessage = function(message, userMessage) {
    var data = {
      "sMail": message.sMail,
      "sHead": message.sHead,
      "sBody": message.sBody
    };

    return $http.post('./api/messages', data).then(function(response) {
      console.log(userMessage);
      return response.data;
    });
  };
});