var jwt = require('jsonwebtoken');
var bodyParser = require('body-parser');
var express = require('express');

// Non so se il multiparty sia necessario qui <-------------------------------------
var multiparty = require('connect-multiparty');
var multipartyMiddleware = multiparty();

// Modelli
var Utente = require("../models/utente");
var Parcheggio = require("../models/parcheggio");
var Carta = require("../models/cartaDiCredito");

// Secret for jsonwebtoken
var secret = "hfq3g7f03Q,£$Fwetf.g3g@#ù_àergeg";

//  Correzione per update
var CorreggiAutista = function (nuovo, vecchio) {
    var result = {
        id: vecchio.id, //   l' id di nuovo e vecchio hanno comunque sempre lo stesso valore
        username: vecchio.username || nuovo.username,
        password: vecchio.password || nuovo.password,
        email: vecchio.email || nuovo.email,
        nome: vecchio.nome || nuovo.nome,
        cognome: vecchio.cognome || nuovo.cognome,
        dataDiNascita: vecchio.dataDiNascita || nuovo.dataDiNascita,
        telefono: vecchio.telefono || nuovo.telefono,
        saldo: vecchio.saldo || nuovo.saldo,
        carta_di_credito: {
            numero_carta: vecchio.username.carta_di_credito.numero_carta || nuovo.username.carta_di_credito.numero_carta,
            pin: vecchio.username.carta_di_credito.pin || nuovo.username.carta_di_credito.pin,
            dataDiScadenza: vecchio.username.carta_di_credito.dataDiScadenza || nuovo.username.carta_di_credito.dataDiScadenza
        }
    }

    return result;
};

//  Da sistemare secondo le specifiche del progetto corrente
var verifyToken = function (req, res, next) {
    var token = req.headers['x-access-token'] || req.body.token || req.query.token;

    if (token) {
        jwt.verify(token, secret, function (err, decoded) {
            if (err)
                return res.json({ success: false, message: 'Failed to authenticate token.' });
            else {
                req.user = decoded;
                next();
            }
        });
    } else {
        return res.status(400).json({
            error: {
                codice: 15,
                info: "Devi essere loggato per eseguire quest' azione."
            }
        });
    }
};

// API ROUTES -------------------
var apiRoutes = express.Router();

// use body parser so we can get info from POST and/or URL parameters
apiRoutes.use(bodyParser.urlencoded({ extended: false }));
apiRoutes.use(bodyParser.json());

apiRoutes.post('/signup', function (req, res) {
    if (!req.body.autista)
        res.status(400).json({
            error: {
                codice: 7,
                info: "Campi mancanti."
            }
        });
    else
        Utente.addAutista(req.body.autista, function (err, result) {
            if (err)
                res.status(400).json({
                    error: {
                        codice: 50,
                        info: "Nome utente o email già in uso."
                    }
                });
            else
                res.json({
                    successful: {
                        codice: 200,
                        info: "L'utente è stato registrato correttamente."
                    }
                });
        });
});

