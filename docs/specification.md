### iGov.ua APIs
 <a name="0_contents">*Contents*</a><br/>
<a href="#1_userLogin">1. Логин пользователя</a><br/>
<a href="#2_userLogout">2. Логаут пользователя</a><br/>
<a href="#3_activitiRun">3. Запуск процесса Activiti</a><br/>
<a href="#4_activitiRunTasks">4. Загрузка задач из Activiti</a><br/>
<a href="#5_activitiRunServices">5. Загрузка каталога сервисов из Activiti</a><br/>
<a href="#6_loadFileFromDb">6. Загрузки прикрепленного к заявке файла из постоянной базы</a><br/>
<a href="#7_workWithMerchants">7. Работа с мерчантами</a><br/>
<a href="#8_workWithTables">8. Бэкап/восстановление данных таблиц сервисов и мест</a><br/>
<a href="#9_workWithDocuments">9. Работа с документами</a><br/>
<a href="#10_workWithSubjects">10. Работа с субъектами</a><br/>
<a href="#11_accessDocuments">11. Предоставление и проверка доступа к документам</a><br/>
<a href="#12_workWithMessages">12. Работа с сообщениями</a><br/>
<a href="#13_workWithHistoryEvents">13. Работа с историей (Мой журнал)</a><br/>
<a href="#14_uploadFileToDb">14. Аплоад(upload) и прикрепление файла в виде атачмента к таске Activiti</a><br/>
<a href="#15_workWithServices">15. Работа с каталогом сервисов</a><br/>
<a href="#16_getWorkflowStatistics">16. Получение статистики по задачам в рамках бизнес процесса</a><br/>
<a href="#17_workWithHistoryEvent_Services">17. Работа с обьектами событий по услугам</a><br/>
<a href="#18_workWithFlowSlot">18. Работа со слотами потока</a><br/>
<a href="#19">19. Работа с джоинами суьтектами (отделениями/филиалами)</a><br/>
<a href="#20">20. Получение кнопки для оплаты через Liqpay</a><br/>
<a href="#21">21. Работа со странами </a><br/>
<a href="#22">22. Загрузка данных по задачам </a><br/>
### iGov.ua APIs

##### Mandatory HTTP Headers

| Name        | Value           |
| ------------- |:-------------:|
| Content-Type | application/json |
| Accept | application/json |
| Authorization | Basic ... |

<a name="1_userLogin">
####1. Логин пользователя:
</a><a href="#0_contents">↑Up</a>

**HTTP Metod: POST**

**HTTP Context: https://server:port/wf-region/service/auth/login**

| Name        | Value           |
| ------------- |:-------------:|
| Content-Type | application/x-www-form-urlencoded |

* sLogin - Логин пользователя
* sPassword - Пароль пользователя

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

<a name="2_userLogout">
#### 2. Логаут пользователя:
</a><a href="#0_contents">↑Up</a>

**HTTP Metod: POST/DELETE**

**HTTP Context: https://server:port/wf-region/service/auth/logout**

Наличие cookie JSESSIONID

**Response**

```json
	{"session":"97AE7CA414A5DA85749FE379CC843796"}
```

true - Пользователь авторизирован
false - Имя пользователя или пароль не корректны


<a name="3_activitiRun">
#### 3. Запуск процесса Activiti:
</a><a href="#0_contents">↑Up</a>

**HTTP Metod: GET**

**HTTP Context: https://server:port/wf-region/service/rest/start-process/{key}**

* {key} - Ключ процесса
* {nID_Subject} - ID авторизированого субъекта (добавляется в запрос автоматически после аутентификации пользователя)

**Response**

```json
	{
		"id":"31" //[1..1]
	}
```

<a name="4_activitiRunTasks">
####4. Загрузка задач из Activiti
</a><a href="#0_contents">↑Up</a><br/>

**HTTP Metod: GET**

**HTTP Context: https://server:port/wf-region/service/rest/tasks/{assignee}**

* {assignee} - Владелец
* {nID_Subject} - ID авторизированого субъекта (добавляется в запрос автоматически после аутентификации пользователя)

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

<a name="5_activitiRunServices">
####5. Загрузка каталога сервисов из Activiti
</a><a href="#0_contents">↑Up</a><br/>

**HTTP Metod: GET**

**HTTP Context: https://server:port/wf-region/service/rest/process-definitions**

* {nID_Subject} - ID авторизированого субъекта (добавляется в запрос автоматически после аутентификации пользователя)

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
<a name="6_loadFileFromDb">
####6. Загрузки прикрепленного к заявке файла из постоянной базы
</a><a href="#0_contents">↑Up</a><br/>

**HTTP Metod: GET**

**HTTP Context: https://server:port/wf-region/service/rest/download_file_from_db?taskId=XXX&attachmentId=XXX&nFile=XXX**

* {taskId} - ид задачи
* {attachmentID} - ID прикрепленного файла
* {nFile} - порядковый номер прикрепленного файла
* {nID_Subject} - ID авторизированого субъекта (добавляется в запрос автоматически после аутентификации пользователя)

Пример:
https://test.igov.org.ua/wf-region/service/rest/file/download_file_from_db?taskId=82596&attachmentId=6726532&nFile=7


<a name="7_workWithMerchants">
####7. Работа с мерчантами
</a><a href="#0_contents">↑Up</a><br/>

**HTTP Metod: GET**

**HTTP Context: https://server:port/wf-central/service/merchant/getMerchants** - получить весь список обьектов мерчантов

**Response**

```json					
	[
		{
			"nID":1
			,"sID":"Test_sID"
			,"sName":"Test_sName"
			,"sPrivateKey":"test_sPrivateKey"
			,"sURL_CallbackStatusNew":"test_sURL_CallbackStatusNew"
			,"sURL_CallbackPaySuccess":"test_sURL_CallbackPaySuccess"
			,"nID_SubjectOrgan":1
		}
		,{
			"nID":2
			,"sID":"i10172968078"
			,"sName":"igov test"
			,"sPrivateKey":"BStHb3EMmVSYefW2ejwJYz0CY6rDVMj1ZugJdZ2K"
			,"sURL_CallbackStatusNew":"test_sURL_CallbackStatusNew"
			,"sURL_CallbackPaySuccess":"test_sURL_CallbackPaySuccess"
			,"nID_SubjectOrgan":1
		}
	]	
```


Пример:
https://test.igov.org.ua/wf-central/service/merchant/getMerchants



**HTTP Metod: GET**
**HTTP Context: https://server:port/wf-central/service/merchant/getMerchant** - получить обьект мерчанта

* sID - ID-строка мерчанта(публичный ключ)

**Response**

```json	
	{
		"nID":1
		,"sID":"Test_sID"
		,"sName":"Test_sName"
		,"sPrivateKey":"test_sPrivateKey"
		,"sURL_CallbackStatusNew":"test_sURL_CallbackStatusNew"
		,"sURL_CallbackPaySuccess":"test_sURL_CallbackPaySuccess"
		,"nID_SubjectOrgan":1
	}
```

Пример:
https://test.igov.org.ua/wf-central/service/merchant/getMerchant?sID=i10172968078



**HTTP Metod: DELETE**

**HTTP Context: http://server:port/wf-central/service/merchant/removeMerchant** - удалить мерчанта

| Name        | Value           |
| ------------- |:-------------:|
| Content-Type | application/x-www-form-urlencoded |

* sID - ID-строка мерчанта(публичный ключ)

**Response**

```Status 200
```

Пример:
https://test.igov.org.ua/wf-central/service/merchant/removeMerchant?sID=i10172968078




**HTTP Metod: POST**

**HTTP Context: http://server:port/wf-region/service/merchant/setMerchant** - обновить информацию мерчанта

| Name        | Value           |
| ------------- |:-------------:|
| Content-Type | application/x-www-form-urlencoded |

* nID - ID-номер мерчанта(внутренний) //опциональный (если не задан или не найден - будет добавлена запись)
* sID - ID-строка мерчанта(публичный ключ) //опциональный (если не задан или не найден - будет добавлена запись)
* sName - строковое название мерчанта //опциональный (при добавлении записи - обязательный)
* sPrivateKey - приватный ключ мерчанта //опциональный (при добавлении записи - обязательный)
* nID_SubjectOrgan - ID-номер субьекта-органа мерчанта(может быть общий субьект у нескольких мерчантов) //опциональный
* sURL_CallbackStatusNew - строка-URL каллбэка, при новом статусе платежа(проведении проплаты) //опциональный
* sURL_CallbackPaySuccess - строка-URL каллбэка, после успешной отправки платежа //опциональный

**Response**

```json	
	{
		"nID":1
		,"sID":"Test_sID"
		,"sName":"Test_sName22"
		,"sPrivateKey":"test_sPrivateKey"
		,"sURL_CallbackStatusNew":"test_sURL_CallbackStatusNew"
		,"sURL_CallbackPaySuccess":"test_sURL_CallbackPaySuccess"
		,"nID_SubjectOrgan":1
	}
```

