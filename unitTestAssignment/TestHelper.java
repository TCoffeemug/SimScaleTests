package unitTestAssignment;

/**
 *
 * to help testing the private method in CreateUserServlet
 *
 * @author Eisbrenner
 *
 */
public class TestHelper {

	private String mActivationLink;

	public TestHelper(String activationLink) {
		mActivationLink = activationLink;
	}

	public String getActivationLink() {
		return mActivationLink;
	}

}
