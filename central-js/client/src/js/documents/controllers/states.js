define('state/documents/controller', ['angularAMD'], function (angularAMD) {
    angularAMD.controller('DocumentsController', ['$state', '$scope', 'config', 'BankIDService', function ($state, $scope, config, BankIDService) {
        $scope.config = config;

        $scope.loading = false;
        BankIDService.isLoggedIn().then(function () {
            if ($state.is('documents')) {
                $scope.loading = true;
                return $state.go('documents.content').finally(function () {
                    $scope.loading = false;
                });
            }
        }).catch(function () {
            if ($state.is('documents')) {
                return $state.go('documents.bankid');
            }
        });

    }]);
});
define('state/documents/bankid/controller', ['angularAMD'], function (angularAMD) {
    angularAMD.controller('DocumentsBankIdController', function ($scope, $state, $location, $window, BankIDService) {
        $scope.authProcess = false;
        $scope.error = undefined;

        $scope.loginWithBankId = function () {
            var stateForRedirect = $state.href('documents.bankid', {error: ''});
            var redirectURI = $location.protocol() +
                '://' + $location.host() + ':'
                + $location.port()
                + stateForRedirect;
            $window.location.href = './auth/bankID?link=' + redirectURI;
        };

        $scope.loginWithEds = function () {
            var stateForRedirect = $state.href('documents.bankid', {error: ''});
            var redirectURI = $location.protocol() +
                '://' + $location.host() + ':'
                + $location.port()
                + stateForRedirect;
            $window.location.href = './auth/eds?link=' + redirectURI;
        };

        if ($state.is('documents.bankid')) {
            if (!$state.params.error) {
                BankIDService.isLoggedIn().then(function () {
                    $scope.authProcess = true;
                    return $state.go('documents.content').catch(function (fallback) {
                        $state.go('documents.bankid', {error: fallback.error});
                    }).finally(function () {
                        $scope.authProcess = false;
                    });
                });
            } else {
                try {
                    $scope.error = JSON.parse($state.params.error).error;
                } catch (error) {
                    $scope.error = $state.params.error;
                }
            }
        }
    });
});
define('state/documents/content/controller', ['angularAMD'], function (angularAMD) {
    angularAMD.controller('DocumentsContentController',
        function ($scope, $state, documents, FileFactory, ServiceService, $modal) {
            var file = new FileFactory();
            $scope.file = file;

            angular.forEach(documents, function (item) {
                if (item.oDate_Upload === null) {
                    item.oDate_Upload = new Date();
                }
            });
            $scope.documents = documents;
            $scope.sTelephone = '+380';
            $scope.nDaysOptions = [{day: 1, title: '1 день'}, {day: 7, title: '1 тиждень'}, {day: 365, title: '1 рік'}];
            $scope.nDays = $scope.nDaysOptions[1];
            $scope.getDocumentLink = ServiceService.getDocumentLink;

            $scope.shareLink = function (document, sFIO, sTelephone, sMail, nDays) {
                ServiceService.shareLink($state.nID_Subject, document.nID, sFIO,
                    getTelephone(sTelephone), sMail, getDaysInMilliseconds(nDays))
                    .then(showConfirmationModal);
            };

            function getTelephone(sTelephone) {
                if (sTelephone == '+380') {
                    return ' '
                }
                return sTelephone;
            }

            function getDaysInMilliseconds(nDays) {
                if (isNaN(nDays.day * 86400000)) {
                    return 7 * 86400000;
                }
                return nDays * 86400000;
            }

            function showConfirmationModal(reply) {
                if (reply.code) {
                    showShareLinkError(reply);
                    return;
                }
                showShareLinkSuccess(reply);
            }

            function showShareLinkSuccess(reply) {
                $modal.open({
                    animation: true,
                    templateUrl: 'urlmodal.html',
                    controller: 'ModalController',
                    size: '',
                    resolve: {
                        url: function () {
                            return reply.value
                        }
                    }
                });
            }

            function showShareLinkError(reply) {
                switch (reply.code) {
                    case 'BUSINESS_ERR':
                        alert("Сталася помилка\n" + reply.code + ': ' + reply.message);
                        break;
                    default:
                        alert("Сталася помилка\n" + reply.code + ': ' + reply.message);
                }
            }
        });

    angularAMD.controller('ModalController',
        function ($scope, $modalInstance, url) {
            $scope.url = url;

            $scope.close = function () {
                $modalInstance.close();
            };
        });
})
;
