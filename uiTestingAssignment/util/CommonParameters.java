package uiTestingAssignment.util;

/**
 * contains parameters commonly used in several tests
 *
 * @author Eisbrenner
 *
 */
public final class CommonParameters {

	public static final String USER_NAME_CORRECT = "thomas.eisbrenner@gmail.com";
	public static final String USER_NAME_WRONG = "wrong.user@name.com";
	public static final String PASSWORD_CORRECT = "iWant2WorkAtSimScale.";
	public static final String PASSWORD_WRONG = "12345678";

	public static final int TIMEOUT = 20;

	public static final String LOGIN_WELCOME_MESSAGE = "Welcome to SimScale!";
	public static final String LOGIN_ERROR_MESSAGE = "Authentication failed. Please check your credentials and try again.";

	public static final String NAME_PROJECT_1 = "Tutorial-01: Connecting rod stress analysis";
	public static final String NAME_PROJECT_2 = "Tutorial-02: Pipe junction flow";
	public static final String NAME_PROJECT_3 = "Tutorial-03: Differential casing thermal analysis";
	public static final String NAME_PROJECT_RENAMED = "Project-02: Pipe junction flow";
	public static final String NAME_MESH_OPERATION = "Operation 1";

	public static final String GEOMETRY_NAME = "CAD-pipe-junction_v1";

	public static final String PRECONDITION_NOT_MET = "Preconditions not met for %s: %s";
	public static final String POSTCONDITION_NOT_MET = "Postconditions not met for %s: %s";

	public static final String DOES_NOT_EXIST = "does not exists, but should";

}
