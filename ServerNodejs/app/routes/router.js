var jwt = require('jsonwebtoken');  // yes
var bodyParser = require('body-parser');  // yes
var express = require('express');  // yes

var multiparty = require('connect-multiparty');  // boh
var multipartyMiddleware = multiparty();  // boh

// Modelli
var Autista = require("../models/autista");

//  Da sistemare secondo le specifiche del progetto corrente
var verifyToken = function (req, res, next) {
    var token = req.headers['x-access-token'] || req.body.token || req.query.token;

    if (token) {
        jwt.verify(token, secret, function (err, decoded) {
            if (err) {
                return res.json({ success: false, message: 'Failed to authenticate token.' });
            } else {
                database.getUserName(decoded, function (ris, message, result) {
                    if (ris) {
                        req.decoded = result.nome;
                        next();
                    }
                    else
                        res.json({
                            success: false,
                            message: 'Riscontrati problemi con il database.'
                        });
                });
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

apiRoutes.post('/singup', function (req, res) {
    if (!req.body.autista)
        res.status(400).json({
            error: {
                codice: 7,
                info: "Campi mancanti."
            }
        });
    else
        Autista.addAutista(req.body.autista, function (err, result) {
            if (err)
                res.status(400).json({
                    error: {
                        codice: 50,
                        info: "Riscontrati problemi con il database."
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
        Autista.getAutista(req.body.username, req.body.password, function (err, rows) {
            if (err)
                res.status(400).json({
                    error: {
                        codice: 50,
                        info: err
                    }
                });
            else
                if (rows.length > 0)
                    res.json({
                        token: "Non te lo do il token, gne gne.",   // Costruire dopo
                        autista: {
                            id: rows[0].idUtente,
                            username: rows[0].username,
                            email: rows[0].email,
                            password: rows[0].password,
                            nome: rows[0].nome,
                            cognome: rows[0].cognome,
                            data_nascita: rows[0].dataDiNascita,
                            telefono: rows[0].telefono,
                            saldo: rows[0].saldo,
                            carta_di_credito: {
                                numero_carta: rows[0].numeroCarta,
                                pin: rows[0].pinDiVerifica,
                                data_di_scadenza: rows[0].dataDiScadenza
                            }
                        }
                    });
                else
                {
                    res.status(400).json({
                        error: {
                            codice: 7,
                            info: "Dati di login errati."
                        }
                    });
                }
        });
});

module.exports = apiRoutes;