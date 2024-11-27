var mysql = require('mysql');

var con = mysql.createConnection({
  host: "localhost",
  user: "root",
  password: "mishami1411",
  database: "quaero_db"
});

module.exports = con;
