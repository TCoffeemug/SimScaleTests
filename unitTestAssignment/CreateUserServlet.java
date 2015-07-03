package unitTestAssignment;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CreateUserServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private TestHelper mTestHelper;

	private static String convertMailToValidUri(String email) {
		final String regex = "(.*)(@.*)";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(email);
		String localPrefix = "";
		String atAndDomainSuffix = "";
		while (matcher.find()) {
			localPrefix = matcher.group(1);
			atAndDomainSuffix = matcher.group(2);
			try {
				localPrefix = URLEncoder.encode(localPrefix, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				System.err.println("Something went wrong during encoding: " + e.getMessage());
				e.printStackTrace();
			}
		}
		return localPrefix + atAndDomainSuffix;
	}

	/**
	 * Returns the link that the user has to click on to activate her account.
	 *
	 * @param email
	 *            The email of the user (valid)
	 * @param token
	 *            activation token (system generated)
	 * @param invite
	 *            invitation token (system generated)
	 * @return account activation link processed by
	 *         {@link AccountActivationServlet}
	 */
	private static String getActivationLink(String email, String token, String invite) {
		String activationLink = ServerConstants.SERVER_NAME_PRETTY + "AccountActivation?email="
				+ convertMailToValidUri(email) + "&token=" + token;
		if (invite != null) {
			activationLink = activationLink + "&invite=" + invite;
		}
		return activationLink;
	}

	/// something else ///

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// some magic here...

		mTestHelper.setActivationLink(
				getActivationLink(mTestHelper.getEmail(), mTestHelper.activationToken(), mTestHelper.inviteToken()));

		// some magic there...

	}

	public void createTestHelper(String email, String activationToken, String inviteToken) {
		mTestHelper = new TestHelper(email, activationToken, inviteToken);
	}

	public TestHelper getTestHelper() {
		return mTestHelper;
	}

}
