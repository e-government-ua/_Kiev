### Custom Activiti RESTfull APi Specification

##### Mandatory HTTP Headers

| Name        | Value           |
| ------------- |:-------------:|
| Content-Type | application/json |
| Accept | application/json |
| Authorization | Basic ... |

#### 2. Authentificate user

**HTTP Metod: GET**

**HTTP Context: https://seriver:port/wf-dniprorada/serivce/auth/login**

Content-Type: application/x-www-form-urlencoded

sLogin - Логин пользователя
sPassword - Пароль пользователя

**Request**

```text
    sLogin=user&sPassword=password
```

**Response**

```json
	true
```

#### 2. Start process

**HTTP Metod: GET**

**HTTP Context: https://seriver:port/wf-dniprorada/serivce/rest/start-process/{key}**

{key} - Ключ процесса

**Response**

```json
	{
		"id":"31" //[1..1]
	}
```

#### 3. Get tasks

**HTTP Metod: GET**

**HTTP Context: https://seriver:port/wf-dniprorada/serivce/rest/tasks/{assignee}**

{assignee} - Владелец

**Response**

```json
	[                                                     //[0..N]
  		{
    		"delegationState": "RESOLVED",                //[0..1]
		    "id": "38",                                   //[1..1]
		    "name": "Первый процесс пользователя kermit", //[1..1]
		    "description": "Описание процесса",           //[0..1]
		    "priority": 51,                               //[1..1]
		    "owner": "kermit-owner",                      //[1..1]
		    "assignee": "kermit-assignee",                //[1..1]
		    "processInstanceId": "12",                    //[0..1]
		    "executionId": "1",                           //[0..1]
		    "createTime": "2015-04-13 00:51:34.527",      //[1..1]
		    "taskDefinitionKey": "task-definition",       //[0..1]
		    "dueDate": "2015-04-13 00:51:36.527",         //[0..1]
		    "category": "my-category",                    //[0..1]
		    "parentTaskId": "2",                          //[0..1]
		    "tenantId": "diver",                          //[0..1]
		    "formKey": "form-key-12",                     //[0..1]
		    "suspended": true,                            //[1..1]
		    "processDefinitionId": "21"                   //[0..1]
	  }
	]
```

#### 4. Process definitions

**HTTP Metod: GET**

**HTTP Context: https://seriver:port/wf-dniprorada/serivce/rest/process-definitions**

**Response**

```json
	[											                              //[0..N]
  		{
    		"id": "CivilCardAccountlRequest:1:9",                             //[1..1]
		    "category": "http://www.activiti.org/test",                       //[1..1]
		    "name": "Видача картки обліку об’єкта торговельного призначення", //[1..1]
		    "key": "CivilCardAccountlRequest",                                //[1..1]
		    "description": "Описание процесса",                               //[0..1]
		    "version": 1,                                                     //[1..1]
		    "resourceName": "dnepr-2.bpmn",                                   //[1..1]
		    "deploymentId": "1",                                              //[1..1]
		    "diagramResourceName": "dnepr-2.CivilCardAccountlRequest.png",    //[1..1]
		    "tenantId": "diver",                                              //[0..1]
		    "suspended": true                                                 //[1..1]
	  }
	]
```