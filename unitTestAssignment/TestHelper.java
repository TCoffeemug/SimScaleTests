package unitTestAssignment;

public class TestHelper {

	private String mEmail;
	private String mActivationToken;
	private String mInviteToken;
	private String mActivationLink;

	public TestHelper(String email, String activationToken, String inviteToken) {
		mEmail = email;
		mActivationToken = activationToken;
		mInviteToken = inviteToken;
	}

	public String activationToken() {
		return mActivationToken;
	}

	public String getActivationLink() {
		return mActivationLink;
	}

	public String getEmail() {
		return mEmail;
	}

	public String inviteToken() {
		return mInviteToken;
	}

	public void setActivationLink(String activationLink) {
		mActivationLink = activationLink;
	}

}
