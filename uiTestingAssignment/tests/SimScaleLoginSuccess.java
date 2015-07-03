package uiTestingAssignment.tests;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.PageFactory;

import uiTestingAssignment.pages.LoginPage;
import uiTestingAssignment.pages.ProjectPage;
import uiTestingAssignment.util.CommonParameters;
import uiTestingAssignment.util.LoopHelper;
import uiTestingAssignment.util.LoopHelper.LoopTimeoutException;

/**
 * Test: Successful logging in: -The login panel must disappear and the list of
 * project must be populated with a few projects.
 * 
 * @author Eisbrenner
 *
 */
public class SimScaleLoginSuccess {

	private static final int TIMEOUT_LOGIN = 20;

	private LoginPage mLoginPage;
	private ProjectPage mProjectPage;

	@Before
	public void setUp() throws Exception {
		mLoginPage = PageFactory.initElements(new FirefoxDriver(), LoginPage.class);
	}

	@After
	public void tearDown() throws Exception {
		mLoginPage.teardown();
	}

	@Test
	public void testSimScaleLoginSuccess() throws Exception {
		mLoginPage.open();
		mProjectPage = mLoginPage.loginUsingCorrectCredentials(CommonParameters.USER_NAME_CORRECT,
				CommonParameters.PASSWORD_CORRECT);
		assertTrue("Login failed , but should have succeeded!", didLoginSucceed());
	}

	private Boolean areTestProjectElementsPresent() {
		if (mProjectPage.doesProjectExist(CommonParameters.NAME_PROJECT_1)
				&& mProjectPage.doesProjectExist(CommonParameters.NAME_PROJECT_2)
				&& mProjectPage.doesProjectExist(CommonParameters.NAME_PROJECT_3)) {
			return true;
		}
		return false;
	}

	/**
	 * Requirements for a successful login are: 1. The login panel must
	 * disappear 2. Several projects populate the project list
	 *
	 * @return boolean - true, if requirements are fulfilled, false otherwise
	 */
	private boolean didLoginSucceed() {
		LoopHelper<Boolean> loginPanelLoop = new LoopHelper<Boolean>(TIMEOUT_LOGIN, true,
				new LoopHelper.ICheck<Boolean>() {
					@Override
					public Boolean getCurrentState() {
						return null == mLoginPage.getLoginPanelText();
					}
				});
		LoopHelper<Boolean> elementsPresentLoop = new LoopHelper<Boolean>(TIMEOUT_LOGIN, true,
				new LoopHelper.ICheck<Boolean>() {
					@Override
					public Boolean getCurrentState() {
						return areTestProjectElementsPresent();
					}

				});
		try {
			loginPanelLoop.run();
			elementsPresentLoop.run();
		} catch (LoopTimeoutException e) {
			System.err.println("Login failed: " + e.getMessage());
			return false;
		}
		return true;
	}
}
