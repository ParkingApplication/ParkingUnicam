var db = require("../mysql/DB_Connection");

var parcheggio = {
    getAllParcheggi: function (callback) {
        return db.query("SELECT * FROM parcheggi;", callback);
    },
    getNumeroPostiTotaliParcheggio: function (idParcheggio, callback) { // Inutilizzata
        return db.query("SELECT id_parcheggio, nome, numero_posti FROM posti_parcheggio, tipo_parcheggio WHERE tipo_parcheggio.idTipo_parcheggio = posti_parcheggio.id_tipo WHERE id_parcheggio=? ORDER BY id_parcheggio, id_tipo;", [idParcheggio], callback);
    },
    getAllPostiLiberi: function (callback) {
        return db.query("SELECT * FROM posti_parcheggio_liberi_view;", callback);
    },
    getParcheggioFromID: function (id, callback) {
        return db.query("SELECT * FROM parcheggi WHERE idParcheggio=?;", [id], callback);
    }
};

module.exports = parcheggio;

/*var parcheggio = {
    id: Number,
    indirizzo: {
        citta: String,
        provincia: String,
        cap: Number,
        via: String,
        n_civico: String
    },
    coordinate: {
        x: String,
        y: String
    },
    nPostiMacchina: Number,
    nPostiAutobus: Number,
    nPostiDisabile: Number,
    nPostiCamper: Number,
    nPostiMoto: Number,
    tariffaOrariaLavorativi: Number,
    tariffaOrariaFestivi: Number,
}*/