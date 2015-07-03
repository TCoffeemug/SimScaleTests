package uiTestingAssignment.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import uiTestingAssignment.util.CommonParameters;
import uiTestingAssignment.util.LoopHelper;
import uiTestingAssignment.util.LoopHelper.LoopTimeoutException;

public class LoginPage extends WebPage {

	private static final String LOGIN_URL = "https://platform.simscale.com//#authentication";
	private static final int LOGIN_PAGE_TIMEOUT = 20;

	private static final String XPATH_LOGIN_PANEL = "//legend[text()='" + CommonParameters.LOGIN_WELCOME_MESSAGE + "']";
	private static final String XPATH_ERROR_MESSAGE = "//p[text()='" + CommonParameters.LOGIN_ERROR_MESSAGE + "']";

	By loginPanelLocator = By.xpath(XPATH_LOGIN_PANEL);
	By errorMessageLocator = By.xpath(XPATH_ERROR_MESSAGE);

	@FindBy(id = "nameTextBox")
	private WebElement nameBox;

	@FindBy(id = "passwordTextBox")
	private WebElement passwordBox;

	@FindBy(id = "loginButton")
	private WebElement loginButton;

	@FindBy(xpath = XPATH_LOGIN_PANEL)
	private WebElement loginPanel;

	@FindBy(xpath = XPATH_ERROR_MESSAGE)
	private WebElement errorMessage;

	public LoginPage(WebDriver driver) {
		super(driver);
	}

	public void enterCredentialsAndSubmit(String userName, String password) {
		nameBox.clear();
		nameBox.sendKeys(userName);
		passwordBox.clear();
		passwordBox.sendKeys(password);
		loginButton.click();
	}

	/**
	 *
	 * @return - WebElement - ErrorMessage element or null if it does not exist
	 */
	public String getErrorMessage() {
		if (isElementPresent(errorMessageLocator)) {
			return errorMessage.getText();
		}
		return null;
	}

	/**
	 *
	 * @return - WebElement - LoginPanel element or null if it does not exist
	 */
	public String getLoginPanelText() {
		if (isElementPresent(loginPanelLocator)) {
			return loginPanel.getText();
		}
		return null;
	}

	public ProjectPage loginUsingCorrectCredentials(String userName, String password) {
		enterCredentialsAndSubmit(userName, password);
		return PageFactory.initElements(mDriver, ProjectPage.class);
	}

	public LoginPage loginUsingWrongCredentials(String userName, String password) {
		enterCredentialsAndSubmit(userName, password);
		return this;
	}

	public void open() {
		mDriver.get(LOGIN_URL);
		LoopHelper<Boolean> loginPageLoop = new LoopHelper<Boolean>(LOGIN_PAGE_TIMEOUT, true,
				new LoopHelper.ICheck<Boolean>() {
					@Override
					public Boolean getCurrentState() {
						return hasPageLoadedCorrectly();
					}
				});
		try {
			loginPageLoop.run();
		} catch (LoopTimeoutException e) {
			throw new IllegalStateException("Either this is not the login page or webElements are missing. " + e);
		}
	}

	private boolean hasPageLoadedCorrectly() {
		if (!LOGIN_URL.equals(mDriver.getCurrentUrl())) {
			return false;
		} else if (!isElementPresent(loginPanelLocator)) {
			return false;
		}
		return true;
	}

}
