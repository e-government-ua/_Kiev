angular.module('documents').factory('BankIDDocumentsPassportFactory', [
  function() {
    var passport = function() {
      this.type = 'passport';

      this.series = null;
      this.number = null;
      this.issue = null;
      this.dateIssue = null;
    };

    passport.prototype.initialize = function(document) {
      this.type = document.type;

      this.series = document.series;
      this.number = document.number;
      this.issue = document.issue;
      this.dateIssue = document.dateIssue;
    };

    passport.prototype.toString = function() {
      return this.series + this.number + ' ' + this.issue + ' ' + this.dateIssue;
    };

    return passport;
  }
]);
