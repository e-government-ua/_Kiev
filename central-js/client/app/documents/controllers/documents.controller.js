angular.module('documents').controller('DocumentsController', function($state) {

  return $state.go('index.documents.user');
});
