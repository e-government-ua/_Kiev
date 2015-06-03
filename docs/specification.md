### iGov.ua APIs

##### Mandatory HTTP Headers

| Name        | Value           |
| ------------- |:-------------:|
| Content-Type | application/json |
| Accept | application/json |
| Authorization | Basic ... |

#### 1. Логин пользователя:

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

#### 2. Логаут пользователя:

**HTTP Metod: POST/DELETE**

**HTTP Context: https://server:port/wf-region/serivce/auth/logout**

Наличие cookie JSESSIONID

**Response**

```json
	{"session":"97AE7CA414A5DA85749FE379CC843796"}
```

true - Пользователь авторизирован
false - Имя пользователя или пароль не корректны


#### 3. Запуск процесса Activiti:

**HTTP Metod: GET**

**HTTP Context: https://server:port/wf-region/serivce/rest/start-process/{key}**

{key} - Ключ процесса

**Response**

```json
	{
		"id":"31" //[1..1]
	}
```

#### 4. Загрузка задач из Activiti:

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

#### 5. Загрузка каталога сервисов из Activiti:

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
#### 6. Загрузки прикрепленного к заявке файла из постоянной базы:

**HTTP Metod: GET**

**HTTP Context: https://server:port/wf-region/service/rest/download_file_from_db?taskId=XXX&attachmentId=XXX&nFile=XXX**

{taskId} - ид задачи
{attachmentID} - ID прикрепленного файла
{nFile} - порядковый номер прикрепленного файла

Пример:
https://test.igov.org.ua/wf-region/service/rest/file/download_file_from_db?taskId=82596&attachmentId=6726532&nFile=7


#### 7. Работа с мерчантами:

**HTTP Metod: GET**

**HTTP Context: https://server:port/wf-region/service/merchant/getMerchants** - весь список мерчантов

**Response**

```json					
	[				        //[0..N]
	{"idOwner":"14360570"   //[1..1]
	 "ownerName":"ПриватБанк" //[1..1]
	 "id":"1" 				//[1..1]
	}  
	]
```


Пример:
https://test.igov.org.ua/wf-region/service/merchant/setMerchant?id=1

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

Пример:
https://test.igov.org.ua/wf-region/service/merchant/setMerchant?id=1


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

Пример:
https://test.igov.org.ua/wf-region/service/merchant/setMerchant?id=1&ownerName=Shop1&idOwner=543245


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

Пример:
https://test.igov.org.ua/wf-region/service/merchant/addMerchant?id=1


#### 8. Бэкап/восстановление данных таблиц сервисов и мест:

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


----------------------------------------------------------------------------------------------------------------------------

#### 9. Работа с документами


**HTTP Metod: GET**

**HTTP Context: http://server:port/wf-central/service/services/getDocument** - получение документа по ид документа

nID - ИД-номер документа

Пример:
https://poligon.igov.org.ua/wf-central/service/services/getDocument?nID=1

----------------------------------------------------------------------------------------------------------------------------

**HTTP Metod: GET**

**HTTP Context: http://server:port/wf-central/service/services/getDocumentContent** - получение контента документа по ид документа

nID - ИД-номер документа

Пример:
https://poligon.igov.org.ua/wf-central/service/services/getDocumentContent?nID=1

----------------------------------------------------------------------------------------------------------------------------

**HTTP Metod: GET**

**HTTP Context: http://server:port/wf-central/service/services/getDocumentFile** - получение документа в виде файла по ид документа

nID - ИД-номер документа

Пример:
https://poligon.igov.org.ua/wf-central/service/services/getDocumentFile?nID=1

----------------------------------------------------------------------------------------------------------------------------

**HTTP Metod: GET**

**HTTP Context: http://server:port/wf-central/service/services/getDocuments** - получение списка загруженных субъектом документов

nID_Subject - ИД-номер субъекта
Пример:
https://poligon.igov.org.ua/wf-central/service/services/getDocuments?nID_Subject=2

 ---------------------------------------------------------------------------------------------------------------------------

**HTTP Metod: POST**

**HTTP Context: http://server:port/wf-central/service/services/setDocument** - сохранение документа

sID_Subject_Upload - ИД-строка субъекта, который загрузил документ
sSubjectName_Upload - строка-название субъекта, который загрузил документ (временный парметр, будет убран)
sName - строка-название документа
sFile - строка-название и расширение файла
nID_DocumentType - ИД-номер типа документа
sDocumentContentType - строка-тип контента документа
soDocumentContent - контект в виде строки-обьекта
nID_Subject - ИД-номер субъекта докуиента (владельца)

--------------------------------------------------------------------------------------------------------------------------
 
**HTTP Metod: POST**

**HTTP Context: http://server:port/wf-central/service/services/setDocumentFile** - сохранение документа в виде файла
(контент файла шлется в теле запроса)

