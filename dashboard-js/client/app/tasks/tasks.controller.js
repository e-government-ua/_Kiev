'use strict';
angular.module('dashboardJsApp').controller('TasksCtrl',
    ['$scope', '$window', 'tasks', 'processes', 'Modal', 'Auth', '$localStorage', '$filter', 'lunaService', 'PrintTemplateService', 'taskFilterService',
      function ($scope, $window, tasks, processes, Modal, Auth, $localStorage, $filter, lunaService, PrintTemplateService, taskFilterService) {
  $scope.tasks = null;
  $scope.selectedTasks = {};
  $scope.sSelectedTask = "";
  $scope.taskFormLoaded = false;
  $scope.printTemplateList = [];
  $scope.printModalState = {show: false}; // wrapping in object required for 2-way binding
  $scope.taskDefinitions = taskFilterService.getTaskDefinitions();
  $scope.model = {
    printTemplate: null,
    taskDefinition: null
  };
  $scope.filterTypes = tasks.filterTypes;
  $scope.filteredTasks = null;
  $scope.$storage = $localStorage.$default({
    menuType: tasks.filterTypes.selfAssigned,
    selfAssignedTaskDefinitionFilter: $scope.taskDefinitions[0],
    unassignedTaskDefinitionFilter: $scope.taskDefinitions[0],
  });
  function restoreTaskDefinitionFilter() {
    $scope.model.taskDefinition = $scope.$storage[$scope.$storage['menuType']+'TaskDefinitionFilter'];
  };
  restoreTaskDefinitionFilter();
  $scope.taskDefinitionsFilterChange = function() {
    $scope.$storage[$scope.$storage['menuType']+'TaskDefinitionFilter'] = $scope.model.taskDefinition;
    $scope.filteredTasks = taskFilterService.getFilteredTasks($scope.tasks, $scope.model.taskDefinition);
  }
  $scope.menus = [{
    title: 'Тікети',
    type: tasks.filterTypes.tickets,
    count: 0
  }, {
    title: 'В роботі',
    type: tasks.filterTypes.selfAssigned,
    count: 0
  }, {
    title: 'Необроблені',
    type: tasks.filterTypes.unassigned,
    count: 0
  }, {
    title: 'Оброблені',
    type: tasks.filterTypes.finished,
    count: 0
  }];
  $scope.selectedSortOrder = {
    selected: "datetime_asc"
  };

  $scope.predicate = 'createTime';
  $scope.reverse = false;

  $scope.sortOrderOptions = [{ "value": 'datetime_asc', "text": "Від найдавніших" },
    { "value": 'datetime_desc', "text": "Від найновіших" }];

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
  }

  $scope.print = function () {
    if ($scope.selectedTask && $scope.taskForm) {
      if($scope.hasUnPopulatedFields()){
        Modal.inform.error()('Не всі поля заповнені!');
        return;
      }
      $scope.printModalState.show = !$scope.printModalState.show;
    }
  };

  $scope.hasUnPopulatedFields = function () {
    if ($scope.selectedTask && $scope.taskForm) {
      var unpopulated = $scope.taskForm.filter(function (item) {
        return (item.value === undefined || item.value === null || item.value.trim() === "") && (item.required|| $scope.isCommentAfterReject(item));//&& item.type !== 'file'
      });
      return unpopulated.length > 0;
    } else {
      return true;
    }
  }

  $scope.unpopulatedFields = function () {
    if ($scope.selectedTask && $scope.taskForm) {
      var unpopulated = $scope.taskForm.filter(function (item) {
        return (item.value === undefined || item.value === null || item.value.trim() === "") && (item.required|| $scope.isCommentAfterReject(item));//&& item.type !== 'file'
      });
      return unpopulated;
    } else {
      return [];
    }
  }

  $scope.isFormPropertyDisabled = function (formProperty) {
    if ($scope.selectedTask && $scope.selectedTask.assignee === null) {
      return true;
    } else if ($scope.selectedTask && $scope.selectedTask.assignee !== null && !formProperty.writable) {
      return true;
    } else if ($scope.sSelectedTask === 'finished') {
      return true;
    }
    return false;
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
    $scope.tasks = $scope.filteredTasks = null;
    $scope.sSelectedTask = $scope.$storage.menuType;
    $scope.selectedTask = resetSelectedTask ? null : $scope.selectedTasks[menuType];
    $scope.$storage.menuType = menuType;
    restoreTaskDefinitionFilter();
    $scope.taskForm = null;
    $scope.taskId = null;
    $scope.attachments = null;
    $scope.error = null;
    $scope.taskAttachments = null;
    $scope.taskFormLoaded = false;

    if (menuType == tasks.filterTypes.finished)
      $scope.predicate = 'startTime';

    var data = {};
    if ($scope.$storage.menuType == 'tickets'){
      data.bEmployeeUnassigned = $scope.ticketsFilter.bEmployeeUnassigned;
      if ($scope.ticketsFilter.dateMode == 'date' && $scope.ticketsFilter.sDate)
        data.sDate = $filter('date')($scope.ticketsFilter.sDate, 'yyyy-MM-dd');
    }

    tasks
      .list(menuType, null, data)
      .then(function (result) {
        result = JSON.parse(result);
        var tasks = _.filter(result.data, function (task) {
          return task.endTime !== null;
        });
        $scope.tasks = tasks;
        $scope.filteredTasks = taskFilterService.getFilteredTasks($scope.tasks, $scope.model.taskDefinition);
        updateTaskSelection(nID_Task);
      })
      .catch(function (err) {
        $scope.errors.other = err.message;
      });
  };

  $scope.selectTask = function (task) {
    $scope.printTemplateList = [];
    $scope.model.printTemplate = null;
    $scope.taskFormLoaded = false;
    $scope.sSelectedTask = $scope.$storage.menuType;
    $scope.selectedTask = task;
    $scope.selectedTasks[$scope.$storage.menuType] = task;
    $scope.taskForm = null;
    $scope.taskId = task.id;
    $scope.attachments = null;
    $scope.error = null;
    $scope.taskAttachments = null;

    // TODO: move common code to one function
    if (task.endTime) {
      tasks
        .taskFormFromHistory(task.id)
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
    }
    else {
      tasks
        .taskForm(task.id)
        .then(function (result) {
          result = JSON.parse(result);
          $scope.taskForm = result.formProperties;
          $scope.taskForm = addIndexForFileItems($scope.taskForm);
          $scope.printTemplateList = PrintTemplateService.getTemplates($scope.taskForm);
          if ($scope.printTemplateList.length > 0) {
            $scope.model.printTemplate = $scope.printTemplateList[0];
          }
          $scope.taskFormLoaded = true;
        })
        .catch(defaultErrorHandler);
    }

    tasks
      .taskAttachments(task.id)
      .then(function (result) {
        result = JSON.parse(result);
        $scope.attachments = result;
      })
      .catch(defaultErrorHandler);

    tasks.getTaskAttachments(task.id)
      .then(function (result) {
        $scope.taskAttachments = result;
      })
      .catch(defaultErrorHandler);



  };

  $scope.submitTask = function () {
    if ($scope.selectedTask && $scope.taskForm) {
      $scope.taskForm.isSubmitted = true;

      var unpopulatedFields = $scope.unpopulatedFields();
      if(unpopulatedFields.length>0){
        var errorMessage = 'Будь ласка, заповніть поля: ';

        if (unpopulatedFields.length==1){

          var nameToAdd = unpopulatedFields[0].name;
          if (nameToAdd.length > 50) {
            nameToAdd = nameToAdd.substr(0, 50)+"...";
          }

          errorMessage = "Будь ласка, заповніть полe '"+nameToAdd+"'";
        }
        else {
        unpopulatedFields.forEach(function(field){

          var nameToAdd = field.name;
          if (nameToAdd.length > 50) {
            nameToAdd = nameToAdd.substr(0, 50)+"...";
          }
          errorMessage = errorMessage +"'" +nameToAdd+"',<br />";
        });
        var comaIndex = errorMessage.lastIndexOf(',');
        errorMessage = errorMessage.substr(0,comaIndex);
        }
        Modal.inform.error()(errorMessage);
        return;
      }

      $scope.taskForm.isInProcess = true;

      tasks.submitTaskForm($scope.selectedTask.id, $scope.taskForm, $scope.selectedTask)
        .then(function (result) {
          Modal.inform.success(function (result) {
            $scope.lightweightRefreshAfterSubmit();
            //$scope.selectedTask = null;
          })("Форму відправлено." + (result && result.length > 0 ? (': ' + result) : ''));

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
      $scope.filteredTasks = taskFilterService.getFilteredTasks($scope.tasks, $scope.model.taskDefinition);
      $scope.taskForm.isInProcess = false;
      $scope.taskForm.isSuccessfullySubmitted = true;
      if (!$scope.tasks || !$scope.tasks[0]){
         $scope.selectedTask = null;
      }
      //$scope.selectTask($scope.tasks[0]);// - if another task should be selected
      //The next line is commented out to prevent full refresh of the page
      // $scope.applyTaskFilter($scope.$storage.menuType);

  }

  $scope.sDateShort = function (sDateLong) {
    if (sDateLong !== null) {
      var o = new Date(sDateLong); //'2015-04-27T13:19:44.098+03:00'
      //return o.getFullYear() + '-' + (o.getMonth() + 1) + '-' + o.getDate() + ' ' + o.getHours() + ':' + o.getMinutes();
      return o.getFullYear() + '-' + ((o.getMonth() + 1)>9?'':'0')+(o.getMonth() + 1) + '-' + (o.getDate()>9?'':'0')+o.getDate() + ' ' + (o.getHours()>9?'':'0')+o.getHours() + ':' + (o.getMinutes()>9?'':'0')+o.getMinutes();
      //"2015-05-21T00:40:28.801+03:00\"
    }
  };

    /*String.prototype.endsWith = function(suffix) {
        return this.indexOf(suffix, this.length - suffix.length) !== -1;
    };*/
//http://stackoverflow.com/questions/280634/endswith-in-javascript
    function endsWith(s, sSuffix) {
        if(s==null){
            return false;
        }
        return s.indexOf(sSuffix, s.length - sSuffix.length) !== -1;
    }
  $scope.sTaskClass = function (sUserTask) {
    //"_10" - подкрашивать строку - красным цветом
    //"_5" - подкрашивать строку - желтым цветом
    //"_1" - подкрашивать строку - зеленым цветом
    var sClass="";
    if(endsWith(sUserTask, "_red")){
        return "bg_red";
    }
    if(endsWith(sUserTask, "_yellow")){
        return "bg_yellow";
    }
    if(endsWith(sUserTask, "_green")){
        return "bg_green";
    }
    if(endsWith(sUserTask, "usertask1")){
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
    sDate: new Date(),
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

  $scope.searchTaskByOrder = function() {
    if (!/^\d+$/.test($scope.searchTask.orderId)) {
      Modal.inform.error()('ID має складатися тільки з цифр!');
      return;
    }
    tasks.getTasksByOrder($scope.searchTask.orderId)
      .then(function (result) {
        if (result === 'CRC-error') {
          Modal.inform.error()();
        } else if(result === 'Record not found') {
          Modal.inform.error()();
        } else {
          var tid = JSON.parse(result)[0];
          var taskFound = $scope.tasks.some(function(t) {
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
  $scope.searchTaskByText = function() {
    if (_.isEmpty($scope.searchTask.text)) {
      Modal.inform.error()('Будь-ласка введіть текст для пошуку!');
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
          result = JSON.parse(result);
          menu.count = result.data.length;
        });
    });
  }

  function loadSelfAssignedTasks() {
    processes.list().then(function (processesDefinitions) {
        console.log("[loadSelfAssignedTasks]processesDefinitions="+processesDefinitions);
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
    console.log("[updateTaskSelection]nID_Task="+nID_Task);
    if(nID_Task !== null && nID_Task !== undefined){// && $scope.tasks.length >0
        var s = null;
        _.forEach($scope.filteredTasks, function (oItem) {
            console.log("[updateTaskSelection]oItem.id="+oItem.id)
          if (oItem.id === nID_Task) {
            s = nID_Task;//oItem.name;
            $scope.selectTask(oItem);
          }
        });
        console.log("[updateTaskSelection]s="+s);
        if(s === null){
            nID_Task=null;
        }//return s;
    }else{
        nID_Task=null;
    }
    if(nID_Task === null || nID_Task === undefined){
        if ($scope.selectedTask) {
          $scope.selectTask($scope.selectedTask);
        } else if ($scope.filteredTasks && $scope.filteredTasks[0]) {
          $scope.selectTask($scope.filteredTasks[0]);
        }
    }
  }

  function mapErrorHandler(msgMapping) {
    return function (response) {defaultErrorHandler(response, msgMapping); };
  }

  function defaultErrorHandler(response, msgMapping) {
    var msg = response.status + ' ' + response.statusText + '\n' + response.data;
    try {
      var data = JSON.parse(response.data);
      if (data !== null && data !== undefined && ('code' in data) && ('message' in data)) {
        if (msgMapping !== undefined && data.message in msgMapping)
          msg = msgMapping[data.message];
        else
          msg = data.code + ' ' + data.message;
      }
    } catch (e) { console.log(e); }
    if ($scope.taskForm){
    $scope.taskForm.isSuccessfullySubmitted = false;
    $scope.taskForm.isInProcess = false;
    }
    Modal.inform.error()(msg);
  }

  $scope.isCommentAfterReject = function (item) {
    if (item.id != "comment") return false;

    var decision = $.grep($scope.taskForm, function (e) { return e.id == "decide"; });

    if (decision.length == 0) {
      // no decision
    } else if (decision.length == 1) {
      if (decision[0].value == "reject") return true;
    }
    return false;
  };

  $scope.isRequired = function (item) {
    return item.writable && (item.required || $scope.isCommentAfterReject(item));
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

}]);
