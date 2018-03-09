var http = require('http');
var express = require('express');
var router = require('./app/api/router');
var app = express();

//  Togliere da app e spostare solo al sito statico (per registrazione e login admin)
app.use(function allowCrossDomain(req, res, next) {
    res.header('Access-Control-Allow-Origin', '2.226.207.189'); // Mettere poi indirizzo ip esterno
    res.header('Access-Control-Allow-Methods', 'GET, PUT, POST, DELETE, PATCH, OPTIONS');
    res.header('Access-Control-Allow-Headers', 'Content-Type, Authorization, Content-Length, X-Requested-With');

    if ('OPTIONS' === req.method)
        res.sendStatus(200);
    else
        next();
});

// Uso le api express router (COMMENTO DA SISTEMARE)
app.use('/', router);

// Esempio di caricamento sito statico (HTML)
app.use('/site', express.static('site'));

var httpServer = http.createServer(app);


httpServer.listen(5044, "172.16.0.212", function () {
    console.log('server started on 5044.');
});