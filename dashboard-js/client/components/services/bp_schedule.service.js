angular.module('dashboardJsApp')
  .factory('bpForSchedule', function services(processes) {

    var kievMreo1 = 'kiev_mreo_1';

    var bp = {
      processesList: [],
      chosenBp: null,
      onChangeCallback: function() {}
    };

    processes.getUserProcesses().then(function (data) {
      bp.processesList = data;
      if (bp.processesList.length > 0) {

        for (var i = 0; i < bp.processesList.length; i++) {
          if (bp.processesList[i].sID === kievMreo1) {
            bp.chosenBp = bp.processesList[i];
            bp.onChangeCallback();
          }
        }
      }
    });

    return {
      bp : bp
    };
  });
