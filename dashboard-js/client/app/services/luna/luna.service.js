'use strict';

angular.module('dashboardJsApp').factory('lunaService', function () {
  return {
    getLunaValue: function (id) {

      // Number 2187501 must give CRC=3
      // Check: http://planetcalc.ru/2464/
      if(id===null || id === 0){
        return null;
      }
      var n = parseInt(id);
      var nFactor = 1;
      var nCRC = 0;
      var nAddend;

      while (n !== 0) {
        nAddend = Math.round(nFactor * (n % 10));
        nFactor = (nFactor === 2) ? 1 : 2;
        nAddend = nAddend > 9 ? nAddend - 9 : nAddend;
        nCRC += nAddend;
        n = parseInt(n / 10);
      }

      nCRC = nCRC % 10;
      return nCRC;
    }
  };
});
