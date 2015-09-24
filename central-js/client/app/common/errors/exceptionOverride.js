angular.module('app').factory('$exceptionHandler', function($log, ErrorsFactory) {
  return function(exception, cause) {
    exception.message += ' (caused by "' + cause + '")';
    $log.error(exception);

    ErrorsFactory.push({
      type: "danger",
      text:  exception.message
    });
  };
});
