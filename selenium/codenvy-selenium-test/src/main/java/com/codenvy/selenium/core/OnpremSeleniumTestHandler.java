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
package com.codenvy.selenium.core;

import com.google.inject.Guice;
import com.google.inject.Injector;

import org.eclipse.che.selenium.core.inject.SeleniumTestHandler;

/**
 * @author Anatolii Bazko
 */
public class OnpremSeleniumTestHandler extends SeleniumTestHandler {

    @Override
    public Injector createParentInjector() {
        return Guice.createInjector(new OnpremSeleniumSuiteModule());
    }
}