apiRoutes.post('/login', function (req, res) {
    console.log("Login request from: " + req.ip);
    if (!req.body.username || !req.body.password)
        res.status(400).json({
            error: {
                codice: 7,
                info: "Campi mancanti."
            }
        });
    else
        if (!req.body.admin)
            Utente.getAutistaFromUsername(req.body.username, req.body.password, function (err, rows) {
                if (err)
                    res.status(400).json({
                        error: {
                            codice: 50,
                            info: "Riscontrati problemi con il database."
                        }
                    });
                else
                    if (rows.length == 1) {
                        var user = {
                            id: rows[0].idUtente,
                            username: rows[0].username,
                            email: rows[0].email,
                            password: rows[0].password,
                            nome: rows[0].nome,
                            cognome: rows[0].cognome,
                            dataDiNascita: rows[0].dataDiNascita,
                            telefono: rows[0].telefono,
                            saldo: rows[0].saldo,
                            carta_di_credito: {
                                numero_carta: rows[0].numeroCarta,
                                pin: rows[0].pinDiVerifica,
                                dataDiScadenza: rows[0].dataDiScadenza
                            }
                        };
                        res.json({
                            token: jwt.sign(user, secret),
                            autista: user
                        });
                    }
                    else
                        Utente.getAutistaFromEmail(req.body.username, req.body.password, function (err, rows) {
                            if (err)
                                res.status(400).json({
                                    error: {
                                        codice: 50,
                                        info: "Riscontrati problemi con il database."
                                    }
                                });
                            else
                                if (rows.length == 1) {
                                    var user = {
                                        id: rows[0].idUtente,
                                        username: rows[0].username,
                                        email: rows[0].email,
                                        password: rows[0].password,
                                        nome: rows[0].nome,
                                        cognome: rows[0].cognome,
                                        dataDiNascita: rows[0].dataDiNascita,
                                        telefono: rows[0].telefono,
                                        saldo: rows[0].saldo,
                                        carta_di_credito: {
                                            numero_carta: rows[0].numeroCarta,
                                            pin: rows[0].pinDiVerifica,
                                            dataDiScadenza: rows[0].dataDiScadenza
                                        }
                                    };
                                    res.json({
                                        token: jwt.sign(user, secret),
                                        autista: user
                                    });
                                }
                                else {
                                    res.status(400).json({
                                        error: {
                                            codice: 7,
                                            info: "Dati di login errati."
                                        }
                                    });
                                }
                        });
            });
        else
            Utente.getAdmin(req.body.username, req.body.password, function (err, rows) {
                if (err)
                    res.status(400).json({
                        error: {
                            codice: 53,
                            info: "Riscontrati problemi con il database."
                        }
                    });
                else
                    if (rows.length == 1) {
                        var user = {
                            id: rows[0].idUtente,
                            username: rows[0].username,
                            email: rows[0].email,
                            password: rows[0].password,
                            nome: rows[0].nome,
                            cognome: rows[0].cognome,
                            livelloAmministrazione: rows[0].livelloAmministrazione
                        };
                        res.json({
                            token: jwt.sign(user, secret),
                            admin: user
                        });
                    }
                    else {
                        res.status(400).json({
                            error: {
                                codice: 7,
                                info: "Dati di login errati."
                            }
                        });
                    }
            });
});

apiRoutes.post('/getAllParcheggi', function (req, res) {
    Parcheggio.getAllParcheggi(function (err, rows) {
        if (err)
            res.status(400).json({
                error: {
                    codice: 48,
                    info: "Riscontrati problemi con il database."
                }
            });
        else
            if (rows.length > 0) {
                var parcheggi = [];
                var parcheggio;

                for (var i = 0; i < rows.length; i++) {
                    parcheggio = {
                        indirizzo: {
                            citta: rows[i].citta,
                            provincia: rows[i].provincia,
                            cap: rows[i].cap,
                            via: rows[i].via,
                            n_civico: rows[i].n_civico
                        },
                        coordinate: {
                            x: rows[i].coordinataX,
                            y: rows[i].coordinataY
                        },
                        nPostiMacchina: rows[i].nPostiMacchina,
                        nPostiAutobus: rows[i].nPostiAutobus,
                        nPostiDisabile: rows[i].nPostiDisabile,
                        nPostiCamper: rows[i].nPostiCamper,
                        nPostiMoto: rows[i].nPostiMoto,
                        tariffaOrariaLavorativi: rows[i].tariffaOrariaLavorativi,
                        tariffaOrariaFestivi: rows[i].tariffaOrariaFestivi
                    };
                    parcheggi[i] = parcheggio;
                }

                res.json({
                    parcheggi: parcheggi
                });
            }
            else
                res.status(400).json({
                    error: {
                        codice: 46,
                        info: "Non sono presenti percheggi nel database."
                    }
                });
    });
});

apiRoutes.use(verifyToken);

