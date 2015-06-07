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

* {key} - Ключ процесса

**Response**

```json
	{
		"id":"31" //[1..1]
	}
```

#### 4. Загрузка задач из Activiti:

**HTTP Metod: GET**

**HTTP Context: https://server:port/wf-region/serivce/rest/tasks/{assignee}**

* {assignee} - Владелец

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

* {taskId} - ид задачи
* {attachmentID} - ID прикрепленного файла
* {nFile} - порядковый номер прикрепленного файла

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

* idOwner - ОКПО
* id - id мерчанта

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

* idOwner - ОКПО
* ownerName - название организации
* id - id мерчанта

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

* idOwner - ОКПО
* ownerName - название организации
* id - id мерчанта

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

* nID - ИД-номер документа

Пример:
https://poligon.igov.org.ua/wf-central/service/services/getDocument?nID=1

**Response**

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



----------------------------------------------------------------------------------------------------------------------------

**HTTP Metod: GET**

**HTTP Context: http://server:port/wf-central/service/services/getDocumentContent** - получение контента документа по ид документа

* nID - ИД-номер документа

Пример:
https://poligon.igov.org.ua/wf-central/service/services/getDocumentContent?nID=1

----------------------------------------------------------------------------------------------------------------------------

**HTTP Metod: GET**

**HTTP Context: http://server:port/wf-central/service/services/getDocumentFile** - получение документа в виде файла по ид документа

* nID - ИД-номер документа

Пример:
https://poligon.igov.org.ua/wf-central/service/services/getDocumentFile?nID=1

----------------------------------------------------------------------------------------------------------------------------

**HTTP Metod: GET**

**HTTP Context: http://server:port/wf-central/service/services/getDocuments** - получение списка загруженных субъектом документов

* nID_Subject - ИД-номер субъекта

Пример:
https://poligon.igov.org.ua/wf-central/service/services/getDocuments?nID_Subject=2

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
* nID_Subject - ИД-номер субъекта докуиента (владельца)

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
* nID_Subject - ИД-номер субъекта документа (владельца)
* oFile - обьект файла (тип MultipartFile)

--------------------------------------------------------------------------------------------------------------------------


#### 10. Работа с субъктами


**HTTP Metod: GET**

**HTTP Context: http://server:port/wf-central/service/subject/syncSubject** - получение субъекта, если таков найден, или добавление субъекта в противном случае

От клиента ожидается ОДИН и только ОДИН параметр из нижеперечисленных

* nID - ИД-номер субъекта
* sINN - строка-ИНН (субъект - человек)
* sOKPO - строка-ОКПО (субъек - организация)

Примеры:

https://poligon.igov.org.ua/wf-central/service/subject/syncSubject?sINN=34125265377

https://poligon.igov.org.ua/wf-central/service/subject/syncSubject?sOKPO=123

https://poligon.igov.org.ua/wf-central/service/subject/syncSubject?nID=1

--------------------------------------------------------------------------------------------------------------------------

#### 11. Предоставление и проверка доступа к документам

**HTTP Metod: POST**

**HTTP Context: 
https://seriver:port/wf-central/service/setDocumentLink - запись на доступ, с генерацией и получением уникальной ссылки на него

* nID_Document - ИД-номер документа
* sFIO - ФИО, кому доступ
* sTarget - цель получения доступа
* sTelephone - телефон того, кому доступ предоставляется
* nDays - число милисекунд, на которое предоставляется доступ
* sMail - эл. почта того, кому доступ предоставляется

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

**Response**

```json					
	[				        //[0..N]
	{"name":"sURL",   //[1..1]
	 "value":"https://seriver:port/index#nID_Access=4345&sSecret=JHg3987JHg3987JHg3987" //[1..1]
	}  
	]
```


#### 12. Работа с сообщениями

**HTTP Metod: GET**

**HTTP Context: http://server:port/wf-central/service/messages/getMessage** - получение массива сообщений

Примеры:

https://poligon.igov.org.ua/wf-central/service/messages/getMessages

Ответ:
[{"nID":76,"sHead":"Закликаю владу перевести цю послугу в електронну форму!","sBody":"Дніпропетровськ - Видача витягу з технічної документації про нормативну грошову оцінку земельної ділянки","sDate":"2015-06-03 22:09:16.536","nID_Subject":0,"sMail":"bvv4ik@gmail.com","sContacts":"","sData":""}]


**HTTP Metod: GET**

**HTTP Context: http://server:port/wf-central/service/messages/getMessage** - получение сообщения

