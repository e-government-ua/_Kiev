angular.module('app').factory('FileFactory', function($q, $rootScope, ActivitiService, uiUploader) {
  var file = function() {
    this.fileName = null;
    this.value = null;
  };

  file.prototype.remove = function(file) {
    uiUploader.remove(file);
  };

  file.prototype.removeAll = function() {
    uiUploader.removeAll();
  };

  file.prototype.setFiles = function(files) {
    uiUploader.removeAll();
    uiUploader.addFiles(files);

    this.fileName = files[0].name;
  };

  file.prototype.addFiles = function(files) {
    //files[0].name = "123" +  files[0].name;
    console.log('files[0].name='+files[0].name);
    uiUploader.addFiles(files);

    this.fileName = files[0].name;
  };

  file.prototype.upload = function(oServiceData) {
    var self = this;
    var scope = $rootScope.$new(true, $rootScope);
    uiUploader.startUpload({
      url: ActivitiService.getUploadFileURL(oServiceData)+'&originalFilename='+encodeURIComponent(this.fileName),
      data: {'originalFilename': this.fileName},
      concurrency: 1,
      onProgress: function(file) {
        scope.$apply(function() {
        });
      },
      onCompleted: function(file, response) {
        scope.$apply(function() {
          self.value = response;
        });
      },
      onCompletedAll: function() {
        console.log('All files loaded successfully');
      }
    });
  };

  file.prototype.uploadDocument = function() {
    console.log('test');
  };

  file.prototype.get = function() {
    return this.value;
  };

  return file;
});