Примеры обновления:
https://test.igov.org.ua/wf-central/service/merchant/setMerchant?sID=Test_sID&sName=Test_sName2
https://test.igov.org.ua/wf-central/service/merchant/setMerchant?nID=1&sName=Test_sName22
Пример добавления:
https://test.igov.org.ua/wf-central/service/merchant/setMerchant?sID=Test_sID3&sName=Test_sName3&sPrivateKey=121212 





<a name="8_workWithTables">
####8. Бэкап/восстановление данных таблиц сервисов и мест
</a><a href="#0_contents">↑Up</a><br/>

**HTTP Metod: GET**

**HTTP Context: http://server:port/wf-central/service/services/getServicesAndPlacesTables** - Скачать данные в виде json

| Name        | Value           |
| ------------- |:-------------:|
| Content-Type | application/json |

 * nID_Subject - ID авторизированого субъекта (добавляется в запрос автоматически после аутентификации пользователя)
 

**HTTP Metod: GET**

**HTTP Context: http://server:port/wf-central/service/services/downloadServicesAndPlacesTables** - Скачать данные в json файле

| Name        | Value           |
| ------------- |:-------------:|
| Content-Disposition | attachment |

 * nID_Subject - ID авторизированого субъекта (добавляется в запрос автоматически после аутентификации пользователя)

**HTTP Metod: POST**

**HTTP Context: http://server:port/wf-central/service/services/setServicesAndPlacesTables** - Загрузить в виде json (в теле POST запроса)

| Name        | Value           |
| ------------- |:-------------:|
| Content-Type | application/json |

 * nID_Subject - ID авторизированого субъекта (добавляется в запрос автоматически после аутентификации пользователя)

**HTTP Metod: POST**

**HTTP Context: http://server:port/wf-central/service/services/uploadServicesAndPlacesTables** - Загрузить из json файла

| Name        | Value           |
| ------------- |:-------------:|
| Content-Type | application/json |

 * nID_Subject - ID авторизированого субъекта (добавляется в запрос автоматически после аутентификации пользователя)

Пример страницы формы загрузки из файла:

&lt;html&gt;<br/>&lt;body&gt;<br/>&lt;form method=&quot;POST&quot; enctype=&quot;multipart/form-data&quot;<br/>action=&quot;http://localhost:8080/wf-central/service/services/uploadServicesAndPlacesTables&quot;&gt;<br/>File to upload: &lt;input type=&quot;file&quot; name=&quot;file&quot;&gt;&lt;br /&gt; &lt;input type=&quot;submit&quot;<br/>value=&quot;Upload&quot;&gt; Press here to upload the file!<br/>&lt;/form&gt;<br/>&lt;/body&gt;<br/>&lt;/html&gt;


----------------------------------------------------------------------------------------------------------------------------

<a name="9_workWithDocuments">
####9. Работа с документами
</a><a href="#0_contents">↑Up</a><br/>


**HTTP Metod: GET**

**HTTP Context: http://server:port/wf-central/service/services/getDocument** - получение документа по ид документа

* nID - ИД-номер документа
* nID_Subject - ID авторизированого субъекта (добавляется в запрос автоматически после аутентификации пользователя)

Пример:
https://test.igov.org.ua/wf-central/service/services/getDocument?nID=1

**Response**
```json
{
	"sDate_Upload":"2015-01-01",
	"sContentType":"text/plain",
	"contentType":"text/plain",
	"nID":1,
	"sName":"Паспорт",
	"oDocumentType":{"nID":0,"sName":"Другое"},
	"sID_Content":"1",
	"oDocumentContentType":{"nID":2,"sName":"text/plain"},
	"sFile":"dd.txt",
	"oDate_Upload":1420063200000,
	"sID_Subject_Upload":"1",
	"sSubjectName_Upload":"ПриватБанк",
	"oSubject_Upload":{"nID":1,"sID":"ПАО","sLabel":"ПАО ПриватБанк", "sLabelShort":"ПриватБанк"},
	 "oSubject":{"nID":1,"sID":"ПАО","sLabel":"ПАО ПриватБанк","sLabelShort":"ПриватБанк"}
 }
```


----------------------------------------------------------------------------------------------------------------------------

**HTTP Metod: GET**

**HTTP Context: http://server:port/wf-central/service/services/getDocumentContent** - получение контента документа по ид документа

* nID - ИД-номер документа
* nID_Subject - ID авторизированого субъекта (добавляется в запрос автоматически после аутентификации пользователя)

Пример:
https://test.igov.org.ua/wf-central/service/services/getDocumentContent?nID=1

**Response**
КОНТЕНТ ДОКУМЕНТА В ВИДЕ СТРОКИ


----------------------------------------------------------------------------------------------------------------------------

**HTTP Metod: GET**

**HTTP Context: http://server:port/wf-central/service/services/getDocumentFile** - получение документа в виде файла по ид документа

* nID - ИД-номер документа
* nID_Subject - ID авторизированого субъекта (добавляется в запрос автоматически после аутентификации пользователя)

* sCode_DocumentAccess - строковой код доступа к документу, к которому получен доступ //опциональный
* nID_DocumentOperator_SubjectOrgan - ИД-номер оператора документов, к которому получен доступ //опциональный
* nID_DocumentType - ИД-номер типа документа, к которому получен доступ //опциональный
* sPass - строковой пароль доступа к документу //опциональный


Пример:
https://test.igov.org.ua/wf-central/service/services/getDocumentFile?nID=1

**Response**
ЗАГРУЖЕННЫЙ ФАЙЛ 

----------------------------------------------------------------------------------------------------------------------------

**HTTP Metod: GET**

**HTTP Context: http://server:port/wf-central/service/services/getDocuments** - получение списка загруженных субъектом документов

* nID_Subject - ID авторизированого субъекта (добавляется в запрос автоматически после аутентификации пользователя)

Пример:
https://test.igov.org.ua/wf-central/service/services/getDocuments?nID_Subject=2

**Response**
```json
[
	{
	"sDate_Upload":"2015-01-01",
	"sContentType":"text/plain",
	"contentType":"text/plain",
	"nID":1,
	"sName":"Паспорт",
	"oDocumentType":{"nID":0,"sName":"Другое"},
	"sID_Content":"1",
	"oDocumentContentType":{"nID":2,"sName":"text/plain"},
	"sFile":"dd.txt",
	"oDate_Upload":1420063200000,
	"sID_Subject_Upload":"1",
	"sSubjectName_Upload":"ПриватБанк",
	"oSubject_Upload":{"nID":1,"sID":"ПАО","sLabel":"ПАО ПриватБанк", "sLabelShort":"ПриватБанк"},
	 "oSubject":{"nID":1,"sID":"ПАО","sLabel":"ПАО ПриватБанк","sLabelShort":"ПриватБанк"}
         },
         {
	"sDate_Upload":"2015-01-01",
	"sContentType":"text/plain",
	"contentType":"text/plain",
	"nID":2,
	"sName":"Паспорт",
	"oDocumentType":{"nID":0,"sName":"Другое"},
	"sID_Content":"2",
	"oDocumentContentType":{"nID":2,"sName":"text/plain"},
	"sFile":"dd.txt",
	"oDate_Upload":1420063200000,
	"sID_Subject_Upload":"1",
	"sSubjectName_Upload":"ПриватБанк",
	"oSubject_Upload":{"nID":1,"sID":"ПАО","sLabel":"ПАО ПриватБанк", "sLabelShort":"ПриватБанк"},
	 "oSubject":{"nID":1,"sID":"ПАО","sLabel":"ПАО ПриватБанк","sLabelShort":"ПриватБанк"}
         }
]
```
 ---------------------------------------------------------------------------------------------------------------------------
 **HTTP Metod: GET**
 
 **HTTP Context: http://server:port/wf-central/service/services/getDocumentAccessByHandler** - получение контента документа по коду доступа,оператору, типу документа и паролю
 
 * sCode_DocumentAccess - код доступа документа
 * nID_DocumentOperator_SubjectOrgan - код органа(оператора)
 * nID_DocumentType - типа документа (опциональный)
 * sPass - пароль для доступа к документу (опциональный, пока только для документов у которы sCodeType=SMS)
 
Пример: https://test.igov.org.ua/wf-central/service/services/getDocumentAccessByHandler?sCode_DocumentAccess=2&nID_DocumentOperator_SubjectOrgan=2&sPass=123&nID_DocumentType=1

Response КОНТЕНТ ДОКУМЕНТА В ВИДЕ СТРОКИ
 ---------------------------------------------------------------------------------------------------------------------------

**HTTP Metod: POST**

**HTTP Context: http://server:port/wf-central/service/services/setDocument** - сохранение документа

