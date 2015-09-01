/*
 Примеры роутов:

 Это может быть Хэш- параметр, например:
 https://test.igov.org.ua/service/159/general#nID_Region=5&nID_City=6
 https://test.igov.org.ua/service/159/general#nID_Region=5
 https://test.igov.org.ua/service/159/general#nID_Region=16
 https://test.igov.org.ua/service/159/general#sID_UA_Region=8000000000&sID_UA_City=8000000000
 https://test.igov.org.ua/service/159/general#sID_UA_Region=8000000000
 https://test.igov.org.ua/service/159/general#sID_UA_Region=3200000000
 https://test.igov.org.ua/service/159/general#sRegion=Київ&sCity=Київ
 https://test.igov.org.ua/service/159/general#sRegion=Київ
 https://test.igov.org.ua/service/159/general#sRegion=Київська

 или обычный параметр, например:
 https://test.igov.org.ua/service/159/general?nID_Region=5&nID_City=6
 https://test.igov.org.ua/service/159/general?nID_Region=5
 https://test.igov.org.ua/service/159/general?nID_Region=16
 https://test.igov.org.ua/service/159/general?sID_UA_Region=8000000000&sID_UA_City=8000000000
 https://test.igov.org.ua/service/159/general?sID_UA_Region=8000000000
 https://test.igov.org.ua/service/159/general?sID_UA_Region=3200000000
 https://test.igov.org.ua/service/159/general?sRegion=Київ&sCity=Київ
 https://test.igov.org.ua/service/159/general?sRegion=Київ
 https://test.igov.org.ua/service/159/general?sRegion=Київська
 */
angular.module('app').factory('serviceLocationParser', function ($location) {
  var paramsNames = {
    region: ['nID_Region', 'sID_UA_Region', 'sRegion'],
    city: ['nID_City', 'sID_UA_City', 'sCity']
  };

  var modelNames = ['nID', 'sID_UA', 'sName'];

  return {
    getParams: function () {
      var result = $location.search();
      var hash = $location.hash();
      if (hash.length > 0) {
        angular.forEach(hash.split('&'), function (hashItem) {
          var slit = hashItem.split("=");
          result[slit[0]] = slit[1];
        })
      }
      return result;
    },

    getSelectedFromArray: function (namesArray, dataArray) {
      var result = null;
      var params = this.getParams();
      var nameParam = null;
      var nameModel = null;
      angular.forEach(namesArray, function (name, i) {
        if (!nameParam && params[name]) {
          nameParam = name;
          nameModel = modelNames[i];
        }
      });
      if (nameParam) {
        angular.forEach(dataArray, function (dataItem) {
          if (!result && params[nameParam] == dataItem[nameModel])
            result = dataItem;
        });
      }
      return result;
    },

    getSelectedRegion: function (regions) {
      return this.getSelectedFromArray(paramsNames.region, regions);
    },

    getSelectedCity: function (cities) {
      return this.getSelectedFromArray(paramsNames.city, cities);
    }
  };
});
