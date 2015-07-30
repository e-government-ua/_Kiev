angular.module('app').directive('fileField', function() {
  return {
    require: 'ngModel',
    restrict: 'E',
    link: function(scope, element, attrs, ngModel) {
      var fileField = element.find('input');

      
      try{
        console.log('scope.data.formData.params[ngModel.$name].fileName='+scope.data.formData.params[ngModel.$name].fileName);
        scope.data.formData.params[ngModel.$name].fileName="123"+scope.data.formData.params[ngModel.$name].fileName
        console.log('scope.data.formData.params[ngModel.$name].fileName(after)='+scope.data.formData.params[ngModel.$name].fileName);
      }catch(_){
        console.log('ERROR:scope.data.formData.params[ngModel.$name].fileName:'+_);
      }

      fileField.bind('change', function(event) {
        scope.$apply(function() {
          var oFile = scope.data.formData.params[ngModel.$name];
          oFile.setFiles(event.target.files);
          oFile.upload(scope.oServiceData);
          console.log('ngModel.$name='+ngModel.$name);
        });
      });

      fileField.bind('click', function(e) {
        e.stopPropagation();
      });
      element.bind('click', function(e) {
        e.preventDefault();
        fileField[0].click();
      });
    },
    template: '<form><button type="button" class="btn btn-success"><span class="glyphicon glyphicon-file" aria-hidden="true"></span><span>Обрати файл</span><input type="file" style="display:none"></button> <label ng-if="data.formData.params[property.id].value">Файл: {{data.formData.params[property.id].fileName}}</label></form>',
    replace: true,
    transclude: true
  };
});