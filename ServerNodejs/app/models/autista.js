var CartaDiCredito = require("./cartaDiCredito");
var db = require("../DB_Connection");

var autista = {
    addAutista: function (autista, callback) {
        return db.query("insert into utenti (`username`, `password`, `email`, `nome`, `cognome`, `dataDiNascita`, `telefono`, `saldo`) values (?,?,?,?,?,?,?,?)", [autista.username, autista.password, autista.email, autista.nome, autista.cognome, autista.dataDiNascita, autista.telefono, 0], callback);
    },
    getAutista: function (username, password, callback) {
        return db.query("select * from ... where username=? and password=?", [username, password], callback);
    }
};

module.exports = autista;