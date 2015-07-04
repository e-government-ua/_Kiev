angular.module('app').directive('slotPicker', function($http) {
  return {
    restrict: 'EA',
    templateUrl: 'html/form/directives/slotPicker.html',
    scope: {
      serviceId: "=",
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
          $http.post('/api/service/flow/set/' + newValue.nID).then(function(response) {
            scope.ngModel = JSON.stringify({
              nID_FlowSlotTicket: response.data.nID_Ticket,
              sDate: scope.selected.date.sDate + ' ' + scope.selected.slot.sTime + ':00.00'
            });
          });
        }
        else
          scope.ngModel = null;
      });

      scope.slotsData = {};

      $http.get('/api/service/flow/' + scope.serviceId).then(function(response) {
        scope.slotsData = response.data;
      });
    }
  };
});
