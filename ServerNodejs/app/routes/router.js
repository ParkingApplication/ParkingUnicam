var jwt = require('jsonwebtoken');
var bodyParser = require('body-parser');
var express = require('express');
var crypto = require('crypto');
var path = require('path');
var fs = require('fs');

// Modelli
var Utente = require("../models/utente");
var Parcheggio = require("../models/parcheggio");
var Carta = require("../models/cartaDiCredito");
var VerificaEmail = require("../models/verificaEmail");
var Prenotazione = require("../models/prenotazione");

// Database interno per tenere conto del numero dei posti liberi
var NeDB = require("../NeDB/DB_Connection");
NeDB.loadPostiLiberiFromServer();

// Servizio posta elettronica
var EmailSender = require('../EmailSender');

// Configurazioni varie
var ConfigConnessione = require("../config/configConnessione");
var TipoPosto = require("../config/configTipoPosto");

//  Verifica del token
var verifyToken = function (req, res, next) {
    var token = req.headers['x-access-token'] || req.body.token || req.query.token;

    if (token) {
        jwt.verify(token, ConfigConnessione.secret, function (err, decoded) {
            if (err)
                return res.json({
                    error: {
                        codice: -1,
                        info: "Riscontrati problemi nell' autenticare il token."
                    }
                });
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
            else {
                // Genero il codice di verifica
                var milliseconds = new Date().getMilliseconds();
                var data = milliseconds + result.insertId;
                var codice = crypto.createHash('md5').update(data.toString()).digest('hex');

                // Aggiungo il codice di verifica al database
                VerificaEmail.addEmailCode(result.insertId, codice, function (err) {
                    if (err)
                        res.status(400).json({
                            error: {
                                codice: 59,
                                info: "Riscontrati problemi con il database per la verifica email."
                            }
                        });
                    else {
                        // Setto i dati per l'invio dell' email di verifica
                        var indirizzo = "http://" + ConfigConnessione.ipExternal + ":" + ConfigConnessione.portExternal + "/verify?code=" + codice;
                        var testo = "Ciao " + req.body.autista.nome + ",\ngrazie per esserti iscritto alla nostra applicazione." + "\n\nSegui il link per procedere con la registrazione:\n" + indirizzo;

                        //  Invio l'email di verifica
                        EmailSender.sendEmail(req.body.autista.email, "Conferma registrazione ParkingUnicam", testo,
                            function (err, info) {
                                if (err)
                                    console.log("Email sender error > " + err);
                            });

                        res.json({
                            successful: {
                                codice: 200,
                                info: "Ti stiamo inviando un email per confermare la registrazione."
                            }
                        });
                    }
                });
            }
        });
});


// TEST
apiRoutes.post('/parcheggio/entrataAutomobilista', function (req, res) {
    if (!req.body.QRCODE)
        res.status(400).json({
            error: {
                codice: 7,
                info: "Campi mancanti."
            }
        });
    else {
        console.log(req.body.QRCODE);
        if (req.body.QRCODE === "1111")
            res.json({
                successful: {
                    codice: 200,
                    info: "Sei abilitato ad entrare nel parcheggio."
                }
            });
        else
            res.status(400).json({
                error: {
                    codice: 7,
                    info: "QRcode non esistente."
                }
            });
    }
});

apiRoutes.post('/parcheggio/uscitaAutomobilista', function (req, res) {
    if (!req.body.QRCODE)
        res.status(400).json({
            error: {
                codice: 7,
                info: "Campi mancanti."
            }
        });
    else {
        console.log(req.body.QRCODE);
        if (req.body.QRCODE === "2222")
            res.json({
                successful: {
                    codice: 200,
                    info: "Sei abilitato ad uscire dal parcheggio."
                }
            });
        else
            res.status(400).json({
                error: {
                    codice: 7,
                    info: "QRcode non esistente."
                }
            });
    }
});

// ENDTEST

