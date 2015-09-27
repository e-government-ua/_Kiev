angular.module('app').directive('slotPicker', function($http, dialogs) {
  return {
    restrict: 'EA',
    templateUrl: 'app/common/components/form/directives/slotPicker.html',
    scope: {
      serviceData: "=",
      ngModel: "="
    },
    link: function(scope) {

      scope.selected = {
        date: null,
        slot: null
      };

      scope.$watch('selected.date', function() {
        scope.selected.slot = null;
      });

      scope.$watch('selected.slot', function(newValue) {
        if (newValue) {
          $http.post('/api/service/flow/set/' + newValue.nID + '?sURL=' + scope.serviceData.sURL).then(function(response) {
            scope.ngModel = JSON.stringify({
              nID_FlowSlotTicket: response.data.nID_Ticket,
              sDate: scope.selected.date.sDate + ' ' + scope.selected.slot.sTime + ':00.00'
            });
          }, function() {
            scope.slotsData = {};
            scope.selected.date = null;
            scope.selected.slot = null;
            scope.ngModel = null;
            scope.loadList();
            dialogs.error('Помилка', 'Неможливо вибрати час. Спробуйте обрати інший або пізніше, будь ласка');
          });
        }
        else
          scope.ngModel = null;
      });

      scope.slotsData = {};

      scope.loadList = function(){
        return $http.get('/api/service/flow/' + scope.serviceData.nID + '?sURL=' + scope.serviceData.sURL).then(function(response) {
          scope.slotsData = response.data;
        });
      };

      scope.loadList();
    }
  };
});
