angular.module('documents').factory('BankIDAddressesFactualFactory', [
  function() {

/*
"country":"UA",
"state":"ДНЕПРОПЕТРОВСКАЯ",
"city":"ДНЕПРОПЕТРОВСК",
"street":"МИРА",
"houseNo":"11",
"flatNo":"75",
"dateModification": "19.06.2015 00:47:36"},
*/
    var oAddress = function() {
      this.type = 'factual';

      this.country = null;
      this.state = null;
      this.city = null;
      this.street = null;
      this.houseNo = null;
      this.flatNo = null;
      this.dateModification = null;
    };

    oAddress.prototype.initialize = function(document) {
      this.type = document.type;
      
      this.country = document.country;
      this.state = document.state;
      this.city = document.city;
      this.street = document.street;
      this.houseNo = document.houseNo;
      this.flatNo = document.flatNo;
      this.dateModification = document.dateModification;
    };

    oAddress.prototype.toString = function() {
      //return this.series + this.number + ' ' + this.issue + ' ' + this.dateIssue;
      return this.city + ', ' + this.street + ' ' + this.houseNo + ', ' + this.flatNo;
    };

    oAddress.prototype.getCountyCodeTwo = function() {
      return this.country;
    };

    return oAddress;
  }
]);