* sID_Subject_Upload - ИД-строка субъекта, который загрузил документ
* sSubjectName_Upload - строка-название субъекта, который загрузил документ (временный парметр, будет убран)
* sName - строка-название документа
* sFile - строка-название и расширение файла
* nID_DocumentType - ИД-номер типа документа
* sDocumentContentType - строка-тип контента документа
* soDocumentContent - контект в виде строки-обьекта
* nID_Subject - ИД-номер субъекта документа (владельца) ????????????????????????????????????

Пример:
https://test.igov.org.ua/wf-central/service/services/setDocument?sID_Subject_Upload=123&sSubjectName_Upload=Vasia&sName=Pasport&sFile=file.txt&nID_DocumentType=1&sDocumentContentType=application/zip&soDocumentContent=ffffffffffffffffff&nID_Subject=1

**Response**
ИД ДОКУМЕНТА

--------------------------------------------------------------------------------------------------------------------------
 
**HTTP Metod: POST**

**HTTP Context: http://server:port/wf-central/service/services/setDocumentFile** - сохранение документа в виде файла
(контент файла шлется в теле запроса)

* sID_Subject_Upload - ИД-строка субъекта, который загрузил документ
* sSubjectName_Upload - строка-название субъекта, который загрузил документ (временный парметр, нужно убрать его)
* sName - строка-название документа
* nID_DocumentType - ИД-номер типа документа
* sDocumentContentType - строка-тип контента документа
* soDocumentContent - контент в виде строки-обьекта
* nID_Subject - ИД-номер субъекта документа (владельца)????????????????????????????????????
* oFile - обьект файла (тип MultipartFile)


**Response**
ИД ДОКУМЕНТА

----------------------------------------------------------------------------------------------------------------------------
 ТИПЫ ДОКУМЕНТОВ

----------------------------------------------------------------------------------------------------------------------------
**HTTP Metod: GET**

**HTTP Context: http://server:port/wf-central/service/services/getDocumentTypes**
 - получение списка всех "нескрытых" типов документов, т.е. у которых поле bHidden=false

Пример:
https://test.igov.org.ua/wf-central/service/services/getDocumentTypes

**Response**
```json
[
	{"nID":0,"sName":"Другое", "bHidden":false},
	{"nID":1,"sName":"Справка", "bHidden":false},
	{"nID":2,"sName":"Паспорт", "bHidden":false}
]
```

--------------------------------------------------------------------------------------------------------------------------
**HTTP Metod: GET**

**HTTP Context: http://server:port/wf-central/service/services/setDocumentType** - добавить/изменить запись типа документа
параметры:

 * nID -- ид записи (число)
 * sName -- название записи (строка)
 * bHidden -- скрывать/не скрывать (при отдаче списка всех записей, булевское, по умолчанию = false)

 Если запись с ид=nID не будет найдена, то создастся новая запись (с автогенерируемым nID), иначе -- обновится текущая.
 
  примеры:
  
создать новый тип:
https://test.igov.org.ua/wf-central/service/services/setDocumentType?nID=100&sName=test

ответ: ```{"nID":20314,"sName":"test", , "bHidden":false}```

изменить (взять ид из предыдущего ответа):
https://test.igov.org.ua/wf-central/service/services/setDocumentType?nID=20314&sName=test2

ответ: ```{"nID":20314,"sName":"test2", "bHidden":false}```

--------------------------------------------------------------------------------------------------------------------------
**HTTP Metod: GET**

**HTTP Context: http://server:port/wf-central/service/services/removeDocumentType** - удаление записи по ее ид
параметры:
 *nID -- ид записи

  Если запись с ид=nID не будет найдена, то вернется ошибка *403. Record not found*, иначе -- запись удалится.

пример:
https://test.igov.org.ua/wf-central/service/services/removeDocumentType?nID=20314

ответ: ```200 ok ```

--------------------------------------------------------------------------------------------------------------------------
 ТИПЫ КОНТЕНТА ДОКУМЕНТОВ

----------------------------------------------------------------------------------------------------------------------------
**HTTP Metod: GET**

**HTTP Context: http://server:port/wf-central/service/services/getDocumentContentTypes** - получение списка типов контента документов

Пример:
https://test.igov.org.ua/wf-central/service/services/getDocumentContentTypes

**Response**
```json
[
	{"nID":0,"sName":"application/json"},
	{"nID":1,"sName":"application/xml"},
	{"nID":2,"sName":"text/plain"},
	{"nID":3,"sName":"application/jpg"}
]
```

--------------------------------------------------------------------------------------------------------------------------
**HTTP Metod: GET**

**HTTP Context: http://server:port/wf-central/service/services/setDocumentContentType** - добавить/изменить запись типа контента документа
параметры:

 *nID -- ид записи

 *sName -- название записи

 Если запись с ид=nID не будет найдена, то создастся новая запись (с автогенерируемым nID), иначе -- обновится текущая.
 
  примеры:
  
создать новый тип:
https://test.igov.org.ua/wf-central/service/services/setDocumentContentType?nID=100&sName=test

ответ: ```{"nID":20311,"sName":"test"}```

изменить (взять ид из предыдущего ответа):
https://test.igov.org.ua/wf-central/service/services/setDocumentContentType?nID=20311&sName=test2

ответ: ``` {"nID":20311,"sName":"test2"}```

--------------------------------------------------------------------------------------------------------------------------
**HTTP Metod: GET**

**HTTP Context: http://server:port/wf-central/service/services/removeDocumentContentType** - удаление записи по ее ид
параметры:
 *nID -- ид записи

  Если запись с ид=nID не будет найдена, то вернется ошибка *403. Record not found*, иначе -- запись удалится.

пример:
https://test.igov.org.ua/wf-central/service/services/removeDocumentContentType?nID=20311

ответ: ```200 ok ```

--------------------------------------------------------------------------------------------------------------------------


<a name="10_workWithSubjects">
####10. Работа с субъектами
</a><a href="#0_contents">↑Up</a><br/>


**HTTP Metod: GET**

**HTTP Context: http://server:port/wf-central/service/subject/syncSubject** - получение субъекта, если таков найден, или добавление субъекта в противном случае

От клиента ожидается ОДИН и только ОДИН параметр из нижеперечисленных

* nID - ИД-номер субъекта
* sINN - строка-ИНН (субъект - человек)
* sOKPO - строка-ОКПО (субъек - организация)
* nID_Subject - ID авторизированого субъекта (добавляется в запрос автоматически после аутентификации пользователя)

Примеры:

https://test.igov.org.ua/wf-central/service/subject/syncSubject?sINN=34125265377

https://test.igov.org.ua/wf-central/service/subject/syncSubject?sOKPO=123

https://test.igov.org.ua/wf-central/service/subject/syncSubject?nID=1

**Response**
```json
{
	"nID":150,
	"sID":"34125265377",
	"sLabel":null,
	"sLabelShort":null
}
```
--------------------------------------------------------------------------------------------------------------------------

**HTTP Metod: GET**

**HTTP Context: http://server:port/wf-central/service/services/getDocumentOperators** - получение всех операторов(органов) которые имею право доступа к документу

Примеры: https://test.igov.org.ua/wf-central/service/services/getDocumentOperators

**Response**
```json
[
    {
        "nID_SubjectOrgan": 2,
        "sHandlerClass": "org.wf.dp.dniprorada.model.document.DocumentAccessHandler_IGov",
        "nID": 1,
        "sName": "iGov"
    }
]
```
--------------------------------------------------------------------------------------------------------------------------

<a name="11_accessDocuments">
####11. Предоставление и проверка доступа к документам
</a><a href="#0_contents">↑Up</a><br/>

**HTTP Metod: POST**

**HTTP Context: 
https://seriver:port/wf-central/service/setDocumentLink - запись на доступ, с генерацией и получением уникальной ссылки на него

* nID_Document - ИД-номер документа
* sFIO - ФИО, кому доступ
* sTarget - цель получения доступа
* sTelephone - телефон того, кому доступ предоставляется
* nDays - число милисекунд, на которое предоставляется доступ
* sMail - эл. почта того, кому доступ предоставляется
* nID_Subject - ID авторизированого субъекта (добавляется в запрос автоматически после аутентификации пользователя)

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
https://seriver:port/wf-central/service/getDocumentLink - проверка доступа к документу и получения данных о нем, если доступ есть

* nID_Document - ИД-номер документа
* sSecret - секретный ключ
* nID_Subject - ID авторизированого субъекта (добавляется в запрос автоматически после аутентификации пользователя)

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
https://seriver:port/wf-central/service/getDocumentAccess - Получение подтверждения на доступ к документу(с отсылкой СМС ОТП-паролем на телефон))

* nID_Document - ИД-номер документа
* sSecret - секретный ключ
* nID_Subject - ID авторизированого субъекта (добавляется в запрос автоматически после аутентификации пользователя)

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
https://seriver:port/wf-central/service/setDocumentAccess - Установка подтверждения на доступ к документу, по введенному коду, из СМС-ки(ОТП-паролем), и возвратом уникальной разовой ссылки на докуемнт.

