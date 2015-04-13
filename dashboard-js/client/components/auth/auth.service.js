'use strict';

angular.module('dashboardJsApp')
  .factory('Auth', function Auth($location, $rootScope, $http, User, Base64, $cookieStore, $q) {
    /**currentUser: Object
    email: "kermit@activiti.org"
    firstName: "Kermit"
    id: "kermit"
    lastName: "The Frog"
    pictureUrl: "https://52.17.126.64:8080/wf-dniprorada/service/identity/users/kermit/picture"
    url: "https://52.17.126.64:8080/wf-dniprorada/service/identity/users/kermit"
    **/
    var currentUser = {};

    if ($cookieStore.get('auth') && $cookieStore.get('user')) {
      currentUser = $cookieStore.get('user');
    }

    return {
      /**
       * Authenticate user and save user data
       *
       * @param  {Object}   user     - login info
       * @param  {Function} callback - optional
       * @return {Promise}
       */
      login: function(user, callback) {
        var cb = callback || angular.noop;
        var deferred = $q.defer();
        var creds = 'Basic ' + Base64.encode(user.login + ':' + user.password);

        var req = {
          method: 'POST',
          url: '/auth/activiti',
          headers: {
            'Authorization': creds
          },
          data: {
            login: user.login,
            password: user.password
          }
        };

        $http(req).
        success(function(data) {
          $cookieStore.put('auth', creds);
          $cookieStore.put('user', data);
          currentUser = data;
          deferred.resolve(data);
          return cb();
        }).
        error(function(err) {
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
      logout: function() {
        $cookieStore.remove('auth');
        $cookieStore.remove('user');
        currentUser = {};
      },

      /**
       * Gets all available info on authenticated user
       *
       * @return {Object} user
       */
      getCurrentUser: function() {
        return currentUser;
      },

      /**
       * Check if a user is logged in
       *
       * @return {Boolean}
       */
      isLoggedIn: function() {
        return currentUser.hasOwnProperty('id');
      },

      /**
       * Waits for currentUser to resolve before checking if user is logged in
       */
      isLoggedInAsync: function(cb) {
        if (currentUser.hasOwnProperty('$promise')) {
          currentUser.$promise.then(function() {
            cb(true);
          }).catch(function() {
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
      isAdmin: function() {
        return currentUser.role === 'admin';
      }
    };
  });