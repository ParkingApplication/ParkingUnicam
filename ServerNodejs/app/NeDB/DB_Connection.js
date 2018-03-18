var Datastore = require('nedb')
    , db = new Datastore({ filename: './app/NeDB/data/database.db'});

var funzionalita = {
    loadPostiLiberiFromServer: function () {
        var Parcheggio = require("../models/parcheggio");
        db.loadDatabase(); // Elimino i dati precedenti
        db.remove({});
        // Carico il numero di tutti i posti liberi di ogni parcheggio in memoria da mysql
        Parcheggio.getAllPostiLiberi(function (err, rows) {
            if (err) {
                console.log(err);
                postiLiberi = null;
            }
            else
                for (var i = 0; i < rows.length; i++) {
                    var posto = {
                        id_parcheggio: rows[i].id_parcheggio,
                        id_tipo: rows[i].id_tipo,
                        postiLiberi: rows[i].postiLiberi
                    };
                    db.insert(posto);
                }
        });
    },
    getPostiLiberiFromParcheggio: function (idParcheggio, callback) {
        db.loadDatabase();
        return db.find({ id_parcheggio: idParcheggio });
        
    },
    getAllPostiLiberi: function (callback) {
        db.loadDatabase();
        return db.find({}, callback);
    }
};

module.exports = funzionalita;