* nID_Access - ид доступа
* sSecret - секретный ключ
* sAnswer - ответ (введенный пользователем ОТП-пароль из СМС)
* nID_Subject - ID авторизированого субъекта (добавляется в запрос автоматически после аутентификации пользователя)

**Response**

```json					
	[				        //[0..N]
	{"name":"sURL",   //[1..1]
	 "value":"https://seriver:port/index#nID_Access=4345&sSecret=JHg3987JHg3987JHg3987" //[1..1]
	}  
	]
```


<a name="12_workWithMessages">
####12. Работа с сообщениями
</a><a href="#0_contents">↑Up</a><br/>

**HTTP Metod: GET**

**HTTP Context: http://server:port/wf-central/service/messages/getMessage** - получение массива сообщений

Примеры:

https://test.igov.org.ua/wf-central/service/messages/getMessages

* nID_Subject - ID авторизированого субъекта (добавляется в запрос автоматически после аутентификации пользователя)

Response:
```json
[
	{
		"nID":76,"sHead":"Закликаю владу перевести цю послугу в електронну форму!"
		,"sBody":"Дніпропетровськ - Видача витягу з технічної документації про нормативну грошову оцінку земельної ділянки"
		,"sDate":"2015-06-03 22:09:16.536"
		,"nID_Subject":0
		,"sMail":"bvv4ik@gmail.com"
		,"sContacts":""
		,"sData":""
		,"oSubjectMessageType": {
			"sDescription": "Просьба добавить услугу",
			"nID": 0,
			"sName": "ServiceNeed"
		}
	}
]
```

**HTTP Metod: GET**

**HTTP Context: http://server:port/wf-central/service/messages/getMessage** - получение сообщения

* nID - ИД-номер сообщения

Примеры:
https://test.igov.org.ua/wf-central/service/messages/getMessage?nID=76

* nID - ID сообщения

Ответ:
```json
{
	"nID":76
	,"sHead":"Закликаю владу перевести цю послугу в електронну форму!"
	,"sBody":"Дніпропетровськ - Видача витягу з технічної документації про нормативну грошову оцінку земельної ділянки"
	,"sDate":"2015-06-03 22:09:16.536"
	,"nID_Subject":0
	,"sMail":"bvv4ik@gmail.com"
	,"sContacts":""
	,"sData":""
	,"oSubjectMessageType": {
		"sDescription": "Просьба добавить услугу",
		"nID": 0,
		"sName": "ServiceNeed"
	}
}
```
**HTTP Metod: POST**

**HTTP Context: http://server:port/wf-central/service/messages/setMessage** - сохранение сообщения

* sHead - Строка-заглавие сообщения
* sBody - Строка-тело сообщения
* nID_Subject ИД-номер субьекта (автора) //опционально (добавляется в запрос автоматически после аутентификации пользователя)
* sMail - Строка электронного адреса автора //опционально
* sContacts - Строка контактов автора //опционально
* sData - Строка дополнительных данных автора //опционально
* nID_SubjectMessageType - ИД-номер типа сообщения  //опционально (по умолчанию == 0) 

nID_SubjectMessageType:
nID;sName;sDescription
0;ServiceNeed;Просьба добавить услугу
1;ServiceFeedback;Отзыв о услуге


Примеры:
https://test.igov.org.ua/wf-central/service/messages/setMessage?sHead=name&sBody=body&sMail=a@a.a

Ответ:
 Status 200 если Ok

--------------------------------------------------------------------------------------------------------------------------


<a name="13_workWithHistoryEvents">
####13. Работа с историей (Мой журнал)
</a><a href="#0_contents">↑Up</a><br/>

**HTTP Metod: GET**

**HTTP Context: http://server:port/wf-central/service/services/getHistoryEvent** - получение документа по ид документа

* nID - ИД-номер документа
* nID_Subject - ID авторизированого субъекта (добавляется в запрос автоматически после аутентификации пользователя)

Пример:
https://test.igov.org.ua/wf-central/service/services/getHistoryEvent?nID=1

----------------------------------------------------------------------------------------------------------------------------

**HTTP Metod: GET**

**HTTP Context: http://server:port/wf-central/service/services/getHistoryEvents** - загрузка событий

* nID_Subject - ID авторизированого субъекта (добавляется в запрос автоматически после аутентификации пользователя)????????

Пример:
https://test.igov.org.ua/wf-central/service/services/getHistoryEvents?nID_Subject=3

 ---------------------------------------------------------------------------------------------------------------------------

**HTTP Metod: POST**

**HTTP Context: http://server:port/wf-central/service/services/setHistoryEvent** - сохранение события

* nID_Subject - ИД-строка субъекта, который загрузил документ (необязательное поле)???????????????????????????????????
* nID_HistoryEventType - ИД-номер типа документа (необязательное поле)
* sEventName - строка - кастомное описание документа (необязательное поле)
* sMessage - строка - сохраняемое содержимое (обязательное поле)

--------------------------------------------------------------------------------------------------------------------------

<a name="14_uploadFileToDb">
####14. Аплоад(upload) и прикрепление файла в виде атачмента к таске Activiti
</a><a href="#0_contents">↑Up</a><br/>

**HTTP Metod: POST**

**HTTP Context: http://server:port/wf-region/service/rest/file/upload_file_as_attachment** - Аплоад(upload) и прикрепление файла в виде атачмента к таске Activiti

* taskId - ИД-номер таски
* description - описание
* file - в html это имя элемента input типа file - <input name="file" type="file" />. в HTTP заголовках - Content-Disposition: form-data; name="file" ...
* nID_Subject - ID авторизированого субъекта (добавляется в запрос автоматически после аутентификации пользователя)

Пример:
http://test.igov.org.ua/wf-region/service/rest/file/upload_file_as_attachment?taskId=68&description=ololo"

Ответ без ошибок:
```json
{"taskId":"38","processInstanceId":null,"userId":"kermit","name":"jmt.png","id":"45","type":"image/png;png","description":"SomeDocumentDescription","time":1433539278957,"url":null} 
ID созданного attachment - "id":"45"
```
Ответ с ошибкой:
```json
{"code":"SYSTEM_ERR","message":"Cannot find task with id 384"}
```

--------------------------------------------------------------------------------------------------------------------------

<a name="15_workWithServices">
####15. Работа с каталогом сервисов
</a><a href="#0_contents">↑Up</a><br/>

**HTTP Context: http://server:port/wf-central/service/services/getServicesTree** - Получение делева сервисов

**HTTP Metod: GET**

* sFind - фильтр по имени сервиса (не обязательный параметр). Если задано, то производится фильтрация данных - возвращаются только сервиса в имени которых встречается значение этого параметра, без учета регистра.
* nID_Subject - ID авторизированого субъекта (добавляется в запрос автоматически после аутентификации пользователя)

Пример:
https://test.igov.org.ua/wf-central/service/services/getServicesTree

Ответ:
```json
[{"nID":1,"sID":"Citizen","sName":"Громадянам","nOrder":1,"aSubcategory":[{"nID":1,"sName":"Будівництво, нерухомість, земля","sID":"Build","nOrder":1,"aService":[{"sSubjectOperatorName":"Міська Рада","subjectOperatorName":"Міська Рада","nID":6,"sName":"Видача відомостей з документації, що включена до місцевого фонду документації із землеустрою.","nOrder":6,"nSub":1},{"sSubjectOperatorName":"Міська Рада","subjectOperatorName":"Міська Рада","nID":8,"sName":"Надання довідки про перебування на квартирному обліку при міськвиконкомі за місцем проживання та в житлово-будівельному кооперативі.","nOrder":8,"nSub":1},{"sSubjectOperatorName":"Міська Рада","subjectOperatorName":"Міська Рада","nID":9,"sName":"Надання довідки про перебування на обліку бажаючих отримати земельну ділянку під індивідуальне будівництво","nOrder":9,"nSub":0},{"sSubjectOperatorName":"Міська Рада","subjectOperatorName":"Міська Рада","nID":10,"sName":"Видача витягу з технічної документації про нормативну грошову оцінку земельної ділянки","nOrder":10,"nSub":2},{"sSubjectOperatorName":"Міська Рада","subjectOperatorName":"Міська Рада","nID":11,"sName":"Надання відомостей з Державного земельного кадастру у формі витягу з Державного земельного кадастру про земельну ділянку","nOrder":11,"nSub":0},{"sSubjectOperatorName":"Міська Рада","subjectOperatorName":"Міська Рада","nID":12,"sName":"Присвоєння поштової адреси об’єкту нерухомого майна","nOrder":12,"nSub":1},{"sSubjectOperatorName":"Міська Рада","subjectOperatorName":"Міська Рада","nID":13,"sName":"Видача довідок про перебування на квартирному обліку","nOrder":13,"nSub":0}]
```

**HTTP Context: http://server:port/wf-central/service/services/getService** - Получение сервиса

**HTTP Metod: GET**

