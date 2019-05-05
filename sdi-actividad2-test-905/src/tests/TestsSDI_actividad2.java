package tests;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

import tests.pageobjects.PO_HomeView;
import tests.pageobjects.PO_LoginView;
import tests.pageobjects.PO_OfferView;
import tests.pageobjects.PO_PrivateView;
import tests.pageobjects.PO_RegisterView;
import tests.pageobjects.PO_View;
import tests.util.SeleniumUtils;
import utilMongoDB.ControladorBaseDeDatosMongoDB;

public class TestsSDI_actividad2 {

	static String PathFirefox64 = "C:\\Program Files\\Mozilla Firefox\\firefox.exe";
	static String Geckdriver022 = "D:\\David\\Documents\\SDI 2019\\NODE\\SeleniumUtilsTests\\geckodriver024win64.exe";
	static WebDriver driver = getDriver(PathFirefox64, Geckdriver022);
	static String APP_URL = "http://localhost:8081";
	static String URL = "";

	public static WebDriver getDriver(String PathFirefox, String Geckdriver) {
		System.setProperty("webdriver.firefox.bin", PathFirefox);
		System.setProperty("webdriver.gecko.driver", Geckdriver);
		WebDriver driver = new FirefoxDriver();
		return driver;
	}

	// Antes de cada prueba se navega al URL home de la aplicaciónn
	@Before
	public void setUp() throws Exception {
		ControladorBaseDeDatosMongoDB cMongo = new ControladorBaseDeDatosMongoDB();
		cMongo.preparacionBase();
		driver.navigate().to(URL);

	}

	// Antes de la primera prueba
	@BeforeClass
	static public void begin() {
		URL = APP_URL;
	}

	// Despu�s de cada prueba se borran las cookies del navegador
	@After
	public void tearDown() {
		driver.manage().deleteAllCookies();
	}

	// Al finalizar la última prueba
	@AfterClass
	static public void end() {
		// Cerramos el navegador al finalizar las pruebas
		driver.quit();
	}

	/** TESTS APLICACION WEB **/
	// [Prueba1] Registro de Usuario con datos válidos.
	@Test
	public void P01() {
		// Vamos al formulario de registro
		driver.navigate().to(URL + "/registrarse");
		// Rellenamos el formulario.
		PO_RegisterView.fillForm(driver, "juan@mail.com", "Josefo", "Perez", "123456", "123456");
		PO_View.checkElement(driver, "text", "Ofertas disponibles");
	}

	// [Prueba2] Registro de Usuario con datos inválidos (email vacío, repetición de
	// contraseña inválida).
	@Test
	public void P02() {
		// Vamos al formulario de registro
		driver.navigate().to(URL + "/registrarse");
		// Rellenamos el formulario.
		PO_RegisterView.fillForm(driver, "", "", "", "123456", "12345612");
		PO_View.checkElement(driver, "text", "Registrate");
	}

	// [Prueba3] Registro de Usuario con email existente.
	@Test
	public void P03() {
		// Vamos al formulario de registro
		driver.navigate().to(URL + "/registrarse");
		// Rellenamos el formulario.
		PO_RegisterView.fillForm(driver, "andrei@mail.com", "Josefo", "Perez", "123456", "123456");
		PO_View.checkElement(driver, "text", "Email ya existente");
	}

	// [Prueba4] Inicio de sesión con datos válidos.
	@Test
	public void P04() {
		// Vamos al formulario de logueo.
		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
		// Rellenamos el formulario
		PO_LoginView.fillForm(driver, "andrei@mail.com", "1234");
		// COmprobamos que entramos en la pagina privada del usuario
		PO_View.checkElement(driver, "text", "Ofertas disponibles");
	}

	// [Prueba5] Inicio de sesión con datos inválidos (email existente, pero
	// contraseña incorrecta).
	@Test
	public void P05() {
		// Vamos al formulario de logueo.
		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
		// Rellenamos el formulario
		PO_LoginView.fillForm(driver, "andrei@mail.com", "admin");
		// COmprobamos que entramos en la pagina privada del usuario
		PO_View.checkElement(driver, "text", "Email o password incorrecto");
	}

	// [Prueba6] Inicio de sesión con datos inválidos (campo email o contraseña
	// vacíos).
	@Test
	public void P06() {
		// Vamos al formulario de logueo.
		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
		// Rellenamos el formulario
		PO_LoginView.fillForm(driver, "", "");
		// COmprobamos que entramos en la pagina privada del usuario
		PO_View.checkElement(driver, "text", "Identificación de usuario");
	}

