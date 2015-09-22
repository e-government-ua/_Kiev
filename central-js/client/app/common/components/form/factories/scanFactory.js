angular.module('app').factory('ScanFactory', function () {

  var scan = function Scan() {
    this.value = null;
    this.isLoading = false;
  };

  scan.prototype.createFactory = function(){
    return new scan();
  };

  scan.prototype.get = function () {
    return this.value;
  };

  scan.prototype.setScan = function (scan) {
    this.scan = scan;
  };

  scan.prototype.getScan = function () {
    return this.scan;
  };

  scan.prototype.isFit = function (property) {
    return property.id && property.id.startsWith('bankId_scan_');
  };

  scan.prototype.getName = function (scanType) {
    return 'bankId_scan_' + scanType;
  };

  scan.prototype.loading = function () {
    this.value = null;
    this.isLoading = true;
  };

  scan.prototype.loaded = function (valueToStore) {
    this.value = valueToStore;
    this.isLoading = false;
  };

  return scan;
});
