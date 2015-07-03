package uiTestingAssignment.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Point;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.PageFactory;

import uiTestingAssignment.pages.LoginPage;
import uiTestingAssignment.pages.ProjectPage;
import uiTestingAssignment.util.CommonParameters;

/**
 *
 * Test: Rename a project - Starting from login, right-click on a project in the
 * left panel and choose rename. - The project must be displayed in the list of
 * projects in the same position, with the new name.
 *
 * @author Eisbrennner
 *
 */
public class RenameProject {

	private static final String PRECONDITION_NOT_MET = "Preconditions not met for %s: %s";
	private static final String POSTCONDITION_NOT_MET = "Postconditions not met for %s: %s";
	private static final String DOES_EXIST = "does exists, but shouldn't";
	private static final String DOES_NOT_EXIST = "does not exists, but shouldn";

	private LoginPage mLoginPage;
	private ProjectPage mProjectPage;

	@After
	public void cleanup() throws Exception {
		mProjectPage.changeProjectName(CommonParameters.NAME_PROJECT_RENAMED, CommonParameters.NAME_PROJECT_2);
		mLoginPage.teardown();
	}

	@Before
	public void setUp() throws Exception {
		mLoginPage = PageFactory.initElements(new FirefoxDriver(), LoginPage.class);
		mLoginPage.open();
		mProjectPage = mLoginPage.loginUsingCorrectCredentials(CommonParameters.USER_NAME_CORRECT,
				CommonParameters.PASSWORD_CORRECT);
	}

	@Test
	public void testRenameProject() throws Exception {
		String oldName = CommonParameters.NAME_PROJECT_2;
		String newName = CommonParameters.NAME_PROJECT_RENAMED;
		// check preconditions
		String errorMessage = String.format(PRECONDITION_NOT_MET, oldName, DOES_NOT_EXIST);
		assertTrue(errorMessage, mProjectPage.doesProjectExist(oldName));

		errorMessage = String.format(PRECONDITION_NOT_MET, newName, DOES_EXIST);
		assertFalse(errorMessage, mProjectPage.doesProjectExist(newName));

		Point projectPositionBefore = mProjectPage.getProjectPosition(oldName);

		// change name
		mProjectPage.changeProjectName(oldName, newName);

		// post check
		errorMessage = String.format(POSTCONDITION_NOT_MET, oldName, DOES_EXIST);
		assertFalse(errorMessage, mProjectPage.doesProjectExist(oldName));

		errorMessage = String.format(POSTCONDITION_NOT_MET, newName, DOES_NOT_EXIST);
		assertTrue(errorMessage, mProjectPage.doesProjectExist(newName));

		Point projectPositionAfter = mProjectPage.getProjectPosition(newName);
		errorMessage = String.format(POSTCONDITION_NOT_MET + "", newName, "has different Position");
		assertTrue(errorMessage, mProjectPage.isSamePosition(projectPositionBefore, projectPositionAfter));
	}

}
