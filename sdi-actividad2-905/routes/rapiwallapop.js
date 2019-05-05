module.exports = function (app, gestorBD) {

    //LOGIN

    app.post("/api/login/", function (req, res) {
        var seguro = app.get("crypto").createHmac('sha256', app.get('clave'))
            .update(req.body.password).digest('hex');
        var criterio = {
            email: req.body.email,
            password: seguro
        }

        gestorBD.obtenerUsuarios(criterio, function (usuarios) {
            if (usuarios == null || usuarios.length == 0) {
                res.status(401); // Unauthorized
                res.json({
                    autenticado: false
                })
            } else {
                var token = app.get('jwt').sign(
                    {usuario: criterio.email, tiempo: Date.now() / 1000},
                    "secreto");
                req.session.usuario = req.body.email;
                res.status(200);
                res.json({
                    autenticado: true,
                    token: token
                })
            }

        });
    });


    //OBTENER OFERTAS PARA EL USUARIO
    app.get("/api/ofertas", function (req, res) {
        var criterio = {
            "creador": {
                $ne: req.headers.email
            }
        }

        gestorBD.obtenerOfertas(criterio, function (ofertas) {
            if (ofertas == null) {
                res.status(500);
                res.json({
                    error: "se ha producido un error"
                })
            } else {
                res.status(200);
                res.send(JSON.stringify(ofertas));
            }
        });
    });


//INSERTAR MENSAJE
    app.post("/api/crearMensaje", function (req, res) {
        var mensaje = {
            fecha: new Date(),
            mensaje: req.body.mensaje,
            conversacion: req.body.conversacion,
            autorMensaje: req.headers.email,
            leido: "false"
        }
        gestorBD.insertarMensaje(mensaje, function (id) {
            if (id == null) {
                res.status(500);
                res.json({
                    error: "se ha producido un error"
                })
            } else {
                res.status(201);
                res.json({
                    mensaje: "mensaje insertard",
                    objecto: mensaje,
                    _id: id
                })
            }
        });

    });

    //OBTENER MENSAJES/CONVERSACIONES
    app.get("/api/conversaciones/:id", function (req, res) {
        var owner = "";
        if (req.headers.vendedor == req.headers.email) {
            owner = "true";
        } else {
            owner = "false";
        }
        var criterio = {};

        if (owner == "true") {
            criterio = {
                oferta: req.params.id,
                vendedor: req.headers.email
            }
        } else {
            criterio = {
                oferta: req.params.id,
                posibleComprador: req.headers.email
            }
        }
        gestorBD.obtenerConversaciones(criterio, function (conversaciones) {
            if (conversaciones == null) {

                res.status(500);
                res.json({
                    error: "se ha producido un error"
                })
            } else {

                if (owner == "true") {
                    res.status(200);
                    res.send(JSON.stringify(conversaciones));

                } else {
                    var idCon = "";
                    if (conversaciones[0] != null || conversaciones[0] != undefined) {
                        criterio = {
                            conversacion: conversaciones[0]._id.toString()
                        }
                        idCon = conversaciones[0]._id.toString();
                    } else {
                        var conversacion = {
                            oferta: req.params.id,
                            vendedor: req.headers.vendedor,
                            posibleComprador: req.headers.email,
                            titulo: "Kit de supervivencia"
                        }
                        gestorBD.insertarConversacion(conversacion, function (id) {
                            if (id == null) {

                                res.status(500);
                                res.json({
                                    error: "se ha producido un error creando la conver"
                                })

                            } else {
                                idCon = id.toString();
                                criterio = {
                                    conversacion: id
                                }
                            }
                        });
                    }
                    gestorBD.obtenerMensajes(criterio, function (mensajes) {
                        if (mensajes == null) {
                            res.status(500);
                            res.json({
                                error: "se ha producido un error"
                            })
                        } else {
                            res.status(200);
                            console.log(idCon);
                            var resp = {idCon: idCon.toString(), mens: mensajes};

                            res.send(JSON.stringify(resp));

                        }
                    });
                }
            }
        });
    });

    //MARCAR COMO LEIDO MENSAJE

    app.put("/api/marcarLeido/:id", function (req, res) {
        var criterio = {"_id": gestorBD.mongo.ObjectID(req.params.id)};
        var mensaje = {"leido": "true"};
        gestorBD.modificarMensaje(criterio, mensaje, function (result) {
            if (result == null) {
                res.status(500);
                res.json({
                    error: "se ha producido un error"
                })
            } else {
                res.status(200);
                res.json({
                    mensaje: "mensaje modificado",
                    _id: req.params.id
                })
            }
        });
    });

    //BORRAR CONVERSACION
    app.delete("/api/borrarConver/:id", function (req, res) {
        var criterio = {"_id": gestorBD.mongo.ObjectID(req.params.id)};
        var criterio2 = {"conversacion": req.params.id};

        gestorBD.eliminarConversacion(criterio, function (conversaciones) {
            if (conversaciones == null) {
                res.status(500);
                res.json({
                    error: "se ha producido un error"
                })
            } else {
                gestorBD.eliminarMensajes(criterio2, function (mensajes) {
                    if (mensajes == null) {
                        res.status(500);
                        res.json({
                            error: "se ha producido un error"
                        })
                    } else {
                        res.status(200);
                        var resp = {convers: conversaciones, mens: mensajes};
                        res.send(JSON.stringify(resp));
                    }
                });
            }
        });
    });


    //OBTENER CONVERSACIONES PARA EL USUARIO
    app.get("/api/conversUsuario", function (req, res) {
        criterio = {
            $or: [{vendedor: req.headers.email}, {posibleComprador: req.headers.email}]
        }

        gestorBD.obtenerConversaciones(criterio, function (conversaciones) {
            if (conversaciones == null) {
                res.status(500);
                res.json({
                    error: "se ha producido un error"
                })
            } else {
                res.status(200);
                res.send(JSON.stringify(conversaciones));
            }
        });
    });

//OBTENER MENSAJES CONVERSACION
    app.get("/api/mensajesConversacion/:id", function (req, res) {
       var criterio = {
            conversacion: req.params.id
        }

        gestorBD.obtenerMensajes(criterio, function (mensajes) {
            if (mensajes == null) {
                res.status(500);
                res.json({
                    error: "se ha producido un error"
                })
            } else {
                res.status(200);
                var resp = {idCon: req.params.id, mens: mensajes};

                res.send(JSON.stringify(resp));
            }
        });
    });

    //FIN ARCHIVO
}