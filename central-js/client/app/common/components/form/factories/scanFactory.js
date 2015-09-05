angular.module('app').factory('ScanFactory', function() {

  var SCAN = 'SCAN_MODE';
  var FILE = 'FILE_MODE';

  var scan = function() {
    this.value = null;
    this.mode = SCAN;
  };

  scan.prototype.get = function() {
    return this.value;
  };

  scan.prototype.setScan = function(scan){
    this.scan = scan;
  };

  scan.prototype.getScan = function(){
    return this.scan;
  };

  scan.prototype.isFit = function(property){
    return property.id && property.id.startsWith('bankId_scan_');
  };

  scan.prototype.getName = function(scanType){
    return 'bankId_scan_' + scanType;
  };

  scan.prototype.SCAN = SCAN;
  scan.prototype.FILE = FILE;

  return scan;
});
