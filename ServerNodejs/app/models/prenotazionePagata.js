var db = require("../mysql/DB_Connection");

var prenotazioniPagate = {
    addPrenotazionePagata: function (idPrenotazioneInAtto, idUtente, idParcheggio, dataora, orePermanenza, tipoPosto, callback) {
        return db.query("INSERT INTO prenotazioni_pagate (idPrenotazione, idUtente, idParcheggio, dataPrenotazione, oraPermanenza, tipoPosto) VALUES (?,?,?,?,?,?);", [idPrenotazione, idUtente, idParcheggio, dataora, orePermanenza, tipoPosto], callback);
    },
    getPrenotazioniFromUtente: function (idUtente, callback) {
        return db.query("SELECT * FROM prenotazioni_pagate WHERE idUtente=?;", [idUtente], callback);

    }
};

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
module.exports = prenotazioniPagate;