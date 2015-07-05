define('config', ['angularAMD'], function (angularAMD) {
    angularAMD.factory('config', function ($resource) {
        return $resource('./config/config.json').get();
    });

    return angularAMD;
});
