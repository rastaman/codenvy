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
import org.eclipse.che.selenium.core.provider.TestDashboardUrlProvider;
import org.eclipse.che.selenium.core.provider.TestIdeUrlProvider;
import org.eclipse.che.selenium.core.user.DefaultTestUser;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.eclipse.che.selenium.core.constant.TestTimeoutsConstants.LOAD_PAGE_TIMEOUT_SEC;
import static org.eclipse.che.selenium.core.constant.TestTimeoutsConstants.REDRAW_UI_ELEMENTS_TIMEOUT_SEC;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOf;

/**
 * @author Dmytro Nochevnov
 */
@Singleton
public class DashboardSettings extends CodenvyAdminDashboard {

    private interface Constants {
        String LICENSE_AREA_XPATH          = "//textarea[@name='licenseKey']";
        String APPLY_LICENSE_BUTTON_XPATH  = "//che-button-primary[@che-button-title='Apply']";
        String REMOVE_LICENSE_BUTTON_XPATH = "//che-button-primary[@che-button-title='Remove']";
        String LICENSE_INFO_XPATH          = "//div[@che-label-name='License']//span[contains(@class, 'onprem-license-info-row')]";
        String LICENSE_EXPIRATION_XPATH    = "//div[@che-label-name='Expiration']//span[contains(@class, 'onprem-license-info-row')]";
    }

    @FindBy(xpath = Constants.LICENSE_AREA_XPATH)
    public WebElement licenseArea;

    @FindBy(xpath = Constants.APPLY_LICENSE_BUTTON_XPATH)
    public WebElement applyLicenseButton;

    @FindBy(xpath = Constants.REMOVE_LICENSE_BUTTON_XPATH)
    public WebElement removeLicenseButton;

    @FindBy(xpath = Constants.LICENSE_INFO_XPATH)
    public WebElement licenseInfo;

    @FindBy(xpath = Constants.LICENSE_EXPIRATION_XPATH)
    public WebElement licenseExpiration;

    @Inject
    public DashboardSettings(SeleniumWebDriver seleniumWebDriver,
                             DefaultTestUser defaultUser,
                             TestIdeUrlProvider testIdeUrlProvider,
                             TestDashboardUrlProvider testDashboardUrlProvider) {
        super(seleniumWebDriver, defaultUser, testIdeUrlProvider, testDashboardUrlProvider);
        PageFactory.initElements(seleniumWebDriver, this);
    }

    @Override
    public void waitPageOpened() {
        new WebDriverWait(seleniumWebDriver, LOAD_PAGE_TIMEOUT_SEC)
                .until(visibilityOf(licenseArea));
    }

    public void applyLicense(String license) {
        new WebDriverWait(seleniumWebDriver, REDRAW_UI_ELEMENTS_TIMEOUT_SEC)
                .until(visibilityOf(licenseArea))
                .sendKeys(license);

        new WebDriverWait(seleniumWebDriver, REDRAW_UI_ELEMENTS_TIMEOUT_SEC)
                .until(visibilityOf(applyLicenseButton))
                .click();
    }

    public void removeLicense() {
        new WebDriverWait(seleniumWebDriver, REDRAW_UI_ELEMENTS_TIMEOUT_SEC)
                .until(visibilityOf(removeLicenseButton))
                .click();
    }

    public String getLicenseInfo() {
        new WebDriverWait(seleniumWebDriver, REDRAW_UI_ELEMENTS_TIMEOUT_SEC)
                .until(visibilityOf(licenseInfo));
        return licenseInfo.getText();
    }

    public String getLicenseExpiration() {
        new WebDriverWait(seleniumWebDriver, REDRAW_UI_ELEMENTS_TIMEOUT_SEC)
                .until(visibilityOf(licenseExpiration));
        return licenseExpiration.getText();
    }

    public void waitOnRemoveLicenseButtonAppears() {
        new WebDriverWait(seleniumWebDriver, REDRAW_UI_ELEMENTS_TIMEOUT_SEC)
                .until(visibilityOf(removeLicenseButton));
    }

    public void waitOnApplyLicenseButtonAppears() {
        new WebDriverWait(seleniumWebDriver, REDRAW_UI_ELEMENTS_TIMEOUT_SEC)
                .until(visibilityOf(applyLicenseButton));
    }
}