* nID - ИД-номер сообщения

Примеры:
https://poligon.igov.org.ua/wf-central/service/messages/getMessage?nID=76

Ответ:
{"nID":76,"sHead":"Закликаю владу перевести цю послугу в електронну форму!","sBody":"Дніпропетровськ - Видача витягу з технічної документації про нормативну грошову оцінку земельної ділянки","sDate":"2015-06-03 22:09:16.536","nID_Subject":0,"sMail":"bvv4ik@gmail.com","sContacts":"","sData":""}

**HTTP Metod: POST**

**HTTP Context: http://server:port/wf-central/service/messages/setMessage** - сохранение сообщения

* sHead - Строка-заглавие сообщения
* sBody - Строка-тело сообщения
* nID_Subject ИД-номер субьекта (автора) //опционально
* sMail - Строка электронного адреса автора //опционально
* sContacts - Строка контактов автора //опционально
* sData - Строка дополнительных данных автора //опционально

Примеры:
https://poligon.igov.org.ua/wf-central/service/messages/setMessage?sHead=name&sBody=body&sMail=a@a.a

Ответ:
Status 200 если Ok

--------------------------------------------------------------------------------------------------------------------------


#### 13. Работа с историей (Мой журнал)

**HTTP Metod: GET**

**HTTP Context: http://server:port/wf-central/service/services/getHistoryEvent** - получение документа по ид документа

* nID - ИД-номер документа

Пример:
https://poligon.igov.org.ua/wf-central/service/services/getHistoryEvent?nID=1

----------------------------------------------------------------------------------------------------------------------------

**HTTP Metod: GET**

**HTTP Context: http://server:port/wf-central/service/services/getHistoryEvents** - получение списка загруженных субъектом документов

* nID_Subject - ИД-номер субъекта

Пример:
https://poligon.igov.org.ua/wf-central/service/services/getHistoryEvents?nID_Subject=3

 ---------------------------------------------------------------------------------------------------------------------------

**HTTP Metod: POST**

**HTTP Context: http://server:port/wf-central/service/services/setHistoryEvent** - сохранение документа

* nID_Subject - ИД-строка субъекта, который загрузил документ ?? (необязательное поле)
* nID_HistoryEventType - ИД-номер типа документа (необязательное поле)
* sEventName - строка - кастомное описание документа (необязательное поле)
* sMessage - строка - сохраняемое содержимое (обязательное поле)

--------------------------------------------------------------------------------------------------------------------------

#### 14. Аплоад(upload) и прикрепление файла в виде атачмента к таске Activiti

**HTTP Metod: POST**

**HTTP Context: http://server:port/wf-region/service/rest/file/upload_file_as_attachment** - Аплоад(upload) и прикрепление файла в виде атачмента к таске Activiti

* taskId - ИД-номер таски
* description - описание
* file - в html это имя элемента input типа file - <input name="file" type="file" />. в HTTP заголовках - Content-Disposition: form-data; name="file" ...

Пример:
http://test.igov.org.ua/wf-region/service/rest/file/upload_file_as_attachment?taskId=68&description=ololo"

Ответ без ошибок:
{"taskId":"38","processInstanceId":null,"userId":"kermit","name":"jmt.png","id":"45","type":"image/png;png","description":"SomeDocumentDescription","time":1433539278957,"url":null} 
ID созданного attachment - "id":"45"

Ответ с ошибкой:
{"code":"SYSTEM_ERR","message":"Cannot find task with id 384"}


--------------------------------------------------------------------------------------------------------------------------

#### 15. Работа с каталогом сервисов

**HTTP Context: http://server:port/wf-central/service/services/getServicesTree** - Получение делева сервисов

**HTTP Metod: GET**

* sFind - фильтр по имени сервиса (не обязательный параметр). Если задано, то производится фильтрация данных - возвращаются только сервиса в имени которых встречается значение этого параметра, без учета регистра.

Пример:
https://poligon.igov.org.ua/wf-central/service/services/getServicesTree

