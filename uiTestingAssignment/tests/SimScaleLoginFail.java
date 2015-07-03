package uiTestingAssignment.tests;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.PageFactory;

import uiTestingAssignment.pages.LoginPage;
import uiTestingAssignment.util.CommonParameters;
import uiTestingAssignment.util.LoopHelper;
import uiTestingAssignment.util.LoopHelper.LoopTimeoutException;

/**
 * Test: Failed logging attempt, e.g., invalid password: The login panel must
 * remain shown and the corresponding error message must be displayed to the
 * user.
 *
 * @author Eisbrenner
 *
 */
public class SimScaleLoginFail {

	private static final int TIMEOUT_LOGIN = 20;

	private LoginPage mLoginPage;

	@Before
	public void setUp() throws Exception {
		mLoginPage = PageFactory.initElements(new FirefoxDriver(), LoginPage.class);
	}

	@After
	public void tearDown() throws Exception {
		mLoginPage.teardown();
	}

	@Test
	public void testSimScaleLoginFailCredentialsWrong() throws Exception {
		mLoginPage.open();
		mLoginPage.loginUsingWrongCredentials(CommonParameters.USER_NAME_WRONG, CommonParameters.PASSWORD_WRONG);
		assertTrue("Login succeeded , but should have failed!", didLoginFail());
	}

	@Test
	public void testSimScaleLoginFailWrongPassword() throws Exception {
		mLoginPage.open();
		mLoginPage.loginUsingWrongCredentials(CommonParameters.USER_NAME_CORRECT, CommonParameters.PASSWORD_WRONG);
		assertTrue("Login succeeded , but should have failed!", didLoginFail());
	}

	@Test
	public void testSimScaleLoginFailWrongUser() throws Exception {
		mLoginPage.open();
		mLoginPage.loginUsingWrongCredentials(CommonParameters.USER_NAME_WRONG, CommonParameters.PASSWORD_CORRECT);
		assertTrue("Login succeeded , but should have failed!", didLoginFail());
	}

	/**
	 * Requirements for a failed login are: 1. The login panel must remain shown
	 * 2. The the corresponding error message must be displayed
	 *
	 * @return boolean - true, if requirements are fulfilled, false otherwise
	 */
	private boolean didLoginFail() {
		LoopHelper<Boolean> LoginPanelLoop = new LoopHelper<Boolean>(TIMEOUT_LOGIN, true,
				new LoopHelper.ICheck<Boolean>() {
					@Override
					public Boolean getCurrentState() {
						return mLoginPage.getLoginPanelText().equals(CommonParameters.LOGIN_WELCOME_MESSAGE);
					}
				});
		LoopHelper<Boolean> ErrorMessageLoop = new LoopHelper<Boolean>(TIMEOUT_LOGIN, true,
				new LoopHelper.ICheck<Boolean>() {
					@Override
					public Boolean getCurrentState() {
						return mLoginPage.getErrorMessage().equals(CommonParameters.LOGIN_ERROR_MESSAGE);
					}
				});
		try {
			LoginPanelLoop.run();
			ErrorMessageLoop.run();
		} catch (LoopTimeoutException e) {
			System.err.println("Login failed: " + e.getMessage());
			return false;
		}
		return true;
	}
}
