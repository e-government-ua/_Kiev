angular.module('app').service('AdminService', function() {
  var cookieKey = 'admin';

  this.processAccountResponse = function(response) {
    if (response.data.admin)
      $.cookie(cookieKey, JSON.stringify(response.data.admin), {path: '/'});
    else
      $.removeCookie(cookieKey, {path: '/'});
  };

  this.isAdmin = function() {
    var result = $.cookie(cookieKey);
    if (!result)
      return false;
    result = JSON.parse(result);
    return !(!result.token || !result.inn);
  };
});
