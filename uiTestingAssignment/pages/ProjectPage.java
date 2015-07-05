package uiTestingAssignment.pages;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;

import uiTestingAssignment.util.CommonParameters;
import uiTestingAssignment.util.LoopHelper;
import uiTestingAssignment.util.LoopHelper.LoopTimeoutException;

/**
 * Class representing the project page, providing its functionality
 *
 * @author Eisbrenner
 *
 */
public class ProjectPage extends WebPage {

	public enum Button {

		MESH_GEOMETRY("//a[@id='gwt-uid-104']"), MESH_OPERATION_START(
				"//a[@id='gwt-uid-231']"), MESH_OPERATION_CONFIRMATION_YES("Please confirm-Yes");

		private final String xpath;

		Button(String xpath) {
			this.xpath = xpath;
		}

		public String getXpath() {
			return xpath;
		}

	}

	public enum JobStatus {

		FINISHED("Finished"), STARTED("Started");

		private final String status;

		JobStatus(String status) {
			this.status = status;
		}

		public String getStatus() {
			return status;
		}

	}

	public static final String PROJECT_URL = "https://platform.simscale.com//#startAppFrame";
	public static final String X_PATH_PROJECTS = "//div[@class=\"GKXRPOKCCP projectCellList\"]";
	public static final String X_PATH_PROJECT_1 = "//span[text()='Tutorial-01: Connecting rod stress analysis']";
	public static final String X_PATH_PROJECT_2 = "//span[text()='Tutorial-02: Pipe junction flow']";

	public static final String X_PATH_PROJECT_3 = "//span[text()='Tutorial-03: Differential casing thermal analysis']";
	private static final String XPATH_RENAME_OPTION = "//td[@id=\"gwt-uid-27\"]";
	private static final String XPATH_RENAME_SUBMIT_BUTTON = "//a[@id='submitButton'][normalize-space()='Rename']";
	private static final String XPATH_RENAME_TEXTBOX = "//input[@class='gwt-TextBox']";
	private static final String XPATH_WORKSPACE_LEGEND = "//legend[@class=\"workspaceLegend\"]";
	private static final String XPATH_MESH_CREATOR = "//div[@id='gwt-uid-969']/div/div/div/ul/li[2]/a";
	private static final String XPATH_AVALABLE_GEOMETRIES = "//legend[text()='Available Geometries']";
	private static final String XPATH_GEOMETRIES_ROOT = "//div[@class='gwt-Tree componentTreePanel']/div[2]";
	private static final String XPATH_GEOMETRIES_ELEMENT = "//div[@class='gwt-Tree componentTreePanel']/div[2]/table";
	private static final String XPATH_MESH_OPERATION_PANEL = "//legend[text()='Mesh Operation']/..";
	private static final String XPATH_DIALOG_BOXES = "//div[@class='modal closableDialogBox in']";
	private static final String XPATH_MESH_OPERATION_JOBS = "//table[@class='jobStatusTable']/tbody/tr/td/div/div[3]/div/div[2]/div/div/table/tbody";

	private static final String XPATH_GEOMETRY_BASE_VIEWER = "//iframe[@class='gwt-Frame meshViewer']";

	private static final int PROJECT_PAGE_TIMEOUT = 10;
	private static final boolean DEBUG_MODE = false;

	@FindBy(xpath = X_PATH_PROJECTS)
	private WebElement projectContainer;

	@FindBys({ @FindBy(xpath = X_PATH_PROJECTS + "//*") })
	private List<WebElement> projects;

	@FindBys({ @FindBy(xpath = XPATH_RENAME_SUBMIT_BUTTON) })
	private List<WebElement> submitButtons;

	@FindBy(xpath = XPATH_RENAME_TEXTBOX)
	private WebElement renameTextBox;

	@FindBy(xpath = XPATH_RENAME_SUBMIT_BUTTON)
	private WebElement renameSubmitButton;

	@FindBy(xpath = XPATH_WORKSPACE_LEGEND)
	private WebElement workspaceLegend;

	@FindBy(xpath = XPATH_MESH_CREATOR)
	private WebElement meshCreator;

	@FindBy(xpath = XPATH_GEOMETRIES_ELEMENT)
	private WebElement geometriesRootElement;

	@FindBys({ @FindBy(xpath = XPATH_GEOMETRIES_ROOT + "//*") })
	private List<WebElement> geometries;

	@FindBys({ @FindBy(xpath = XPATH_DIALOG_BOXES) })
	private List<WebElement> dialogBoxes;

