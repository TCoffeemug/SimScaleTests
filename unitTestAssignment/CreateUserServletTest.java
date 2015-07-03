package unitTestAssignment;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.URL;

import javax.servlet.ServletException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CreateUserServletTest {

	private final String VALID_BUT_STRANGE_EMAIL = "\"very.(),:;<>[]\\\".VERY.\\\"very@\\\\ \\\"very\\\".unusual\"@strange.example.com";
	private final String ACTIVATION_TOKEN = "1234";
	private final String INVITE_TOKEN = "5678";

	private CreateUserServlet mUserServlet;

	@Before
	public void setUp() throws Exception {
		mUserServlet = new CreateUserServlet();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() throws ServletException, IOException {

		mUserServlet.createTestHelper(VALID_BUT_STRANGE_EMAIL, ACTIVATION_TOKEN, INVITE_TOKEN);
		mUserServlet.doGet(null, null);

		try {
			final URL url = new URL(mUserServlet.getTestHelper().getActivationLink());
			assertTrue("http".equals(url.getProtocol()));
		} catch (Exception e1) {
			fail();
		}

	}

}
