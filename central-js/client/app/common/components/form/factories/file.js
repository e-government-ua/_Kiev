angular.module('app').factory('FileFactory', function ($q, $rootScope, ActivitiService, uiUploader) {
  var file = function () {
    this.isUploading = false;
    this.fileName = null;
    this.value = null;//{fileID: 'file id from redis', oSignData : 'information about eds' }
  };

  file.prototype.createFactory = function(){
    return new file();
  };

  file.prototype.remove = function (file) {
    uiUploader.remove(file);
  };

  file.prototype.removeAll = function () {
    uiUploader.removeAll();
  };

  file.prototype.reset = function () {
    uiUploader.removeAll();
    this.fileName = null;
    this.value = null;
  };

  file.prototype.setFiles = function (files) {
    uiUploader.removeAll();
    uiUploader.addFiles(files);

    this.fileName = files[0].name;
  };

  file.prototype.addFiles = function (files) {
    //files[0].name = "123" +  files[0].name;
    console.log('files[0].name=' + files[0].name);
    uiUploader.addFiles(files);

    this.fileName = files[0].name;
  };

  file.prototype.upload = function (oServiceData) {
    var self = this;
    var scope = $rootScope.$new(true, $rootScope);
    uiUploader.startUpload({
      url: ActivitiService.getUploadFileURL(oServiceData),
      concurrency: 1,
      onProgress: function (file) {
        self.isUploading = true;
        scope.$apply(function () {
        });
      },
      onCompleted: function (file, fileid) {
        self.value = {id : fileid, signInfo: null, fromDocuments: false};
        scope.$apply(function () {
          ActivitiService.checkFileSign(oServiceData, fileid).then(function(signInfo){
            self.value.signInfo = Object.keys(signInfo).length === 0 ? null : signInfo;
          }).catch(function(error){
            self.value.signInfo = null;
          }).finally(function(){
            self.isUploading = false;
          });
        });
      },
      onCompletedAll: function () {
        console.log('All files loaded successfully');
      }
    });
  };

  file.prototype.uploadDocument = function (documentType, documentName, callback) {
    //TODO maybe we should delete it. Dont' know where it is really used. Only one place in directive fileUploadButton that is not used also
    var self = this;
    var scope = $rootScope.$new(true, $rootScope);
    uiUploader.startUpload({
      url: './api/documents/upload?'
        + 'documentType=' + documentType
        + '&documentName=' + documentName
        + '&documentFileName=' + self.fileName,
      concurrency: 1,
      onProgress: function (file) {
        scope.$apply(function () {
        });
      },
      onCompleted: function (file, fileid) {
        scope.$apply(function () {
          self.value = {id : fileid, signInfo: null, fromDocuments: true};
          callback();
        });
      },
      onCompletedAll: function () {
        console.log('All documents loaded successfully');
      }
    });
  };

  file.prototype.get = function () {
    return this.value ? this.value.id : null;
  };

  file.prototype.isFit = function(property){
    return property.type === 'file';
  };

  return file;
});
