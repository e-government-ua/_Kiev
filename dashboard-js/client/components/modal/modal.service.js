'use strict';

angular.module('dashboardJsApp')
  .factory('Modal', function($rootScope, $modal) {
    /**
     * Opens a modal
     * @param  {Object} scope      - an object to be merged with modal's scope
     * @param  {String} modalClass - (optional) class(es) to be applied to the modal
     * @return {Object}            - the instance $modal.open() returns
     */
    function openModal(scope, modalClass) {
      var modalScope = $rootScope.$new();
      scope = scope || {};
      modalClass = modalClass || 'modal-default';

      angular.extend(modalScope, scope);

      return $modal.open({
        templateUrl: 'components/modal/modal.html',
        windowClass: modalClass,
        scope: modalScope
      });
    }

    // Public API here
    return {
      assignTask: function(redirectCallback, message, dismissCallback) {
        var warningModal = openModal({
          modal: {
            dismissable: true,
            title: 'Успіх!',
            html: '<strong>' + message + '</strong>',
            buttons: [{
              classes: 'btn-success',
              text: 'Почати опрацювання задачі',
              click: function(e) {
                redirectCallback();
                warningModal.close(e);
              }
            }, {
              classes: 'btn-success',
              text: 'Продовжити роботу з необробленими',
              click: function(e) {
                warningModal.close(e);
              }
            }]
          }
        }, 'modal-success');
        warningModal.result.then(function (selectedItem) {
          dismissCallback();
        }, function () {
          dismissCallback();
        });

      },
      inform: {
        info: function(callBack) {
          return function() {
            var args = Array.prototype.slice.call(arguments),
              name = args.shift(),
              warningModal;

            warningModal = openModal({
              modal: {
                dismissable: true,
                title: 'Успіх!',
                html: '<strong>' + name + '</strong>',
                buttons: [{
                  classes: 'btn-info',
                  text: 'Ok',
                  click: function(e) {
                    warningModal.close(e);
                    //document.location.href="/";
                  }
                }]
              }
            }, 'modal-info');

            warningModal.result.then(function(event) {
              if(callBack){
                callBack.apply(event, args);
              }
            });
          };
        },

        success: function(callBack) {
          return function() {
            var args = Array.prototype.slice.call(arguments),
              name = args.shift(),
              warningModal;

            warningModal = openModal({
              modal: {
                dismissable: true,
                title: 'Успіх!',
                html: '<strong>' + name + '</strong>',
                buttons: [{
                  classes: 'btn-success',
                  text: 'Ok',
                  click: function(e) {
                    warningModal.close(e);
                  }
                }]
              }
            }, 'modal-success');

            warningModal.result.then(function (event) {
              callBack.apply(event, args);
            }, function () {
              callBack.apply(event, args);
            });


          };
        },

        error: function(callBack) {
          return function() {
            var args = Array.prototype.slice.call(arguments),
              name = args.shift(),
              warningModal;

            warningModal = openModal({
              modal: {
                dismissable: true,
                title: 'Помилка!',
                html: '<strong>' + name + '</strong>',
                buttons: [{
                  classes: 'btn-danger',
                  text: 'Ok',
                  click: function(e) {
                    warningModal.close(e);
                  }
                }]
              }
            }, 'modal-danger');

            warningModal.result.then(function(event) {
              if (callBack) {
                callBack.apply(event, args);
              }
            });
          };
        },

        warning: function(callBack) {
          return function() {
            var args = Array.prototype.slice.call(arguments),
              name = args.shift(),
              warningModal;

            warningModal = openModal({
              modal: {
                dismissable: true,
                title: 'Увага!',
                html: '<strong>' + name + '</strong>',
                buttons: [{
                  classes: 'btn-warning',
                  text: 'Ок',
                  click: function(e) {
                    warningModal.close(e);
                  }
                }]
              }
            }, 'modal-warning');

            warningModal.result.then(function(event) {
              if(callBack){
                callBack.apply(event, args);
              }
            });
          };
        }
      },
      /* Confirmation modals */
      confirm: {
        keepalive: function(callBackYes, callBackNo) {
          callBackYes = callBackYes || angular.noop;
          callBackNo = callBackNo || angular.noop;

          /**
           * Open a delete confirmation modal
           * @param  {String} name   - name or info to show on modal
           * @param  {All}           - any additional args are passed straight to callback
           */
          return function() {
            var args = Array.prototype.slice.call(arguments),
              htmlConstruct = function(name){
                  return '<strong>' + name + '</strong>';
              },
              name = args.shift(),
              scope = {
                modal: {
                  dismissable: true,
                  title: 'Продовжити робочу сесію ?',
                  html: htmlConstruct(name),
                  buttons: [{
                    classes: 'btn-info',
                    text: 'Продовжити',
                    click: function(e) {
                      keepaliveModal.close(e);
                    }
                  }, {
                    classes: 'btn-default',
                    text: 'Вийти',
                    click: function(e) {
                      keepaliveModal.dismiss(e);
                      callBackNo.apply(event, args);
                    }
                  }]
                }
              };
            var keepaliveModal = openModal(scope, 'modal-warning');

            keepaliveModal.result.then(function(event) {
              callBackYes.apply(event, args);
            });

            keepaliveModal.update = function(name) {
                scope.modal.html = htmlContruct(name);
            };

            return keepaliveModal;
          };
        },
        /**
         * Create a function to open a delete confirmation modal (ex. ng-click='myModalFn(name, arg1, arg2...)')
         * @param  {Function} callBack - callback, ran when delete is confirmed
         * @return {Function}     - the function to open the modal (ex. myModalFn)
         */
        auth: function(callBack) {
          callBack = callBack || angular.noop;

          /**
           * Open a delete confirmation modal
           * @param  {String} name   - name or info to show on modal
           * @param  {All}           - any additional args are passed staight to del callback
           */
          return function() {
            var args = Array.prototype.slice.call(arguments),
              name = args.shift(),
              authModal;

            authModal = openModal({
              modal: {
                dismissable: true,
                title: 'Необхідно пройти авторизацію',
                html: '<strong>' + name + '</strong>',
                buttons: [{
                  classes: 'btn-info',
                  text: 'Авторизація',
                  click: function(e) {
                    authModal.close(e);
                  }
                }, {
                  classes: 'btn-default',
                  text: 'Відмінити',
                  click: function(e) {
                    authModal.dismiss(e);
                  }
                }]
              }
            }, 'modal-info');

            authModal.result.then(function(event) {
              callBack.apply(event, args);
            });
          };
        }
      }
    };
  });
