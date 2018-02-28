var http = require('http');
var express = require('express');
var app = express();

//  Togliere da app e spostare solo al sito statico (per registrazione e login admin)
app.use(function allowCrossDomain(req, res, next) {
    res.header('Access-Control-Allow-Origin', 'localhost');
    res.header('Access-Control-Allow-Methods', 'GET, PUT, POST, DELETE, PATCH, OPTIONS');
    res.header('Access-Control-Allow-Headers', 'Content-Type, Authorization, Content-Length, X-Requested-With');

    if ('OPTIONS' === req.method)
        res.sendStatus(200);
    else
        next();
});

// Esempio di caricamento sito statico (HTML)
//app.use('/', express.static('site'));

var httpServer = http.createServer(app);

httpServer.listen(80, "localhost", function () {
    console.log('server started on 80.');
});