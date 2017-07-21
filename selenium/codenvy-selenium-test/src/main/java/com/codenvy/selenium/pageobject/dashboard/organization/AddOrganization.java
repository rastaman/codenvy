/*
 *  [2012] - [2017] Codenvy, S.A.
 *  All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Codenvy S.A. and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Codenvy S.A.
 * and its suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Codenvy S.A..
 */
package com.codenvy.selenium.pageobject.dashboard.organization;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import org.eclipse.che.selenium.core.SeleniumWebDriver;
import org.eclipse.che.selenium.core.utils.WaitUtils;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.eclipse.che.selenium.core.constant.TestTimeoutsConstants.LOAD_PAGE_TIMEOUT_SEC;
import static org.eclipse.che.selenium.core.constant.TestTimeoutsConstants.REDRAW_UI_ELEMENTS_TIMEOUT_SEC;

/**
 * @author Sergey Skorik
 */
@Singleton
public class AddOrganization {

    private interface Locators {
        String ADD_ORGANIZATION_TITLE     = "//div[contains(@class, 'che-toolbar-title')]//span";
        String NEW_ORGANIZATION_NAME      = "//input[@name='name']";
        String ADD_MEMBER_BUTTON          = "//che-button-default[@class = 'che-list-add-button']//button";
        String CREATE_ORGANIZATION_BUTTON = "//che-button-primary[@id='create-organization-button']//button";
    }

    private static final String NEW_ORGANIZATION = "Create New Organization";

    private static final String NEW_SUB_ORGANIZATION = "Create Sub-Organization";

    private final WebDriverWait     redrawUiElementsTimeout;
    private final SeleniumWebDriver seleniumWebDriver;

    @FindBy(xpath = Locators.ADD_ORGANIZATION_TITLE)
    WebElement addOrganizationTitle;

    @FindBy(xpath = Locators.NEW_ORGANIZATION_NAME)
    WebElement newOrganizationName;

    @FindBy(xpath = Locators.ADD_MEMBER_BUTTON)
    WebElement addMemberButton;

    @FindBy(xpath = Locators.CREATE_ORGANIZATION_BUTTON)
    WebElement createOrganizationButton;

    @Inject
    public AddOrganization(SeleniumWebDriver seleniumWebDriver) {
        this.seleniumWebDriver = seleniumWebDriver;
        this.redrawUiElementsTimeout = new WebDriverWait(seleniumWebDriver, REDRAW_UI_ELEMENTS_TIMEOUT_SEC);
        PageFactory.initElements(seleniumWebDriver, this);
    }

    public void waitAddOrganization() {
        WaitUtils.sleepQuietly(1);
        new WebDriverWait(seleniumWebDriver, LOAD_PAGE_TIMEOUT_SEC)
                .until(ExpectedConditions.visibilityOf(addOrganizationTitle)).getText()
                .equals(NEW_ORGANIZATION);
    }

    public void waitAddSubOrganization() {
        redrawUiElementsTimeout.until(ExpectedConditions.visibilityOf(addOrganizationTitle)).getText().equals(NEW_SUB_ORGANIZATION);
    }

    public void setOrganizationName(String name) {
        redrawUiElementsTimeout.until(ExpectedConditions.visibilityOf(newOrganizationName)).clear();
        redrawUiElementsTimeout.until(ExpectedConditions.visibilityOf(newOrganizationName)).sendKeys(name);
        redrawUiElementsTimeout.until(ExpectedConditions.visibilityOf(newOrganizationName)).getText().equals(name);
    }

    public void clickAddMemberButton() {
        redrawUiElementsTimeout.until(ExpectedConditions.visibilityOf(addMemberButton)).click();
    }

    public void clickCreateOrganizationButton() {
        redrawUiElementsTimeout.until(ExpectedConditions.visibilityOf(createOrganizationButton)).click();
    }

    public boolean checkAddOrganizationButtonEnabled() {
        return redrawUiElementsTimeout.until(ExpectedConditions.visibilityOf(createOrganizationButton)).isEnabled();
    }
}
