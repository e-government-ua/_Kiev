angular.module('app').factory('$exceptionHandler', function($log) {
  return function(exception, cause) {
    exception.message += ' (caused by "' + cause + '")';
    $log.error(exception);
  };
});