	// [Prueba7] Inicio de sesión con datos inválidos (email no existente en la
	// aplicación).
	@Test
	public void P07() {
		// Vamos al formulario de logueo.
		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
		// Rellenamos el formulario
		PO_LoginView.fillForm(driver, "Esteemail@noexiste.com", "1234");
		// COmprobamos que entramos en la pagina privada del usuario
		PO_View.checkElement(driver, "text", "Email o password incorrecto");
	}

	// [Prueba8] Hacer click en la opción de salir de sesión y comprobar que se
	// redirige a la página de inicio de sesión (Login).
	@Test
	public void P08() {
		// Rellenamos el formulario
		PO_LoginView.fillForm(driver, "andrei@mail.com", "1234");
		// COmprobamos que entramos en la pagina privada del usuario
		PO_View.checkElement(driver, "text", "Ofertas disponibles");
		// Ahora nos desconectamos
		driver.navigate().to(URL + "/desconectarse");
		PO_View.checkElement(driver, "text", "Usuario desconectado");
	}

	// [Prueba9] Comprobar que el botón cerrar sesión no está visible si el usuario
	// no está autenticado.
	@Test
	public void P09() {
		ExpectedConditions.invisibilityOfElementLocated(By.partialLinkText("Desconectarse"));
	}

	// [Prueba10] Mostrar el listado de usuarios y comprobar que se muestran todos
	// los que existen en el sistema.

	@Test
	public void P10() {
		PO_LoginView.fillForm(driver, "admin@email.com", "admin");
		PO_View.checkElement(driver, "text", "Ofertas disponibles");
		driver.navigate().to(URL + "/adm/users");
		// Comprobar los 5 usuarios
		PO_View.checkElement(driver, "text", "andrei@mail.com");
		PO_View.checkElement(driver, "text", "rosario@mail.com");
		PO_View.checkElement(driver, "text", "david@mail.com");
		PO_View.checkElement(driver, "text", "cova@mail.com");
		PO_View.checkElement(driver, "text", "raul@mail.com");
		// Ahora nos desconectamos
		driver.navigate().to(URL + "/desconectarse");
		PO_View.checkElement(driver, "text", "Usuario desconectado");
	}
	// [Prueba11] Ir a la lista de usuarios, borrar el primer usuario de la lista,
	// comprobar que la lista se actualiza y dicho usuario desaparece.

	@Test
	public void P11() {
		PO_LoginView.fillForm(driver, "admin@email.com", "admin");
		PO_View.checkElement(driver, "text", "Ofertas disponibles");
		driver.navigate().to(URL + "/adm/users");
		// Comprobar los 5 usuarios
		PO_View.checkElement(driver, "text", "andrei@mail.com");
		PO_View.checkElement(driver, "text", "rosario@mail.com");
		PO_View.checkElement(driver, "text", "david@mail.com");
		PO_View.checkElement(driver, "text", "cova@mail.com");
		PO_View.checkElement(driver, "text", "raul@mail.com");
		// Marcar el primero y borrarlo
		driver.findElement(By.xpath(
				"(.//*[normalize-space(text()) and normalize-space(.)='andrei@mail.com'])[1]/following::input[1]"))
				.click();
		By boton = By.className("btn");
		driver.findElement(boton).click();
		// Comprobar que no esta
		ExpectedConditions.invisibilityOfElementLocated(By.partialLinkText("andrei"));
		// Ahora nos desconectamos
		driver.navigate().to(URL + "/desconectarse");
		PO_View.checkElement(driver, "text", "Usuario desconectado");
	}
	// [Prueba12] Ir a la lista de usuarios, borrar el último usuario de la lista,
	// comprobar que la lista se actualiza y dicho usuario desaparece.

	@Test
	public void P12() {
		PO_LoginView.fillForm(driver, "admin@email.com", "admin");
		PO_View.checkElement(driver, "text", "Ofertas disponibles");
		driver.navigate().to(URL + "/adm/users");
		// Comprobar los 5 usuarios
		PO_View.checkElement(driver, "text", "andrei@mail.com");
		PO_View.checkElement(driver, "text", "rosario@mail.com");
		PO_View.checkElement(driver, "text", "david@mail.com");
		PO_View.checkElement(driver, "text", "cova@mail.com");
		PO_View.checkElement(driver, "text", "raul@mail.com");
		// Marcar el primero y borrarlo
		driver.findElement(By
				.xpath("(.//*[normalize-space(text()) and normalize-space(.)='raul@mail.com'])[1]/following::input[1]"))
				.click();
		By boton = By.className("btn");
		driver.findElement(boton).click();
		// Comprobar que no esta
		ExpectedConditions.invisibilityOfElementLocated(By.partialLinkText("raul"));
		// Ahora nos desconectamos
		driver.navigate().to(URL + "/desconectarse");
		PO_View.checkElement(driver, "text", "Usuario desconectado");
	}

