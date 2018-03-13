var db = require("../DB_Connection");

var utente = {
    addAutista: function (autista, callback) {
        return db.query("INSERT INTO utenti (`username`, `password`, `email`, `nome`, `cognome`, `dataDiNascita`, `telefono`, `saldo`) VALUES (?,?,?,?,?,?,?,?);", [autista.username, autista.password, autista.email, autista.nome, autista.cognome, autista.dataDiNascita, autista.telefono, 0], callback);
    },
    getAutistaFromUsername: function (username, password, callback) {
        return db.query("SELECT * FROM autisti_view WHERE username=? and password=?;", [username, password], callback);
    }, getAutistaFromEmail: function (email, password, callback) {
        return db.query("SELECT * FROM autisti_view WHERE email=? and password=?;", [email, password], callback);
    },
    getAdmin: function (username, password, callback) {
        return db.query("SELECT * FROM admin_view WHERE username=? and password=?;", [username, password], callback);
    },
    getAllAutisti: function (callback) {
        return db.query("SELECT * FROM autisti_view ORDER BY idUtente;", callback);
    },
    delAutista: function (id, callback) {
        return db.query("DELETE FROM utenti WHERE idUtente=?;", [id], callback);
    },
    updateAutista: function (autista, callback) {
        return db.query("UPDATE utenti SET `username`=?, `password`=?, `email`=?, `nome`=?, `cognome`=?, `dataDiNascita`=?, `telefono`=?, `saldo`=?  WHERE idUtente=?;", [autista.username, autista.password, autista.email, autista.nome, autista.cognome, autista.dataDiNascita, autista.telefono, autista.saldo, autista.id], callback);
    }
};

module.exports = utente;