apiRoutes.post('/getAllAutisti', function (req, res) {
    if (!req.user.livelloAmministrazione)
        res.status(400).json({
            error: {
                codice: 500,
                info: "Devi avere i diritti di amministratore."
            }
        });
    else
        if (req.user.livelloAmministrazione > 0)
            Utente.getAllAutisti(function (err, rows) {
                if (err)
                    res.status(400).json({
                        error: {
                            codice: 78,
                            info: "Riscontrati problemi con il database."
                        }
                    });
                else
                    if (rows.length > 0) {
                        var autisti = [];
                        var autista;

                        for (var i = 0; i < rows.length; i++) {
                            var dateN, dateS;

                            if (!rows[i].dataDiNascita)
                                dateN = "";
                            else {
                                var app = new Date(rows[i].dataDiNascita);
                                dateN = app.getFullYear() + "-" + (app.getMonth() + 1) + "-" + app.getDate();
                            }

                            if (!rows[i].dataDiScadenza)
                                dateS = "";
                            else {
                                var app = new Date(rows[i].dataDiScadenza);
                                dateS = app.getFullYear() + "-" + (app.getMonth() + 1) + "-" + app.getDate();
                            }

                            autista = {
                                id: rows[i].idUtente,
                                username: rows[i].username,
                                email: rows[i].email,
                                password: rows[i].password,
                                nome: rows[i].nome,
                                cognome: rows[i].cognome,
                                dataDiNascita: dateN,
                                telefono: rows[i].telefono || "",
                                saldo: rows[i].saldo || 0,
                                carta_di_credito: {
                                    numero_carta: rows[i].numeroCarta || "",
                                    pin: rows[i].pinDiVerifica || "",
                                    dataDiScadenza: dateS
                                }
                            };
                            autisti[i] = autista;
                        }

                        res.json({
                            autisti: autisti
                        });
                    }
                    else
                        res.status(400).json({
                            error: {
                                codice: 46,
                                info: "Non sono presenti percheggi nel database."
                            }
                        });
            });
        else
            res.status(400).json({
                error: {
                    codice: 500,
                    info: "Devi avere dei privilegi più alti."
                }
            });
});

apiRoutes.delete('/deleteAutista', function (req, res) {
    if (!req.body.id)
        res.status(400).json({
            error: {
                codice: 7,
                info: "Campi mancanti."
            }
        });
    else
        if (!req.user.livelloAmministrazione)
            res.status(400).json({
                error: {
                    codice: 500,
                    info: "Devi avere i diritti di amministratore."
                }
            });
        else
            if (req.user.livelloAmministrazione > 0) {
                Utente.delAutista(req.body.id, function (err) {
                    if (err)
                        res.status(400).json({
                            error: {
                                codice: 53,
                                info: "Riscontrati problemi con il database."
                            }
                        });
                    else
                        res.json({
                            successful: {
                                codice: 210,
                                info: "L'utente è stato eliminato correttamente."
                            }
                        });
                });
            }
            else
                res.status(400).json({
                    error: {
                        codice: 500,
                        info: "Devi avere dei privilegi più alti."
                    }
                });
});

