angular.module('app')
  .factory('EditServiceTreeFactory', function($http, $modal, CatalogService, messageBusService) {

    var createObjectOnlyWithChanges = function(editedObject, originalObject){
      var objectToSend = {
        nID: editedObject.nID
      };

      var addChangedPropertyToObject = function(propertyName){
        if (!originalObject || originalObject[propertyName] !== editedObject[propertyName]){
          objectToSend[propertyName] = editedObject[propertyName];
        }
      };

      addChangedPropertyToObject('sID');
      addChangedPropertyToObject('sName');
      addChangedPropertyToObject('nOrder');
      addChangedPropertyToObject('sSubjectOperatorName');

      return objectToSend;
    };

    var initCatalogUpdate = function(){
        messageBusService.publish('catalog:initUpdate');
    };

    var openDeletionModal = function(modalTitle, message, confirmationLevel){
      var modalInstance = $modal.open({
        animation: true,
        size: 'md',
        templateUrl: 'app/common/factories/editor/deleteItem.html',
        controller: 'DeletionModalController',
        resolve: {
          title: function() {
            return modalTitle;
          },
          message: function() {
            return message;
          },
          level: function(){
            return confirmationLevel;
          }
        }
      });

      return modalInstance.result;
    };

    var categoryEditor = (function(){
      var openModal = function (category) {
        var modalInstance = $modal.open({
          animation: true,
          size: 'lg',
          templateUrl: 'app/common/factories/editor/editCategory.html',
          controller: 'EditorModalController',
          resolve: {
            entityToEdit: function () {
              if (category){
                return {
                  nID: category.nID,
                  sID: category.sID,
                  sName: category.sName,
                  nOrder: category.nOrder
                };
              }else{
                return undefined;
              }
            }
          }
        });

        modalInstance.result.then(function (editedCategory) {
          var categoryToSend = createObjectOnlyWithChanges(editedCategory, category);
          var catalogToSend = [ categoryToSend ];
          CatalogService.setServicesTree(catalogToSend)
            .then(function(){
              initCatalogUpdate();
            });
        });
      };

      return {
        add: function(){
          openModal();
        },
        edit: function(category){
          openModal(category)
        },
        remove: function(category){
          var title = 'Видалення категоії';
          var message = 'Ви впевнені, що бажаєте видалити категорію ' + category.sName + '?';
          var numberOfSubcategories =  category.aSubcategory.length;
          if (numberOfSubcategories > 1){
            message = 'Ви намагєтесь видалити категорію з ' + numberOfSubcategories + ' підкатегоріями. ' + message;
          } else if (numberOfSubcategories === 1){
            message = 'Ви намагєтесь видалити категорію з однією підкатегорію. ' + message;
          }

          var confirmationLevel = 0;
          if (numberOfSubcategories > 0) {
            confirmationLevel = 2;
          }

          openDeletionModal(title, message, confirmationLevel)
            .then(function(){
              CatalogService.removeCategory(category.nID, true)
                .then(function(){
                  initCatalogUpdate();
                });
            });
        }
      }
    })();

    var subcategoryEditor = (function(){

      var openModal = function (categoryId, subcategory) {
        var modalInstance = $modal.open({
          animation: true,
          size: 'lg',
          templateUrl: 'app/common/factories/editor/editSubcategory.html',
          controller: 'EditorModalController',
          resolve: {
            entityToEdit: function () {
              if (subcategory){
                return {
                  nID: subcategory.nID,
                  sID: subcategory.sID,
                  sName: subcategory.sName,
                  nOrder: subcategory.nOrder
                }
              }
              return undefined;
            }
          }
        });

        modalInstance.result.then(function (editedSubcategory) {
          var subcategoryToSend = createObjectOnlyWithChanges(editedSubcategory, subcategory);

          if (!subcategoryToSend.nID){
            subcategoryToSend.oCategory = {
              nID : categoryId
            };
          }

          var catalogToSend = [
            {
              nID: categoryId,
              aSubcategory: [ subcategoryToSend ]
            }
          ];

          CatalogService.setServicesTree(catalogToSend)
            .then(function(){
              initCatalogUpdate();
            });
        });
      };

      return {
        add: function(categoryId){
          openModal(categoryId);
        },
        edit: function(categoryId, subcategory){
          openModal(categoryId, subcategory)
        },
        remove: function(subcategory){
          var title = 'Видалення підкатегорії';
          var message = 'Ви впевнені, що бажаєте видалити підкатегорію ' + subcategory.sName + '?';
          var numberOfServices = subcategory.aService.length;
          if (numberOfServices > 1){
            message = 'Ви намагєтесь видалити підкатегорію з ' + numberOfServices + ' послугами. ' + message;
          } else if (numberOfServices === 1){
            message = 'Ви намагєтесь видалити підкатегорію з однією послугою. ' + message;
          }

          var confirmationLevel = 0;
          if (numberOfServices > 10){
            confirmationLevel = 2;
          } else if (numberOfServices > 0){
            confirmationLevel = 1;
          }

          openDeletionModal(title, message, confirmationLevel)
            .then(function() {
              CatalogService.removeSubcategory(subcategory.nID, true)
                .then(function(){
                  initCatalogUpdate();
                });
            });
        }
      }
    })();

    var serviceEditor = (function(){
      var openModal = function (categoryId, subcategoryId, service) {

        var modalInstance = $modal.open({
          animation: true,
          size: 'lg',
          templateUrl: 'app/common/factories/editor/editService.html',
          controller: 'EditorModalController',
          resolve: {
            entityToEdit: function () {
              if (service){
                return {
                  nID: service.nID,
                  sID: service.sID,
                  sName: service.sName,
                  nOrder: service.nOrder,
                  sSubjectOperatorName: service.sSubjectOperatorName
                }
              }
              return undefined;
            }
          }
        });

        modalInstance.result.then(function (editedService) {
          var serviceToSend = createObjectOnlyWithChanges(editedService, service);

          if (!serviceToSend.nID){
            serviceToSend.oSubcategory = {
              nID : subcategoryId
            };
            serviceToSend.sFAQ  = "";
            serviceToSend.sInfo  = "";
            serviceToSend.sLaw  = "";
          }

          var catalogToSend = [
            {
              nID: categoryId,
              aSubcategory: [
                {
                  nID: subcategoryId,
                  "aService": [ serviceToSend ]
                }
              ]
            }
          ];

          CatalogService.setServicesTree(catalogToSend)
            .then(function(){
              initCatalogUpdate();
            });
        });
      };

      return {
        add: function(categoryId, subcategoryId){
          openModal(categoryId, subcategoryId);
        },
        edit: function(categoryId, subcategoryId, serviceToEdit){
          openModal(categoryId, subcategoryId, serviceToEdit)
        },
        remove: function(service){
          var title = 'Видалення послуги';
          var message = 'Ви впевнені, що бажаєте видалити послугу ' + service.sName + '?';
          var numberOfSubItems = service.nSub.length;
          if (numberOfSubItems > 1){
            message = 'Ви намагєтесь видалити послугу з ' + numberOfSubItems + ' вкладеними елементами. ' + message;
          } else if (numberOfSubItems === 1){
            message = 'Ви намагєтесь видалити послугу з одним вкладеним елементом. ' + message;
          }

          var confirmationLevel = 0;
          if (numberOfSubItems > 0) {
            confirmationLevel = 1;
          }

          openDeletionModal(title, message, confirmationLevel).then(function() {
            CatalogService.removeService(service.nID, true)
              .then(function(){
                initCatalogUpdate();
              });
            });
        }
      }
    })();

    return {
      category: categoryEditor,
      subcategory: subcategoryEditor,
      service: serviceEditor
    };
});
