angular.module('app').directive('slotPicker', function($http, dialogs) {
  return {
    restrict: 'EA',
    templateUrl: 'app/common/components/form/directives/slotPicker.html',
    scope: {
      serviceData: "=",
      service: "=",
      ngModel: "=",
      formData: "=",
      property: "="
    },
    link: function(scope) {

      scope.selected = {
        date: null,
        slot: null
      };

      scope.$watch('selected.date', function() {
        scope.selected.slot = null;
      });

      var resetData = function()
      {
        scope.slotsData = {};
        scope.selected.date = null;
        scope.selected.slot = null;
        scope.ngModel = null;
      };

      scope.$watch('selected.slot', function(newValue) {
        if (newValue) {
          //$http.post('/api/service/flow/set/' + newValue.nID + '?sURL=' + scope.serviceData.sURL).then(function(response) {
          $http.post('/api/service/flow/set/' + newValue.nID + '?nID_Server=' + scope.serviceData.nID_Server).then(function(response) {
            scope.ngModel = JSON.stringify({
              nID_FlowSlotTicket: response.data.nID_Ticket,
              sDate: scope.selected.date.sDate + ' ' + scope.selected.slot.sTime + ':00.00'
            });
          }, function() {
            resetData();
            scope.loadList();
            dialogs.error('Помилка', 'Неможливо вибрати час. Спробуйте обрати інший або пізніше, будь ласка');
          });
        }
        else
          scope.ngModel = null;
      });

      scope.slotsData = {};

      scope.loadList = function(nID_SubjectOrganDepartment){
        scope.slotsLoading = true;
        var data = {
          //sURL: scope.serviceData.sURL,
          nID_Server: scope.serviceData.nID_Server,
          nID_Service: (scope && scope.service && scope.service!==null ? scope.service.nID : null)
        };
        if (angular.isDefined(nID_SubjectOrganDepartment))
        {
          data.nID_SubjectOrganDepartment = nID_SubjectOrganDepartment;
        }
        
        return $http.get('/api/service/flow/' + scope.serviceData.nID, {params:data}).then(function(response) {
          scope.slotsData = response.data;
          scope.slotsLoading = false;
        });
      };

      var departmentProperty = 'nID_Department_' + scope.property.id;
      var departmentParam = scope.formData.params[departmentProperty];
      if (angular.isDefined(departmentParam)) {
        scope.$watch('formData.params.' + departmentProperty + '.value', function (newValue) {
          resetData();
          if (newValue)
          {
            scope.loadList(newValue);
          }
        });
      } else {
        scope.loadList();
      }
    }
  };
});
