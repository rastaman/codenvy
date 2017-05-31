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
package com.codenvy.api.permission.server.filter.check;

import com.codenvy.api.permission.server.SuperPrivilegesChecker;

import org.eclipse.che.api.core.ForbiddenException;
import org.eclipse.che.commons.env.EnvironmentContext;
import org.eclipse.che.commons.subject.Subject;

import javax.inject.Inject;
import javax.inject.Singleton;

import static com.codenvy.api.permission.server.AbstractPermissionsDomain.SET_PERMISSIONS;

/**
 * @author Anton Korneta
 */
@Singleton
public class DefaultRemovePermissionsChecker implements RemovePermissionsChecker {

    private final SuperPrivilegesChecker superPrivilegesChecker;

    @Inject
    public DefaultRemovePermissionsChecker(SuperPrivilegesChecker superPrivilegesChecker) {
        this.superPrivilegesChecker = superPrivilegesChecker;
    }

    @Override
    public void check(String user, String domainId, String instance) throws ForbiddenException {
        final Subject subject = EnvironmentContext.getCurrent().getSubject();
        if (!(subject.getUserId().equals(user)
              || superPrivilegesChecker.isPrivilegedToManagePermissions(domainId)
              || subject.hasPermission(domainId, instance, SET_PERMISSIONS))) {
            throw new ForbiddenException("Default message");
        }
    }
}