	public ProjectPage(WebDriver driver) {
		super(driver);
		LoopHelper<Boolean> projectPageLoop = new LoopHelper<Boolean>(PROJECT_PAGE_TIMEOUT, true,
				new LoopHelper.ICheck<Boolean>() {
					@Override
					public Boolean getCurrentState() {
						return hasPageLoadedCorrectly();
					}
				});
		try {
			projectPageLoop.run();
		} catch (LoopTimeoutException e) {
			throw new IllegalStateException("Either this is not the login page or webElements are missing. " + e);
		}
	}

	/**
	 * changes the name of a project
	 *
	 * @param originalName
	 *            - String - name of the project to rename
	 * @param newName
	 *            - String - name of the project after successful renaming
	 * @return boolean - true if changed successfully, false otherwise
	 */
	public void changeProjectName(String originalName, String newName) {
		WebElement project = getProjectFromProjectList(originalName);
		rightClick(project);
		clickMenuOption(project, "Rename");
		renameTextBox.sendKeys(newName);
		clickRenameSubmitButton(originalName);
		waitForProjectNameChange(newName);
	}

	public void clickButton(Button button) {
		WebElement buttonToClick = null;
		String xpath = button.getXpath();
		if (xpath.contains("//")) {
			buttonToClick = getButtonByXPath(xpath);
		} else {
			// the string in button xpath does not contain xpath, but search
			// parameters to find the correct xpath
			// e.g.: "Please confirm-Yes"
			final String regex = "(.*)-(.*)";
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(xpath);
			String dialog_clue = "";
			String button_clue = "";
			while (matcher.find()) {
				dialog_clue = matcher.group(1);
				button_clue = matcher.group(2);
				WebElement dialogBox = getDialogBox(dialog_clue);
				buttonToClick = getButtonFromDialog(dialogBox, button_clue);
			}
		}
		clickElement(buttonToClick);
	}

	public void clickGeometry(String geometryName) {
		clickElement(geometriesRootElement);
		WebElement geometry = getGeometryFromTree(geometryName);
		clickElement(geometry);
		waitForGeometryToLoad(PROJECT_PAGE_TIMEOUT);
	}

	public void clickMeshCreator() {
		clickElement(meshCreator);
		waitForMeshCreatorToShow(PROJECT_PAGE_TIMEOUT);
	}

	public void clickProject(String projectName) {
		WebElement project = getProjectFromProjectList(projectName);
		clickElement(project);
		waitForProjectPanelToShow(PROJECT_PAGE_TIMEOUT);
	}

	public void deleteMeshOperationJob(String name) {
		for (WebElement job : getMeshOperationJobs()) {
			if (name.equals(getJobName(job))) {
				debugMessage(job.findElement(By.xpath("td[5]")).getTagName());
				clickElement(job.findElement(By.xpath("td[5]")));
			}
		}
	}

	/**
	 *
	 * @param name
	 *            - String - name of project to look for
	 * @return boolean - true, if project is in project list. False otherwise
	 */
	public boolean doesProjectExist(String name) {
		if (projectContainer.getText().contains(name)) {
			return true;
		}
		return false;
	}

	public Point getProjectPosition(String projectName) {
		WebElement project = getProjectFromProjectList(projectName);
		return project.getLocation();
	}

	public boolean isGeometryDisplayedCorrectly(String projectName) {
		// TODO a check for the correct geometry has to be implemented here,
		// comparing 2 screenshots
		// for this assignment scope this is not a feasible thing to do, thus
		// the following check has to suffice for now
		LoopHelper<Boolean> geometryPanelLoop = new LoopHelper<Boolean>(25, true, new LoopHelper.ICheck<Boolean>() {
			@Override
			public Boolean getCurrentState() {
				return isElementPresent(By.xpath(XPATH_GEOMETRY_BASE_VIEWER));
			}
		});
		try {
			geometryPanelLoop.run();
		} catch (LoopTimeoutException e) {
			return false;
		}
		return true;
	}

	public boolean isSamePosition(Point positionBefore, Point positionAfter) {
		int margin = 2;
		int bX = positionBefore.getX();
		int bY = positionBefore.getY();
		int aX = positionAfter.getX();
		int aY = positionAfter.getY();
		if (Math.abs(aX - bX) <= margin && Math.abs(aY - bY) <= margin) {
			return true;
		}
		return false;
	}

