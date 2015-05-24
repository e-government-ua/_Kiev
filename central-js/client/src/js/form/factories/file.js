define('file/factory', ['angularAMD'], function (angularAMD) {
    angularAMD.factory('FileFactory', ['$q', '$rootScope', 'ActivitiService', 'uiUploader', function ($q, $rootScope, ActivitiService, uiUploader) {
        var file = function () {
            this.value = null;
        };
		
		file.prototype.remove = function(file) {
			uiUploader.remove(file);
		};
		
		file.prototype.removeAll = function() {
			uiUploader.removeAll();
		};
		
		file.prototype.addFiles = function(files) {
			uiUploader.addFiles(files);
		};
		
		file.prototype.upload = function(oServiceData) {
			var scope = $rootScope.$new(true, $rootScope);
			
            uiUploader.startUpload({
                url: ActivitiService.getUploadFileURL(oServiceData),
                concurrency: 1,
                onProgress: function(file) {
					scope.$apply(function() {
						console.log('onProgress', file);
					});
				},
                onCompleted: function(file, response) {
					scope.$apply(function() {
						console.log('onCompleted', file);
					});
				}
            });
		};
		
		file.prototype.get = function() {
			return this.value;
		};

        return file;
    }]);
});