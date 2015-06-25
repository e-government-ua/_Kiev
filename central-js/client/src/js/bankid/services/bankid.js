define('bankid/service', ['angularAMD'], function (angularAMD) {
    angularAMD.factory('BankIDService', ['$http', '$q', 'AdminService', function ($http, $q, AdminService) {
        var bankIDLogin;
        var bankIDAccount;

        return {
            isLoggedIn: function () {
                var deferred = $q.defer();

                $http.get('./api/auth/check').success(function(data, status) {
                    deferred.resolve(true);
                }).error(function(data, status) {
                    bankIDLogin = undefined;
                    bankIDAccount = undefined;
                    deferred.reject(true);
                });

                return deferred.promise;
            },

            login: function (code, redirect_uri) {
                var data = {
                    'code': code,
                    'redirect_uri': redirect_uri
                };
                if (bankIDLogin) {
                    var deferred = $q.defer();
                    deferred.resolve(bankIDLogin);
                    return deferred.promise;
                } else {
                    return $http.get('./api/bankid/login', {
                        params: data,
                        data: data
                    }).then(function (response) {
                        return bankIDLogin = response.data;
                    });
                }
            },

            account: function (access_token) {
                var data = {
                    'access_token': access_token
                };
                if (bankIDAccount) {
                    var deferred = $q.defer();
                    deferred.resolve(bankIDAccount);
                    return deferred.promise;
                } else {
                    return $http.get('./api/bankid/account', {
                        params: data,
                        data: data
                    }).then(function (response) {
                        AdminService.processAccountResponse(response);
                        return bankIDAccount = response.data;
                    });
                }
            }
        }
    }]);
});