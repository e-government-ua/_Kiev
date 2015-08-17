'use strict';

angular.module('appBoilerPlate').provider('statesRepository', function StatesRepositoryProvider() {
  var findModeRegexp = /(\w*).(\w*).(\w*)(\w*|.*)*/;

  this.init = function (domen) {
    //test.kiev.igov.org.ua

    this.domen = domen;
    if (domen.split(':')[0] !== 'localhost') {
      var matches = findModeRegexp.exec(domen);
      if (matches[1] === 'test' ) {// || matches[1] === 'test-version'
        if (matches[2] === 'version' ) {
            this.mode = matches[3];
        }else{
            this.mode = matches[2];
        }
      } else {
        this.mode = matches[1];
      }
    } else {
      this.mode = 'local';
    }

  };

  this.isCentral = function () {
    return this.mode === 'local' || this.mode === 'igov';
  };

  var getHeader = function (mode) {
    var header;
    if (mode === 'kyiv' || mode === 'kiev') {
      header = 'kyiv.header.html';
    } else {
      header = 'header.html';
    }
    return 'app/header/' + header;
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
          templateUrl: 'app/footer/footer.html'
        }
      }
    };
  };

  var StatesRepository = function (mode, domain) {
    this.mode = mode;
    this.domain = domain;
  };

  StatesRepository.prototype.getIDPlaces = function(){
    if(this.mode === 'kyiv' || this.mode === 'kiev'){
      return ['3200000000','8000000000'];
      //return ['123','456'];
    }
    return [];
  };

  this.$get = [function StatesRepositoryFactory() {
    return new StatesRepository(this.mode, this.domain);
  }];
});
