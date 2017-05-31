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


import com.codenvy.api.permission.shared.model.Permissions;

import org.eclipse.che.api.core.ForbiddenException;

/**
 * Defines contract for domain specific checks, before set permissions.
 *
 * @author Anton Korneta
 */
public interface SetPermissionsChecker {

    /**
     * Checks if the current user is allowed to set permissions.
     *
     * @param permissions
     *         permission to set
     * @throws ForbiddenException
     *         when it is not allowed to set {@code permissions}
     */
    void check(Permissions permissions) throws ForbiddenException;

}
