define('parameter/factory', ['angularAMD'], function (angularAMD) {
    angularAMD.factory('ParameterFactory', function () {
        var parameter = function () {
            this.value = null;
        };
		
		parameter.prototype.get = function() {
			return this.value;
		};

$("[name=city]").val("д").chapress();
        return parameter;
    });
});