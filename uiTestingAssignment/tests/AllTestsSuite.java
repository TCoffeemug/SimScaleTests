package uiTestingAssignment.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * JUnit test suite to run all test cases
 *
 * @author Eisbrenner
 *
 */
@RunWith(value = Suite.class)
@SuiteClasses(value = { SimScaleLoginSuccess.class, SimScaleLoginFail.class, RenameProject.class,
		VisualizingAGeometry.class, MeshGeometry.class })
public class AllTestsSuite {
}
