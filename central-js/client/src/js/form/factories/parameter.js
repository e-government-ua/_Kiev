define('parameter/factory', ['angularAMD'], function (angularAMD) {
    angularAMD.factory('ParameterFactory', function () {
        var parameter = function () {
            this.value = null;
        };

        return parameter;
    });
});