angular.module('app').provider('statesRepository', function StatesRepositoryProvider() {
  var findModeRegexp = /(\w*).(\w*)(\w*|.*)*/;

  var getHeader = function (mode) {
    var header;
    if (mode === 'kyiv' || mode === 'kiev') {
      header = 'kyiv.header.html';
    } else {
      header = 'header.html'
    }
    return 'app/header/' + header;
  }

  this.init = function (domen) {
    //test.kiev.igov.org.ua

    this.domen = domen;
    if (domen !== 'localhost') {
      var matches = findModeRegexp.exec(domen);
      if (matches[1] === 'test') {
        this.mode = matches[2];
      } else {
        this.mode = matches[1];
      }
    } else {
      this.mode = 'local'
    }

  };

  this.index = function () {
    return {
      url: '/',
      views: {
        header: {
          templateUrl: getHeader(this.mode)
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
  }

  var StatesRepository = function(mode, domain){
    this.mode = mode;
    this.domain = domain;
  }

  this.$get = [function StatesRepositoryFactory() {
    return new StatesRepository(this.mode, this.domain);
  }];
});