	// [Prueba13] Ir a la lista de usuarios, borrar 3 usuarios, comprobar que la
	// lista se actualiza y dichos usuarios desaparecen.
	@Test
	public void P13() {
		PO_LoginView.fillForm(driver, "admin@email.com", "admin");
		PO_View.checkElement(driver, "text", "Ofertas disponibles");
		driver.navigate().to(URL + "/adm/users");
		// Comprobar los 5 usuarios
		PO_View.checkElement(driver, "text", "andrei@mail.com");
		PO_View.checkElement(driver, "text", "rosario@mail.com");
		PO_View.checkElement(driver, "text", "david@mail.com");
		PO_View.checkElement(driver, "text", "cova@mail.com");
		PO_View.checkElement(driver, "text", "raul@mail.com");
		// Marcar el primero y borrarlo
		driver.findElement(By.xpath(
				"(.//*[normalize-space(text()) and normalize-space(.)='david@mail.com'])[1]/following::input[1]"))
				.click();
		driver.findElement(By.xpath(
				"(.//*[normalize-space(text()) and normalize-space(.)='rosario@mail.com'])[1]/following::input[1]"))
				.click();
		driver.findElement(By
				.xpath("(.//*[normalize-space(text()) and normalize-space(.)='cova@mail.com'])[1]/following::input[1]"))
				.click();
		By boton = By.className("btn");
		driver.findElement(boton).click();
		// Comprobar que no esta
		ExpectedConditions.invisibilityOfElementLocated(By.partialLinkText("david"));
		ExpectedConditions.invisibilityOfElementLocated(By.partialLinkText("cova"));
		ExpectedConditions.invisibilityOfElementLocated(By.partialLinkText("rosario"));
		// Ahora nos desconectamos
		driver.navigate().to(URL + "/desconectarse");
		PO_View.checkElement(driver, "text", "Usuario desconectado");
	}

	// [Prueba14] Ir al formulario de alta de oferta, rellenarla con datos válidos y
	// pulsar el botón Submit. Comprobar que la oferta sale en el listado de ofertas
	// de dicho usuario.
	@Test
	public void P14() {
		// Vamos al formulario de registro
		PO_LoginView.fillForm(driver, "andrei@mail.com", "1234");
		PO_View.checkElement(driver, "text", "Ofertas");
		driver.navigate().to(URL + "/usr/agregarOferta");
		// Rellenamos el formulario.
		PO_OfferView.fillForm(driver, "Prueba de titulo", "Esto es una descripcion", "12.0", "false");
		PO_View.checkElement(driver, "text", "Oferta insertada con exito.");
		// Ahora nos desconectamos
		driver.navigate().to(URL + "/desconectarse");
		PO_View.checkElement(driver, "text", "Usuario desconectado");
	}

	// [Prueba15] Ir al formulario de alta de oferta, rellenarla con datos inválidos
	// (campo título vacío) y pulsar el botón Submit. Comprobar que se muestra el
	// mensaje de campo obligatorio.
	@Test
	public void P15() {
		// Vamos al formulario de registro
		PO_LoginView.fillForm(driver, "andrei@mail.com", "1234");
		PO_View.checkElement(driver, "text", "Ofertas");
		driver.navigate().to(URL + "/usr/agregarOferta");
		// Rellenamos el formulario.
		PO_OfferView.fillForm(driver, "", "Esto es una descripcion", "12.0", "false");
		PO_View.checkElement(driver, "text", "¿Deseas destacar tu oferta?");
		// Ahora nos desconectamos
		driver.navigate().to(URL + "/desconectarse");
		PO_View.checkElement(driver, "text", "Usuario desconectado");
	}

	// [Prueba16] Mostrar el listado de ofertas para dicho usuario y comprobar que
	// se muestran todas los que existen para este usuario.
	@Test
	public void P16() {
		PO_LoginView.fillForm(driver, "andrei@mail.com", "1234");
		PO_View.checkElement(driver, "text", "Ofertas disponibles");
		driver.navigate().to(URL + "/usr/listarCreadas");
		// Comprobar los 5 usuarios
		PO_View.checkElement(driver, "text", "Ford Focus tdi");
		PO_View.checkElement(driver, "text", "Renault Clio tdi");
		PO_View.checkElement(driver, "text", "Sara picaso");
		// Ahora nos desconectamos
		driver.navigate().to(URL + "/desconectarse");
		PO_View.checkElement(driver, "text", "Usuario desconectado");
	}

