package tests.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class PO_OfferView extends PO_NavView {
	static public void fillForm(WebDriver driver, String titulo, String descripcion, String price, String destacada) {
		WebElement title1 = driver.findElement(By.name("titulo"));
		title1.click();
		title1.clear();
		title1.sendKeys(titulo);
		WebElement descrip = driver.findElement(By.name("descripcion"));
		descrip.click();
		descrip.clear();
		descrip.sendKeys(descripcion);
		WebElement pric = driver.findElement(By.name("precio"));
		pric.click();
		pric.clear();
		pric.sendKeys(price);
		Select dropdown = new Select(driver.findElement(By.name("destacada")));
		 dropdown.selectByValue(destacada);
		By boton = By.className("btn");
		driver.findElement(boton).click();
	}
}
