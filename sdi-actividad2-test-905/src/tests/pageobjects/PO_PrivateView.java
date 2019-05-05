package tests.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class PO_PrivateView extends PO_NavView {

	static public void search(WebDriver driver, String search) {
		WebElement input = driver.findElement(By.name("busqueda"));
		input.click();
		input.clear();
		input.sendKeys(search);

		By boton = By.className("btn");
		driver.findElement(boton).click();
	}
	
	static public void enviarMensaje(WebDriver driver, String search) {
		WebElement input = driver.findElement(By.name("texto"));
		input.click();
		input.clear();
		input.sendKeys(search);

		By boton = By.id("boton-message");
		driver.findElement(boton).click();
		driver.findElement(boton).click();
		driver.findElement(boton).click();
		driver.findElement(boton).click();
		driver.findElement(boton).click();
	}

}