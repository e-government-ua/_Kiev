define('admin/service', ['angularAMD'], function (angularAMD) {

	angularAMD.service('AdminService', [function () {
		var cookieKey = 'admin';

		this.processAccountResponse = function (response) {
			if (response.data.admin)
				$.cookie(cookieKey, JSON.stringify(response.data.admin), { path: '/' });
			else
				$.removeCookie(cookieKey, { path: '/' });
		};

		this.isAdmin = function () {
			var result = $.cookie(cookieKey);
			if (!result) {
				console.log('admin. no cookie');
				return false;
			}
			result = JSON.parse(result);
			if (!result.token || !result.inn) {
				console.log('admin. bad cookie');
				return false;
			}
			console.log('admin. OK cookie', result);
			return true;
		};
	}]);
});