apiRoutes.patch('/cambiaCredenziali', function (req, res) {
    if (!req.body.autista)
        res.status(400).json({
            error: {
                codice: 7,
                info: "Campi mancanti."
            }
        });
    else
        if (!req.user.livelloAmministrazione) // L'autista ha richiesto la modifica dei propri dati
            if (req.user.id == req.body.autista.id) {
                // Correggo i dati dell' autista (es: aggiungo i campi mancanti con i dati che già conosco)
                req.body.autista = CorreggiAutista(req.body.autista, req.user);

                Utente.updateAutista(req.body.autista, function (err) {
                    if (err)
                        res.status(400).json({
                            error: {
                                codice: 51,
                                info: "Riscontrati problemi con il database."
                            }
                        });
                    else
                        if (!req.body.autista.carta_di_credito)
                            res.json({
                                successful: {
                                    codice: 210,
                                    info: "I dati utente sono stati aggiornati correttamente."
                                }
                            });
                        else
                            if (!req.body.autista.carta_di_credito.pin || !req.body.autista.carta_di_credito.numero_carta || !req.body.autista.carta_di_credito.dataDiScadenza)
                                res.json({
                                    successful: {
                                        codice: 210,
                                        info: "I dati utente sono stati aggiornati correttamente."
                                    }
                                });
                            else
                                Carta.getCartaFromIdUtente(req.body.autista.id, function (err, rows) {
                                    if (err)
                                        res.status(400).json({
                                            error: {
                                                codice: 51,
                                                info: "Riscontrati problemi con il database, dati carta non aggiornati."
                                            }
                                        });
                                    else
                                        if (rows.length == 0)
                                            Carta.addCarta(req.body.autista, function (err) {
                                                if (err)
                                                    res.status(400).json({
                                                        error: {
                                                            codice: 51,
                                                            info: "Riscontrati problemi con il database, dati carta non aggiornati."
                                                        }
                                                    });
                                                else
                                                    res.json({
                                                        successful: {
                                                            codice: 210,
                                                            info: "I dati utente e della carta sono stati aggiornati correttamente."
                                                        }
                                                    });
                                            });
                                        else
                                            Carta.updateCarta(req.body.autista, function (err) {
                                                if (err)
                                                    res.status(400).json({
                                                        error: {
                                                            codice: 51,
                                                            info: "Riscontrati problemi con il database, dati carta non aggiornati."
                                                        }
                                                    });
                                                else
                                                    res.json({
                                                        successful: {
                                                            codice: 210,
                                                            info: "I dati utente e della carta sono stati aggiornati correttamente."
                                                        }
                                                    });
                                            });
                                });
                });
            }
            else
                res.status(400).json({
                    error: {
                        codice: 500,
                        info: "Impossibile modificare le credenziali di un altro autista."
                    }
                });
        else
            if (req.user.livelloAmministrazione > 0) // L'amministratore ha richiesto la modifica dei dati di un utente
                Utente.updateAutista(req.body.autista, function (err) {
                    if (err)
                        res.status(400).json({
                            error: {
                                codice: 51,
                                info: "Riscontrati problemi con il database."
                            }
                        });
                    else
                        if (!req.body.autista.carta_di_credito)
                            res.json({
                                successful: {
                                    codice: 210,
                                    info: "I dati utente sono stati aggiornati correttamente."
                                }
                            });
                        else
                            if (!req.body.autista.carta_di_credito.pin || !req.body.autista.carta_di_credito.numero_carta || !req.body.autista.carta_di_credito.dataDiScadenza)
                                res.json({
                                    successful: {
                                        codice: 210,
                                        info: "I dati utente sono stati aggiornati correttamente."
                                    }
                                });
                            else
                                Carta.getCartaFromIdUtente(req.body.autista.id, function (err, rows) {
                                    if (err)
                                        res.status(400).json({
                                            error: {
                                                codice: 51,
                                                info: "Riscontrati problemi con il database, dati carta non aggiornati."
                                            }
                                        });
                                    else
                                        if (rows.length == 0)
                                            Carta.addCarta(req.body.autista, function (err) {
                                                if (err)
                                                    res.status(400).json({
                                                        error: {
                                                            codice: 51,
                                                            info: "Riscontrati problemi con il database, dati carta non aggiornati."
                                                        }
                                                    });
                                                else
                                                    res.json({
                                                        successful: {
                                                            codice: 210,
                                                            info: "I dati utente e della carta sono stati aggiornati correttamente."
                                                        }
                                                    });
                                            });
                                        else
                                            Carta.updateCarta(req.body.autista, function (err) {
                                                if (err)
                                                    res.status(400).json({
                                                        error: {
                                                            codice: 51,
                                                            info: "Riscontrati problemi con il database, dati carta non aggiornati."
                                                        }
                                                    });
                                                else
                                                    res.json({
                                                        successful: {
                                                            codice: 210,
                                                            info: "I dati utente e della carta sono stati aggiornati correttamente."
                                                        }
                                                    });
                                            });
                                });
                });
            else
                res.status(400).json({
                    error: {
                        codice: 500,
                        info: "Privilegi amministratore insufficienti."
                    }
                });
});

module.exports = apiRoutes;