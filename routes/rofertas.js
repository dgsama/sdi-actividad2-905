module.exports = function (app, swig, gestorBD) {


    //HOME
    app.get("/home", function (req, res) {
        var criterio = {
            "creador": {
                $ne: req.session.usuario
            },
            "destacada": "false"
        };
        var criterioFav = {
            "creador": {
                $ne: req.session.usuario
            },
            "destacada": "true"
        };
        var pg = parseInt(req.query.pg); // Es String !!!
        if (req.query.pg == null) { // Puede no venir el param
            pg = 1;
        }

        gestorBD.obtenerOfertasPg(criterio, pg, function (ofertas, total) {
            if (ofertas == null) {
                res.send("Error al listar ");
            } else {
                var ultimaPg = total / 4;
                if (total % 4 > 0) { // Sobran decimales
                    ultimaPg = ultimaPg + 1;
                }
                var paginas = []; // paginas mostrar
                for (var i = pg - 2; i <= pg + 2; i++) {
                    if (i > 0 && i <= ultimaPg) {
                        paginas.push(i);
                    }
                }
                gestorBD.obtenerOfertas(criterioFav, function (ofertasFav) {
                    if (ofertasFav == null) {
                        res.send("Error al listar ");
                    } else {
                        var respuesta = swig.renderFile('views/home.html', {
                            usuario: req.session.usuario,
                            dinero: req.session.dinero,
                            rol: req.session.rol,
                            ofertas: ofertas,
                            paginas: paginas,
                            actual: pg,
                            ofertasFav: ofertasFav
                        });
                        res.send(respuesta);
                    }
                });
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
                                    gestorBD.modificarUsuario(criterio, usuarioD, function (result) {
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
        var criterio = {"_id": gestorBD.mongo.ObjectID(req.params.id)};
        gestorBD.eliminarOferta(criterio, function (ofertas) {
            if (ofertas == null) {
                console.log("Problemas al eliminar la oferta")
            } else {
                res.redirect("/usr/listarCreadas" + "?mensaje=Oferta eliminada con exito" +
                    "&tipoMensaje=alert-success ");
            }
        });
    })

    //MARCAR COMO DESTACADA
    app.get('/usr/marcarDestacada/:id', function (req, res) {
        if (req.session.dinero >= 20) {

            var criterio = {"_id": gestorBD.mongo.ObjectID(req.params.id)};
            var ofertaN = {
                destacada: "true"
            }
            gestorBD.modificarOferta(criterio, ofertaN, function (result) {
                if (result == null) {
                    console.log("Fallo al marcar como destacada");
                } else {
                    var total = req.session.dinero - 20;
                    req.session.dinero = total;

                    //OBTENER USUARIO Y MODIFICAR SU DINERO

                    var criterio = {email: req.session.usuario};
                    var usuarioD = {
                        dinero: total
                    }
                    gestorBD.modificarUsuario(criterio, usuarioD, function (result) {
                        if (result == null) {
                            console.log("error al modificar")
                        } else {
                            console.log("dinero modificado con exito");
                        }
                    });
                    res.redirect("/usr/listarCreadas" + "?mensaje=Oferta destacada con exito" +
                        "&tipoMensaje=alert-success ");
                }
            });
        } else {
            res.redirect("/usr/listarCreadas" + "?mensaje=Para marcar la oferta como destacada necesitas al menos 20€" +
                "&tipoMensaje=alert-danger ");
        }
    })

//COMPRAR OFERTA

    app.get('/usr/comprar/:id/:precio', function (req, res) {
        if (req.session.dinero >= req.params.precio) {

            var criterio = {"_id": gestorBD.mongo.ObjectID(req.params.id)};
            var ofertaN = {
                comprador: req.session.usuario
            }
            gestorBD.modificarOferta(criterio, ofertaN, function (result) {
                if (result == null) {
                    console.log("Fallo al marcar como destacada");
                } else {
                    var total = req.session.dinero - req.params.precio;
                    req.session.dinero = total;

                    //OBTENER USUARIO Y MODIFICAR SU DINERO

                    var criterio = {email: req.session.usuario};
                    var usuarioD = {
                        dinero: total
                    }
                    gestorBD.modificarUsuario(criterio, usuarioD, function (result) {
                        if (result == null) {
                            console.log("error al modificar")
                        } else {
                            console.log("dinero modificado con exito");
                        }
                    });
                    res.redirect("/home" + "?mensaje=Oferta comprada con exito" +
                        "&tipoMensaje=alert-success ");
                }
            });
        } else {
            res.redirect("/home" + "?mensaje=No tienes suficiente dinero para comprar esta oferta" +
                "&tipoMensaje=alert-danger ");
        }
    })

    //BUSCAR OFERTAS
    app.get("/usr/buscarOferta", function (req, res) {
        var criterio = {};
        var criterioFav = {
            "creador": {
                $ne: req.session.usuario
            },
            "destacada": "true"
        };
        if (req.query.busqueda != null) {
            criterio = {
                "titulo": {$regex: ".*" + req.query.busqueda + ".*", $options: "i"},
                "creador": {
                    $ne: req.session.usuario
                },
                "destacada": "false"
            };
            req.session.busqued = req.query.busqueda;
        } else if (req.session.busqued != null) {
            criterio = {
                "titulo": {$regex: ".*" + req.session.busqued + ".*", $options: "i"},
                "creador": {
                    $ne: req.session.usuario
                },
                "destacada": "false"
            };
        } else {
            criterio = {
                "creador": {
                    $ne: req.session.usuario
                },
                "destacada": "false"
            };
        }

        var pg = parseInt(req.query.pg); // Es String !!!
        if (req.query.pg == null) { // Puede no venir el param
            pg = 1;
        }
        gestorBD.obtenerOfertasPg(criterio, pg, function (ofertas, total) {
            if (ofertas == null) {
                res.send("Error al listar ");
            } else {
                var ultimaPg = total / 4;
                if (total % 4 > 0) { // Sobran decimales
                    ultimaPg = ultimaPg + 1;
                }
                var paginas = []; // paginas mostrar
                for (var i = pg - 2; i <= pg + 2; i++) {
                    if (i > 0 && i <= ultimaPg) {
                        paginas.push(i);
                    }
                }

                gestorBD.obtenerOfertas(criterioFav, function (ofertasFav) {
                    if (ofertasFav == null) {
                        res.send("Error al listar ");
                    } else {
                        var respuesta = swig.renderFile('views/home.html', {
                            usuario: req.session.usuario,
                            dinero: req.session.dinero,
                            rol: req.session.rol,
                            ofertas: ofertas,
                            paginas: paginas,
                            actual: pg,
                            ofertasFav: ofertasFav
                        });
                        res.send(respuesta);
                    }
                });
            }
        });
    });


//FIN DEL ARCHIVO
}
;