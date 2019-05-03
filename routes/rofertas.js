module.exports = function (app, swig, gestorBD) {


    //HOME
    app.get("/home", function (req, res) {
        var criterio={};
        gestorBD.obtenerOfertas(criterio, function (ofertas) {
            if (ofertas == null) {
                res.send("Error al listar ");
            } else {
                var respuesta = swig.renderFile('views/home.html', {
                    usuario: req.session.usuario,
                    dinero: req.session.dinero,
                    rol: req.session.rol,
                    ofertas:ofertas
                });
                res.send(respuesta);
            }
        });

    });

    //LISTAR OFERTAS

    app.get("/usr/listarCreadas", function (req, res) {
        var criterio = {creador: req.session.usuario};
        gestorBD.obtenerOfertas(criterio, function (ofertas) {
            if (ofertas == null) {
                res.send("Error al listar ");
            } else {
                var respuesta = swig.renderFile('views/listOwnOffers.html', {
                    usuario: req.session.usuario,
                    dinero: req.session.dinero,
                    rol: req.session.rol,
                    ofertas: ofertas
                });
                res.send(respuesta);
            }
        });
    });

    app.get("/usr/listarCompradas", function (req, res) {
        var criterio = {comprador: req.session.usuario};
        gestorBD.obtenerOfertas(criterio, function (ofertas) {
            if (ofertas == null) {
                res.send("Error al listar ");
            } else {
                var respuesta = swig.renderFile('views/listBoughtOffers.html', {
                    usuario: req.session.usuario,
                    dinero: req.session.dinero,
                    rol: req.session.rol,
                    ofertas: ofertas
                });
                res.send(respuesta);
            }
        });
    });

    //AGREGAR OFERTA
    app.get("/usr/agregarOferta", function (req, res) {
        var respuesta = swig.renderFile('views/addOffer.html', {
            usuario: req.session.usuario,
            dinero: req.session.dinero,
            rol: req.session.rol
        });
        res.send(respuesta);
    });

    app.post('/usr/crearOferta', function (req, res) {
            if (req.body.titulo != "") {
                if (req.body.descripcion != "") {
                    if (req.body.precio > 0) {
                        var prueba = req.session.dinero - 20;
                        var oferta = {
                            titulo: req.body.titulo,
                            descripcion: req.body.descripcion,
                            fecha: new Date(),
                            precio: req.body.precio,
                            creador: req.session.usuario,
                            comprador: "",
                            destacada: req.body.destacada
                        }
                        if (req.body.destacada == "false") {
                            gestorBD.insertarOferta(oferta, function (id) {
                                if (id == null) {
                                    res.redirect("/registrarse" +
                                        "?mensaje=Algo ha fallado. Póngase en contacto con el administrador o pruebe más tarde" +
                                        "&tipoMensaje=alert-danger ");
                                } else {
                                    res.redirect("/home" +
                                        "?mensaje=Oferta insertada con exito." +
                                        "&tipoMensaje=alert-success ");
                                }
                            });
                        } else if (req.body.destacada == "true" && prueba >= 0) {
                            gestorBD.insertarOferta(oferta, function (id) {
                                if (id == null) {
                                    res.redirect("/registrarse" +
                                        "?mensaje=Algo ha fallado. Póngase en contacto con el administrador o pruebe más tarde" +
                                        "&tipoMensaje=alert-danger ");
                                } else {
                                    var total = req.session.dinero - 20;
                                    req.session.dinero = total;

                                    //OBTENER USUARIO Y MODIFICAR SU DINERO

                                    var criterio = {email: req.session.usuario};
                                    var usuarioD = {
                                        dinero: total
                                    }
                                    gestorBD.modificarDineroUsuario(criterio, usuarioD, function (result) {
                                        if (result == null) {
                                            console.log("error al modificar")
                                        } else {
                                            console.log("dinero modificado con exito");
                                        }

                                    });
                                    res.redirect("/home" +
                                        "?mensaje=Oferta insertada con exito." +
                                        "&tipoMensaje=alert-success ");
                                }
                            });
                        } else {
                            res.redirect("/usr/agregarOferta" +
                                "?mensaje=Para hacer una oferta destacada necesitas tener al menos 20€" +
                                "&tipoMensaje=alert-danger ");
                        }
                    } else {
                        res.redirect("/usr/agregarOferta" +
                            "?mensaje=El precio no puede ser 0 o negativo" +
                            "&tipoMensaje=alert-danger ");
                    }

                } else {
                    res.redirect("/usr/agregarOferta" +
                        "?mensaje=La descripción no puede estar vacia" +
                        "&tipoMensaje=alert-danger ");
                }

            } else {
                res.redirect("/usr/agregarOferta" +
                    "?mensaje=El título no puede estar vacio" +
                    "&tipoMensaje=alert-danger ");
            }
        }
    );
//ELIMINAR OFERTA

    app.get('/usr/eliminarOferta/:id', function (req, res) {
        var criterio = {"_id" : gestorBD.mongo.ObjectID(req.params.id) };
        gestorBD.eliminarOferta(criterio,function(ofertas){
            if ( ofertas == null ){
                res.send(respuesta);
            } else {
                res.redirect("/usr/listarCreadas" + "?mensaje=Oferta eliminada con exito" +
                    "&tipoMensaje=alert-success ");
            }});
    })



//COMPRAR OFERTA

///usr/comprar/{{ oferta._id.toString() }}


//FIN DEL ARCHIVO
}
;