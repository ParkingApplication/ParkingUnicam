var mysql = require('mysql');
var config = require('./config/configDB');

var connection = mysql.createPool(config);

module.exports = connection;