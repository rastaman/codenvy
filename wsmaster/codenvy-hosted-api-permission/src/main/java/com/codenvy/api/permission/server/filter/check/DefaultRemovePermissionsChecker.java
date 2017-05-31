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

import org.eclipse.che.api.core.ForbiddenException;
import org.eclipse.che.commons.env.EnvironmentContext;

import javax.inject.Singleton;

import static com.codenvy.api.permission.server.AbstractPermissionsDomain.SET_PERMISSIONS;

/**
 * Common checks while removing permissions.
 *
 * @author Anton Korneta
 */
@Singleton
public class DefaultRemovePermissionsChecker implements RemovePermissionsChecker {

    @Override
    public void check(String user, String domainId, String instance) throws ForbiddenException {
        if (!EnvironmentContext.getCurrent().getSubject().hasPermission(domainId, instance, SET_PERMISSIONS)) {
            throw new ForbiddenException("User can't edit permissions for this instance");
        }
    }

}
