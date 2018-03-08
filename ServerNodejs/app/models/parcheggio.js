var parcheggio = {
    id: Number,
    indirizzo: {
        citta: String,
        provincia: String,
        cap: Number,
        via: String,
        n_civico: String
    },
    coordinate: Coordinates,    // Cercare come va utilizzato
    nPostiMacchina: Number,     //  Cercare tipologie vere di parcheggio
    nPostiAutobus: Number,
    nPostiDisabile: Number,
    nPostiCamper: Number,
    nPostiMoto: Number,
    tariffaOrariaLavorativi: Number,
    tariffaOrariaFestivi: Number,
}

module.exports = parcheggio;