package uiTestingAssignment.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;

import uiTestingAssignment.util.LoopHelper;
import uiTestingAssignment.util.LoopHelper.LoopTimeoutException;

public class ProjectPage extends WebPage {

	public static final String PROJECT_URL = "https://platform.simscale.com//#startAppFrame";

	public static final String X_PATH_PROJECTS = "//div[@class=\"GKXRPOKCCP projectCellList\"]";
	public static final String X_PATH_PROJECT_1 = "//span[text()='Tutorial-01: Connecting rod stress analysis']";
	public static final String X_PATH_PROJECT_2 = "//span[text()='Tutorial-02: Pipe junction flow']";
	public static final String X_PATH_PROJECT_3 = "//span[text()='Tutorial-03: Differential casing thermal analysis']";
	private static final String XPATH_RENAME_OPTION = "//td[@id=\"gwt-uid-27\"]";
	private static final String XPATH_RENAME_SUBMIT_BUTTON = "//a[@id='submitButton'][normalize-space()='Rename']";
	private static final String XPATH_RENAME_TEXTBOX = "//input[@class='gwt-TextBox']";

	private static final int PROJECT_PAGE_TIMEOUT = 10;

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
}
