'use strict';

angular.module('appBoilerPlate').provider('statesRepository', function StatesRepositoryProvider() {
  var selfProvider = this;
  var findModeRegexp = /(\w*).(\w*).(\w*)(\w*|.*)*/;

  var modeModel = {
    "ternopil": {
      "header": "ternopil.header.html",
      "footer": "ternopil.footer.html",
      "placesID": ['6110100000', '6100000000']
    },
    "kyiv": {
      "header": "kyiv.header.html",
      "footer": "kyiv.footer.html",
      "placesID": ['8000000000', '8000000000']
    },
    "kharkiv": {
      "header": "kharkiv.header.html",
      "footer": "kharkiv.footer.html",
      "placesID": ['6310100000', '6300000000']
    },
    "mvd": {
      "header": "mvd.header.html",
      "footer": "mvd.footer.html",
      "placesID": ['3200000000', '8000000000']
    }
  };

  var modes = {
    "ternopil": modeModel.ternopil,
    "ternopol": modeModel.ternopil,
    "kyiv": modeModel.kyiv,
    "kiev": modeModel.kyiv,
    "kharkiv": modeModel.kharkiv,
    "kharkov": modeModel.kharkiv,
    "mvd": modeModel.mvd
  };
  this.init = function (domen) {
    //test.kiev.igov.org.ua
    
    this.domain = domen;


    if (domen.split(':')[0] !== 'localhost') {
      if (domen.indexOf('kievcity') >= 0) {
        //https://es.kievcity.gov.ua
        this.mode = 'kyiv';
        //this.mode = modes.kyiv;
      } else if (domen.indexOf('mvs') >= 0) {
        //https://es.kievcity.gov.ua
        this.mode = 'mvd';
        //this.mode = modes.kyiv;
      } else if (domen.indexOf('ternopil') >= 0) {
        //ternopil.igov.org.ua
        this.mode = 'ternopil';
      } else {
        var matches = findModeRegexp.exec(domen);
        if (matches[1] === 'test') {// || matches[1] === 'test-version'
          if (matches[2] === 'version') {
            this.mode = matches[3];
          } else {
            this.mode = matches[2];
          }
        } else {
          this.mode = matches[1];
        }
      }

    } else {
      this.mode = 'local';
    }

  };

  this.isCentral = function () {
    return this.mode === 'local' || this.mode === 'igov';
  };

  var getHeader = function (mode) {
    var hdr;
    if (!!modes[mode]) {
      hdr = modes[mode].header;
    } else {
      hdr = 'header.html';
    }
    return 'app/header/' + hdr;
  };

  var getFooter = function (mode) {
    var footer;
    if (!!modes[mode]) {
      footer = modes[mode].footer;
    } else {
      footer = 'footer.html';
    }
    return 'app/footer/' + footer;
  };


  this.index = function () {
    return {
      url: '/',
      views: {
        header: {
          templateUrl: getHeader(this.mode),
          controller: 'IndexController'
        },
        'main@': {
          templateUrl: 'app/service/index/services.html',
          controller: 'ServiceController'
        },
        footer: {
          //templateUrl: 'app/footer/footer.html'
          templateUrl: getFooter(this.mode),
        }
      }
    };
  };

  var StatesRepository = function (mode, domain) {
    this.mode = mode;
    this.domain = domain;
  };

  StatesRepository.prototype.getIDPlaces = function () {
    if (!!modes[this.mode]) {
      return modes[this.mode].placesID;
    }
    return [];
  };

  StatesRepository.prototype.getRegion = function (regions) {
    var result = null;
    var placesID = this.getIDPlaces();
    if (placesID) {
      angular.forEach(regions, function (region) {
        if (region.sID_UA == placesID[0]) {
          result = region;
        }
      });
    }
    return result;
  };

  StatesRepository.prototype.getCity = function (cities) {
    var result = null;
    var placesID = this.getIDPlaces();
    if (placesID) {
      angular.forEach(cities, function (city) {
        if (city.sID_UA == placesID[1]) {
          result = city;
        }
      });
    }
    return result;
  };

  StatesRepository.prototype.isCentral = function () {
    return selfProvider.isCentral();
  };

  this.$get = [function StatesRepositoryFactory() {
    return new StatesRepository(this.mode, this.domain);
  }];
});
