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
package com.codenvy.selenium.workspaces;

import org.eclipse.che.selenium.pageobject.ProjectExplorer;
import org.eclipse.che.selenium.pageobject.ToastLoader;
import com.google.inject.Inject;

import org.eclipse.che.api.core.model.workspace.Workspace;
import org.eclipse.che.api.core.model.workspace.WorkspaceStatus;
import org.eclipse.che.selenium.core.client.TestWorkspaceServiceClient;
import org.eclipse.che.selenium.core.user.InjectTestUser;
import org.eclipse.che.selenium.core.user.TestUser;
import org.eclipse.che.selenium.core.utils.WaitUtils;
import org.eclipse.che.selenium.core.workspace.InjectTestWorkspace;
import org.eclipse.che.selenium.core.workspace.TestWorkspace;
import org.eclipse.che.selenium.pageobject.Ide;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

import static org.testng.Assert.assertEquals;

/**
 * @author Musienko Maxim
 */
public class CheckStoppingWsByTimeoutTest {
    private final static String USER_NAME = "user-for-check-ws";

    @InjectTestUser(value = USER_NAME)
    private TestUser                   testUser;
    @InjectTestWorkspace(user = USER_NAME)
    private TestWorkspace              testWorkspace;
    @Inject
    private Ide                        ide;
    @Inject
    private ProjectExplorer            projectExplorer;
    @Inject
    private ToastLoader                toastLoader;
    @Inject
    private TestWorkspaceServiceClient workspaceServiceClient;

    @BeforeClass
    public void setUp() throws Exception {
        ide.open(testWorkspace);
    }

    @Test
    public void checkStoppingWsWithApi() throws Exception {
        projectExplorer.waitProjectExplorer();
        WaitUtils.sleepQuietly(760, TimeUnit.SECONDS);
        Workspace workspace = workspaceServiceClient.getByName(testWorkspace.getName(), USER_NAME, testUser.getAuthToken());
        assertEquals(workspace.getStatus(), WorkspaceStatus.STOPPED);
        toastLoader.waitStartButtonInToastLoader();
    }
}
