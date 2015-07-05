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

/**
 * Test: Visualizing a geometry: - Starting from Login, click on "Mesh Creator"
 * -> "Geometries" -> and the first geometry. - The panel on the right must
 * display the geometry, according to the screenshot that you will create
 * yourself.
 *
 * TODO: isGeometryDisplayedCorrecly() is not fully implemented. Test verifies
 * correct display of the mesh viewer panel
 *
 * @author Eisbrenner
 *
 */
public class VisualizingAGeometry {

	private LoginPage mLoginPage;
	private ProjectPage mProjectPage;

	@After
	public void cleanup() throws Exception {
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
	public void testVisualizeAGeometry() throws Exception {
		String projectName = CommonParameters.NAME_PROJECT_2;
		// check preconditions
		String errorMessage = String.format(CommonParameters.PRECONDITION_NOT_MET, projectName,
				CommonParameters.DOES_NOT_EXIST);
		assertTrue(errorMessage, mProjectPage.doesProjectExist(projectName));

		// test actions
		mProjectPage.clickProject(projectName);
		mProjectPage.clickMeshCreator();
		mProjectPage.clickGeometry(CommonParameters.GEOMETRY_NAME);

		// check
		errorMessage = String.format("Geometry is not displayed correctly");
		// TODO: isGeometryDisplayedCorrecly() is not fully implemented. Test
		// verifies correct display of the mesh viewer panel
		assertTrue(errorMessage, mProjectPage.isGeometryDisplayedCorrectly(projectName));

	}

}
