angular.module('dashboardJsApp').factory('reports', function tasks($http) {

  return {
    exportLink: function (exportParams) {
      var data = {
        'sID_BP': 'dnepr_spravka_o_doxodax',
        'sID_State_BP': 'usertask1',
        'sDateAt': exportParams.from,
        'sDateTo': exportParams.to,
        'saFields': '${nID_Task};${sDateCreate};${area};${bankIdinn};;;${bankIdlastName} ${bankIdfirstName} ${bankIdmiddleName};4;${aim};${date_start1};${date_stop1};${place_living};${bankIdPassport};1;${phone};${email}',
        'sID_Codepage': 'win1251',
        'nASCI_Spliter': '18',
        'sDateCreateFormat': 'dd.mm.yyyy hh:MM:ss',
        'sFileName': 'dohody.dat'
      };

      return './api/reports/export?' +
        'sID_BP=' + data.sID_BP + '&' +
        'sID_State_BP=' + data.sID_State_BP + '&' +
        'sDateAt=' + data.sDateAt + '&' +
        'sDateTo=' + data.sDateTo + '&' +
        'saFields=' + data.saFields + '&' +
        'sID_Codepage=' + data.sID_Codepage + '&' +
        'nASCI_Spliter=' + data.nASCI_Spliter + '&' +
        'sDateCreateFormat=' + data.sDateCreateFormat + '&' +
        'sFileName=' + data.sFileName;
    }
    
    , statisticLink: function (statisticParams) {
      var data = {
        'sID_BP': statisticParams.sBP,
        'sDateAt': statisticParams.from,
        'sDateTo': statisticParams.to
      };
      return './api/reports/statistic?' +
        'sID_BP_Name=' + data.sID_BP + '&' +
        'sDateAt=' + data.sDateAt + '&' +
        'sDateTo=' + data.sDateTo;
    }
    
  }
});

'use strict';

