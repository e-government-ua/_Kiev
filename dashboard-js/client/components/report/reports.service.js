angular.module('dashboardJsApp').factory('reports', function tasks($http) {

  function getDefaultDataArray(exportParams) {
    var dataArray = {
      'sID_BP': exportParams.sBP, //'dnepr_spravka_o_doxodax',
      'sID_State_BP': null,//'usertask1'
      'sDateAt': exportParams.from,
      'sDateTo': exportParams.to,
      'saFields': exportParams.bExportAll ? '*' : '${nID_Task};${sDateCreate};${area};${bankIdinn};;;${bankIdlastName} ${bankIdfirstName} ${bankIdmiddleName};4;${aim};${date_start1};${date_stop1};${place_living};${bankIdPassport};1;${phone};${email}',
      'sID_Codepage': 'win1251',
      'nASCI_Spliter': '18',
      'sDateCreateFormat': 'dd.MM.yyyy HH:mm:ss',
      'sFileName': 'dohody.dat',
      'bHeader': false, // есть/нет хеадера
      'saFieldsCalc': '', // поля для калькуляций
      'saFieldSummary': '' // поля для агрегатов      
    };
    return dataArray;
  }

  function getExportUrl(dataArray) {
    var exportUrl = './api/reports/export?';
    for (var key in dataArray) {
      exportUrl = exportUrl + key + '=' + dataArray[key] + '&';
    }
    return exportUrl;
  }

  function defaultHandler(exportParams, callback) {
    var dataArray = getDefaultDataArray(exportParams);
    var exportUrl = getExportUrl(dataArray); 

    //return export URL to client's reports.controller
    callback(exportUrl);

  }
  return {

    exportLink: function (exportParams, callback) {
      var dataArray = getDefaultDataArray(exportParams);
      
      //loading properties from .properties file for a selected Business Process      
      var getReportParametersUrl = '/api/reports/template?sPathFile=/export/' + exportParams.sBP + '.properties';

      $http.get(getReportParametersUrl).then(function (result) {

        if (result.data != '') {
          _.each(result.data.split("\n"), function (line) {
            var sr = line.split("=");
            if (!!sr && sr.length >= 2) {
              var propKey = sr[0].trim();
              var propValue = sr[1].trim();
              //if there is already such property in default properties - it's value should be overwritten
              if (dataArray[propKey]) {
                dataArray[propKey] = propValue;
              }
            }
          });

          var exportUrl = getExportUrl(dataArray);
          callback(exportUrl);
        }
        else {
          //if returned properties are empty
          defaultHandler(exportParams, callback);
        }
      }, function () {
        //if error 403 when loading .properties
        defaultHandler(exportParams, callback);
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

      callback(statUrl);
    }

  }
});

'use strict';

