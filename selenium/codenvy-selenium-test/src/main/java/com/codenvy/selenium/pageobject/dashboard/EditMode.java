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
package com.codenvy.selenium.pageobject.dashboard;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import org.eclipse.che.selenium.core.SeleniumWebDriver;
import org.eclipse.che.selenium.core.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.eclipse.che.selenium.core.constant.TestTimeoutsConstants.REDRAW_UI_ELEMENTS_TIMEOUT_SEC;
import static org.openqa.selenium.support.ui.ExpectedConditions.invisibilityOfElementLocated;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOf;

/**
 * The edit/save mode with SAVE and CANCEL buttons.
 *
 * @author Ann Shumilova
 */
@Singleton
public class EditMode {

    private interface Locators {
        String MODE_BAR_XPATH           = "//div[contains(@class, 'workspace-edit-mode-overlay')]";
        String MODE_SAVE_BUTTON_XPATH   = MODE_BAR_XPATH + "//che-button-save-flat[@che-button-title='Save']";
        String MODE_CANCEL_BUTTON_XPATH = MODE_BAR_XPATH + "//che-button-cancel-flat[@che-button-title='Cancel']";
    }

    private final SeleniumWebDriver seleniumWebDriver;

    @FindBy(xpath = Locators.MODE_BAR_XPATH)
    WebElement modeBar;

    @FindBy(xpath = Locators.MODE_SAVE_BUTTON_XPATH)
    WebElement saveButton;

    @FindBy(xpath = Locators.MODE_CANCEL_BUTTON_XPATH)
    WebElement cancelButton;

    @Inject
    public EditMode(SeleniumWebDriver seleniumWebDriver) {
        this.seleniumWebDriver = seleniumWebDriver;
        PageFactory.initElements(seleniumWebDriver, this);
    }

    /**
     * Wait edit mode is displayed.
     */
    public void waitDisplayed() {
        new WebDriverWait(seleniumWebDriver, REDRAW_UI_ELEMENTS_TIMEOUT_SEC)
                .until(visibilityOf(modeBar));
    }

    /**
     * Wait edit mode is hidden.
     */
    public void waitHidden() {
        new WebDriverWait(seleniumWebDriver, REDRAW_UI_ELEMENTS_TIMEOUT_SEC)
                .until(invisibilityOfElementLocated(By.xpath(Locators.MODE_BAR_XPATH)));
    }

    /**
     * Performs click on Cancel button.
     */
    public void clickCancel() {
        cancelButton.click();
    }

    /**
     * Performs click on Confirm button.
     */
    public void clickSave() {
        saveButton.click();
    }

    /**
     * Performs click on Confirm button.
     */
    public boolean isSaveEnabled() {
        // animation duration:
        WaitUtils.sleepQuietly(1);
        return !Boolean.valueOf(saveButton.getAttribute("aria-disabled"));
    }
}
