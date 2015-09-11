angular.module('app').factory('MarkersFactory', function() {
  var markers =
  {
    validate: {
      PhoneUA: {
        aField_ID: ['privatePhone', 'workPhone', 'phone', 'tel']
      },
      Mail: {
        aField_ID: ['privateMail', 'email']
      },
      AutoVIN: {
        aField_ID: ['vin_code', 'vin_code1', 'vin']
      },
      TextUA: {
        aField_ID: ['textUa',
          'lastName_UA1','firstName_UA1','middleName_UA1',
          'lastName_UA2','firstName_UA2','middleName_UA2',
          'lastName_UA3','firstName_UA3','middleName_UA3',
          'lastName_UA4','firstName_UA4','middleName_UA4',
          'lastName_UA5','firstName_UA5','middleName_UA5',
          'sFamily_UA','sName_UA','sSurname_UA'
        ]
      },
      TextRU: {
        aField_ID: ['textRu',
          'lastName_RU1','firstName_RU1','middleName_RU1',
          'lastName_RU2','firstName_RU2','middleName_RU2',
          'lastName_RU3','firstName_RU3','middleName_RU3',
          'lastName_RU4','firstName_RU4','middleName_RU4',
          'lastName_RU5','firstName_RU5','middleName_RU5',
          'sFamily_RU','sName_RU','sSurname_RU'
        ]
      },
      DateFormat: {
        aField_ID: ['dateFormat'],
        sFormat: 'YYYY-MM-DD'
      },
      DateElapsed: {
        aField_ID: ['dateOrder'],
        bFuture: true, // якщо true, то дата modelValue має бути у майбутньому
        bLess: true, // якщо true, то 'дельта' між modelValue та зараз має бути 'менше ніж' вказана нижніми параметрами
        nDays: 10,
        nMonths: 0,
        nYears: 0
        //,sDebug: 'Додаткова опція - інформація для дебагу'
        //,bDebug: false; // Опція для дебагу
      }
    },
    attributes: {
      Editable_1: {aField_ID:['sPhone_User1', 'sMail_User1', 'bankIdlastName'], bValue: true},
      Editable_2: {aField_ID:[], bValue: false}
    },
    motion: {

    }
  };

  return {
    getMarkers: function () {
      return markers;
    },
    grepByPrefix: function (section, prefix) {
      return _.transform(_.pairs(markers[section]), function (result, value) {
        if (value[0].indexOf(prefix) === 0) result.push(value[1]);
      });
    }
  }
});
