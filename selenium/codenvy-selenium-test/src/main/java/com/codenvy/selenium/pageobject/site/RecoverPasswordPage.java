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
package com.codenvy.selenium.pageobject.site;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import org.eclipse.che.selenium.core.SeleniumWebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.eclipse.che.selenium.core.constant.TestTimeoutsConstants.REDRAW_UI_ELEMENTS_TIMEOUT_SEC;

/**
 * @author Musienko Maxim
 */
@Singleton
public class RecoverPasswordPage {

    private final SeleniumWebDriver seleniumWebDriver;
    WebDriverWait redrawWait;

    @Inject
    public RecoverPasswordPage(SeleniumWebDriver seleniumWebDriver) {
        this.seleniumWebDriver = seleniumWebDriver;
        PageFactory.initElements(seleniumWebDriver, this);
        redrawWait = new WebDriverWait(seleniumWebDriver, REDRAW_UI_ELEMENTS_TIMEOUT_SEC);
    }

    private static final class Locators {
        private static final String MAIN_PAGE                     = "//*[contains(text(),'Forgot your password?')]";
        private static final String EMAIL_FIELD                   = "emailid";
        private static final String RESET_PASS_BTN                = "input[value='Reset password']";
        private static final String PAGE_ABOUT_SEND_MESS          = "//*[contains(text(),'sent a reset password link to your email.')]";
        private static final String SET_NEW_PASSWORD_FIELD_CSS    = "form#resetPasswordForm input[placeholder='New Password']";
        private static final String REPEAT_NEW_PASSWORD_FIELD_CSS = "form#resetPasswordForm input[placeholder='Repeat New Password']";
        private static final String SAVE_NEW_PASSWORD_BUTTON      = "form#resetPasswordForm input[Value='Save']";
    }

    @FindBy(xpath = Locators.MAIN_PAGE)
    WebElement mainPageContainer;

    @FindBy(id = Locators.EMAIL_FIELD)
    WebElement emailField;

    @FindBy(css = Locators.RESET_PASS_BTN)
    WebElement resetButton;

    @FindBy(xpath = Locators.PAGE_ABOUT_SEND_MESS)
    WebElement pageWithsendMessageInfo;

    @FindBy(css = Locators.SET_NEW_PASSWORD_FIELD_CSS)
    WebElement setNewPasswordField;

    @FindBy(css = Locators.REPEAT_NEW_PASSWORD_FIELD_CSS)
    WebElement repeatNewPasswordField;

    @FindBy(css = Locators.SAVE_NEW_PASSWORD_BUTTON)
    WebElement saveNewPasswordField;


    public void sendMessageResetPassword(String userEmai) {
        redrawWait.until(ExpectedConditions.visibilityOf(mainPageContainer));
        redrawWait.until(ExpectedConditions.visibilityOf(emailField)).sendKeys(userEmai);
        resetButton.click();
        redrawWait.until(ExpectedConditions.visibilityOf(pageWithsendMessageInfo));
    }

    public void setNewPasswordAndSave(String newPassword) {
        redrawWait.until(ExpectedConditions.visibilityOf(setNewPasswordField)).sendKeys(newPassword);
        repeatNewPasswordField.sendKeys(newPassword);
        saveNewPasswordField.click();
    }
}

