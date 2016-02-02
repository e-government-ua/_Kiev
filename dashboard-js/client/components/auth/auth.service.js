'use strict';

angular.module('dashboardJsApp')
  .factory('Auth', function Auth($location, $rootScope, $http, Base64, $cookieStore, $q) {
    /**currentUser: Object
     email: "kermit@activiti.org"
     firstName: "Kermit"
     id: "kermit"
     lastName: "The Frog"
     pictureUrl: "https://52.17.126.64:8080/wf/service/identity/users/kermit/picture"
     url: "https://52.17.126.64:8080/wf/service/identity/users/kermit"
     **/
    var currentUser = {};
    var sessionSettings;

    if ($cookieStore.get('user')) {
      currentUser = $cookieStore.get('user');
    }

    if ($cookieStore.get('sessionSettings')) {
      sessionSettings = $cookieStore.get('sessionSettings');
    }

    return {
      /**
       * Authenticate user and save user data
       *
       * @param  {Object}   user     - login info
       * @param  {Function} callback - optional
       * @return {Promise}
       */
      login: function (user, callback) {
        var cb = callback || angular.noop;
        var deferred = $q.defer();

        var req = {
          method: 'POST',
          url: '/auth/activiti',
          data: {
            login: user.login,
            password: user.password
          }
        };

        $http(req).
        success(function (data) {
          currentUser = data;
          deferred.resolve(data);
          return cb();
        }).
        error(function (err) {
          this.logout();
          deferred.reject(err);
          return cb(err);
        }.bind(this));

        return deferred.promise;
      },

      pingSession: function (callback) {
        var cb = callback || angular.noop;
        var deferred = $q.defer();

        var req = {
          method: 'POST',
          url: '/auth/activiti/ping'
        };

        $http(req).
        success(function (data) {
          deferred.resolve(data);
          return cb();
        }).
        error(function (err) {
          this.logout();
          deferred.reject(err);
          return cb(err);
        }.bind(this));

        return deferred.promise;
      },
      /**
       * Delete access token and user info
       *
       * @param  {Function}
       */
      logout: function (callback) {
        $cookieStore.remove('user');
        $cookieStore.remove('sessionSettings');
        $cookieStore.remove('JSESSIONID');
        currentUser = {};
        sessionSettings = undefined;

        var cb = callback || angular.noop;
        var deferred = $q.defer();

        var req = {
          method: 'POST',
          url: '/auth/activiti/logout'
        };

        $http(req).
        success(function (data) {
          deferred.resolve(data);
          return cb();
        }).
        error(function (err) {
          deferred.reject(err);
          return cb(err);
        }.bind(this));

        return deferred.promise;
      },

      /**
       * Gets all available info on authenticated user
       *
       * @return {Object} user
       */
      getCurrentUser: function () {
        return currentUser;
      },

      getSessionSettings: function () {
        return sessionSettings;
      },

      /**
       * Check if a user is logged in
       *
       * @return {Boolean}
       */
      isLoggedIn: function () {
        return currentUser.hasOwnProperty('id');
      },

      /**
       * Waits for currentUser to resolve before checking if user is logged in
       */
      isLoggedInAsync: function (cb) {
        if (currentUser.hasOwnProperty('$promise')) {
          currentUser.$promise.then(function () {
            cb(true);
          }).catch(function () {
            cb(false);
          });
        } else if (currentUser.hasOwnProperty('id')) {
          cb(true);
        } else {
          cb(false);
        }
      },

      /**
       * Check if a user is an admin
       *
       * @return {Boolean}
       */
      isAdmin: function () {
        return this.hasRole('admin');
      },

      /**
       * set of input roles
       * @returns {boolean} if user has a role from the input set
       */
      hasOneOfRoles: function () {
        var hasRole = false;
        if (arguments && arguments.length > 0) {
          for (var i = 0; i < arguments.length; i++) {
            if (currentUser.roles.indexOf(arguments[i]) !== -1 ) {
              hasRole = true;
              break;
            }
          }
        }
        return hasRole;
      }
    };
  });
