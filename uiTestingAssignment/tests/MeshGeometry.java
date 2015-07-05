package uiTestingAssignment.tests;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.PageFactory;

import uiTestingAssignment.pages.LoginPage;
import uiTestingAssignment.pages.ProjectPage;
import uiTestingAssignment.pages.ProjectPage.Button;
import uiTestingAssignment.util.CommonParameters;

/**
 * Test: Mesh Geometry: - Starting from "Visualizing a Geometry", click on
 * "Mesh Geometry". - In the Mesh Operation Panel press "Start" - In the
 * confirmation pop up , press "yes" - The "Job Status" panel in the lower left
 * corner should display that a new operation started. After maximum 5 minutes,
 * it should display "Finished".
 *
 * @author Eisbrenner
 *
 */
public class MeshGeometry {

	private static final int TIMEOUT_JOB_STATUS_SEC = 300;

	private LoginPage mLoginPage;
	private ProjectPage mProjectPage;

	@After
	public void cleanup() throws Exception {
		mProjectPage.deleteMeshOperationJob(CommonParameters.NAME_MESH_OPERATION);
		mLoginPage.teardown();
	}

	@Before
	public void setUp() throws Exception {
		mLoginPage = PageFactory.initElements(new FirefoxDriver(), LoginPage.class);
		mLoginPage.open();
		mProjectPage = mLoginPage.loginUsingCorrectCredentials(CommonParameters.USER_NAME_CORRECT,
				CommonParameters.PASSWORD_CORRECT);
		String projectName = CommonParameters.NAME_PROJECT_2;

		// check preconditions
		String errorMessage = String.format(CommonParameters.PRECONDITION_NOT_MET, projectName,
				CommonParameters.DOES_NOT_EXIST);
		assertTrue(errorMessage, mProjectPage.doesProjectExist(projectName));

		// pre-test actions
		mProjectPage.clickProject(projectName);
		mProjectPage.clickMeshCreator();
		mProjectPage.clickGeometry(CommonParameters.GEOMETRY_NAME);
		// make sure no Operation with a name similar to the one we create
		// exists
		mProjectPage.deleteMeshOperationJob(CommonParameters.NAME_MESH_OPERATION);
	}

	@Test
	public void testMeshAGeometry() throws Exception {
		// test actions
		mProjectPage.clickButton(Button.MESH_GEOMETRY);
		mProjectPage.waitForMeshOperationPanelToShow(CommonParameters.TIMEOUT);
		mProjectPage.clickButton(Button.MESH_OPERATION_START);
		mProjectPage.clickButton(Button.MESH_OPERATION_CONFIRMATION_YES);
		// check
		assertTrue("Operation should have started, but didn't",
				mProjectPage.meshOperationStarted(CommonParameters.NAME_MESH_OPERATION));
		assertTrue("Operation should have finished within " + TIMEOUT_JOB_STATUS_SEC + " seconds, but didn't",
				mProjectPage.meshOperationFinished(CommonParameters.NAME_MESH_OPERATION, TIMEOUT_JOB_STATUS_SEC));
	}

}