	public boolean meshOperationFinished(String operationName, int timeoutSec) {
		final String name = operationName;
		LoopHelper<Boolean> meshOperationFinishedLoop = new LoopHelper<Boolean>(timeoutSec, true,
				new LoopHelper.ICheck<Boolean>() {
					@Override
					public Boolean getCurrentState() {
						try {
							for (WebElement meshJob : getMeshOperationJobs()) {
								if (getJobName(meshJob).equals(name)) {
									if (JobStatus.FINISHED.equals(getJobStatus(meshJob))) {
										return true;
									}
									return false;
								}
							}
						} catch (StaleElementReferenceException e) {
							return false;
						}
						return false;
					}
				});
		try {
			meshOperationFinishedLoop.run();
		} catch (LoopTimeoutException e) {
			System.err.println("Mesh Operation job did not start. " + e);
			return false;
		}
		return true;
	}

	public boolean meshOperationStarted(String operationName) {
		final String name = operationName;
		LoopHelper<Boolean> meshOperationStartedLoop = new LoopHelper<Boolean>(PROJECT_PAGE_TIMEOUT, 5, true,
				new LoopHelper.ICheck<Boolean>() {
					@Override
					public Boolean getCurrentState() {
						try {
							for (WebElement meshJob : getMeshOperationJobs()) {
								if (getJobName(meshJob).equals(name)) {
									return true;
								}
							}
						} catch (NoSuchElementException e) {
							return false;
						}
						return false;
					}
				});
		try {
			meshOperationStartedLoop.run();
		} catch (LoopTimeoutException e) {
			System.err.println("Mesh Operation job did not start. " + e);
			return false;
		}
		return true;
	}

	public void waitForMeshOperationPanelToShow(int timeout) {
		LoopHelper<Boolean> meshOperationPanelLoop = new LoopHelper<Boolean>(timeout, true,
				new LoopHelper.ICheck<Boolean>() {
					@Override
					public Boolean getCurrentState() {
						try {
							isElementPresent(By.xpath(XPATH_MESH_OPERATION_PANEL));
						} catch (ElementNotFoundException e) {
							return false;
						}
						return true;
					}
				});
		try {
			meshOperationPanelLoop.run();
		} catch (LoopTimeoutException e) {
			throw new LoopTimeoutException("Mesh Operation Panel did not show. " + e);
		}
	}

	private void clickMenuOption(WebElement parent, String optionName) {
		WebElement element;
		switch (optionName) {
		case "Rename":
			element = mDriver.findElement(By.xpath(XPATH_RENAME_OPTION));
			break;
		default:
			element = parent;
		}
		clickElement(element);
	}

	private void clickRenameSubmitButton(String originalName) {
		for (WebElement submitButton : submitButtons) {
			WebElement parentProject = submitButton.findElement(By.xpath("../../../.."));
			String parentText = parentProject.getText();
			if (parentText.contains(originalName)) {
				clickElement(submitButton);
			}
		}
	}

	private void debugMessage(String message) {
		if (DEBUG_MODE) {
			System.out.println(message);
		}
	}

	private WebElement getButtonByXPath(String buttonXpath) {
		final String xpath = buttonXpath;
		LoopHelper<Boolean> meshOperationStartedLoop = new LoopHelper<Boolean>(PROJECT_PAGE_TIMEOUT, 5, true,
				new LoopHelper.ICheck<Boolean>() {
					@Override
					public Boolean getCurrentState() {
						try {
							if (null != mDriver.findElement(By.xpath(xpath))) {
								return true;
							}
						} catch (NoSuchElementException e) {
							return false;
						}
						return false;
					}
				});
		try {
			meshOperationStartedLoop.run();
		} catch (LoopTimeoutException e) {
			throw new NoSuchElementException("Button could not be located using xpath " + xpath + ". " + e);
		}
		return mDriver.findElement(By.xpath(xpath));
	}

	private WebElement getButtonFromDialog(WebElement dialogBox, String button_clue) {
		WebElement formActions = dialogBox.findElement(By.xpath("div[2]/div/div"));
		WebElement button = null;
		switch (button_clue) {
		case ("Cancel"):
			button = formActions.findElement(By.xpath("a[1]"));
			break;
		case ("Yes"):
			button = formActions.findElement(By.xpath("a[2]"));
			break;
		}
		return button;
	}

	private WebElement getDialogBox(String dialog_clue) {
		for (WebElement dialogBox : dialogBoxes) {
			if (dialogBox.getText().contains(dialog_clue)) {
				return dialogBox;
			}
		}
		throw new ElementNotFoundException(dialog_clue, null, null);
	}

