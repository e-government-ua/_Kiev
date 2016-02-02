angular.module('documents').controller('DocumentsUserController', function($scope, $state, $location, $window, BankIDService, ErrorsFactory) {

  $scope.authProcess = false;

  if ($state.params.error) {
    var oFuncNote = {sHead:"Документи", sFunc:"DocumentsUserController"};
    ErrorsFactory.init(oFuncNote, {asParam:['$state.params.error: '+$state.params.error]});
      
    var sErrorText = $state.params.error;
    try {
      sErrorText = JSON.parse($state.params.error).error;
      ErrorsFactory.addFail({sBody:'Помилка контролера!',asParam:['sErrorText: '+sErrorText]});
    } catch (sError) {
      ErrorsFactory.addFail({sBody:'Помилка парсінгу помилки контролера!',sError:sError});
      //sErrorText = $state.params.error;
    }
    /*
    ErrorsFactory.push({
      type: "danger",
      text:  errorText
    });
    */
  }

  BankIDService.isLoggedIn().then(function() {
    $scope.loading = true;
    return $state.go('index.documents.content').finally(function() {
      $scope.loading = false;
    });
  }).catch(function() {
    return $state.go('index.documents.bankid');
  });

});