	// [Prueba17] Ir a la lista de ofertas, borrar la primera oferta de la lista,
	// comprobar que la lista se actualiza y que la oferta desaparece.
	@Test
	public void P17() {
		PO_LoginView.fillForm(driver, "andrei@mail.com", "1234");
		PO_View.checkElement(driver, "text", "Ofertas disponibles");
		driver.navigate().to(URL + "/usr/listarCreadas");
		// Comprobar los 5 usuarios
		PO_View.checkElement(driver, "text", "Ford Focus tdi");
		PO_View.checkElement(driver, "text", "Renault Clio tdi");
		PO_View.checkElement(driver, "text", "Sara picaso");

		driver.findElement(
				By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Ford Focus tdi'])[1]/following::td[2]"))
				.click();
		driver.findElement(By.linkText("Eliminar")).click();
		ExpectedConditions.invisibilityOfElementLocated(By.partialLinkText("Ford Focus tdi"));
		PO_View.checkElement(driver, "text", "Oferta eliminada con exito");
		// Ahora nos desconectamos
		driver.navigate().to(URL + "/desconectarse");
		PO_View.checkElement(driver, "text", "Usuario desconectado");
	}

	// [Prueba18] Ir a la lista de ofertas, borrar la última oferta de la lista,
	// comprobar que la lista se actualiza y que la oferta desaparece
	@Test
	public void P18() {
		PO_LoginView.fillForm(driver, "andrei@mail.com", "1234");
		PO_View.checkElement(driver, "text", "Ofertas disponibles");
		driver.navigate().to(URL + "/usr/listarCreadas");
		// Comprobar los 5 usuarios
		PO_View.checkElement(driver, "text", "Ford Focus tdi");
		PO_View.checkElement(driver, "text", "Renault Clio tdi");
		PO_View.checkElement(driver, "text", "Sara picaso");

		driver.findElement(
				By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Sara picaso'])[1]/following::td[2]"))
				.click();
		driver.findElement(By.linkText("Eliminar")).click();
		ExpectedConditions.invisibilityOfElementLocated(By.partialLinkText("Sara picaso"));
		PO_View.checkElement(driver, "text", "Oferta eliminada con exito");
		// Ahora nos desconectamos
		driver.navigate().to(URL + "/desconectarse");
		PO_View.checkElement(driver, "text", "Usuario desconectado");
	}

	// [Prueba19] Hacer una búsqueda con el campo vacío y probar que se muestra la
	// página que corresponde con el listado de las ofertas existentes en el sistema
	@Test
	public void P19() {
		PO_LoginView.fillForm(driver, "andrei@mail.com", "1234");
		PO_View.checkElement(driver, "text", "Ofertas");
		PO_View.checkElement(driver, "text", "Kit de supervivencia");
		PO_View.checkElement(driver, "text", "Pistola de bengalas modelo k67");
		PO_View.checkElement(driver, "text", "Tekken 6");
		PO_View.checkElement(driver, "text", "Fifa 19");
		PO_View.checkElement(driver, "text", "Husky ojos azules y verdes");
		PO_PrivateView.search(driver, "");
		PO_View.checkElement(driver, "text", "Ofertas");
		PO_View.checkElement(driver, "text", "Kit de supervivencia");
		PO_View.checkElement(driver, "text", "Pistola de bengalas modelo k67");
		PO_View.checkElement(driver, "text", "Tekken 6");
		PO_View.checkElement(driver, "text", "Fifa 19");
		PO_View.checkElement(driver, "text", "Husky ojos azules y verdes");

		// Ahora nos desconectamos
		driver.navigate().to(URL + "/desconectarse");
		PO_View.checkElement(driver, "text", "Usuario desconectado");

	}

	// [Prueba20] Hacer una búsqueda escribiendo en el campo texto que no exista y
	// comprobar que se muestra la página que corresponde, con la lista de ofertas
	// vacía
	@Test
	public void P20() {
		PO_LoginView.fillForm(driver, "andrei@mail.com", "1234");
		PO_View.checkElement(driver, "text", "Ofertas");
		PO_View.checkElement(driver, "text", "Kit de supervivencia");
		PO_View.checkElement(driver, "text", "Pistola de bengalas modelo k67");
		PO_View.checkElement(driver, "text", "Tekken 6");
		PO_View.checkElement(driver, "text", "Fifa 19");
		PO_View.checkElement(driver, "text", "Husky ojos azules y verdes");
		PO_PrivateView.search(driver, "WOLOLOLOLOLO");
		ExpectedConditions.invisibilityOfElementLocated(By.partialLinkText("WOLOLOLO"));
		ExpectedConditions.invisibilityOfElementLocated(By.partialLinkText("Fifa 19"));
		ExpectedConditions.invisibilityOfElementLocated(By.partialLinkText("Kit"));

		// Ahora nos desconectamos
		driver.navigate().to(URL + "/desconectarse");
		PO_View.checkElement(driver, "text", "Usuario desconectado");

	}

	// [Prueba21] Hacer búsqueda escribiendo en el campo texto en minúscula o
	// mayúscula y comprobar que se muestra la página que corresponde, con la lista
	// de ofertas que contengan dicho texto, independientemente que
	// el título esté almacenado en minúsculas o mayúscula
	@Test
	public void P21() {
		PO_LoginView.fillForm(driver, "andrei@mail.com", "1234");
		PO_View.checkElement(driver, "text", "Ofertas");
		PO_View.checkElement(driver, "text", "Kit de supervivencia");
		PO_View.checkElement(driver, "text", "Pistola de bengalas modelo k67");
		PO_View.checkElement(driver, "text", "Tekken 6");
		PO_View.checkElement(driver, "text", "Fifa 19");
		PO_View.checkElement(driver, "text", "Husky ojos azules y verdes");
		PO_PrivateView.search(driver, "FiFa 19");
		PO_View.checkElement(driver, "text", "Fifa 19");
		ExpectedConditions.invisibilityOfElementLocated(By.partialLinkText("Kit"));

		// Ahora nos desconectamos
		driver.navigate().to(URL + "/desconectarse");
		PO_View.checkElement(driver, "text", "Usuario desconectado");

	}

	// [Prueba22] Sobre búsqueda determinada (a elección desarrollador), comprar una
	// oferta que deja un saldo positivo en el contador del comprobador. Y comprobar
	// que el contador se actualiza correctamente en la vista del comprador
	@Test
	public void P22() {

		PO_LoginView.fillForm(driver, "andrei@mail.com", "1234");
		PO_View.checkElement(driver, "text", "Ofertas");

		PO_PrivateView.search(driver, "Kit");

		driver.findElement(By.xpath(
				"(.//*[normalize-space(text()) and normalize-space(.)='Kit de supervivencia'])[1]/following::td[2]"))
				.click();
		driver.findElement(By.linkText("Comprar")).click();

		WebElement money = driver.findElement(By.id("money"));
		assertTrue(money.getText().equals("20€"));

		PO_View.checkElement(driver, "text", "Oferta comprada con exito");
		// Ahora nos desconectamos
		driver.navigate().to(URL + "/desconectarse");
		PO_View.checkElement(driver, "text", "Usuario desconectado");

	}

	// [Prueba23]Sobre búsqueda determinada (a elección de desarrollador), comprar
	// una oferta que deja un saldo 0 en el contador del comprobador. Y comprobar
	// que el contador se actualiza correctamente en la vista del comprador
	@Test
	public void P23() {

		PO_LoginView.fillForm(driver, "andrei@mail.com", "1234");
		PO_View.checkElement(driver, "text", "Ofertas");

		PO_PrivateView.search(driver, "Camino a la decima");

		driver.findElement(By.xpath(
				"(.//*[normalize-space(text()) and normalize-space(.)='Camino a la decima'])[1]/following::td[2]"))
				.click();
		driver.findElement(By.linkText("Comprar")).click();

		WebElement money = driver.findElement(By.id("money"));
		assertTrue(money.getText().equals("0€"));

		PO_View.checkElement(driver, "text", "Oferta comprada con exito");
		// Ahora nos desconectamos
		driver.navigate().to(URL + "/desconectarse");
		PO_View.checkElement(driver, "text", "Usuario desconectado");

	}

	// [Prueba24]Sobre una búsqueda determinada (a elección de desarrollador),
	// intentar comprar una oferta que esté por encima de saldo disponible del
	// comprador. Y comprobar que se muestra el mensaje de saldo no
	// suficiente
	@Test
	public void P24() {

		PO_LoginView.fillForm(driver, "andrei@mail.com", "1234");
		PO_View.checkElement(driver, "text", "Ofertas");

		PO_PrivateView.search(driver, "Husky ojos azules y verdes");

		driver.findElement(By.xpath(
				"(.//*[normalize-space(text()) and normalize-space(.)='Husky ojos azules y verdes'])[1]/following::td[2]"))
				.click();
		driver.findElement(By.linkText("Comprar")).click();

		PO_View.checkElement(driver, "text", "No tienes suficiente dinero para comprar esta oferta");
		// Ahora nos desconectamos
		driver.navigate().to(URL + "/desconectarse");
		PO_View.checkElement(driver, "text", "Usuario desconectado");

	}

	// [Prueba25]Ir a la opción de ofertas del usuario y mostrar la lista. Comprobar
	// que aparecen las ofertas que deben aparecer
	@Test
	public void P25() {
		PO_LoginView.fillForm(driver, "andrei@mail.com", "1234");
		PO_View.checkElement(driver, "text", "Ofertas disponibles");
		driver.navigate().to(URL + "/usr/listarCompradas");
		// Comprobar los 5 usuarios
		PO_View.checkElement(driver, "text", "Camino a la undecima");
		PO_View.checkElement(driver, "text", "La 12+1");

		// Ahora nos desconectamos
		driver.navigate().to(URL + "/desconectarse");
		PO_View.checkElement(driver, "text", "Usuario desconectado");
	}

	// [Prueba26]Al crear una oferta marcar dicha oferta como destacada y a
	// continuación comprobar: i) que aparece en el listado de ofertas destacadas
	// para los usuarios y que el saldo del usuario se actualizate en la
	// vista del ofertante (-20).

	@Test
	public void P26() {
		// Vamos al formulario de registro
		PO_LoginView.fillForm(driver, "rosario@mail.com", "1234");
		PO_View.checkElement(driver, "text", "Ofertas");
		driver.navigate().to(URL + "/usr/agregarOferta");
		// Rellenamos el formulario.
		PO_OfferView.fillForm(driver, "Esto es una oferta destacada", "Esto es una descripcion", "12.0", "true");
		PO_View.checkElement(driver, "text", "Oferta insertada con exito");
		WebElement money = driver.findElement(By.id("money"));
		assertTrue(money.getText().equals("0€"));

		// Ahora nos desconectamos
		driver.navigate().to(URL + "/desconectarse");
		PO_View.checkElement(driver, "text", "Usuario desconectado");

		PO_LoginView.fillForm(driver, "david@mail.com", "1234");
		PO_View.checkElement(driver, "text", "Ofertas disponibles");
		PO_View.checkElement(driver, "text", "Esto es una oferta destacada");
		driver.navigate().to(URL + "/desconectarse");
		PO_View.checkElement(driver, "text", "Usuario desconectado");
	}

	// [Prueba27]Sobre el listado de ofertas de un suario con más de 20 euros de
	// saldo, char en el enlace Destacada y a continuación comprobar: i) que aparece
	// en el listado de ofertas destacadas para losusuariosy que
	// el ldo del usuario se actualiza damente en la vista del ofertante (-20)
	@Test
	public void P27() {
		PO_LoginView.fillForm(driver, "andrei@mail.com", "1234");
		PO_View.checkElement(driver, "text", "Ofertas disponibles");
		driver.navigate().to(URL + "/usr/listarCreadas");
		// Comprobar los 5 usuarios
		PO_View.checkElement(driver, "text", "Ford Focus tdi");
		PO_View.checkElement(driver, "text", "Renault Clio tdi");
		PO_View.checkElement(driver, "text", "Sara picaso");

		driver.findElement(
				By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Ford Focus tdi'])[1]/following::td[2]"))
				.click();
		driver.findElement(By.linkText("Marcar como destacada")).click();
		PO_View.checkElement(driver, "text", "Oferta destacada con exito");
		driver.navigate().to(URL + "/home");
		WebElement money = driver.findElement(By.id("money"));
		assertTrue(money.getText().equals("80€"));
		// Ahora nos desconectamos
		driver.navigate().to(URL + "/desconectarse");
		PO_View.checkElement(driver, "text", "Usuario desconectado");

		PO_LoginView.fillForm(driver, "david@mail.com", "1234");
		PO_View.checkElement(driver, "text", "Ofertas disponibles");
		PO_View.checkElement(driver, "text", "Ford Focus tdi");
		driver.navigate().to(URL + "/desconectarse");
		PO_View.checkElement(driver, "text", "Usuario desconectado");
	}

	// [Prueba28]Sobre el listado de ofertas de un suario con menos de 20 euros de
	// saldo, inchar en el enlace Destacada y a continuación comprobar que se
	// muestra el mensaje de saldo no suficiente.

	@Test
	public void P28() {
		PO_LoginView.fillForm(driver, "raul@mail.com", "1234");
		PO_View.checkElement(driver, "text", "Ofertas disponibles");
		driver.navigate().to(URL + "/usr/listarCreadas");

		driver.findElement(
				By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='La 12+1'])[1]/following::td[2]"))
				.click();
		driver.findElement(By.linkText("Marcar como destacada")).click();
		PO_View.checkElement(driver, "text", "Para marcar la oferta como destacada necesitas al menos 20€");
		// Ahora nos desconectamos
		driver.navigate().to(URL + "/desconectarse");
		PO_View.checkElement(driver, "text", "Usuario desconectado");

	}

	/** TESTS API **/

	// [Prueba29] Inicio de sesión con datos válidos.
	@Test
	public void P29() {
		driver.navigate().to(URL + "/cliente.html?w=login");
		// Rellenamos el formulario
		PO_LoginView.fillFormApi(driver, "andrei@mail.com", "1234");
		// COmprobamos que entramos en la pagina privada del usuario
		PO_View.checkElement(driver, "text","Título");
	}
	// [Prueba30] Inicio de sesión con datos inválidos (email existente, pero
	// contraseña incorrecta
	@Test
	public void P30() {
		driver.navigate().to(URL + "/cliente.html?w=login");
		// Rellenamos el formulario
		PO_LoginView.fillFormApi(driver, "andrei@mail.com", "123sada4");
		// COmprobamos que entramos en la pagina privada del usuario
		PO_View.checkElement(driver, "text","Usuario no encontrado");
	}
	// [Prueba31] Inicio sesión con datos inválidos (campo email o contraseña vacíos
	@Test
	public void P31() {
		driver.navigate().to(URL + "/cliente.html?w=login");
		// Rellenamos el formulario
		PO_LoginView.fillFormApi(driver, "", "");
		// COmprobamos que entramos en la pagina privada del usuario
		PO_View.checkElement(driver, "text","Usuario no encontrado");
	}
	// [Prueba32] Mostrar l listado de ofertas disponibles y comprobar que se
	// muestran todas las que existen, menos las del usuario identificado
	@Test
	public void P32() {
		driver.navigate().to(URL + "/cliente.html?w=login");
		// Rellenamos el formulario
		PO_LoginView.fillFormApi(driver, "andrei@mail.com", "1234");
		// COmprobamos que entramos en la pagina privada del usuario
		PO_View.checkElement(driver, "text", "Kit de supervivencia");
		PO_View.checkElement(driver, "text", "Pistola de bengalas modelo k67");
		PO_View.checkElement(driver, "text", "Tekken 6");
		PO_View.checkElement(driver, "text", "Fifa 19");

		ExpectedConditions.invisibilityOfElementLocated(By.partialLinkText("Ford"));
		ExpectedConditions.invisibilityOfElementLocated(By.partialLinkText("Clio"));
		ExpectedConditions.invisibilityOfElementLocated(By.partialLinkText("Sara"));
		
	}
	
	// [Prueba33] Sobre una búsqueda determinada de ofertas (a elección de
	// desarrollador), enviar un mensaje a una oferta concreta. Se abriría dicha
	// conversación por primera vez. Comprobar que el mensaje aparece en el
	// listado de mensajes
	
	@Test
	public void P33() {
		driver.navigate().to(URL + "/cliente.html?w=login");
		// Rellenamos el formulario
		PO_LoginView.fillFormApi(driver, "andrei@mail.com", "1234");
		// COmprobamos que entramos en la pagina privada del usuario
		PO_View.checkElement(driver, "text", "Gato");
		driver.findElement(
				By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Kit de supervivencia'])[1]/following::td[2]"))
				.click();
		driver.findElement(By.linkText("Ver")).click();
		SeleniumUtils.esperarSegundos(driver, 3);
		PO_View.checkElement(driver, "text", "Autor");
		SeleniumUtils.esperarSegundos(driver, 3);
		PO_PrivateView.enviarMensaje(driver, "Hola que tal?");
		SeleniumUtils.esperarSegundos(driver, 5);
		PO_View.checkElement(driver, "text", "Hola");
		
		
	}
	// [Prueba34] Sobre el listado de conversaciones enviar un mensaje a una
	// conversación ya abierta. Comprobar que el mensaje aparece en el listado de
	// mensajes
	@Test
	public void P34() {
		driver.navigate().to(URL + "/cliente.html?w=login");
		// Rellenamos el formulario
		PO_LoginView.fillFormApi(driver, "andrei@mail.com", "1234");
		// COmprobamos que entramos en la pagina privada del usuario
		PO_View.checkElement(driver, "text", "Gato");
		driver.findElement(
				By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Kit de supervivencia'])[1]/following::td[2]"))
				.click();
		driver.findElement(By.linkText("Ver")).click();
		SeleniumUtils.esperarSegundos(driver, 3);
		PO_View.checkElement(driver, "text", "Autor");
		driver.navigate().to(URL + "/cliente.html?w=ofertas");
		driver.findElement(By.linkText("Conversaciones usuario")).click();
		SeleniumUtils.esperarSegundos(driver, 20);
		driver.findElement(
				By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Kit de supervivencia'])[1]/following::td[2]"))
				.click();
		driver.findElement(By.linkText("Entrar conversacion")).click();
		SeleniumUtils.esperarSegundos(driver, 3);
		driver.findElement(By.linkText("Ver")).click();
		SeleniumUtils.esperarSegundos(driver, 3);
		PO_View.checkElement(driver, "text", "Autor");
		SeleniumUtils.esperarSegundos(driver, 3);
		PO_PrivateView.enviarMensaje(driver, "Hola que tal?");
		SeleniumUtils.esperarSegundos(driver, 5);
		PO_View.checkElement(driver, "text", "Hola");		
	}
	// [Prueba35] Mostrar el listado de conversaciones ya abiertas. Comprobar que el
	// listado contiene las conversaciones que deben ser
	@Test
	public void P35() {
		driver.navigate().to(URL + "/cliente.html?w=login");
		// Rellenamos el formulario
		PO_LoginView.fillFormApi(driver, "andrei@mail.com", "1234");
		// COmprobamos que entramos en la pagina privada del usuario
		PO_View.checkElement(driver, "text", "Gato");
		driver.findElement(
				By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Kit de supervivencia'])[1]/following::td[2]"))
				.click();
		driver.findElement(By.linkText("Ver")).click();
		SeleniumUtils.esperarSegundos(driver, 3);
		PO_View.checkElement(driver, "text", "Autor");
		driver.navigate().to(URL + "/cliente.html?w=ofertas");
		driver.findElement(By.linkText("Conversaciones usuario")).click();
		SeleniumUtils.esperarSegundos(driver, 20);
		PO_View.checkElement(driver, "text", "Kit");	
	}
	// [Prueba36] Sobre el listado de conversaciones ya abiertas. Pinchar el enlace
	// Eliminar de la primera y comprobar que el listado se actualiza correctamente.
	@Test
	public void P36() {
		driver.navigate().to(URL + "/cliente.html?w=login");
		// Rellenamos el formulario
		PO_LoginView.fillFormApi(driver, "andrei@mail.com", "1234");
		// COmprobamos que entramos en la pagina privada del usuario
		PO_View.checkElement(driver, "text", "Gato");
		driver.findElement(
				By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Kit de supervivencia'])[1]/following::td[2]"))
				.click();
		driver.findElement(By.linkText("Ver")).click();
		SeleniumUtils.esperarSegundos(driver, 3);
		PO_View.checkElement(driver, "text", "Autor");
		driver.navigate().to(URL + "/cliente.html?w=ofertas");
		PO_View.checkElement(driver, "text", "Gato");
		driver.findElement(
				By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Tekken 6'])[1]/following::td[2]"))
				.click();
		driver.findElement(By.linkText("Ver")).click();
		SeleniumUtils.esperarSegundos(driver, 3);
		PO_View.checkElement(driver, "text", "Autor");
		driver.navigate().to(URL + "/cliente.html?w=ofertas");
		driver.findElement(By.linkText("Conversaciones usuario")).click();
		SeleniumUtils.esperarSegundos(driver, 20);
		PO_View.checkElement(driver, "text", "Kit");
		driver.findElement(
				By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='andrei@mail.com'])[1]/following::td[2]"))
				.click();
		driver.findElement(By.linkText("Eliminar conversacion")).click();
		SeleniumUtils.esperarSegundos(driver, 5);
	}
	// [Prueba37] Sobre el listado de conversaciones ya abiertas. Pinchar el enlace
	// Eliminar de la última y comprobar que el listado se actualiza correctamente.
	@Test
	public void P37() {
		driver.navigate().to(URL + "/cliente.html?w=login");
		// Rellenamos el formulario
		PO_LoginView.fillFormApi(driver, "andrei@mail.com", "1234");
		// COmprobamos que entramos en la pagina privada del usuario
		PO_View.checkElement(driver, "text", "Gato");
		driver.findElement(
				By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Kit de supervivencia'])[1]/following::td[2]"))
				.click();
		driver.findElement(By.linkText("Ver")).click();
		SeleniumUtils.esperarSegundos(driver, 3);
		PO_View.checkElement(driver, "text", "Autor");
		driver.navigate().to(URL + "/cliente.html?w=ofertas");
		PO_View.checkElement(driver, "text", "Gato");
		driver.findElement(
				By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Tekken 6'])[1]/following::td[2]"))
				.click();
		driver.findElement(By.linkText("Ver")).click();
		SeleniumUtils.esperarSegundos(driver, 3);
		PO_View.checkElement(driver, "text", "Autor");
		driver.navigate().to(URL + "/cliente.html?w=ofertas");
		driver.findElement(By.linkText("Conversaciones usuario")).click();
		SeleniumUtils.esperarSegundos(driver, 20);
		PO_View.checkElement(driver, "text", "Kit");
		driver.findElement(
				By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Kit de supervivencia'])[1]/following::td[2]"))
				.click();
		driver.findElement(By.linkText("Eliminar conversacion")).click();
	}
}
