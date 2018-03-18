var TipoParcheggio = require(tipoParcheggio);

var prenotazioniPagate = {
    id: Number,
    idUtente: Number,
    idParcheggio: Number,
    data: Date,
    orePermanenza: Number,
    tipoParcheggio: TipoParcheggio,
};

module.exports = prenotazioniPagate;