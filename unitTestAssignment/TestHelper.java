package unitTestAssignment;

public class TestHelper {

	private String mEmail;
	private String mActivationToken;
	private String mInviteToken;
	private String mActivationLink;

	public TestHelper(String activationLink) {
		mActivationLink = activationLink;
	}

	public String getActivationLink() {
		return mActivationLink;
	}

}
