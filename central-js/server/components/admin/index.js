var NodeCache = require("node-cache" );
//var uuid = require("uuid");
var crypto = require('crypto');

var adminKeysCache = new NodeCache();
var cacheKey = 'admin-keys-map';
// ��� �������
var aAdminInn = [
	'3119325858'
	,'3167410996' //Забрудский
];

var getAdminKeys = function () {
	var result = adminKeysCache.get(cacheKey);
	if (!result) {
		result = {};
		setAdminKeys(result);
	}
	return result;
};

var setAdminKeys = function (value) {
	adminKeysCache.set(cacheKey, value);
};

var generateAdminToken = function (inn) {
	var unhashed = inn + (new Date()).toString();
	var result = crypto.createHash('sha1').update(unhashed).digest('hex'); //uuid.v1();
	var keys = getAdminKeys();
	keys[inn] = result;
	setAdminKeys(keys);
	return result;
};

var isAdminInn = function(inn) {
	return aAdminInn.indexOf(inn) > -1;
};

var Admin = function() {

};

Admin.generateAdminToken = generateAdminToken;
Admin.isAdminInn = isAdminInn;

module.exports = Admin;