* nID - ИД-номер сервиса
* nID_Subject - ID авторизированого субъекта (добавляется в запрос автоматически после аутентификации пользователя)

Пример:
https://test.igov.org.ua/wf-central/service/services/getService?nID=1

Ответ:
```json
{"sSubjectOperatorName":"МВС","subjectOperatorName":"МВС","nID":1,"sName":"Отримати довідку про несудимість","nOrder":1,"aServiceData":[{"nID":1,"nID_City":{"nID":2,"sName":"Кривий Ріг","nID_Region":{"nID":1,"sName":"Дніпропетровська"}},"nID_ServiceType":{"nID":1,"sName":"Внешняя","sNote":"Пользователь переходит по ссылке на услугу, реализованную на сторонней платформе"},"oSubject_Operator":{"nID":1,"oSubject":{"nID":1,"sID":"ПАО","sLabel":"ПАО ПриватБанк","sLabelShort":"ПриватБанк"},"sOKPO":"093205","sFormPrivacy":"ПАО","sName":"ПриватБанк","sNameFull":"Банк ПриватБанк"},"oData":"{}","sURL":"https://dniprorada.igov.org.ua","bHidden":false}],"sInfo":"","sFAQ":"","sLaw":"","nSub":0}
```

**HTTP Context: http://server:port/wf-central/service/services/setService** - Изменение сервиса. Можно менять/добавлять, но не удалять данные внутри сервиса, на разной глубине вложенности. Передается json в теле POST запроса в том же формате, в котором он был в getService. 

**HTTP Metod: POST**

Вовращает: HTTP STATUS 200 + json представление сервиса после изменения. Чаще всего то же, что было передано в теле POST запроса + сгенерированные id-шники вложенных сущностей, если такие были.

Пример:
https://test.igov.org.ua/wf-central/service/services/setService
```json
{
    "sSubjectOperatorName": "МВС",
    "subjectOperatorName": "МВС",
    "nID": 1,
    "sName": "Отримати довідку про несудимість",
    "nOrder": 1,
    "aServiceData": [
        {
            "nID": 1,
            "nID_City": {
                "nID": 2,
                "sName": "Кривий Ріг",
                "nID_Region": {
                    "nID": 1,
                    "sName": "Дніпропетровська"
                }
            },
            "nID_ServiceType": {
                "nID": 1,
                "sName": "Внешняя",
                "sNote": "Пользователь переходит по ссылке на услугу, реализованную на сторонней платформе"
            },
            "oSubject_Operator": {
                "nID": 1,
                "oSubject": {
                    "nID": 1,
                    "sID": "ПАО",
                    "sLabel": "ПАО ПриватБанк",
                    "sLabelShort": "ПриватБанк"
                },
                "sOKPO": "093205",
                "sFormPrivacy": "ПАО",
                "sName": "ПриватБанк",
                "sNameFull": "Банк ПриватБанк"
            },
            "oData": "{}",
            "sURL": "https://dniprorada.igov.org.ua",
            "bHidden": false
        }
    ],
    "sInfo": "",
    "sFAQ": "",
    "sLaw": "",
    "nSub": 0
}
```
Ответ:
```json
{
    "sSubjectOperatorName": "МВС",
    "subjectOperatorName": "МВС",
    "nID": 1,
    "sName": "Отримати довідку про несудимість",
    "nOrder": 1,
    "aServiceData": [
        {
            "nID": 1,
            "nID_City": {
                "nID": 2,
                "sName": "Кривий Ріг",
                "nID_Region": {
                    "nID": 1,
                    "sName": "Дніпропетровська"
                }
            },
            "nID_ServiceType": {
                "nID": 1,
                "sName": "Внешняя",
                "sNote": "Пользователь переходит по ссылке на услугу, реализованную на сторонней платформе"
            },
            "oSubject_Operator": {
                "nID": 1,
                "oSubject": {
                    "nID": 1,
                    "sID": "ПАО",
                    "sLabel": "ПАО ПриватБанк",
                    "sLabelShort": "ПриватБанк"
                },
                "sOKPO": "093205",
                "sFormPrivacy": "ПАО",
                "sName": "ПриватБанк",
                "sNameFull": "Банк ПриватБанк"
            },
            "oData": "{}",
            "sURL": "https://dniprorada.igov.org.ua",
            "bHidden": false
        }
    ],
    "sInfo": "",
    "sFAQ": "",
    "sLaw": "",
    "nSub": 0
}
```
**HTTP Context: http://server:port/wf-central/service/services/removeService** - Удаление сервиса. 

**HTTP Metod: DELETE**

* nID - ИД-номер сервиса
* bRecursive (не обязательно, по умолчанию false) - Удалять рекурсивно все данные связанные с сервисом. Если false, то при наличии вложенных сущностей, ссылающихся на эту, сервис удален не будет.
* nID_Subject - ID авторизированого субъекта (добавляется в запрос автоматически после аутентификации пользователя)

Вовращает:

HTTP STATUS 200 - удаление успешно.
HTTP STATUS 304 - не удалено.

Пример 1:
https://test.igov.org.ua/wf-central/service/services/removeService?nID=1

Ответ 1: HTTP STATUS 304

Пример 2:
https://test.igov.org.ua/wf-central/service/services/removeService?nID=1&bRecursive=true

Ответ 2: HTTP STATUS 200
```json
{
    "code": "success",
    "message": "class org.wf.dp.dniprorada.model.Service id: 1 removed"
}
```

**HTTP Context: http://server:port/wf-central/service/services/removeServiceData** - Удаление сущности ServiceData.

**HTTP Metod: DELETE**

* nID - идентификатор ServiceData
* bRecursive (не обязательно, по умолчанию false) - Удалять рекурсивно все данные связанные с ServiceData. Если false, то при наличии вложенных сущностей, ссылающихся на эту, ServiceData удалена не будет.
* nID_Subject - ID авторизированого субъекта (добавляется в запрос автоматически после аутентификации пользователя)

Вовращает:

HTTP STATUS 200 - удаление успешно.
HTTP STATUS 304 - не удалено.

Пример:
https://test.igov.org.ua/wf-central/service/services/removeServiceData?nID=1&bRecursive=true

Ответ: HTTP STATUS 200
```json
{
    "code": "success",
    "message": "class org.wf.dp.dniprorada.model.ServiceData id: 1 removed"
}
```

**HTTP Context: http://server:port/wf-central/service/services/removeSubcategory** - Удаление подкатегории.

**HTTP Metod: DELETE**

* nID - идентификатор подкатегории.
* bRecursive (не обязательно, по умолчанию false) - Удалять рекурсивно все данные связанные с подкатегорией. Если false, то при наличии вложенных сущностей, ссылающихся на эту, подкатегория удалена не будет.
* nID_Subject - ID авторизированого субъекта (добавляется в запрос автоматически после аутентификации пользователя)

Вовращает:

HTTP STATUS 200 - удаление успешно.
HTTP STATUS 304 - не удалено.

Пример 1:
https://test.igov.org.ua/wf-central/service/services/removeSubcategory?nID=1

Ответ 1: HTTP STATUS 304

Пример 2:
https://test.igov.org.ua/wf-central/service/services/removeSubcategory?nID=1&bRecursive=true

Ответ 2: HTTP STATUS 200
```json
{
    "code": "success",
    "message": "class org.wf.dp.dniprorada.model.Subcategory id: 1 removed"
}
```

**HTTP Context: http://server:port/wf-central/service/services/removeCategory** - Удаление категории.

**HTTP Metod: DELETE**

* nID - идентификатор подкатегории.
* bRecursive (не обязательно, по умолчанию false) - Удалять рекурсивно все данные связанные с категорией. Если false, то при наличии вложенных сущностей, ссылающихся на эту, категория удалена не будет.
* nID_Subject - ID авторизированого субъекта (добавляется в запрос автоматически после аутентификации пользователя)

Вовращает:

HTTP STATUS 200 - удаление успешно.
HTTP STATUS 304 - не удалено.

Пример 1:
https://test.igov.org.ua/wf-central/service/services/removeCategory?nID=1

Ответ 1: HTTP STATUS 304

Пример 2:
https://test.igov.org.ua/wf-central/service/services/removeCategory?nID=1&bRecursive=true

Ответ 2: HTTP STATUS 200
```json
{
    "code": "success",
    "message": "class org.wf.dp.dniprorada.model.Category id: 1 removed"
}
```

**HTTP Context: http://server:port/wf-central/service/services/removeServicesTree** - Удаление всего дерева сервисов и категорий.

**HTTP Metod: DELETE**

* nID_Subject - ID авторизированого субъекта (добавляется в запрос автоматически после аутентификации пользователя)

Вовращает:

HTTP STATUS 200 - удаление успешно.

Пример 1:
https://test.igov.org.ua/wf-central/service/services/removeServicesTree

Ответ 1: HTTP STATUS 200
```json
{
    "code": "success",
    "message": "ServicesTree removed"
}
```

