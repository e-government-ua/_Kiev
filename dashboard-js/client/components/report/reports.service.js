angular.module('dashboardJsApp').factory('reports', function tasks($http, $q) {
      
  return {
      
    exportLink: function (exportParams,callback) {
      
      var dataArray = {'sID_BP': exportParams.sBP,'sDateAt': exportParams.from,'sDateTo': exportParams.to};      
      
      var getReportParametersUrl = '/api/reports/template?sPathFile=/export/' + exportParams.sBP + '.properties';
      $http.get(getReportParametersUrl).then(function(result){      

        _.each(result.data.split("\n"), function (line) {
            var sr = line.split("=");
            if (!!sr && sr.length >= 2) {
              var propKey = sr[0].trim();
              var propValue = sr[1].trim();
              if (!dataArray[propKey]) {
                dataArray[propKey] = propValue;
              }
              
            }
        });
            var getExportUrl = './api/reports/export?';
            for(var key in dataArray) {
              getExportUrl = getExportUrl + key+'='+dataArray[key]+'&';              
            }
            var fileToSave = "zvit.dat";
            if (dataArray["sFileName"]) fileToSave = dataArray["sFileName"];  
            
            $http.get(getExportUrl).then(function (data, fileName) {
              callback(data, fileToSave);
            }, function (){
              //if error 
              
            });
            
      }, function () {
        //if error 403 when loading properties
        var dataArray = {
          'sID_BP': exportParams.sBP, //'dnepr_spravka_o_doxodax',
          'sID_State_BP': 'usertask1',
          'sDateAt': exportParams.from,
          'sDateTo': exportParams.to,
          'saFields': '${nID_Task};${sDateCreate};${area};${bankIdinn};;;${bankIdlastName} ${bankIdfirstName} ${bankIdmiddleName};4;${aim};${date_start1};${date_stop1};${place_living};${bankIdPassport};1;${phone};${email}',
          'sID_Codepage': 'win1251',
          'nASCI_Spliter': '18',
          'sDateCreateFormat': 'dd.MM.yyyy HH:mm:ss',
          'sFileName': 'dohody.dat'
        };
        var getExportUrl = './api/reports/export?';
        for (var key in dataArray) {
          getExportUrl = getExportUrl + key + '=' + dataArray[key] + '&';
        }
        var fileToSave = "zvit.dat";
        if (dataArray["sFileName"]) fileToSave = dataArray["sFileName"];

        $http.get(getExportUrl).then(function (data, fileName) {
          callback(data, fileToSave);
        }, function () {
          //if error 
              
        });
      })      

    },
    
     statisticLink: function (statisticParams, callback) {
      var data = {
        'sID_BP': statisticParams.sBP,
        'sDateAt': statisticParams.from,
        'sDateTo': statisticParams.to
      };
      var statUrl = './api/reports/statistic?' +
        'sID_BP_Name=' + data.sID_BP + '&' +
        'sDateAt=' + data.sDateAt + '&' +
        'sDateTo=' + data.sDateTo;
        
      var fileToSave = "statistics.dat";
      
        $http.get(statUrl).success(function (data, fileName) {
              callback(data, fileToSave);
            });
    }

  }
});

'use strict';

