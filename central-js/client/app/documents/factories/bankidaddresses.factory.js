angular.module('documents').factory('BankIDAddressesFactory', function(BankIDAddressesFactualFactory) {
  var addresses = function() {
    this.list = [];
  };

  addresses.prototype.initialize = function(list) {
    angular.forEach(list, function(value, key) {
      switch (value.type) {
        case 'factual':
          var element = new BankIDAddressesFactualFactory();
          element.initialize(value);

          this.list.push(element);
          break;
        default:
          break;
      }
    }, this);
  };

  addresses.prototype.getAddress = function() {
    for (var i = 0; i < this.list.length; i++) {
      var element = this.list[i];
      if (element instanceof BankIDAddressesFactualFactory) {
        return element.toString();
      }
    }
    return null;
  };
  addresses.prototype.getCountyCode = function() {
    for (var i = 0; i < this.list.length; i++) {
      var element = this.list[i];
      if (element instanceof BankIDAddressesFactualFactory) {
        return element.getCountyCodeTwo();
      }
    }
    return null;
  };


  return addresses;
});