// Verifica email (acceduta solo da browser, risponde con status 200 e con pagine html)
apiRoutes.get('/verify', function (req, res) {
    if (!req.query.code)
        res.sendFile(path.join(__dirname, './html/errorRegistrazione.html'));
    else
        VerificaEmail.getEmailCode(req.query.code, function (err, rows) {
            if (err)
                res.sendFile(path.join(__dirname, './html/errorRegistrazione.html'));
            else {
                // Abilito l'autista
                if (rows.length == 1) {
                    var idUtente = rows[0].id_utente;
                    Utente.setAbilitazioneAutista(idUtente, 1, function (err) {
                        if (err)
                            res.sendFile(path.join(__dirname, './html/errorRegistrazione.html'));
                        else {
                            res.sendFile(path.join(__dirname, './html/confermaRegistrazione.html'));

                            console.log("Codice email verificato (account abilitato), codice > " + req.query.code);
                            // Elimino il record di verifica dell' utente
                            VerificaEmail.delEmailCode(idUtente, function (err) {
                                if (err)
                                    console.log("Riscontrato problema nell' eliminare EmailCode >\n" + err);
                            });
                        }
                    });
                    return; // Altrimenti node passa all' else successivo anche se non dovrebbe
                }
                else
                    if (rows.length > 1) {
                        res.sendFile(path.join(__dirname, './html/errorRegistrazione.html'));
                        console.log("Riscontrato problema nel database, trovato codice verifica duplicato.\n");
                    }
                    else
                        res.sendFile(path.join(__dirname, './html/alreadyRegistrazione.html'));
            }
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
                            token: jwt.sign(user, ConfigConnessione.secret),
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
                                        token: jwt.sign(user, ConfigConnessione.secret),
                                        autista: user
                                    });
                                }
                                else {
                                    res.status(400).json({
                                        error: {
                                            codice: 7,
                                            info: "Dati di login errati o account non ancora abilitato."
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
                            token: jwt.sign(user, ConfigConnessione.secret),
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

apiRoutes.post('/resetPassword', function (req, res) {
    if (!req.body.email)
        res.status(400).json({
            error: {
                codice: 17,
                info: "Campi mancanti."
            }
        });
    else // Ritorna i dati dell' autista dall' email solo se esso è abilitato
        Utente.getAutistaFromOnlyEmail(req.body.email, function (err, rows) {
            if (err)
                res.status(400).json({
                    error: {
                        codice: 55,
                        info: "Riscontrati problemi con il database."
                    }
                });
            else
                if (rows.length == 1) {
                    // Creo la nuova password
                    var milliseconds = new Date().getMilliseconds();
                    var newPassword = milliseconds + rows[0].idUtente;
                    // Eseguo l'hash della password per salvarla nel db
                    var hashedPassword = crypto.createHash('sha1').update(newPassword.toString()).digest('hex');

                    var user = {
                        id: rows[0].idUtente,
                        username: rows[0].username,
                        email: rows[0].email,
                        password: hashedPassword,
                        nome: rows[0].nome,
                        cognome: rows[0].cognome,
                        dataDiNascita: rows[0].dataDiNascita,
                        telefono: rows[0].telefono,
                        saldo: rows[0].saldo,
                        abilitato: 1, // Se si arriva qui l'utente è obbligatoriamente abilitato
                        carta_di_credito: {
                            numero_carta: rows[0].numeroCarta,
                            pin: rows[0].pinDiVerifica,
                            dataDiScadenza: rows[0].dataDiScadenza
                        }
                    };

                    Utente.updateAutista(user, function (err) {
                        if (err) {
                            console.log(err);
                            res.status(400).json({
                                error: {
                                    codice: 61,
                                    info: "Riscontrati problemi con il database nel resettare la password."
                                }
                            });
                        }
                        else {
                            EmailSender.sendEmail(user.email, "ParkingApp reset password",
                                "La tua nuova password è: " + newPassword + ".");

                            res.json({
                                successful: {
                                    codice: 210,
                                    info: "Ti è stata inviata un email con la nuova password."
                                }
                            });
                        }
                    });
                    return;
                }
                else
                    res.status(400).json({
                        error: {
                            codice: 95,
                            info: "Nessun account è registrato con questa email."
                        }
                    })
        });
});

//  #### da qui in poi è necessaria l'autenticazione via token ####
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
                                abilitato: rows[i].abilitato,
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
        if (!req.user.livelloAmministrazione) { // L'autista ha richiesto la modifica dei propri dati
            // Correggo i dati dell' autista (es: aggiungo i campi mancanti con i dati che già conosco)
            Utente.CorreggiAutista(req.body.autista, req.user, function (resu) {
                req.body.autista = resu;
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
            });
        }
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

// Uno o più parcheggi per città ? (probabilmente più di uno)
apiRoutes.post('/getParcheggiPerCitta', function (req, res) {
    if (!req.body.citta)
        res.status(400).json({
            error: {
                codice: 7,
                info: "Devi specificare in quale città."
            }
        });
    else
        NeDB.getAllPostiLiberi(function (err, doc) {
            var posti = doc;

            if (err)
                res.status(400).json({
                    error: {
                        codice: 7,
                        info: "Riscontrati problemi con il database interno."
                    }
                });
            else
                Parcheggio.getParcheggioFromCitta(req.body.citta, function (err, rows) {
                    if (err)
                        res.status(400).json({
                            error: {
                                codice: 7,
                                info: "Riscontrati problemi con il database."
                            }
                        });
                    else
                        if (rows.length > 0) {
                            var parcheggi = [];
                            var l = 0;

                            for (var i = 0; i < rows.length; i++) {
                                var parcheggio = {
                                    id: rows[i].idParcheggio,
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
                                    tariffaOrariaLavorativi: rows[i].tariffaOrariaLavorativi,
                                    tariffaOrariaFestivi: rows[i].tariffaOrariaFestivi
                                };

                                if (posti.length > 0)
                                    for (var k = 0; k < posti.length; k++) {
                                        if (posti[k].id_parcheggio == parcheggio.id)
                                            switch (posti[k].id_tipo) {
                                                case TipoPosto.auto:
                                                    parcheggio.postiAuto = posti[k].postiLiberi;
                                                    break;
                                                case TipoPosto.autobus:
                                                    parcheggio.postiAutobus = posti[k].postiLiberi;
                                                    break;
                                                case TipoPosto.camper:
                                                    parcheggio.postiCamper = posti[k].postiLiberi;
                                                    break;
                                                case TipoPosto.moto:
                                                    parcheggio.postiMoto = posti[k].postiLiberi;
                                                    break;
                                                case TipoPosto.disabile:
                                                    parcheggio.postiDisabile = posti[k].postiLiberi;
                                                    break;
                                            }
                                    }

                                parcheggi[l] = parcheggio;
                                l++;
                            }

                            res.json({
                                parcheggi: parcheggi
                            });
                        }
                        else
                            res.status(400).json({
                                error: {
                                    codice: 7,
                                    info: "Non sono presenti parcheggi in questa città."
                                }
                            });
                });
        });
});

apiRoutes.post('/effettuaPrenotazione', function (req, res) {
    if (!req.body.idParcheggio || !req.body.tipoParcheggio)
        res.status(400).json({
            error: {
                codice: 77,
                info: "Dati mancanti per procedere con la richiesta."
            }
        });
    else
        NeDB.getAllPostiLiberi(function (err, doc) {
            var posti = doc;

            for (var k = 0; k < posti.length; k++) {
                if (posti[k].id_parcheggio == req.body.idParcheggio && posti[k].id_tipo == req.body.tipoParcheggio)
                    if (posti[k].postiLiberi > 0) {
                        // Genero il codice da cui creare il QRCode
                        var datetime = new Date();
                        var data = datetime.getMilliseconds() + result.insertId;
                        var codice = crypto.createHash('md5').update(data.toString()).digest('hex');
                        var scadenza = datetime.getFullYear() + "-" + datetime.getMonth() + "-" + datetime.getDate() +
                            " " + datetime.getHours() + "-" + (datetime.getMinutes() + 20) + "-00"; // Secondi settati a zero per comodità

                        Prenotazione.addPrenotazione(req.user.id, req.body.idParcheggio, req.body.tipoParcheggio, scadenza, codice,
                            function (err) {
                                if (err)
                                    res.status(400).json({
                                        error: {
                                            codice: 98,
                                            info: "Riscontrati errori con il database."
                                        }
                                    });
                                else { // Rispondo col il codice del qrcode
                                    res.json({
                                        QR_Code: codice
                                    });

                                    // Aggiorno il numero di posti liberi
                                    var nuovo = posti[k];
                                    nuovo.postiLiberi--;

                                    NeDB.updatePostiLiberi(posti[k], nuovo, function (err) {
                                        if (err)
                                            console.log("Errore nell' aggiornare il numero di posti liberi.");
                                    });
                                }
                            });
                    }
                    else
                        res.status(400).json({
                            error: {
                                codice: 88,
                                info: "Non ci sono posti liberi per questo tipo di veicolo in questo parcheggio."
                            }
                        });
                else
                    res.status(400).json({
                        error: {
                            codice: 87,
                            info: "Parcheggio o tipo di parcheggio non esistente."
                        }
                    });
            }
        });
});

apiRoutes.delete('/deletePrenotazione', function (req, res) {
    //controllo che l'utente abbia mandato l'id della prenotazione da cancellare
    if (!req.body.idPrenotazione)
        res.status(400).json({
            error: {
                codice: 7,
                info: "Campi mancanti."
            }
        });
    else
    //se l'utente ha mandato l'id della prenotazione da cancellare, procedo con la cancellazione e attendo la risposta
                Prenotazione.delPrenotazione(req.body.idPrenotazione, function (err) {
                    //se l'operazione non è andata a buon fine ritorno un errore
                    if (err)
                        res.status(400).json({
                            error: {
                                codice: 53,
                                info: "Riscontrati problemi con il database."
                            }
                        });
                    else
                    //se l'operazione è andata a buon fine mando un succesful
                        res.json({
                            successful: {
                                codice: 210,
                                info: "La prenotazione è stata cancellata correttamente."
                            }
                        });
                });
});



apiRoutes.post('/resetQRCode', function (req, res) {
    if (!req.body.idPrenotazione)
        res.status(400).json({
            error: {
                codice: 17,
                info: "Campi mancanti per l'aggiornamento del QRCODE della prenotazione."
            }
        });
    
    
    //controllo per vedere che chi ha chiamato la funzione sia un amministratore di sistema
    //su visual paradigm c'era scritto che la poteva esclusivamente fare l'amministratore
    /*
    if(req.body.Utente.abilitato != 1)
        res.status(400).json({
            error: {
                codice: 21,
                info: "Non si possiede i diritti necessari per effettuare questa azione"
            }
        });
    */
    
         // Genero il codice da cui creare il QRCode
        var datetime = new Date();
        var data = datetime.getMilliseconds() + result.insertId;
        var codice = crypto.createHash('md5').update(data.toString()).digest('hex');
    
        Prenotazione.setNewQRCODE(req.body.idPrenotazione, codice, function (err) {
        //se l'operazione non è andata a buon fine ritorno un errore
            if (err)
                res.status(400).json({
                    error: {
                        codice: 53,
                        info: "Riscontrati problemi con il database."
                    }
                });
            else
        //se l'operazione è andata a buon fine mando un succesful
                res.json({
                    successful: {
                        QRCODE : codice,
                        info: "Il QRCODE è stato rigenerato correttamente."
                    }
                });
        });
    
});

module.exports = apiRoutes;