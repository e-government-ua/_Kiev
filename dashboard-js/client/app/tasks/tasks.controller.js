'use strict';
angular.module('dashboardJsApp').controller('TasksCtrl',
  ['$scope', '$window', 'tasks', 'processes', 'Modal', 'Auth', 'identityUser', '$localStorage', '$filter', 'lunaService',
    'PrintTemplateService', 'taskFilterService', 'MarkersFactory', 'envConfigService', 'iGovNavbarHelper',
    function ($scope, $window, tasks, processes, Modal, Auth, identityUser, $localStorage, $filter, lunaService,
              PrintTemplateService, taskFilterService, MarkersFactory, envConfigService, iGovNavbarHelper) {
      $scope.tasks = null;
      $scope.tasksLoading = false;
      $scope.selectedTasks = {};
      $scope.sSelectedTask = "";
      $scope.taskFormLoaded = false;
      $scope.checkSignState = {inProcess: false, show: false, signInfo: null, attachmentName: null};
      $scope.printTemplateList = [];
      $scope.printModalState = {show: false}; // wrapping in object required for 2-way binding
      $scope.taskDefinitions = taskFilterService.getTaskDefinitions();
      $scope.model = {
        printTemplate: null,
        taskDefinition: null,
        strictTaskDefinition: null,
        userProcess: null
      };
      envConfigService.loadConfig(function (config) {
        iGovNavbarHelper.isTest = config.bTest;
      });
      $scope.userProcesses = taskFilterService.getDefaultProcesses();
      $scope.model.userProcess = $scope.userProcesses[0];
      $scope.resetTaskFilters = function () {
        $scope.model.taskDefinition = $scope.taskDefinitions[0];
        $scope.model.strictTaskDefinition = $scope.strictTaskDefinitions[0];
        $scope.model.userProcess = $scope.userProcesses[0];
        $scope.userProcessFilterChange();
      };
      $scope.$on('taskFilter:strictTaskDefinitions:update', function (ev, data) {
        $scope.strictTaskDefinitions = data;
        // check that current model.strictTaskDefinition is present in data
        if (!data.some(function (taskDefinition) {
            if (!taskDefinition || !$scope.model.strictTaskDefinition) {
              return false;
            }
            if (taskDefinition.id == $scope.model.strictTaskDefinition.id
              && taskDefinition.name == $scope.model.strictTaskDefinition.name) {
              return true;
            }
          })) {
          $scope.model.strictTaskDefinition = data[0];
        }
      });
      taskFilterService.getProcesses().then(function (data) {
        $scope.userProcesses = data;
        $scope.userProcessesLoaded = true;
        console.log('userProcesses', data);
        restoreUserProcessesFilter();
        $scope.userProcessFilterChange();
      });
      function restoreUserProcessesFilter() {
        var storedUserProcess = $scope.$storage[$scope.$storage['menuType'] + 'UserProcessFilter'];
        if (!storedUserProcess) {
          return;
        }
        // check if stored userProcess is presented in selected userprocesses
        if ($scope.userProcesses.some(function (process) {
            if (process.sID == storedUserProcess.sID) {
              return true;
            }
          })) {
          $scope.model.userProcess = storedUserProcess;
        } else {
          $scope.model.userProcess = $scope.userProcesses[0];
        }
      }

      console.log("$scope.userProcesses", $scope.userProcesses);

      $scope.filterTypes = tasks.filterTypes;
      $scope.filteredTasks = null;
      $scope.$storage = $localStorage.$default({
        menuType: tasks.filterTypes.selfAssigned,
        selfAssignedTaskDefinitionFilter: $scope.taskDefinitions[0],
        unassignedTaskDefinitionFilter: $scope.taskDefinitions[0]
      });

      function restoreTaskDefinitionFilter() {
        $scope.model.taskDefinition = $scope.$storage[$scope.$storage['menuType'] + 'TaskDefinitionFilter'];
      }

      restoreTaskDefinitionFilter();
      $scope.taskDefinitionsFilterChange = function () {
        $scope.$storage[$scope.$storage['menuType'] + 'TaskDefinitionFilter'] = $scope.model.taskDefinition;
        $scope.filteredTasks = taskFilterService.getFilteredTasks($scope.tasks, $scope.model);
      };
      $scope.userProcessFilterChange = function () {
        $scope.$storage[$scope.$storage['menuType'] + 'UserProcessFilter'] = $scope.model.userProcess;
        $scope.filteredTasks = taskFilterService.getFilteredTasks($scope.tasks, $scope.model);
      };
      $scope.strictTaskDefinitionFilterChange = function () {
        $scope.filteredTasks = taskFilterService.getFilteredTasks($scope.tasks, $scope.model);
      };
      $scope.menus = [{
        title: 'Необроблені',
        type: tasks.filterTypes.unassigned,
        count: 0
      }, {
        title: 'В роботі',
        type: tasks.filterTypes.selfAssigned,
        count: 0
      }, {
        title: 'Мій розклад',
        type: tasks.filterTypes.tickets,
        count: 0
      }, {
        title: 'Усі',
        type: tasks.filterTypes.all,
        count: 0
      }, {
        title: 'Історія',
        type: tasks.filterTypes.finished,
        count: 0
      }];
      $scope.selectedSortOrder = {
        selected: "datetime_asc"
      };

      $scope.predicate = 'createTime';
      $scope.reverse = false;

      $scope.sortOrderOptions = [{"value": 'datetime_asc', "text": "Від найдавніших"},
        {"value": 'datetime_desc', "text": "Від найновіших"}];

      $scope.selectedSortOrderChanged = function () {
        switch ($scope.selectedSortOrder.selected) {
          case 'datetime_asc':
            if ($scope.$storage.menuType == tasks.filterTypes.finished) $scope.predicate = 'startTime';
            else $scope.predicate = 'createTime';
            $scope.reverse = false;
            break;
          case 'datetime_desc':
            if ($scope.$storage.menuType == tasks.filterTypes.finished) $scope.predicate = 'startTime';
            else $scope.predicate = 'createTime';
            $scope.reverse = true;
            break;
        }
      };

      $scope.print = function () {
        if ($scope.selectedTask && $scope.taskForm) {
          if ($scope.hasUnPopulatedFields()) {
            Modal.inform.error()('Не всі поля заповнені!');
            return;
          }
          $scope.printModalState.show = !$scope.printModalState.show;
        }
      };

      $scope.hasUnPopulatedFields = function () {
        if ($scope.selectedTask && $scope.taskForm) {
          var unpopulated = $scope.taskForm.filter(function (item) {
            return (item.value === undefined || item.value === null || item.value.trim() === "") && (item.required || $scope.isCommentAfterReject(item));//&& item.type !== 'file'
          });
          return unpopulated.length > 0;
        } else {
          return true;
        }
      };

      $scope.unpopulatedFields = function () {
        if ($scope.selectedTask && $scope.taskForm) {
          var unpopulated = $scope.taskForm.filter(function (item) {
            return (item.value === undefined || item.value === null || item.value.trim() === "") && (item.required || $scope.isCommentAfterReject(item));//&& item.type !== 'file'
          });
          return unpopulated;
        } else {
          return [];
        }
      };

      $scope.isFormPropertyDisabled = function (formProperty) {
        if (!($scope.selectedTask && $scope.selectedTask !== null)) {
          return true;
        }
        if ($scope.selectedTask.assignee === null) {
          return true;
        }
        if ($scope.sSelectedTask === null) {
          return true;
        }
        if (formProperty === null) {
          return true;
        }
        if ($scope.sSelectedTask === 'finished') {
          return true;
        }
        var sID_Field = formProperty.id;
        if (sID_Field === null) {
          return true;
        }
        console.log("sID_Field=" + sID_Field + ",formProperty.writable=" + formProperty.writable);
        if (!formProperty.writable) {
          return true;
        }
        var bNotBankID = sID_Field.indexOf("bankId") !== 0;
        console.log("sID_Field=" + sID_Field + ",bNotBankID=" + bNotBankID);
        var bEditable = bNotBankID;
        var sFieldName = formProperty.name;
        if (sFieldName === null) {
          return true;
        }
        var as = sFieldName.split(";");
        console.log("sID_Field=" + sID_Field + ",as=" + as + ",as.length=" + as.length);
        if (as.length > 2) {
          bEditable = as[2] === "writable=true" ? true : as[2] === "writable=false" ? false : bEditable;
        }
        console.log("sID_Field=" + sID_Field + ",bEditable=" + bEditable);

        return !bEditable;//false
      };

      $scope.isTaskFilterActive = function (taskType) {
        $scope.sSelectedTask = $scope.$storage.menuType;
        return $scope.$storage.menuType === taskType;
      };

      $scope.isTaskSelected = function (task) {
        return $scope.selectedTask && $scope.selectedTask.id === task.id;
      };

      $scope.hasAttachment = function () {
        return $scope.taskAttachments !== undefined && $scope.taskAttachments !== null && $scope.taskAttachments.length !== 0;
      };

      $scope.downloadAttachment = function () {
        tasks.downloadDocument($scope.selectedTask.id);
      };

      $scope.applyTaskFilter = function (menuType, nID_Task, resetSelectedTask) {
        $scope.tasksLoading = true;
        $scope.tasks = $scope.filteredTasks = null;
        $scope.sSelectedTask = $scope.$storage.menuType;
        $scope.selectedTask = resetSelectedTask ? null : $scope.selectedTasks[menuType];
        $scope.$storage.menuType = menuType;
        restoreTaskDefinitionFilter();
        restoreUserProcessesFilter();
        $scope.taskForm = null;
        $scope.taskId = null;
        $scope.nID_Process = null; //task.processInstanceId;
        $scope.attachments = null;
        $scope.aOrderMessage = null;
        $scope.error = null;
        $scope.taskAttachments = null;
        $scope.taskFormLoaded = false;

        if (menuType == tasks.filterTypes.finished){
          $scope.predicate = 'startTime';
        }

        var data = {};
        if ($scope.$storage.menuType == 'tickets') {
          data.bEmployeeUnassigned = $scope.ticketsFilter.bEmployeeUnassigned;
          if ($scope.ticketsFilter.dateMode == 'date' && $scope.ticketsFilter.sDate) {
            data.sDate = $filter('date')($scope.ticketsFilter.sDate, 'yyyy-MM-dd');
          }
        }

        tasks.list(menuType, null, data)
          .then(function (result) {
            try {
              var oResult = result;
              if (oResult.data !== null && oResult.data !== undefined) {
                var aTaskFiltered = _.filter(oResult.data, function (oTask) {
                  return oTask.endTime !== null;
                });
                $scope.tasks = aTaskFiltered;
                $scope.filteredTasks = taskFilterService.getFilteredTasks($scope.tasks, $scope.model);
                updateTaskSelection(nID_Task);
              }

            } catch (e) {
              Modal.inform.error()(e);
            }
          })
          .catch(function (err) {
            Modal.inform.error()(err);
          })
          .finally(function () {
            $scope.tasksLoading = false;
          });
      };

      $scope.getUserName = function () {
        identityUser
          .getUserInfo($scope.selectedTask.assignee)
          .then(function (userInfo) {
            return "".concat(userInfo.firstName, " ", userInfo.lastName);
          }).catch(function () {
          return $scope.selectedTask.assignee;
        });
      };

      $scope.unassign = function () {
        tasks.unassign($scope.selectedTask.id)
          .then(function () {
            $scope.selectTask($scope.selectedTask);
          })
          .then(function () {
            return tasks.getTask($scope.selectedTask.id);
          })
          .then(function (updatedTaskResult) {
            angular.copy(updatedTaskResult, $scope.selectedTask);
          })
          .catch(defaultErrorHandler);
      };

      $scope.selectTask = function (oTask) {
        $scope.printTemplateList = [];
        $scope.model.printTemplate = null;
        $scope.taskFormLoaded = false;
        $scope.sSelectedTask = $scope.$storage.menuType;

        $scope.taskForm = null;
        $scope.attachments = null;
        $scope.aOrderMessage = null;
        $scope.error = null;
        $scope.taskAttachments = null;
        $scope.clarify = false;
        $scope.clarifyFields = {};
        if (!(oTask && oTask !== null && oTask !== undefined)) {
          return;
        }

        $scope.selectedTask = oTask;
        $scope.selectedTasks[$scope.$storage.menuType] = oTask;
        $scope.taskId = oTask.id;
        $scope.nID_Process = oTask.processInstanceId;

        // TODO: move common code to one function
        if (oTask.endTime) {
          tasks
            .taskFormFromHistory(oTask.id)
            .then(function (result) {
              result = JSON.parse(result);
              $scope.taskForm = result.data[0].variables;
              $scope.taskForm = addIndexForFileItems($scope.taskForm);
              $scope.printTemplateList = PrintTemplateService.getTemplates($scope.taskForm);
              if ($scope.printTemplateList.length > 0) {
                $scope.model.printTemplate = $scope.printTemplateList[0];
              }
              $scope.taskFormLoaded = true;
            })
            .catch(defaultErrorHandler);
        } else {
          tasks
            .taskForm(oTask.id)
            .then(function (result) {
              result = JSON.parse(result);
              $scope.taskForm = result.formProperties;
              $scope.taskForm = addIndexForFileItems($scope.taskForm);
              $scope.printTemplateList = PrintTemplateService.getTemplates($scope.taskForm);
              if ($scope.printTemplateList.length > 0) {
                $scope.model.printTemplate = $scope.printTemplateList[0];
              }
              $scope.taskFormLoaded = true;
              $scope.taskForm.forEach(function (field) {
                if (field.type === 'markers' && $.trim(field.value)) {
                  var sourceObj = null;
                  try {
                    sourceObj = JSON.parse(field.value);
                  } catch (ex) {
                    console.log('markers attribute ' + field.name + ' contain bad formatted json\n' + ex.name + ', ' + ex.message + '\nfield.value: ' + field.value);
                  }
                  if (sourceObj !== null) {
                    _.merge(MarkersFactory.getMarkers(), sourceObj, function (destVal, sourceVal) {
                      if (_.isArray(sourceVal)) {
                        return sourceVal;
                      }
                    });
                  }
                }
              });
            })
            .catch(defaultErrorHandler);
        }

        tasks
          .taskAttachments(oTask.id)
          .then(function (result) {
            result = JSON.parse(result);
            $scope.attachments = result;
          })
          .catch(defaultErrorHandler);


        tasks
          .getOrderMessages(oTask.processInstanceId)
          .then(function (result) {
            result = JSON.parse(result);
            $scope.aOrderMessage = result;
          })
          .catch(defaultErrorHandler);


        tasks.getTaskAttachments(oTask.id)
          .then(function (result) {
            $scope.taskAttachments = result;
          })
          .catch(defaultErrorHandler);


      };

      $scope.submitTask = function () {
        if ($scope.selectedTask && $scope.taskForm) {
          $scope.taskForm.isSubmitted = true;

          var unpopulatedFields = $scope.unpopulatedFields();
          if (unpopulatedFields.length > 0) {
            var errorMessage = 'Будь ласка, заповніть поля: ';

            if (unpopulatedFields.length == 1) {

              var nameToAdd = unpopulatedFields[0].name;
              if (nameToAdd.length > 50) {
                nameToAdd = nameToAdd.substr(0, 50) + "...";
              }

              errorMessage = "Будь ласка, заповніть полe '" + nameToAdd + "'";
            }
            else {
              unpopulatedFields.forEach(function (field) {

                var nameToAdd = field.name;
                if (nameToAdd.length > 50) {
                  nameToAdd = nameToAdd.substr(0, 50) + "...";
                }
                errorMessage = errorMessage + "'" + nameToAdd + "',<br />";
              });
              var comaIndex = errorMessage.lastIndexOf(',');
              errorMessage = errorMessage.substr(0, comaIndex);
            }
            Modal.inform.error()(errorMessage);
            return;
          }

          $scope.taskForm.isInProcess = true;

          tasks.submitTaskForm($scope.selectedTask.id, $scope.taskForm, $scope.selectedTask)
            .then(function (result) {
              //selectedTask
              // $scope.taskForm
              var sMessage = "Форму відправлено.";
              angular.forEach($scope.taskForm, function (oField) {
                if (oField.id === "sNotifyEvent_AfterSubmit") {
                  sMessage = oField.value;
                }
              });


              Modal.inform.success(function (result) {
                $scope.lightweightRefreshAfterSubmit();
              })(sMessage + " " + (result && result.length > 0 ? (': ' + result) : ''));

            })
            .catch(defaultErrorHandler);
        }
      };

      $scope.assignTask = function () {
        $scope.taskForm.isInProcess = true;

        tasks.assignTask($scope.selectedTask.id, Auth.getCurrentUser().id)
          .then(function (result) {
            Modal.assignTask(function (event) {
              //$scope.lightweightRefreshAfterSubmit();
              $scope.selectedTasks['unassigned'] = null;
              $scope.applyTaskFilter($scope.menus[1].type, $scope.selectedTask.id);
            }, 'Задача у вас в роботі', $scope.lightweightRefreshAfterSubmit);

          })
          .catch(defaultErrorHandler());

      };

      $scope.upload = function (files, propertyID) {
        tasks.upload(files, $scope.taskId).then(function (result) {
          var filterResult = $scope.taskForm.filter(function (property) {
            return property.id === propertyID;
          });
          if (filterResult && filterResult.length === 1) {
            filterResult[0].value = result.response.id;
            filterResult[0].fileName = result.response.name;
          }
        }).catch(function (err) {
          Modal.inform.error()('Помилка. ' + err.code + ' ' + err.message);
        });
      };

      $scope.lightweightRefreshAfterSubmit = function () {
        //lightweight refresh only deletes the submitted task from the array of current type of tasks
        //so we don't need to refresh the whole page
        $scope.selectedTasks[$scope.$storage.menuType] = null;
        loadTaskCounters();
        $scope.tasks = $.grep($scope.tasks, function (e) {
          return e.id != $scope.selectedTask.id;
        });
        $scope.filteredTasks = taskFilterService.getFilteredTasks($scope.tasks, $scope.model);
        $scope.taskForm.isInProcess = false;
        $scope.taskForm.isSuccessfullySubmitted = true;
        if (!$scope.tasks || !$scope.tasks[0]) {
          $scope.selectedTask = null;
        }
      };

      $scope.sDateShort = function (sDateLong) {
        if (sDateLong !== null) {
          var o = new Date(sDateLong); //'2015-04-27T13:19:44.098+03:00'
          return o.getFullYear() + '-' + ((o.getMonth() + 1) > 9 ? '' : '0') + (o.getMonth() + 1) + '-' + (o.getDate() > 9 ? '' : '0') + o.getDate() + ' ' + (o.getHours() > 9 ? '' : '0') + o.getHours() + ':' + (o.getMinutes() > 9 ? '' : '0') + o.getMinutes();
        }
      };

      function endsWith(s, sSuffix) {
        if (s == null) {
          return false;
        }
        return s.indexOf(sSuffix, s.length - sSuffix.length) !== -1;
      }

      $scope.sTaskClass = function (sUserTask) {
        //"_10" - подкрашивать строку - красным цветом
        //"_5" - подкрашивать строку - желтым цветом
        //"_1" - подкрашивать строку - зеленым цветом
        var sClass = "";
        if (endsWith(sUserTask, "_red")) {
          return "bg_red";
        }
        if (endsWith(sUserTask, "_yellow")) {
          return "bg_yellow";
        }
        if (endsWith(sUserTask, "_green")) {
          return "bg_green";
        }
        if (endsWith(sUserTask, "usertask1")) {
          return "bg_first";
        }
      };

      $scope.sFieldLabel = function (sField) {
        var s = '';
        if (sField !== null) {
          var a = sField.split(';');
          s = a[0].trim();
        }
        return s;
      };

      $scope.nID_FlowSlotTicket_FieldQueueData = function (sValue) {
        var nAt = sValue.indexOf(":");
        var nTo = sValue.indexOf(",");
        var s = sValue.substring(nAt + 1, nTo);
        var nID_FlowSlotTicket = 0;
        try {
          nID_FlowSlotTicket = s;
        } catch (_) {
          nID_FlowSlotTicket = 1;
        }
        return nID_FlowSlotTicket;
      };

      $scope.sDate_FieldQueueData = function (sValue) {
        var nAt = sValue.indexOf("sDate");
        var nTo = sValue.indexOf("}");
        var s = sValue.substring(nAt + 5 + 1 + 1 + 1, nTo - 1 - 6);
        var sDate = "Дата назначена!";
        try {
          sDate = s;
        } catch (_) {
          sDate = "Дата назначена!";
        }
        return sDate;
      };


      $scope.sEnumValue = function (aItem, sID) {
        var s = sID;
        _.forEach(aItem, function (oItem) {
          if (oItem.id == sID) {
            s = oItem.name;
          }
        });
        return s;
      };


      $scope.sFieldNotes = function (sField) {
        var s = null;
        if (sField !== null) {
          var a = sField.split(';');
          if (a.length > 1) {
            s = a[1].trim();
            if (s === '') {
              s = null;
            }
          }
        }
        return s;
      };

      $scope.getProcessName = function (processDefinitionId) {
        return processes.getProcessName(processDefinitionId);
      };

      $scope.init = function () {
        loadTaskCounters();
        loadSelfAssignedTasks();
        $scope.taskFormLoaded = false;
      };

      $scope.ticketsFilter = {
        dateMode: 'date',
        dateModeList: [
          {key: 'all', title: 'Всі дати'},
          {key: 'date', title: 'Обрати дату'}
        ],
        sDate: moment().format('YYYY-MM-DD'),
        options: {
          timePicker: false
        },
        bEmployeeUnassigned: false
      };

      $scope.applyTicketsFilter = function () {
        $scope.applyTaskFilter($scope.$storage.menuType, null, true);
      };

      $scope.setTicketsDateMode = function (mode) {
        $scope.ticketsFilter.dateMode = mode;
        $scope.applyTicketsFilter();
      };

      $scope.lunaService = lunaService;

      $scope.searchTask = {
        orderId: null,
        text: null
      };

      $scope.searchTaskByOrder = function () {
        var sID_Order = $scope.searchTask.orderId;
        var nID_Order = 0;
        var nAt = sID_Order.indexOf("-");
        if (nAt >= 0) {
          var as = sID_Order.split("-");
          nID_Order = as[1];
          //nAt
        } else {
          nID_Order = sID_Order;
        }
        tasks.getTasksByOrder(nID_Order)//$scope.searchTask.orderId
          .then(function (result) {
            if (result === 'CRC-error') {
              ///Modal.inform.error()();
              Modal.inform.error()('Невірній номер заявки!');
            } else if (result === 'Record not found') {
              //Modal.inform.error()();
              Modal.inform.error()('Заявка не знайдена!');
            } else {
              var tid = JSON.parse(result)[0];
              var taskFound = $scope.tasks.some(function (t) {
                if (t.id == tid)
                  $scope.selectTask(t);
                return t.id == tid;
              });
              if (!taskFound) Modal.inform.warning()('У даному розділі ID не знайдено, спробуйте виконати пошук у суміжних');
            }
          }).catch(mapErrorHandler({'CRC Error': 'Неправильний ID', 'Record not found': 'ID не знайдено'}));
      };
      var searchResult = {
        tasks: []
      };
      $scope.searchTaskByText = function () {
        if (_.isEmpty($scope.searchTask.text)) {
          Modal.inform.error()('Будь ласка, введіть текст для пошуку!');
          return;
        }
        if (_.isEqual($scope.searchTask.text, searchResult.text) && !_.isEmpty(searchResult.tasks)) {
          var taskId = searchResult.tasks[0];
          searchResult.tasks = _.rest(searchResult.tasks);
          $scope.tasks.some(function (task) {
            if (task.id === taskId) {
              $scope.selectTask(task);
            }
            return task.id === taskId;
          });
          return;
        }
        tasks.getTasksByText($scope.searchTask.text, $scope.sSelectedTask)//sType
          .then(function (result) {
            if (result === 'CRC-error') {
              Modal.inform.error()();
              return;
            }
            if (result === 'Record not found') {
              Modal.inform.error()();
              return;
            }
            searchResult.text = $scope.searchTask.text;
            searchResult.tasks = JSON.parse(result);
            var taskId = searchResult.tasks[0];
            var taskFound = $scope.tasks.some(function (task) {
              if (task.id === taskId) {
                $scope.selectTask(task);
              }
              return task.id === taskId;
            });
            if (!taskFound) {
              Modal.inform.warning()('У даному розділі нічого не знайдено, спробуйте виконати пошук у суміжних');
            }
          }).catch(mapErrorHandler({'CRC Error': 'Неправильний ID', 'Record not found': 'ID не знайдено'}));
      };

      function loadTaskCounters() {
        _.forEach($scope.menus, function (menu) {
          tasks.list(menu.type)
            .then(function (result) {
              try {
                result = JSON.parse(result);
              } catch (e) {
                result = result;
              }
              menu.count = result.data.length;
            });
        });
      }

      function loadSelfAssignedTasks() {
        processes.list().then(function (processesDefinitions) {
          console.log("[loadSelfAssignedTasks]processesDefinitions=" + processesDefinitions);
          $scope.applyTaskFilter($scope.$storage.menuType);
        }).catch(defaultErrorHandler);
      }

      function addIndexForFileItems(val) {
        var idx = 0;
        return (val || []).map(function (item) {
          if (item.type === 'file') {
            item.nFileIdx = idx;
            idx++;
          }
          return item;
        });
      }

      function updateTaskSelection(nID_Task) {
        console.log("[updateTaskSelection]nID_Task=" + nID_Task);
        if (nID_Task !== null && nID_Task !== undefined) {// && $scope.tasks.length >0
          var s = null;
          _.forEach($scope.filteredTasks, function (oItem) {
            console.log("[updateTaskSelection]oItem.id=" + oItem.id)
            if (oItem.id === nID_Task) {
              s = nID_Task;//oItem.name;
              $scope.selectTask(oItem);
            }
          });
          console.log("[updateTaskSelection]s=" + s);
          if (s === null) {
            nID_Task = null;
          }//return s;
        } else {
          nID_Task = null;
        }
        if (nID_Task === null || nID_Task === undefined) {
          if ($scope.selectedTask) {
            $scope.selectTask($scope.selectedTask);
          } else if ($scope.filteredTasks && $scope.filteredTasks[0]) {
            $scope.selectTask($scope.filteredTasks[0]);
          }
        }
      }

      function mapErrorHandler(msgMapping) {
        return function (response) {
          defaultErrorHandler(response, msgMapping);
        };
      }

      function defaultErrorHandler(response, msgMapping) {
        var msg = response.status + ' ' + response.statusText + '\n' + response.data;
        try {
          try {
            var data = JSON.parse(response.data);
          } catch (e) {
            var data = response.data;
          }
          if (data !== null && data !== undefined && ('code' in data) && ('message' in data)) {
            if (msgMapping !== undefined && data.message in msgMapping)
              msg = msgMapping[data.message];
            else
              msg = data.code + ' ' + data.message;
          }
        } catch (e) {
          console.log(e);
        }
        if ($scope.taskForm) {
          $scope.taskForm.isSuccessfullySubmitted = false;
          $scope.taskForm.isInProcess = false;
        }
        Modal.inform.error()(msg);
      }

      $scope.isCommentAfterReject = function (item) {
        if (item.id != "comment") return false;

        var decision = $.grep($scope.taskForm, function (e) {
          return e.id == "decide";
        });

        if (decision.length == 0) {
          // no decision
        } else if (decision.length == 1) {
          if (decision[0].value == "reject") return true;
        }
        return false;
      };

      $scope.isRequired = function (item) {
        return !$scope.isFormPropertyDisabled(item) && (item.required || $scope.isCommentAfterReject(item)); //item.writable
      };

      $scope.isTaskSubmitted = function (item) {
        return $scope.taskForm.isSubmitted;
      };

      $scope.isTaskSuccessfullySubmitted = function () {
        if ($scope.selectedTask && $scope.taskForm) {
          if ($scope.taskForm.isSuccessfullySubmitted != undefined && $scope.taskForm.isSuccessfullySubmitted)
            return true;
        }
        return false;
      };


      $scope.isTaskInProcess = function () {
        if ($scope.selectedTask && $scope.taskForm) {
          if ($scope.taskForm.isInProcess != undefined && $scope.taskForm.isInProcess)
            return true;
        }
        return false;
      };

      $scope.clarify = false;

      $scope.clarifyToggle = function () {
        $scope.clarify = !$scope.clarify;
      };

      $scope.clarifyFields = {};
      $scope.clarifyModel = {
        sBody: ''
      };

      $scope.clarifySend = function () {
        var oData = {
          //nID_Protected: $scope.taskId,
          //nID_Order: $scope.nID_Process,
          nID_Process: $scope.nID_Process,
          saField: '',
          sMail: '',
          sBody: $scope.clarifyModel.sBody
        };
        var aFields = [];
        angular.forEach($scope.taskForm, function (item) {
          if (angular.isDefined($scope.clarifyFields[item.id]) && $scope.clarifyFields[item.id].clarify)
            aFields.push({
              sID: item.id,
              sName: $scope.sFieldLabel(item.name),
              sType: item.type,
              sValue: item.value,
              sValueNew: item.value,
              sNotify: $scope.clarifyFields[item.id].text
            });

          if (item.id == 'email')
            oData.sMail = item.value;
        });
        oData.saField = JSON.stringify(aFields);
        tasks.setTaskQuestions(oData).then(function () {
          $scope.clarify = false;
          Modal.inform.success(function () {
          })('Зауваження відправлено успішно');
        });
      };

      $scope.checkAttachmentSign = function (nID_Task, nID_Attach, attachmentName) {
        $scope.checkSignState.inProcess = true;
        tasks.checkAttachmentSign(nID_Task, nID_Attach).then(function (signInfo) {
          if (signInfo.customer) {
            $scope.checkSignState.show = !$scope.checkSignState.show;
            $scope.checkSignState.signInfo = signInfo;
            $scope.checkSignState.attachmentName = attachmentName;
          } else if (signInfo.code) {
            $scope.checkSignState.show = false;
            $scope.checkSignState.signInfo = null;
            $scope.checkSignState.attachmentName = null;
            Modal.inform.warning()(signInfo.message);
          } else {
            $scope.checkSignState.show = false;
            $scope.checkSignState.signInfo = null;
            $scope.checkSignState.attachmentName = null;
            Modal.inform.warning()('Немає підпису');
          }
        }).catch(function (error) {
          $scope.checkSignState.show = false;
          $scope.checkSignState.signInfo = null;
          $scope.checkSignState.attachmentName = null;
          Modal.inform.error()(error.message);
        }).finally(function () {
          $scope.checkSignState.inProcess = false;
        });
      }
    }]);
