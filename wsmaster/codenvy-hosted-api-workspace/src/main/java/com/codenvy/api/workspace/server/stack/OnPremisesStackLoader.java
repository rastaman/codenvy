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
package com.codenvy.api.workspace.server.stack;

import com.codenvy.api.workspace.server.spi.jpa.JpaStackPermissionsDao;

import org.eclipse.che.api.core.ConflictException;
import org.eclipse.che.api.core.NotFoundException;
import org.eclipse.che.api.core.ServerException;
import org.eclipse.che.api.workspace.server.model.impl.stack.StackImpl;
import org.eclipse.che.api.workspace.server.spi.StackDao;
import org.eclipse.che.api.workspace.server.stack.StackLoader;
import org.eclipse.che.core.db.DBInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.nio.file.Path;
import java.util.Map;

import static com.codenvy.api.workspace.server.stack.StackDomain.SEARCH;
import static java.lang.String.format;
import static java.util.Collections.singletonList;

/**
 * Loads predefined stacks and allows you to search for them to any user.
 *
 * @author Anton Korneta
 */
@Singleton
public class OnPremisesStackLoader extends StackLoader {

    private static final Logger LOG = LoggerFactory.getLogger(OnPremisesStackLoader.class);

    private final JpaStackPermissionsDao permissionsDao;

    @Inject
    @SuppressWarnings("unused")
    public OnPremisesStackLoader(@Named("codenvy.predefined.stacks.reload_on_start") boolean reloadStacksOnStart,
                                 @Named(CHE_PREDEFINED_STACKS) Map<String, String> stacks2images,
                                 StackDao stackDao,
                                 DBInitializer dbInitializer,
                                 JpaStackPermissionsDao permissionsDao) {
        super(reloadStacksOnStart, stacks2images, stackDao, dbInitializer);
        this.permissionsDao = permissionsDao;
    }

    protected void loadStack(StackImpl stack, Path imagePath) {
        setIconData(stack, imagePath);
        try {
            try {
                stackDao.update(stack);
            } catch (NotFoundException ignored) {
                stackDao.create(stack);
            }
            permissionsDao.store(new StackPermissionsImpl("*", stack.getId(), singletonList(SEARCH)));
        } catch (ServerException | ConflictException ex) {
            LOG.warn(format("Failed to load stack with id '%s' ", stack.getId()), ex.getMessage());
        }
    }

}