sID_Subject_Upload - ИД-строка субъекта, который загрузил документ
sSubjectName_Upload - строка-название субъекта, который загрузил документ (временный парметр, нужно убрать его)
sName - строка-название документа
nID_DocumentType - ИД-номер типа документа
sDocumentContentType - строка-тип контента документа
soDocumentContent - контент в виде строки-обьекта
nID_Subject - ИД-номер субъекта документа (владельца)
oFile - обьект файла (тип MultipartFile)

--------------------------------------------------------------------------------------------------------------------------


#### 10. Работа с субъктами


**HTTP Metod: GET**

**HTTP Context: http://server:port/wf-central/service/subject/syncSubject** - получение субъекта, если таков найден, или добавление субъекта в противном случае

От клиента ожидается ОДИН и только ОДИН параметр из нижеперечисленных

nID - ИД-номер субъекта
sINN - строка-ИНН (субъект - человек)
sOKPO - строка-ОКПО (субъек - организация)

Примеры:

https://poligon.igov.org.ua/wf-central/service/subject/syncSubject?sINN=34125265377

https://poligon.igov.org.ua/wf-central/service/subject/syncSubject?sOKPO=123

https://poligon.igov.org.ua/wf-central/service/subject/syncSubject?nID=1

--------------------------------------------------------------------------------------------------------------------------

#### 11. DocumentAccess
Cервис предоставления доступа на документ

**HTTP Metod: POST**

**HTTP Context: 
https://seriver:port/wf-central/service/setDocumentLink - запись на доступ

nID_Document - ИД-номер документа
sFIO - ФИО, кому доступ
sTarget - цель получения доступа
sTelephone - телефон того, кому доступ предоставляется
nDays - число милисекунд, на которое предоставляется доступ
sMail - эл. почта того, кому доступ предоставляется

**Response**

```json					
	[				        //[0..N]
	{"name":"sURL",   //[1..1]
	 "value":"https://e-gov.org.ua/index#nID_Access=4345&sSecret=JHg3987JHg3987JHg3987" //[1..1]
	}  
	]
```

**HTTP Metod: POST**

**HTTP Context: 
https://seriver:port/wf-central/service/getDocumentLink - проверка доступа и получения данных о нем, если доступ есть

nID_Document - ИД-номер документа
sSecret - секретный ключ

**Response**

HTTP STATUS 200
```json					
	[				        //[0..N]
{	
	"nID":4355
	,"nID_Document":53245
	,"sDateCreate":"2015-05-05 22:32:24.425"
	,"nMS":3523
	,"sFIO":"Вася Пупкин"
	,"sTarget":"По прикколу"
	,"sTelephone":"001 354 3456"
	,"sMail":"vasya@i.ua" 
}
	]
```
Если доступа нет, возвращается HTTP STATUS 403
Если доступ есть, но секрет не совпадает, возвращается HTTP STATUS 403
Если доступ просрочен, возвращается HTTP STATUS 403
Если возникла исключительная ситуация, возвращается HTTP STATUS 400. В заголовок ответа добавляется параметр Reason, в котором описана причина возникновения ситуации.

**HTTP Metod: POST**

**HTTP Context: 
https://seriver:port/wf-central/service/getDocumentAccess

nID_Document - ИД-номер документа
sSecret - секретный ключ

**Response**

```json					
	[				        //[0..N]
	{"name":"sURL",   //[1..1]
	 "value":"https://seriver:port/index#nID_Access=4345&sSecret=JHg3987JHg3987JHg3987" //[1..1]
	}  
	]
```

**HTTP Metod: POST**

**HTTP Context: 
https://seriver:port/wf-central/service/setDocumentAccess

* nID_Access - ид доступа
* sSecret - секретный ключ
* sAnswer - ответ

**Response**

```json					
	[				        //[0..N]
	{"name":"sURL",   //[1..1]
	 "value":"https://seriver:port/index#nID_Access=4345&sSecret=JHg3987JHg3987JHg3987" //[1..1]
	}  
	]
```



----------------------------------------------------------------------------------------------------------------------------

#### 12. SubjectMessage


**HTTP Metod: GET**

**HTTP Context: http://server:port/wf-central/service/messages/getMessages - получение всех сообщений

Пример:
https://poligon.igov.org.ua/wf-central/service/messages/getMessages

----------------------------------------------------------------------------------------------------------------------------

**HTTP Metod: GET**

**HTTP Context: http://server:port/wf-central/service/messages/getMessage?nID=1 - получение контента сообщения по ид сообщения

nID - ИД-номер сообщения

Пример:
https://poligon.igov.org.ua/wf-central/service/messages/getMessage?nID=76

----------------------------------------------------------------------------------------------------------------------------

**HTTP Metod: POST**

**HTTP Context: http://server:port/wf-central/service/messages/setMessage - сохранение сообщения 
 
nID - ид-шник сообщения уникальный, автоинкрементируемый

обязательные поля

sHead - заголовок до 200 символов текста
sBody - текстовое поле 

не обяззательные поля

nID_Subject - число-ИД субьекта (по умолчанию 0)

по умолчанию пустые строки

sMail - до 100 символов 
sContacts - до 200 символов
sData - до 200 символов текста