Ответ:
[{"nID":1,"sID":"Citizen","sName":"Громадянам","nOrder":1,"aSubcategory":[{"nID":1,"sName":"Будівництво, нерухомість, земля","sID":"Build","nOrder":1,"aService":[{"sSubjectOperatorName":"Міська Рада","subjectOperatorName":"Міська Рада","nID":6,"sName":"Видача відомостей з документації, що включена до місцевого фонду документації із землеустрою.","nOrder":6,"nSub":1},{"sSubjectOperatorName":"Міська Рада","subjectOperatorName":"Міська Рада","nID":8,"sName":"Надання довідки про перебування на квартирному обліку при міськвиконкомі за місцем проживання та в житлово-будівельному кооперативі.","nOrder":8,"nSub":1},{"sSubjectOperatorName":"Міська Рада","subjectOperatorName":"Міська Рада","nID":9,"sName":"Надання довідки про перебування на обліку бажаючих отримати земельну ділянку під індивідуальне будівництво","nOrder":9,"nSub":0},{"sSubjectOperatorName":"Міська Рада","subjectOperatorName":"Міська Рада","nID":10,"sName":"Видача витягу з технічної документації про нормативну грошову оцінку земельної ділянки","nOrder":10,"nSub":2},{"sSubjectOperatorName":"Міська Рада","subjectOperatorName":"Міська Рада","nID":11,"sName":"Надання відомостей з Державного земельного кадастру у формі витягу з Державного земельного кадастру про земельну ділянку","nOrder":11,"nSub":0},{"sSubjectOperatorName":"Міська Рада","subjectOperatorName":"Міська Рада","nID":12,"sName":"Присвоєння поштової адреси об’єкту нерухомого майна","nOrder":12,"nSub":1},{"sSubjectOperatorName":"Міська Рада","subjectOperatorName":"Міська Рада","nID":13,"sName":"Видача довідок про перебування на квартирному обліку","nOrder":13,"nSub":0}]


**HTTP Context: http://server:port/wf-central/service/services/getService** - Получение сервиса

**HTTP Metod: GET**

* nID - ИД-номер сервиса

Пример:
https://poligon.igov.org.ua/wf-central/service/services/getService?nID=1

Ответ:
{"sSubjectOperatorName":"МВС","subjectOperatorName":"МВС","nID":1,"sName":"Отримати довідку про несудимість","nOrder":1,"aServiceData":[{"nID":1,"nID_City":{"nID":2,"sName":"Кривий Ріг","nID_Region":{"nID":1,"sName":"Дніпропетровська"}},"nID_ServiceType":{"nID":1,"sName":"Внешняя","sNote":"Пользователь переходит по ссылке на услугу, реализованную на сторонней платформе"},"oSubject_Operator":{"nID":1,"oSubject":{"nID":1,"sID":"ПАО","sLabel":"ПАО ПриватБанк","sLabelShort":"ПриватБанк"},"sOKPO":"093205","sFormPrivacy":"ПАО","sName":"ПриватБанк","sNameFull":"Банк ПриватБанк"},"oData":"{}","sURL":"https://dniprorada.igov.org.ua","bHidden":false}],"sInfo":"","sFAQ":"","sLaw":"","nSub":0}

**HTTP Context: http://server:port/wf-central/service/services/setService** - Изменение сервиса. Можно менять/добавлять, но не удалять данные внутри сервиса, на разной глубине вложенности. Передается json в теле POST запроса в том же формате, в котором он был в getService. 

**HTTP Metod: POST**

Вовращает: HTTP STATUS 200 + json представление сервиса после изменения. Чаще всего то же, что было передано в теле POST запроса + сгенерированные id-шники вложенных сущностей, если такие были.

Пример:
https://poligon.igov.org.ua/wf-central/service/services/setService

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

Ответ:
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

**HTTP Context: http://server:port/wf-central/service/services/removeService** - Удаление сервиса. 

**HTTP Metod: DELETE**

* nID - ИД-номер сервиса
* bRecursive (не обязательно, по умолчанию false) - Удалять рекурсивно все данные связанные с сервисом. Если false, то при наличии вложенных сущностей, ссылающихся на эту, сервис удален не будет.

Вовращает:

HTTP STATUS 200 - удаление успешно.
HTTP STATUS 304 - не удалено.

Пример 1:
https://poligon.igov.org.ua/wf-central/service/services/removeService?nID=1

Ответ 1: HTTP STATUS 304

Пример 2:
https://poligon.igov.org.ua/wf-central/service/services/removeService?nID=1&bRecursive=true

Ответ 2: HTTP STATUS 200

{
    "code": "success",
    "message": "class org.wf.dp.dniprorada.model.Service id: 1 removed"
}


**HTTP Context: http://server:port/wf-central/service/services/removeServiceData** - Удаление сущности ServiceData.

**HTTP Metod: DELETE**

* nID - идентификатор ServiceData
* bRecursive (не обязательно, по умолчанию false) - Удалять рекурсивно все данные связанные с ServiceData. Если false, то при наличии вложенных сущностей, ссылающихся на эту, ServiceData удалена не будет.

Вовращает:

HTTP STATUS 200 - удаление успешно.
HTTP STATUS 304 - не удалено.

Пример:
https://poligon.igov.org.ua/wf-central/service/services/removeServiceData?nID=1&bRecursive=true

Ответ: HTTP STATUS 200

{
    "code": "success",
    "message": "class org.wf.dp.dniprorada.model.ServiceData id: 1 removed"
}


**HTTP Context: http://server:port/wf-central/service/services/removeSubcategory** - Удаление подкатегории.

**HTTP Metod: DELETE**

* nID - идентификатор подкатегории.
* bRecursive (не обязательно, по умолчанию false) - Удалять рекурсивно все данные связанные с подкатегорией. Если false, то при наличии вложенных сущностей, ссылающихся на эту, подкатегория удалена не будет.

Вовращает:

HTTP STATUS 200 - удаление успешно.
HTTP STATUS 304 - не удалено.

Пример 1:
https://poligon.igov.org.ua/wf-central/service/services/removeSubcategory?nID=1

Ответ 1: HTTP STATUS 304

Пример 2:
https://poligon.igov.org.ua/wf-central/service/services/removeSubcategory?nID=1&bRecursive=true

Ответ 2: HTTP STATUS 200

{
    "code": "success",
    "message": "class org.wf.dp.dniprorada.model.Subcategory id: 1 removed"
}


**HTTP Context: http://server:port/wf-central/service/services/removeCategory** - Удаление категории.

**HTTP Metod: DELETE**

* nID - идентификатор подкатегории.
* bRecursive (не обязательно, по умолчанию false) - Удалять рекурсивно все данные связанные с категорией. Если false, то при наличии вложенных сущностей, ссылающихся на эту, категория удалена не будет.

Вовращает:

HTTP STATUS 200 - удаление успешно.
HTTP STATUS 304 - не удалено.

Пример 1:
https://poligon.igov.org.ua/wf-central/service/services/removeCategory?nID=1

Ответ 1: HTTP STATUS 304

Пример 2:
https://poligon.igov.org.ua/wf-central/service/services/removeCategory?nID=1&bRecursive=true

Ответ 2: HTTP STATUS 200

{
    "code": "success",
    "message": "class org.wf.dp.dniprorada.model.Category id: 1 removed"
}


**HTTP Context: http://server:port/wf-central/service/services/removeServicesTree** - Удаление всего дерева сервисов и категорий.

**HTTP Metod: DELETE**

Вовращает:

HTTP STATUS 200 - удаление успешно.

Пример 1:
https://poligon.igov.org.ua/wf-central/service/services/removeServicesTree

Ответ 1: HTTP STATUS 200

{
    "code": "success",
    "message": "ServicesTree removed"
}


**HTTP Context: http://server:port/wf-central/service/services/getPlaces** - Получения дерева мест (регионов и городов).

**HTTP Metod: GET**

Пример:
https://poligon.igov.org.ua/wf-central/service/services/getPlaces

Ответ:

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


**HTTP Context: http://server:port/wf-central/service/services/setPlaces** - Изменение дерева мест (регионов и городов). Можно менять регионы (не добавлять и не удалять) + менять/добавлять города (но не удалять), Передается json в теле POST запроса в том же формате, в котором он был в getPlaces. 

**HTTP Metod: POST**

Возвращает: HTTP STATUS 200 + json представление сервиса после изменения. Чаще всего то же, что было передано в теле POST запроса + сгенерированные id-шники вложенных сущностей, если такие были.

Пример:
https://poligon.igov.org.ua/wf-central/service/services/setPlaces

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

Ответ: HTTP STATUS 200

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


**HTTP Context: http://server:port/wf-central/service/services/setServicesTree** - Изменение дерева категорий (с вложенными подкатегориями и сервисами). Можно менять категории (не добавлять и не удалять) + менять/добавлять (но не удалять) вложенные сущности, Передается json в теле POST запроса в том же формате, в котором он был в getServicesTree. 

**HTTP Metod: POST**

Возвращает: HTTP STATUS 200 + json представление сервиса после изменения. Чаще всего то же, что было передано в теле POST запроса + сгенерированные id-шники вложенных сущностей, если такие были.

Пример:
https://poligon.igov.org.ua/wf-central/service/services/setServicesTree

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

Ответ: HTTP STATUS 200

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
