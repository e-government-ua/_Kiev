'use strict';
angular.module('dashboardJsApp').controller('TasksCtrl', function ($scope, $window, tasks, processes, Modal, Auth, PrintTemplate, $localStorage) {
  $scope.tasks = [];
  $scope.selectedTasks = {};
  $scope.sSelectedTask = "";
  $scope.$storage = $localStorage.$default({
    menuType: tasks.filterTypes.selfAssigned
  });
  $scope.menus = [{
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

  $scope.predicate = 'createTime';
  $scope.reverse = false;
  $scope.sort_order = 'order_increase';
  $scope.order = function(predicate, reverse) {
    $scope.reverse = reverse;
    $scope.predicate = predicate;
  };
  $scope.sortOrder = function() {
    switch($scope.sort_order) {
      case 'order_increase': $scope.reverse = false; break;
      case 'order_decrease': $scope.reverse = true; break;
    }
  }

  $scope.printTemplate = new PrintTemplate();

  $scope.print = function () {
    if ($scope.selectedTask && $scope.taskForm) {
      if($scope.hasUnPopulatedFields()){
        Modal.inform.error()('Не всі поля заповнені!');
        return;
      }
      $scope.printTemplate.task = $scope.selectedTask;
      $scope.printTemplate.form = $scope.taskForm;
      $scope.printTemplate.showPrintModal = !$scope.printTemplate.showPrintModal;
    }
  };

  $scope.hasUnPopulatedFields = function () {
    if ($scope.selectedTask && $scope.taskForm) {
      var unpopulated = $scope.taskForm.filter(function (item) {
        return (item.value === undefined || item.value === null || item.value.trim() === "") && item.required;//&& item.type !== 'file'
      });
      return unpopulated.length > 0;
    } else {
      return true;
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

  $scope.applyTaskFilter = function (menuType, nID_Task) {
    $scope.sSelectedTask = $scope.$storage.menuType;
    $scope.selectedTask = $scope.selectedTasks[menuType];
    $scope.$storage.menuType = menuType;
    $scope.taskForm = null;
    $scope.taskId = null;
    $scope.attachments = null;
    $scope.error = null;
    $scope.taskAttachments = null;

    tasks
      .list(menuType)
      .then(function (result) {
        result = JSON.parse(result);
        var tasks = _.filter(result.data, function (task) {
          return task.endTime !== null;
        });
        $scope.tasks = tasks;
        updateTaskSelection(nID_Task);
      })
      .catch(function (err) {
        $scope.errors.other = err.message;
      });
  };

  $scope.selectTask = function (task) {
    $scope.sSelectedTask = $scope.$storage.menuType;
    $scope.selectedTask = task;
    $scope.selectedTasks[$scope.$storage.menuType] = task;
    $scope.taskForm = null;
    $scope.taskId = task.id;
    $scope.attachments = null;
    $scope.error = null;
    $scope.taskAttachments = null;

    if (task.endTime) {
      tasks
        .taskFormFromHistory(task.id)
        .then(function (result) {
          result = JSON.parse(result);
          $scope.taskForm = result.data[0].variables;
          $scope.taskForm = addIndexForFileItems($scope.taskForm);
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
      if($scope.hasUnPopulatedFields()){
        Modal.inform.error()('Не всі поля заповнені!');
        return;
      }
      tasks.submitTaskForm($scope.selectedTask.id, $scope.taskForm)
        .then(function (result) {
          Modal.inform.success(function (event) {
            $scope.selectedTasks[$scope.$storage.menuType] = null;
            loadTaskCounters();
            $scope.applyTaskFilter($scope.$storage.menuType);
          })('Форму відправлено' + (result && result.length > 0 ? (': ' + result) : ''));
        })
        .catch(defaultErrorHandler);
    }
  };

  $scope.assignTask = function () {
    tasks.assignTask($scope.selectedTask.id, Auth.getCurrentUser().id).then(function (result) {
      Modal.assignTask(function (event) {
        $scope.selectedTasks[$scope.$storage.menuType] = null;
        loadTaskCounters();
        console.log("$scope.selectedTask.id="+$scope.selectedTask.id)
        $scope.applyTaskFilter($scope.menus[0].type, $scope.selectedTask.id);
      }, 'Задача у вас в роботі');
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

  $scope.sDateShort = function (sDateLong) {
    if (sDateLong !== null) {
      var o = new Date(sDateLong); //'2015-04-27T13:19:44.098+03:00'
      return o.getFullYear() + '-' + (o.getMonth() + 1) + '-' + o.getDate() + ' ' + o.getHours() + ':' + o.getMinutes();
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



  $scope.aPatternPrintNew = function (taskForm) {
//    console.log("[aPatternPrintNew]")
    var printTemplateResult = null;
    if(taskForm){//this.form
        printTemplateResult = taskForm.filter(function (item) {//form//this.form
            //if(item.id && item.id.indexOf('sBody') >= 0 && item.value !== "" ){
          return item.id && item.id.indexOf('sBody') >= 0 && item.value !== "";//item.id === s
        });
//        console.log("[aPatternPrintNew]printTemplateResult.length="+printTemplateResult.length)
    }
    //return printTemplateResult.length !== 0 ? printTemplateResult[0].value : "";
    return (printTemplateResult!==null && printTemplateResult.length !== 0) ? printTemplateResult : [];
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
    var s = sValue.substring(nAt + 5 + 1 + 1, nTo - 1 - 6);
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
    console.log("$scope.init");
    loadTaskCounters();
    loadSelfAssignedTasks();
  };

  $scope.searchTaskByOrder = function() {
    if (!/^\d+$/.test($scope.orderIdInput)) {
      Modal.inform.error()('ID має складатися тільки з цифр!');
      return;
    }
    tasks.getTasksByOrder($scope.orderIdInput)
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
    console.log("[loadSelfAssignedTasks]");
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
        _.forEach($scope.tasks, function (oItem) {
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
        } else if ($scope.tasks[0]) {
          $scope.selectTask($scope.tasks[0]);
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
    Modal.inform.error()(msg);
  }
});