**HTTP Context: http://server:port/wf-central/service/services/getPlaces** - Получения дерева мест (регионов и городов).

**HTTP Metod: GET**

* nID_Subject - ID авторизированого субъекта (добавляется в запрос автоматически после аутентификации пользователя)

Пример:
https://test.igov.org.ua/wf-central/service/services/getPlaces

Ответ:
```json
[
    {
        "nID": 1,
        "sName": "Дніпропетровська",
        "aCity": [
            {
                "nID": 1,
                "sName": "Дніпропетровськ"
            },
            {
                "nID": 2,
                "sName": "Кривий Ріг"
            }
        ]
    },
    {
        "nID": 2,
        "sName": "Львівська",
        "aCity": [
            {
                "nID": 3,
                "sName": "Львів"
            }
        ]
    },
    {
        "nID": 3,
        "sName": "Івано-Франківська",
        "aCity": [
            {
                "nID": 4,
                "sName": "Івано-Франківськ"
            },
            {
                "nID": 5,
                "sName": "Калуш"
            }
        ]
    },
    {
        "nID": 4,
        "sName": "Миколаївська",
        "aCity": []
    },
    {
        "nID": 5,
        "sName": "Київська",
        "aCity": [
            {
                "nID": 6,
                "sName": "Київ"
            }
        ]
    },
    {
        "nID": 6,
        "sName": "Херсонська",
        "aCity": [
            {
                "nID": 7,
                "sName": "Херсон"
            }
        ]
    },
    {
        "nID": 7,
        "sName": "Рівненська",
        "aCity": [
            {
                "nID": 8,
                "sName": "Кузнецовськ"
            }
        ]
    },
    {
        "nID": 8,
        "sName": "Волинська",
        "aCity": [
            {
                "nID": 9,
                "sName": "Луцьк"
            }
        ]
    }
]
```

**HTTP Context: http://server:port/wf-central/service/services/setPlaces** - Изменение дерева мест (регионов и городов). Можно менять регионы (не добавлять и не удалять) + менять/добавлять города (но не удалять), Передается json в теле POST запроса в том же формате, в котором он был в getPlaces. 

**HTTP Metod: POST**

* nID_Subject - ID авторизированого субъекта (добавляется в запрос автоматически после аутентификации пользователя)

Возвращает: HTTP STATUS 200 + json представление сервиса после изменения. Чаще всего то же, что было передано в теле POST запроса + сгенерированные id-шники вложенных сущностей, если такие были.

Пример:
https://test.igov.org.ua/wf-central/service/services/setPlaces
```json
[
    {
        "nID": 1,
        "sName": "Дніпропетровська",
        "aCity": [
            {
                "nID": 1,
                "sName": "Cічеслав"
            },
            {
                "nID": 2,
                "sName": "Кривий Ріг"
            }
        ]
    }
]
```
Ответ: HTTP STATUS 200
```json
[
    {
        "nID": 1,
        "sName": "Дніпропетровська",
        "aCity": [
            {
                "nID": 1,
                "sName": "Cічеслав"
            },
            {
                "nID": 2,
                "sName": "Кривий Ріг"
            }
        ]
    }
]
```

**HTTP Context: http://server:port/wf-central/service/services/setServicesTree** - Изменение дерева категорий (с вложенными подкатегориями и сервисами). Можно менять категории (не добавлять и не удалять) + менять/добавлять (но не удалять) вложенные сущности, Передается json в теле POST запроса в том же формате, в котором он был в getServicesTree. 

**HTTP Metod: POST**

* nID_Subject - ID авторизированого субъекта (добавляется в запрос автоматически после аутентификации пользователя)

Возвращает: HTTP STATUS 200 + json представление сервиса после изменения. Чаще всего то же, что было передано в теле POST запроса + сгенерированные id-шники вложенных сущностей, если такие были.

Пример:
https://test.igov.org.ua/wf-central/service/services/setServicesTree
```json
[
    {
        "nID": 1,
        "sID": "Citizen",
        "sName": "Гражданин",
        "nOrder": 1,
        "aSubcategory": [
            {
                "nID": 5,
                "sName": "Праця2",
                "sID": "Work",
                "nOrder": 3,
                "aService": [
                    {
                        "nID": 3,
                        "sName": "Видача картки обліку об’єкта торговельного призначення, сфери послуг та з виробництва продуктів харчування",
                        "nOrder": 3
                    },
                    {
                        "nID": 4,
                        "sName": "Легалізація об’єднань громадян шляхом повідомлення",
                        "nOrder": 4
                    }
                ]
            }
            ]
         }
]
```
Ответ: HTTP STATUS 200
```json
[
    {
        "nID": 1,
        "sID": "Citizen",
        "sName": "Гражданин",
        "nOrder": 1,
        "aSubcategory": [
            {
                "nID": 5,
                "sName": "Праця2",
                "sID": "Work",
                "nOrder": 3,
                "aService": [
                    {
                        "nID": 3,
                        "sName": "Видача картки обліку об’єкта торговельного призначення, сфери послуг та з виробництва продуктів харчування",
                        "nOrder": 3
                    },
                    {
                        "nID": 4,
                        "sName": "Легалізація об’єднань громадян шляхом повідомлення",
                        "nOrder": 4
                    }
                ]
            }
            ]
         }
]
```

<a name="16_getWorkflowStatistics">
#### 16. Получение статистики по задачам в рамках бизнес процесса
</a><a href="#0_contents">↑Up</a><br/>


**HTTP Metod: GET**

**HTTP Context: https://server:port/wf-region/service/rest/download_bp_timing?sID_BP_Name=XXX&sDateAt=XXX8&sDateTo=XXX**

* {sID_BP_Name} - ID бизнес процесса
* {sDateAt} - Дата начала периода для выборки в формате yyyy-MM-dd
* {sDateTo} - Дата окончания периода для выборки в формате yyyy-MM-dd
* {nRowsMax} - необязательный параметр. Максимальное значение завершенных задач для возврата. По умолчанию 1000.
* {nRowStart} - Необязательный параметр. Порядковый номер завершенной задачи в списке для возврата. По умолчанию 0.

Метод возвращает .csv файл со информацией о завершенных задачах в указанном бизнес процессе за период.
Формат выходного файла
Assignee - кто выполнял задачу
Start Time - Дата и время начала
Duration in millis - Длительность выполнения задачи в миллисекундах
Duration in hours - Длительность выполнения задачи в часах
Name of Task - Название задачи


Пример:
https://test.region.igov.org.ua/wf-region/service/rest/file/download_bp_timing?sID_BP_Name=lviv_mvk-1&sDateAt=2015-06-28&sDateTo=2015-07-01

Пример выходного файла

```
"Assignee","Start Time","Duration in millis","Duration in hours","Name of Task"
"kermit","2015-06-21:09-20-40","711231882","197","Підготовка відповіді на запит: пошук документа"
```


<a name="17_workWithHistoryEvent_Services">
#### 17. Работа с обьектами событий по услугам
</a><a href="#0_contents">↑Up</a><br/>
**HTTP Metod: GET**

**HTTP Context: https://server:port/wf-central/service/services/getHistoryEvent_Service?nID_Protected=ххх***
получает объект события по услуге, параметры: 
* nID_Protected - проверочное число-ид

сначала проверяется корректность числа nID_Protected, где последняя цифра - это последний разряд контрольной суммы (по
<a href="https://ru.wikipedia.org/wiki/%D0%90%D0%BB%D0%B3%D0%BE%D1%80%D0%B8%D1%82%D0%BC_%D0%9B%D1%83%D0%BD%D0%B0">алгоритму Луна</a>) для всего числа без нее.
- если не совпадает -- возвращается ошибка "CRC Error" (код состояния HTTP 403) 
- если совпадает -- ищется запись по nID = nID_Protected без последней цифры
- Если не найдена запись, то возвращает объект ошибки со значением "Record not found"  (код состояния HTTP 403)
- иначе возвращает обьект

пример:
http://test.igov.org.ua/wf-central/service/services/getHistoryEvent_Service?nID_Protected=11

**HTTP Metod: GET**

**HTTP Context: https://server:port/wf-central/service/services/addHistoryEvent_Service?nID_Task=xxx&sStatus=xxx&nID_Subject=xxx***

 добавляет объект события по услуге, параметры: 
 * nID_Task - ИД-номер задачи (long)
 * nID_Subject - ИД-номер (long) //опциональный
 * sStatus - строка-статус (long)
 * sID_Status - строка-статус (long) //опциональный (для авто-генерации значения поля sID)

при добавлении записи генерируется поле nID_Protected по принципу
nID_Protected = nID (ид новой записи) + "контрольная цифра"

контрольная цифра -- это последний разряд суммы цифр числа nID по
<a href="https://ru.wikipedia.org/wiki/%D0%90%D0%BB%D0%B3%D0%BE%D1%80%D0%B8%D1%82%D0%BC_%D0%9B%D1%83%D0%BD%D0%B0">алгоритму Луна</a>
это поле используется для проверки корректности запрашиваемого ид записи (в методах get и update)

