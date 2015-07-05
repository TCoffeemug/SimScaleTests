package uiTestingAssignment.pages;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import uiTestingAssignment.util.CommonParameters;

/**
 * parent class for all page classes
 *
 * @author Eisbrenner
 *
 */
public class WebPage {

	protected WebDriver mDriver;
	private boolean mAcceptNextAlert = true;

	public WebPage(WebDriver driver) {
		mDriver = driver;
	}

	public void clickElement(WebElement element) {
		new WebDriverWait(mDriver, CommonParameters.TIMEOUT).until(ExpectedConditions.visibilityOf(element)).click();
	}

	public String closeAlertAndGetItsText() {
		try {
			Alert alert = mDriver.switchTo().alert();
			String alertText = alert.getText();
			if (mAcceptNextAlert) {
				alert.accept();
			} else {
				alert.dismiss();
			}
			return alertText;
		} finally {
			mAcceptNextAlert = true;
		}
	}

	public WebDriver getDriver() {
		return this.mDriver;
	}

	public boolean isAlertPresent() {
		try {
			mDriver.switchTo().alert();
			return true;
		} catch (NoAlertPresentException e) {
			return false;
		}
	}

	public boolean isElementPresent(By by) {
		try {
			mDriver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	public void printForDebug(String string) {
		System.out.println(string);
	}

	public void rightClick(WebElement project) {
		Actions clickAction = new Actions(mDriver);
		clickAction.moveToElement(project);
		clickAction.contextClick(project).build().perform();
	}

	public void teardown() {
		mDriver.quit();
	}

	public void timeout(int i) {
		mDriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

}