	private WebElement getGeometryFromTree(String geometryName) {
		for (int i = 0; i < geometries.size(); i++) {
			WebElement geometry = geometries.get(i);
			String projectName = geometry.getText();
			if (projectName.equals(geometryName)) {
				return geometry.findElement(By.xpath("div/table"));
			}
		}
		throw new ElementNotFoundException(geometryName, null, null);
	}

	private String getJobName(WebElement meshJob) {
		debugMessage(meshJob.findElement(By.xpath("td")).getText());
		return meshJob.findElement(By.xpath("td")).getText();
	}

	private JobStatus getJobStatus(WebElement meshJob) {
		debugMessage(meshJob.findElement(By.xpath("td[3]")).getText());
		String status = meshJob.findElement(By.xpath("td[3]")).getText();
		switch (status) {
		case ("Finished"):
			return JobStatus.FINISHED;
		case ("Started"):
			return JobStatus.STARTED;
		}
		return null;
	}

	private List<WebElement> getMeshOperationJobs() {
		List<WebElement> jobs = mDriver.findElements(By.xpath(XPATH_MESH_OPERATION_JOBS + "/tr"));
		return jobs;
	}

	private WebElement getProjectFromProjectList(String name) {
		for (int i = 0; i < projects.size(); i++) {
			WebElement project = projects.get(i);
			String projectName = project.getText();
			if (projectName.equals(name)) {
				return project;
			}
		}
		throw new ElementNotFoundException(name, null, null);
	}

	private Boolean hasPageLoadedCorrectly() {
		if (!PROJECT_URL.equals(mDriver.getCurrentUrl())) {
			return false;
		} else if (!isElementPresent(By.xpath(X_PATH_PROJECTS))) {
			return false;
		} else if (!isElementPresent(By.xpath(X_PATH_PROJECT_2))) {
			return false;
		}
		return true;
	}

	private void waitForGeometryToLoad(int timeout) {
		LoopHelper<Boolean> geometryPanelLoop = new LoopHelper<Boolean>(timeout, true,
				new LoopHelper.ICheck<Boolean>() {
					@Override
					public Boolean getCurrentState() {
						return isElementPresent(By.xpath(XPATH_GEOMETRY_BASE_VIEWER));
					}
				});
		try {
			geometryPanelLoop.run();
		} catch (LoopTimeoutException e) {
			throw new LoopTimeoutException("Geometry Viewer did not show correctly. " + e);
		}
	}

	private void waitForMeshCreatorToShow(int timeout) {
		LoopHelper<Boolean> geometryPanelLoop = new LoopHelper<Boolean>(timeout, true,
				new LoopHelper.ICheck<Boolean>() {
					@Override
					public Boolean getCurrentState() {
						return isElementPresent(By.xpath(XPATH_AVALABLE_GEOMETRIES));
					}
				});
		try {
			geometryPanelLoop.run();
		} catch (LoopTimeoutException e) {
			throw new LoopTimeoutException("Geometry Panel did not show with correct name. " + e);
		}
	}

	private void waitForProjectNameChange(String name) {
		final String newName = name;
		LoopHelper<Boolean> nameChangeLoop = new LoopHelper<Boolean>(PROJECT_PAGE_TIMEOUT, true,
				new LoopHelper.ICheck<Boolean>() {
					@Override
					public Boolean getCurrentState() {
						try {
							getProjectFromProjectList(newName);
						} catch (ElementNotFoundException e) {
							return false;
						}
						return true;
					}
				});
		try {
			nameChangeLoop.run();
		} catch (LoopTimeoutException e) {
			throw new LoopTimeoutException("Project did not change to new name. " + e);
		}
	}

	private void waitForProjectPanelToShow(int timeout) {
		LoopHelper<Boolean> projectPanelLoop = new LoopHelper<Boolean>(timeout, true, new LoopHelper.ICheck<Boolean>() {
			@Override
			public Boolean getCurrentState() {
				try {
					workspaceLegend.getText().equals("Project " + CommonParameters.NAME_PROJECT_2);
				} catch (ElementNotFoundException e) {
					return false;
				}
				return true;
			}
		});
		try {
			projectPanelLoop.run();
		} catch (LoopTimeoutException e) {
			throw new LoopTimeoutException("Project Panel did not show with correct name. " + e);
		}
	}

}