пример:
http://test.igov.org.ua/wf-central/service/services/addHistoryEvent_Service?nID_Task=2&sStatus=new&nID_Subject=2

ответ:
```json
{"nID":1001,"sID":null,"nID_Task":2,"nID_Subject":2,"sStatus":"new","sID_Status":null,"nID_Protected":10013,"id":1001}
```

**HTTP Metod: GET**

**HTTP Context: https://server:port/wf-central/service/services/updateHistoryEvent_Service?nID=xxx&sStatus=xxx***

 обновляет объект события по услуге,
параметры:
* nID_Protected - проверочное число-ид
* sStatus - строка-статус
* sID_Status - строка-статус (long) //опциональный

- сначала проверяется корректность числа nID_Protected -- последняя цифра должна быть "контрольной" (по
<a href="https://ru.wikipedia.org/wiki/%D0%90%D0%BB%D0%B3%D0%BE%D1%80%D0%B8%D1%82%D0%BC_%D0%9B%D1%83%D0%BD%D0%B0">алгоритму Луна</a>) для всего числа без нее.
- если не совпадает -- возвращается ошибка "CRC Error"  (код состояния HTTP 403)
- если совпадает -- ищется запись по nID = nID_Protected без последней цифры
- Если не найдена запись, то возвращает объект ошибки со значением "Record not found"  (код состояния HTTP 403)
- обновление записи (если были изменения)

пример
http://test.igov.org.ua/wf-central/service/services/updateHistoryEvent_Service?nID_Protected=11&sStatus=finish


<a name="18_workWithFlowSlot">
#### 18. Работа со слотами потока
</a><a href="#0_contents">↑Up</a><br/>

**HTTP Context: http://server:port/wf-central/service/flow/getFlowSlots_ServiceData** - Получение слотов по сервису сгруппированных по дням.

**HTTP Metod: GET**

Параметры:
* nID_ServiceData - ID сущности ServiceData (обязательный)
* bAll - если false то из возвращаемого объекта исключаются элементы, содержащие "bHasFree":false "bFree":false (опциональный, по умолчанию false)
* nDays - колличество дней от сегодняшего включительно, до nDays в будующее за который нужно вернуть слоты (опциональный, по умолчанию 60)

Пример:
https://test.igov.org.ua/wf-central/service/flow/getFlowSlots_ServiceData?nID_ServiceData=1

Ответ:  HTTP STATUS 200
```json
{
    "aDay": [
        {
            "sDate": "2015-07-19",
            "bHasFree": true,
            "aSlot": [
                {
                    "nID": 1,
                    "sTime": "18:00",
                    "nMinutes": 15,
                    "bFree": true
                }
            ]
        },
        {
            "sDate": "2015-07-20",
            "bHasFree": true,
            "aSlot": [
                {
                    "nID": 3,
                    "sTime": "18:15",
                    "nMinutes": 15,
                    "bFree": true
                }
            ]
        }
    ]
}
```
Калькулируемые поля в ответе:

флаг "bFree" - является ли слот свободным? Слот считается свободным если на него нету тикетов у которых nID_Task_Activiti равен null, а у тех у которых nID_Task_Activiti = null - время создания тикета (sDateEdit) не позднее чем текущее время минус 5 минут (предопределенная константа)

флаг "bHasFree" равен true , если данных день содержит хотя бы один свободный слот.


**HTTP Context: http://server:port/wf-central/service/flow/setFlowSlots_ServiceData** - Создание или обновление тикета в указанном слоте.

**HTTP Metod: POST**

Параметры:
* nID_FlowSlot - ID сущности FlowSlot (обязательный)
* nID_Subject - ID сущнсоти Subject - субьект пользователь услуги, который подписывается на слот (обязательный)
* nID_Task_Activiti - ID таски активити процесса предоставления услуги (не обязательный - вначале он null, а потом засчивается после подтверждения тикета, и создания процесса)

Пример:
http://test.igov.org.ua/wf-central/service/flow/setFlowSlot_ServiceData
* nID_FlowSlot=1
* nID_Subject=2

Ответ:  HTTP STATUS 200

{
    "nID_Ticket": 1000
}

Поля в ответе:

поле "nID_Ticket" - ID созданной/измененной сущности FlowSlotTicket.


**HTTP Context: http://server:port/wf-central/service/flow/buildFlowSlots** - Генерация слотов на заданный интервал для заданного потока.

**HTTP Metod: POST**

Параметры:
* nID_Flow_ServiceData - номер-ИД потока по данным сервиса (по которому генерируется слоты) (обязательный)
* sDateStart - дата "начиная с такого-то момента времени", в формате "2015-06-28 12:12:56.001" (опциональный)
* sDateStop - дата "заканчивая к такому-то моменту времени", в формате "2015-07-28 12:12:56.001" (опциональный)

Пример:
http://test.igov.org.ua/wf-central/service/flow/buildFlowSlots
* nID_Flow_ServiceData=1
* sDateStart=2015-06-01 00:00:00.000
* sDateStop=2015-06-07 00:00:00.000

Ответ:  HTTP STATUS 200 + json перечисление всех сгенерированных слотов.

Ниже приведена часть json ответа:
```json
[
    {
        "nID": 1000,
        "sTime": "08:00",
        "nMinutes": 15,
        "bFree": true
    },
    {
        "nID": 1001,
        "sTime": "08:15",
        "nMinutes": 15,
        "bFree": true
    },
    {
        "nID": 1002,
        "sTime": "08:30",
        "nMinutes": 15,
        "bFree": true
    },
...
]
```
Если на указанные даты слоты уже сгенерены то они не будут генерится повторно, и в ответ включаться не будут.


**HTTP Context: http://server:port/wf-central/service/flow/clearFlowSlots** - Удаление слотов на заданный интервал для заданного потока.

**HTTP Metod: DELETE**

Параметры:
* nID_Flow_ServiceData - номер-ИД потока по данным сервиса (по которому удаляются слоты) (обязательный)
* sDateStart - дата "начиная с такого-то момента времени", в формате "2015-06-28 12:12:56.001" (обязательный)
* sDateStop - дата "заканчивая к такому-то моменту времени", в формате "2015-07-28 12:12:56.001" (обязательный)
* bWithTickets - удалять ли слоты с тикетами, отвязывая тикеты от слотов? (опциональный, по умолчанию false)

Пример:
http://test.igov.org.ua/wf-central/service/flow/clearFlowSlots?nID_Flow_ServiceData=1&sDateStart=2015-06-01 00:00:00.000&sDateStop=2015-06-07 00:00:00.000

Ответ:  HTTP STATUS 200 + json Обьект содержащий 2 списка:
* aDeletedSlot - удаленные слоты
* aSlotWithTickets - слоты с тикетами. Елси bWithTickets=true то эти слоты тоже удаляются и будут перечислены в aDeletedSlot, иначе - не удаляются.

Ниже приведена часть json ответа:
```json
{
    "aDeletedSlot": [
        {
            "nID": 1000,
            "sTime": "08:00",
            "nMinutes": 15,
            "bFree": true
        },
        {
            "nID": 1001,
            "sTime": "08:15",
            "nMinutes": 15,
            "bFree": true
        },
        ...
     ],
     "aSlotWithTickets": []
}
```

