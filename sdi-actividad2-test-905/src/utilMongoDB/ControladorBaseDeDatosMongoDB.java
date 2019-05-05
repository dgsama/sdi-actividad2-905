package utilMongoDB;

import java.text.ParseException;
import java.util.Date;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class ControladorBaseDeDatosMongoDB {

	private MongoClient mongoClient;
	private MongoDatabase mongodb;

	public void connectDatabase() {
		try {
			setMongoClient(new MongoClient(new MongoClientURI(
					"mongodb://admin:sdi@trabajosdi-uo237464-shard-00-00-bt3rt.mongodb.net:27017,trabajosdi-uo237464-shard-00-01-bt3rt.mongodb.net:27017,trabajosdi-uo237464-shard-00-02-bt3rt.mongodb.net:27017/test?ssl=true&replicaSet=TrabajoSdi-UO237464-shard-0&authSource=admin&retryWrites=true")));
			setMongodb(getMongoClient().getDatabase("test"));
		} catch (Exception ex) {
			System.out.println(ex.toString());
		}
	}

	public void insertarDatos() throws ParseException {
		try {
			MongoCollection<Document> col = getMongodb().getCollection("usuarios");
			col.insertOne(new Document().append("nombre", "Administrador").append("apellido", "del Sistema")
					.append("email", "admin@email.com")
					.append("password", "ebd5359e500475700c6cc3dd4af89cfd0569aa31724a1bf10ed1e3019dcfdb11")
					.append("rol", "admin").append("dinero", 9999));
			col.insertOne(new Document().append("nombre", "Andrei").append("apellido", "Manu")
					.append("email", "andrei@mail.com")
					.append("password", "353f9f25a52fbbe951bc1176019b58d9a7dd04b3094bc0334115862118846098")
					.append("rol", "user").append("dinero", 100));
			col.insertOne(new Document().append("nombre", "Rosario").append("apellido", "Palacios")
					.append("email", "rosario@mail.com")
					.append("password", "353f9f25a52fbbe951bc1176019b58d9a7dd04b3094bc0334115862118846098")
					.append("rol", "user").append("dinero", 20));
			col.insertOne(new Document().append("nombre", "David").append("apellido", "Garcia")
					.append("email", "david@mail.com")
					.append("password", "353f9f25a52fbbe951bc1176019b58d9a7dd04b3094bc0334115862118846098")
					.append("rol", "user").append("dinero", 100));
			col.insertOne(new Document().append("nombre", "Cova").append("apellido", "Arroyo")
					.append("email", "cova@mail.com")
					.append("password", "353f9f25a52fbbe951bc1176019b58d9a7dd04b3094bc0334115862118846098")
					.append("rol", "user").append("dinero", 100));
			col.insertOne(new Document().append("nombre", "Raul").append("apellido", "Gonzalez Blanco")
					.append("email", "raul@mail.com")
					.append("password", "353f9f25a52fbbe951bc1176019b58d9a7dd04b3094bc0334115862118846098")
					.append("rol", "user").append("dinero", 19));

			col = getMongodb().getCollection("ofertas");
			/** OFERTAS USER 1**/
			col.insertOne(new Document().append("titulo", "Ford Focus tdi").append("descripcion",
					"Coche de segunda mano con 200k kilometros, diesel, año 2007 color rojo pasión. Se regalan neumaticos de invierno")
					.append("precio", "6000").append("creador", "andrei@mail.com").append("comprador", "")
					.append("destacada", "false").append("fecha", new Date()));
			col.insertOne(new Document().append("titulo", "Renault Clio tdi").append("descripcion",
					"Coche de segunda mano con 200k kilometros, diesel, año 2007 color rojo pasión. Se regalan neumaticos de invierno")
					.append("precio", "6000").append("creador", "andrei@mail.com").append("comprador", "rosario@mail.com")
					.append("destacada", "true").append("fecha", new Date()));
			col.insertOne(new Document().append("titulo", "Sara picaso").append("descripcion",
					"Coche de segunda mano con 200k kilometros, diesel, año 2007 color rojo pasión. Se regalan neumaticos de invierno")
					.append("precio", "6000").append("creador", "andrei@mail.com").append("comprador", "rosario@mail.com")
					.append("destacada", "false").append("fecha", new Date()));
			
			
			/** OFERTAS USER 2**/
			col.insertOne(new Document().append("titulo", "Kit de supervivencia").append("descripcion",
					"Esto es una descripcion para el producto de rosario")
					.append("precio", "80").append("creador", "rosario@mail.com").append("comprador", "")
					.append("destacada", "false").append("fecha", new Date()));
			col.insertOne(new Document().append("titulo", "Mochila kargo").append("descripcion",
					"Esto es una descripcion para el producto de rosario")
					.append("precio", "1000").append("creador", "rosario@mail.com").append("comprador", "david@mail.com")
					.append("destacada", "true").append("fecha", new Date()));
			col.insertOne(new Document().append("titulo", "Pistola de bengalas modelo k67").append("descripcion",
					"Esto es una descripcion para el producto de rosario")
					.append("precio", "12").append("creador", "rosario@mail.com").append("comprador", "david@mail.com")
					.append("destacada", "false").append("fecha", new Date()));
			
			
			/** OFERTAS USER 3**/
			col.insertOne(new Document().append("titulo", "Tekken 6").append("descripcion",
					"Esto es una descripcion para el producto de david")
					.append("precio", "35").append("creador", "david@mail.com").append("comprador", "")
					.append("destacada", "false").append("fecha", new Date()));
			col.insertOne(new Document().append("titulo", "God of war 4").append("descripcion",
					"Esto es una descripcion para el producto de david")
					.append("precio", "69").append("creador", "david@mail.com").append("comprador", "cova@mail.com")
					.append("destacada", "true").append("fecha", new Date()));
			col.insertOne(new Document().append("titulo", "Fifa 19").append("descripcion",
					"Esto es una descripcion para el producto de david")
					.append("precio", "45").append("creador", "david@mail.com").append("comprador", "cova@mail.com")
					.append("destacada", "false").append("fecha", new Date()));
			
			
			/** OFERTAS USER 4**/
			col.insertOne(new Document().append("titulo", "Husky ojos azules y verdes").append("descripcion",
					"Esto es una descripcion para el producto de cova")
					.append("precio", "180").append("creador", "cova@mail.com").append("comprador", "")
					.append("destacada", "false").append("fecha", new Date()));
			col.insertOne(new Document().append("titulo", "Alaskan malamute hembra").append("descripcion",
					"Esto es una descripcion para el producto de cova")
					.append("precio", "200").append("creador", "cova@mail.com").append("comprador", "raul@mail.com")
					.append("destacada", "true").append("fecha", new Date()));
			col.insertOne(new Document().append("titulo", "Gato").append("descripcion",
					"Esto es una descripcion para el producto de cova")
					.append("precio", "100").append("creador", "cova@mail.com").append("comprador", "raul@mail.com")
					.append("destacada", "false").append("fecha", new Date()));
			
			
			/** OFERTAS USER 5**/
			col.insertOne(new Document().append("titulo", "Camino a la decima").append("descripcion",
					"Esto es una descripcion para el producto del 7 de españa")
					.append("precio", "100").append("creador", "raul@mail.com").append("comprador", "")
					.append("destacada", "false").append("fecha", new Date()));
			col.insertOne(new Document().append("titulo", "Camino a la undecima").append("descripcion",
					"Esto es una descripcion para el producto del 7 de españa")
					.append("precio", "120").append("creador", "raul@mail.com").append("comprador", "andrei@mail.com")
					.append("destacada", "true").append("fecha", new Date()));
			col.insertOne(new Document().append("titulo", "La 12+1").append("descripcion",
					"Esto es una descripcion para el producto del 7 de españa")
					.append("precio", "19").append("creador", "raul@mail.com").append("comprador", "andrei@mail.com")
					.append("destacada", "false").append("fecha", new Date()));
			
		
		
		} catch (Exception ex) {
			System.out.print(ex.toString());
		}

	}

	public void borrarDatos() {
		getMongodb().getCollection("ofertas").drop();
		getMongodb().getCollection("usuarios").drop();
		getMongodb().getCollection("mensajes").drop();
		getMongodb().getCollection("conversaciones").drop();

	}

	public void preparacionBase() throws ParseException {
		ControladorBaseDeDatosMongoDB javaMongodbInsertData = new ControladorBaseDeDatosMongoDB();
		System.out.println("Conectando a la base");
		javaMongodbInsertData.connectDatabase();
		System.out.println("Eliminando la base");
		javaMongodbInsertData.borrarDatos();
		System.out.println("Insertando en la base");
		javaMongodbInsertData.insertarDatos();
	}

	public MongoDatabase getMongodb() {
		return mongodb;
	}

	public void setMongodb(MongoDatabase mongodb) {
		this.mongodb = mongodb;
	}

	public MongoClient getMongoClient() {
		return mongoClient;
	}

	public void setMongoClient(MongoClient mongoClient) {
		this.mongoClient = mongoClient;
	}
}
