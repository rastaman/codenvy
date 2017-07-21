/*******************************************************************************
 * Copyright (c) [2012] - [2017] Red Hat, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Red Hat, Inc. - initial API and implementation
 *******************************************************************************/
package com.codenvy.selenium.pageobject.site;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import org.eclipse.che.selenium.core.SeleniumWebDriver;
import org.eclipse.che.selenium.pageobject.Loader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Arrays;

import static org.eclipse.che.selenium.core.constant.TestTimeoutsConstants.LOAD_PAGE_TIMEOUT_SEC;

/**
 * @author Dmytro Nochevnov
 */
@Singleton
public class AcceptFairSourceLicensePage {
    private final SeleniumWebDriver seleniumWebDriver;
    private final Loader            loader;

    @Inject
    public AcceptFairSourceLicensePage(SeleniumWebDriver seleniumWebDriver, Loader loader) {
        this.loader = loader;
        this.seleniumWebDriver = seleniumWebDriver;
        PageFactory.initElements(seleniumWebDriver, this);
    }

    private interface Locators {
        String ACCEPT_XPATH = "//input[@value='Accept']";
    }

    @FindBy(xpath = Locators.ACCEPT_XPATH)
    private WebElement acceptButton;

    /**
     * wait main elements on page
     */
    public void waitPage() {
        waitMainElementsOnPage();
    }

    /**
     * wait appearance main buttons and fields on page
     */
    public void waitMainElementsOnPage() {
        new WebDriverWait(seleniumWebDriver, LOAD_PAGE_TIMEOUT_SEC)
                .until(ExpectedConditions.visibilityOfAllElements(Arrays.asList(acceptButton)));
    }

    /**
     * wait main elements on page
     */
    public void waitPageClosed() {
        new WebDriverWait(seleniumWebDriver, LOAD_PAGE_TIMEOUT_SEC)
                .until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(Locators.ACCEPT_XPATH)));
    }

    /**
     * click on Accept button and wait closing of page
     */
    public void clickOnAcceptButtonAndGo() {
        acceptButton.click();
        loader.waitOnClosed();
    }

}