<a name="19">
#### 19. Работа с джоинами субьектами (отделениями/филиалами)
</a><a href="#0_contents">↑Up</a><br/>
(таска: https://github.com/e-government-ua/i/issues/487)

Поля:
* nID - ИД-номер автоитеррируемый (уникальный, обязательный) (long)
* nID_SubjectOrgan - ИД-номер (long)
* sNameRu - строка <200 символов
* sNameUa - ИД-строка <200 символов
* sID_Privat - ИД-строка ключ-частный <60 символов //опциональный
* sID_Public - строка ключ-публичный <60 символов
* sGeoLongitude - строка долготы //опциональный
* sGeoLatitude - строка широты //опциональный
* nID_Region - ИД-номер //опциональный
* nID_City - ИД-номер //опциональный
* sID_UA - ИД-строка кода классификатора КОАТУУ //опциональный


**getSubjectOrganJoins - получает весь массив объектов п.2 (либо всех либо в рамках заданных в запросе nID_Region, nID_City или sID_UA)**
<br>
**Method: GET**
Параметры:
* nID_SubjectOrgan - ИД-номер (в урл-е)
* nID_Region - ИД-номер (в урл-е) //опциональный (только если надо задать или задан)
* nID_City - ИД-номер (в урл-е) //опциональный (только если надо задать или задан)
* sID_UA - ИД-строка (в урл-е) //опциональный (только если надо задать или задан)

Пример ответа:
```json
[
	{	"nID_SubjectOrgan":32343
		,"sNameUa":"Українська мова"
		,"sNameRu":"Русский язык"
		,"sID_Privat":"12345"
		,"sID_Public":"130501"
		,"sGeoLongitude":"15.232312"
		,"sGeoLatitude":"23.234231"
		,"nID_Region":11
		,"nID_City":33
		,"sID_UA":"1"
	}
]
```
Пример:
https://test.igov.org.ua/wf-central/service/services/getSubjectOrganJoins?nID_SubjectOrgan=1&sID_UA=1


**setSubjectOrganJoin - добавляет/обновляет массив объектов п.2 (сопоставляя по по ИД, и связывая новые с nID_Region, nID_City или sID_UA, по совпадению их названий)**
<br>
**Method: POST**
Параметры:
* nID_SubjectOrgan - ИД-номер
* nID //опциональный, если добавление
* sNameRu //опциональный
* sNameUa //опциональный
* sID_Privat //опциональный
* sID_Public //опциональный, если апдейт
* sGeoLongitude //опциональный
* sGeoLatitude //опциональный
* nID_Region //опциональный
* nID_City //опциональный
* sID_UA //опциональный

Пример:
https://test.igov.org.ua/wf-central/service/services/setSubjectOrganJoin?nID_SubjectOrgan=1&sNameRu=Днепр.РОВД
<br>


**removeSubjectOrganJoins - удаляет массив объектов п.2 (находя их по ИД)**
<br>
**Method: POST**
Параметры:
* nID_SubjectOrgan - ИД-номер (в урл-е)
* asID_Public - массив ИД-номеров  (в урл-е) (например [3423,52354,62356,63434])

Пример:
https://test.igov.org.ua/wf-central/service/services/removeSubjectOrganJoins?nID_SubjectOrgan=1&asID_Public=130505,130506,130507,130508


<a name="20">
#### 20. Получение кнопки для оплаты через LiqPay
<br><a href="#0_contents">↑Up</a>
**Method: GET**

**HTTP Context: https://server:port/wf-central/service/services/getPayButtonHTML_LiqPay**

Параметры:
* sID_Merchant - ид меранта
* sSum - сумма оплаты
* oID_Currency - валюта
* oLanguage - язык
* sDescription - описание
* sID_Order - ид заказа
* sURL_CallbackStatusNew - URL для отправки статуса
* sURL_CallbackPaySuccess - URL для отправки ответа
* nID_Subject - ид субъекта
* bTest - тестовый вызов или нет

Пример:
https://test.igov.org.ua/wf-central/service/services/getPayButtonHTML_LiqPay?sID_Merchant=i10172968078&sSum=55,00&oID_Currency=UAH&oLanguage=RUSSIAN&sDescription=test&sID_Order=12345&sURL_CallbackStatusNew=&sURL_CallbackPaySuccess=&nID_Subject=1&bTest=true

<a name="21">
####21. Работа со странами
</a><a href="#0_contents">↑Up</a>

----------------------

**HTTP Context: https://server:port/wf-central/service/services/setCountry**

**Method: GET**

 апдейтит элемент (если задан один из уникальных ключей) или вставляет (если не задан nID), и отдает экземпляр нового объекта.
 
Параметры:
 * nID - ИД-номер, идентификатор записи
 * nID_UA - ИД-номер Код, в Украинском классификаторе (уникальное)
 * sID_Two - ИД-строка Код-двухсимвольный, международный (уникальное, строка 2 символа)
 * sID_Three - ИД-строка Код-трехсимвольный, международный (уникальное, строка 3 символа)
 * sNameShort_UA - Назва країни, коротка, Украинская (уникальное, строка до 100 символов)
 * sNameShort_EN - Назва країни, коротка, англійською мовою (уникальное, строка до 100 символов)
 * sReference_LocalISO - Ссылка на локальный ISO-стандарт, с названием (a-teg с href) (строка до 100 символов)
 
Если нет ни одного параметра  возвращает ошибку ```403. All args are null!```
Если есть один из уникальных ключей, но запись не найдена -- ошибка ```403. Record not found!```
Если кидать "новую" запись с одним из уже существующих ключем nID_UA -- то обновится существующая запись по ключу nID_UA, если будет дублироваться другой ключ -- ошибка ```403. Could not execute statement``` (из-за уникальных констрейнтов)

----------------------

**HTTP Context: https://server:port/wf-central/service/services/getCountries**

**Method: GET**

 возвращает весь список стран (залит справочник согласно <a href="https://uk.wikipedia.org/wiki/ISO_3166-1">Википеции</a> и <a href="http://www.profiwins.com.ua/uk/letters-and-orders/gks/4405-426.html">Класифікації країн світу</a>) 

пример: https://test.igov.org.ua/wf-central/service/services/getCountries

----------------------

**HTTP Context: https://server:port/wf-central/service/services/getCountry**

**Method: GET**

 возвращает объект Страны по первому из 4х ключей (nID, nID_UA, sID_Two, sID_Three). 

Если нет ни одного параметра  возвращает ошибку ```403. required at least one of parameters (nID, nID_UA, sID_Two, sID_Three)!```

Eсли задано два ключа от разных записей -- вернется та, ключ который "первее" в таком порядке: nID, nID_UA, sID_Two, sID_Three.

пример: https://test.igov.org.ua/wf-central/service/services/getCountry?nID_UA=123

ответ:
```json
{
"nID_UA":123,
"sID_Two":"AU",
"sID_Three":"AUS",
"sNameShort_UA":"Австралія",
"sNameShort_EN":"Australy",
"sReference_LocalISO":"ISO_3166-2:AU",
"nID":20340
}
```

----------------------

**HTTP Context: https://server:port/wf-central/service/services/removeCountry**

**Method: GET**
 
 удаляет обьект по одному из четырех ключей (nID, nID_UA, sID_Two, sID_Three) или кидает ошибку ```403. Record not found!```.


<a name="22">
####22. Загрузка данных по задачам
</a><a href="#0_contents">↑Up</a>

**Method: GET**

**HTTP Context: https://server:port/wf-region/service/rest/file/downloadTasksData**

Загрузка полей по задачам в виде файла.

Параметры:
* sID_BP - название бизнесс процесса
* sID_State_BP - состояние задачи, по умолчанию **исключается из фильтра** Берется из поля taskDefinitionKey задачи
* saFields - имена полей для выборкы разделенных через ';', чтобы добавить все поля можно использовать - '*'
* nASCI_Spliter - ASCII код для разделителя
* sFileName - имя исходящего файла, по умолчанию - **data_BP-bpName_<today>.txt"**
* sID_Codepage - кодировка исходящего файла, по умолчанию - **win1251**
* sDateCreateFormat - форматирование даты создания таски, по умолчанию - **yyyy-MM-dd HH:mm:ss**
* sDateAt - начальная дата создания таски, по умолчанию - **вчера**
* sDateTo - конечная дата создания таски, по умолчанию - **сегодня**
* nRowStart - начало выборки для пейджирования, по умолчанию - **0**
* nRowsMax - размер выборки для пейджирования, по умолчанию - **1000**

Поля по умолчанию, которые всегда включены в выборку:
* nID_Task - "id таски"
* sDateCreate - "дата создания таски" (в формате sDateCreateFormat)

Особенности обработки полей: 
* Если тип поля enum, то брать не его ИД пункта в энуме а именно значение Если тип поля enum, и в значении присутствует знак ";", то брать только то ту часть текста, которая находится справа от этого знака

Пример:
https://test.region.igov.org.ua/wf-region/service/rest/file/downloadTasksData?&sID_BP=dnepr_spravka_o_doxodax&sID_State_BP=usertask1&sDateAt=2015-06-01&sDateTo=2015-08-01&saFields=${nID_Task};${sDateCreate};${area};;;0;${bankIdlastName} ${bankIdfirstName} ${bankIdmiddleName};4;${aim};${date_start};${date_stop};${place_living};${bankIdPassport};1;${phone};${email}&sID_Codepage=win1251&nASCI_Spliter=18&sDateCreateFormat=dd.mm.yyyy hh:MM:ss&sFileName=dohody.dat

Пример ответа:
```
1410042;16.32.2015 10:07:17;АНД (пров. Універсальний, 12);;;0;БІЛЯВЦЕВ ВОЛОДИМИР ВОЛОДИМИРОВИЧ;4;мета;16/07/2015;17/07/2015;мокешрмшгкеу;АЕ432204 БАБУШКИНСКИМ РО ДГУ УМВД 26.09.1996;1;380102030405;mendeleev.ua@gmail.com
995161;07.07.2015 05:07:27;${area};;;0;ДУБІЛЕТ ДМИТРО ОЛЕКСАНДРОВИЧ;4;для роботи;01/07/2015;07/07/2015;Дніпропетровська, Дніпропетровськ, вул. Донецьке шосе, 15/110;АМ765369 ЖОВТНЕВИМ РВ ДМУ УМВС УКРАЙНИ В ДНИПРОПЕТРОВСЬКИЙ ОБЛАСТИ 18.03.2002;1;${phone};ukr_rybak@rambler.ru
```

----------------------


