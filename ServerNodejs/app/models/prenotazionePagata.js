var db = require("../mysql/DB_Connection");

var prenotazioniPagate = {
    addPrenotazionePagata: function (idPrenotazioneInAtto, idUtente, idParcheggio, dataora, minutiPermanenza, tipoPosto, callback) {
        return db.query("INSERT INTO prenotazioni_pagate (idPrenotazione, idUtente, idParcheggio, dataPrenotazione, minutiPermanenza, tipoPosto) VALUES (?,?,?,?,?,?);", [idPrenotazione, idUtente, idParcheggio, dataora, minutiPermanenza, tipoPosto], callback);
    },
    getPrenotazioniFromUtente: function (idUtente, callback) {
        return db.query("SELECT * FROM prenotazioni_pagate WHERE idUtente=? AND minutiPermanenza>=0 ;", [idUtente], callback);
    },
    getPrenotazioniDaFinireFromUtente: function (idUtente, callback) {
        return db.query("SELECT * FROM prenotazioni_pagate WHERE idUtente=? AND minutiPermanenza<0 ;", [idUtente], callback);
    },
    getPrenotazioniFromParcheggio: function (idParcheggio, callback) {
        return db.query("SELECT * FROM prenotazioni_pagate WHERE idParcheggio=? AND minutiPermanenza>=0 ORDER BY dataPrenotazione DESC LIMIT 10;", [idParcheggio], callback);
    },
    pagaPrenotazineDaFinire: function (idPrenotazione, minutiPermanenza, callback) {
        return db.query("UPDATE prenotazioni_pagate SET minutiPermanenza=?, codice='' WHERE idPrenotazione=?;", [minutiPermanenza, idPrenotazione], callback);
    }
};

module.exports = prenotazioniPagate;

/*
var prenotazioniPagate = {
    idPrenotazione: Number,
    idUtente: Number,
    idParcheggio: Number,
    data: Date,
    orePermanenza: Number,
    tipoParcheggio: TipoParcheggio
};
*/