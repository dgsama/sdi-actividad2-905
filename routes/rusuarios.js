module.exports = function (app, swig, gestorBD) {

    //LOGIN

    app.get("/login", function (req, res) {
        var respuesta = swig.renderFile('views/login.html', {});
        res.send(respuesta);
    });

    app.post("/identificarse", function (req, res) {
        var seguro = app.get("crypto").createHmac('sha256', app.get('clave'))
            .update(req.body.password).digest('hex');
        var criterio = {
            email: req.body.email,
            password: seguro
        }
        gestorBD.obtenerUsuarios(criterio, function (usuarios) {
            if (usuarios == null || usuarios.length == 0) {
                req.session.usuario = null;
                res.redirect("/login" +
                    "?mensaje=Email o password incorrecto" +
                    "&tipoMensaje=alert-danger ");
            } else {
                req.session.usuario = usuarios[0].email;
                req.session.rol = usuarios[0].rol;
                req.session.dinero = usuarios[0].dinero;
                res.redirect("/home");
            }
        });
    });

    //REGISTRO

    app.get("/registrarse", function (req, res) {
        var respuesta = swig.renderFile('views/registro.html', {});
        res.send(respuesta);
    });

    app.post('/registrarUsuario', function (req, res) {
        if (req.body.password == req.body.password2) {
            var criterio = {
                email: req.body.email
            }
            gestorBD.obtenerUsuarios(criterio, function (usuarios) {
                if (usuarios == null || usuarios.length == 0) {
                    var seguro = app.get("crypto").createHmac('sha256', app.get('clave'))
                        .update(req.body.password).digest('hex');
                    var usuario = {
                        email: req.body.email,
                        nombre: req.body.name,
                        apellidos: req.body.lastname,
                        password: seguro,
                        dinero: 100,
                        rol: "user"
                    }
                    gestorBD.insertarUsuario(usuario, function (id) {
                        if (id == null) {
                            res.redirect("/registrarse" +
                                "?mensaje=Algo ha fallado. Póngase en contacto con el administrador o pruebe más tarde" +
                                "&tipoMensaje=alert-danger ");
                        } else {
                            req.session.usuario = req.body.email;
                            req.session.rol = usuario.rol;
                            req.session.dinero = usuario.dinero;
                            res.redirect("/home");
                        }
                    });
                } else {
                    req.session.usuario = null;
                    res.redirect("/registrarse" +
                        "?mensaje=Email ya existente" +
                        "&tipoMensaje=alert-danger ");
                }
            });
        } else {
            res.redirect("/registrarse" +
                "?mensaje=Las contraseñas deben coincidir" +
                "&tipoMensaje=alert-danger ");
        }
    })

    //LOGOUT

    app.get('/desconectarse', function (req, res) {
        req.session.usuario = null;
        res.redirect("/login" +
            "?mensaje=Usuario desconectado" +
            "&tipoMensaje=alert-success");
    })


    //MANEJO DE USUARIOS

    app.get("/adm/users", function (req, res) {
        var respuesta = swig.renderFile('views/listUsers.html', {});
        res.send(respuesta);
    });


//FIN DEL ARCHIVO
};