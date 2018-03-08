var mysql = require('mysql');
var config = require('./config/config');

var connection = mysql.createPool(config);

module.exports = connection;