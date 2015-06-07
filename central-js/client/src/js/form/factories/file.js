define('file/factory', ['angularAMD'], function(angularAMD) {
  angularAMD.factory('FileFactory', function($q, $rootScope, ActivitiService, uiUploader) {
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
      uiUploader.addFiles(files);

      this.fileName = files[0].name;
    };

    file.prototype.upload = function(oServiceData) {
      var self = this;
      var scope = $rootScope.$new(true, $rootScope);

      uiUploader.startUpload({
        url: ActivitiService.getUploadFileURL(oServiceData),
        concurrency: 1,
        onProgress: function(file) {
          scope.$apply(function() {
          });
        },
        onCompleted: function(file, response) {
          scope.$apply(function() {
            self.value = response;
          });
        }
      });
    };

    file.prototype.get = function() {
      return this.value;
    };

    return file;
  });
});