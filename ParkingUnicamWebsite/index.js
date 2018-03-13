var http = require('http');
var express = require('express');
var app = express();

app.use(function allowCrossDomain(req, res, next) {
    res.header('Access-Control-Allow-Origin', '172.16.0.212');
    res.header('Access-Control-Allow-Methods', 'GET, PUT, POST, DELETE, PATCH, OPTIONS');
    res.header('Access-Control-Allow-Headers', 'Content-Type, Authorization, Content-Length, X-Requested-With');

    if ('OPTIONS' === req.method)
        res.sendStatus(200);
    else
        next();
});

app.use('/', express.static('sito'));


var httpServer = http.createServer(app);

httpServer.listen(80, "172.16.0.212", function () {
    console.log('server started on 80.');
});