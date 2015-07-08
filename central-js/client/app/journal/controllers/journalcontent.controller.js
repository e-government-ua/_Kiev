angular.module('journal').controller('JournalContentController', function($rootScope, $scope, $state, journal) {
  $scope.journal = journal;
  angular.forEach($scope.journal, function(item, index) {
    $scope.journal[index]['sDate'] = new Date(item.sDate);
  });
});
