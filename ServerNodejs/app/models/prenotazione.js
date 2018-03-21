var db = require("../mysql/DB_Connection");

var prenotazione = {
    addPrenotazione: function (idUtente, idParcheggio, idPosto, dataora, codice, callback) {
        return db.query("INSERT INTO prenotazioni_in_atto (`id_utente`, `id_parcheggio`, `id_tipo_posto`, `data_scadenza`, `codice`) VALUES (?,?,?,?,?,?);", [idUtente, idParcheggio, idPosto, dataora, codice], callback);
    },
    getPrenotazioniFromUtente: function (idUtente, callback) {
        return db.query("SELECT * FROM prenotazioni_in_atto WHERE id_utente=? ORDER BY data_scadenza ASC;", [idUtente], callback);
    },
    updateQRCODE: function (idPrenotazione, QRCODE, callback) {
        return db.query("UPDATE prenotazioni_in_atto SET codice=? WHERE id_prenotazione=?;", [QRCODE, idPrenotazione], callback);
    },
    getPrenotazioneUtente: function (idUtente, idPrenotazione, callback) {
        return db.query("SELECT * FROM prenotazioni_in_atto WHERE id_utente=? AND id_prenotazione=?;", [idUtente, idPrenotazione], callback);
    },
    delPrenotazione: function (idPrentoazione, callback) {
        return db.query("DELETE FROM prenotazioni_in_atto WHERE id_prenotazione=?; ", [idPrentoazione], callback);
    }
};

module.exports = prenotazione;