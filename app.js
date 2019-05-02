// Módulos
var express = require('express');
var app = express();

var mongo = require('mongodb');
var swig = require('swig');
var crypto = require('crypto');
var expressSession = require('express-session');

app.use(expressSession({
    secret: 'abcdefg',
    resave: true,
    saveUninitialized: true
}));

app.set('db','mongodb://admin:sdi@trabajosdi-uo237464-shard-00-00-bt3rt.mongodb.net:27017,trabajosdi-uo237464-shard-00-01-bt3rt.mongodb.net:27017,trabajosdi-uo237464-shard-00-02-bt3rt.mongodb.net:27017/test?ssl=true&replicaSet=TrabajoSdi-UO237464-shard-0&authSource=admin&retryWrites=true');

var fileUpload = require('express-fileupload');
app.use(fileUpload());


var gestorBD = require("./modules/gestorBD.js");
gestorBD.init(app,mongo);
app.set('clave','abcdefg');
app.set('crypto',crypto);

var bodyParser = require('body-parser');
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: true}));


app.use(express.static('public'));

// Variables
app.set('port', 8081);

//Rutas/controladores por lógica
require("./routes/rusuarios.js")(app, swig, gestorBD);
require("./routes/rofertas.js")(app, swig, gestorBD);

//Página principal
app.get('/', function (req, res) {
    res.redirect('/login');
})
// lanzar el servidor
app.listen(app.get('port'), function () {
    console.log("Servidor activo");
})