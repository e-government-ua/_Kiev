### Custom Activiti RESTfull APi Specification

##### Mandatory HTTP Headers

| Name        | Value           |
| ------------- |:-------------:|
| Content-Type | application/json |
| Accept | application/json |
| Authorization | Basic ... |

#### 1. Login user

**HTTP Metod: POST**

**HTTP Context: https://server:port/wf-region/serivce/auth/login**

| Name        | Value           |
| ------------- |:-------------:|
| Content-Type | application/x-www-form-urlencoded |

sLogin - Логин пользователя
sPassword - Пароль пользователя

**Request**

```text
    sLogin=user&sPassword=password
```

**Response**

```json
	{"session":"true"} //[1..1]
```

true - Пользователь авторизирован
false - Имя пользователя или пароль не корректны

#### 2. Logout user

**HTTP Metod: POST/DELETE**

**HTTP Context: https://server:port/wf-region/serivce/auth/logout**

Наличие cookie JSESSIONID

**Response**

```json
	{"session":"97AE7CA414A5DA85749FE379CC843796"}
```

true - Пользователь авторизирован
false - Имя пользователя или пароль не корректны


#### 3. Start process

**HTTP Metod: GET**

**HTTP Context: https://server:port/wf-region/serivce/rest/start-process/{key}**

{key} - Ключ процесса

**Response**

```json
	{
		"id":"31" //[1..1]
	}
```

#### 4. Get tasks

**HTTP Metod: GET**

**HTTP Context: https://server:port/wf-region/serivce/rest/tasks/{assignee}**

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

#### 5. Process definitions

**HTTP Metod: GET**

**HTTP Context: https://server:port/wf-region/serivce/rest/process-definitions**

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
#### 6. Download file from DB
Сервис загрузки прикрепленного к заявке файла из постоянной базы:

**HTTP Metod: GET**

**HTTP Context: https://server:port/wf-region/service/rest/download_file_from_db?taskId=XXX&attachmentId=XXX&nFile=XXX**

{taskId} - ид задачи
{attachmentID} - ID прикрепленного файла
{nFile} - порядковый номер прикрепленного файла

https://52.17.126.64:8080/wf-region/service/rest/file/download_file_from_db?taskId=82596&attachmentId=6726532&nFile=7

#### 7. Merchants
Сервис работы с мерчантами

**HTTP Metod: GET**

**HTTP Context: http://server:port/wf-region/service/merchant/getMerchants** - весь список мерчантов

**Response**

```json					
	[				        //[0..N]
	{"idOwner":"14360570"   //[1..1]
	 "ownerName":"ПриватБанк" //[1..1]
	 "id":"1" 				//[1..1]
	}  
	]
```


**HTTP Metod: DELETE**

**HTTP Context: http://server:port/wf-region/service/merchant/removeMerchant** - удалить мерчанта

| Name        | Value           |
| ------------- |:-------------:|
| Content-Type | application/x-www-form-urlencoded |

idOwner - ОКПО
id - id мерчанта

**Request**

```text
    idOwner=idOwner&id=id
```

**HTTP Metod: POST**

**HTTP Context: http://server:port/wf-region/service/merchant/setMerchant** - обновить информацию мерчанта

| Name        | Value           |
| ------------- |:-------------:|
| Content-Type | application/x-www-form-urlencoded |

idOwner - ОКПО
ownerName - название организации
id - id мерчанта

**Request**

```text
    idOwner=idOwner&ownerName=ownerName&id=id
```

**HTTP Metod: PUT**

**HTTP Context: http://server:port/wf-region/service/merchant/addMerchant** - добавить мерчанта

| Name        | Value           |
| ------------- |:-------------:|
| Content-Type | application/x-www-form-urlencoded |

idOwner - ОКПО
ownerName - название организации
id - id мерчанта

**Request**

```text
    idOwner=idOwner&ownerName=ownerName&id=id
```


**Функциональность бэкапа данных таблиц сервисов и мест:**

**HTTP Metod: GET**

**HTTP Context: http://server:port/wf-central/service/services/getServicesAndPlacesTables** - Скачать данные в виде json

| Name        | Value           |
| ------------- |:-------------:|
| Content-Type | application/json |

**HTTP Metod: GET**

**HTTP Context: http://server:port/wf-central/service/services/downloadServicesAndPlacesTables** - Скачать данные в json файле

| Name        | Value           |
| ------------- |:-------------:|
| Content-Disposition | attachment |

**HTTP Metod: POST**

**HTTP Context: http://server:port/wf-central/service/services/setServicesAndPlacesTables** - Загрузить в виде json (в теле POST запроса)

| Name        | Value           |
| ------------- |:-------------:|
| Content-Type | application/json |

**HTTP Metod: POST**

**HTTP Context: http://server:port/wf-central/service/services/uploadServicesAndPlacesTables** - Загрузить из json файла

| Name        | Value           |
| ------------- |:-------------:|
| Content-Type | application/json |

Пример страницы формы загрузки из файла:

&lt;html&gt;<br/>&lt;body&gt;<br/>&lt;form method=&quot;POST&quot; enctype=&quot;multipart/form-data&quot;<br/>action=&quot;http://localhost:8080/wf-central/service/services/uploadServicesAndPlacesTables&quot;&gt;<br/>File to upload: &lt;input type=&quot;file&quot; name=&quot;file&quot;&gt;&lt;br /&gt; &lt;input type=&quot;submit&quot;<br/>value=&quot;Upload&quot;&gt; Press here to upload the file!<br/>&lt;/form&gt;<br/>&lt;/body&gt;<br/>&lt;/html&gt;


#### 8. Документы
Сервиса работы с документом

**HTTP Metod: GET**

**HTTP Context: http://server:port/wf-central/service/services/getDocument** - получение документа по ид документа

nID - ид документа
!----------------------------------------------------------------------------------------------------------------------------

**HTTP Metod: GET**

**HTTP Context: http://server:port/wf-central/service/services/getDocumentContent** - получение контента документа по ид документа

nID - ид документа
-----------------------------------------------------------------------------------------------------------------------------

**HTTP Metod: GET**

**HTTP Context: http://server:port/wf-central/service/services/getDocumentFile** - получение документа в виде файла по ид документа

nID - ид документа
-----------------------------------------------------------------------------------------------------------------------------

**HTTP Metod: GET**

**HTTP Context: http://server:port/wf-central/service/services/getDocuments** - получение списка загруженных субъектом документов

sID_Subject - идентификатор субъекта
-----------------------------------------------------------------------------------------------------------------------------

**HTTP Metod: POST**

**HTTP Context: http://server:port/wf-central/service/services/setDocument** - сохранение документа

sID_Subject_Upload - идентификатор субъекта, который загрузил документ (временный парметр, нужно убрать его)
sSubjectName_Upload - название субъекта, который загрузил документ (временный парметр, нужно убрать его)
sName - название документа
sFile - название и расширение файла
nID_DocumentType - тип документа
sDocumentContentType - тип контента документа
soDocumentContent - контект в виде строки
nID_Subject_Upload - ид субъекта, который загрузил документ





