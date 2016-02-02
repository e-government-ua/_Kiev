angular.module('app').directive('fileField', function () {
  return {
    require: 'ngModel',
    restrict: 'E',
    link: function (scope, element, attrs, ngModel) {
      var fileField = element.find('input');
      var oFile = scope.data.formData.params[ngModel.$name];

      try {
        console.log('scope.data.formData.params[ngModel.$name].fileName=' + scope.data.formData.params[ngModel.$name].fileName);
        scope.data.formData.params[ngModel.$name].fileName = "123" + scope.data.formData.params[ngModel.$name].fileName;
        console.log('scope.data.formData.params[ngModel.$name].fileName(after)=' + scope.data.formData.params[ngModel.$name].fileName);
      } catch (_) {
        console.log('ERROR:scope.data.formData.params[ngModel.$name].fileName:' + _);
      }

      fileField.bind('change', function (event) {
        scope.$apply(function () {
          if (event.target.files && event.target.files.length > 0) {
            oFile.setFiles(event.target.files);
            oFile.upload(scope.oServiceData);
            console.log('ngModel.$name=' + ngModel.$name);
          }
        });
      });

      fileField.bind('click', function (e) {
        e.stopPropagation();
      });
      element.bind('click', function (e) {
        e.preventDefault();
        fileField[0].click();
      });
    },
    template: '<p>' +
    ' <button type="button" class="btn btn-success" ng-disabled="data.formData.params[property.id].isUploading"' +
    '  <span class="glyphicon glyphicon-file" aria-hidden="true">' +
    '  </span>' +
    '  <span ng-disabled="data.formData.params[property.id].isUploading">Обрати файл</span>' +
    '  <span class="small-loading" ng-if="data.formData.params[property.id].isUploading"></span>' +
    '  <input type="file" style="display:none"  ng-disabled="data.formData.params[property.id].isUploading">' +
    ' </button>' +
    ' <br/>' +
    ' <label ng-if="data.formData.params[property.id].value">Файл: {{data.formData.params[property.id].fileName}}</label>' +
    ' <br/>' +
    ' <label ng-if="data.formData.params[property.id].value && data.formData.params[property.id].value.signInfo"  class="form-control_"> ' +
    '    Підпис: {{data.formData.params[property.id].value.signInfo.name}} ' +
    ' </label> ' +
    '</p>',
    replace: true,
    transclude: true
  };
});
