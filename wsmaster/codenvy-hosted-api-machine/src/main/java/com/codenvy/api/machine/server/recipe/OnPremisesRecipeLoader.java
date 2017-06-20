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
package com.codenvy.api.machine.server.recipe;

import com.codenvy.api.machine.server.jpa.JpaRecipePermissionsDao;

import org.eclipse.che.api.core.ConflictException;
import org.eclipse.che.api.core.NotFoundException;
import org.eclipse.che.api.core.ServerException;
import org.eclipse.che.api.machine.server.recipe.RecipeImpl;
import org.eclipse.che.api.machine.server.recipe.RecipeLoader;
import org.eclipse.che.api.machine.server.spi.RecipeDao;
import org.eclipse.che.core.db.DBInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.Set;

import static java.util.Collections.singletonList;

/**
 * Loads predefined recipes, with public permissions.
 *
 * @author Anton Korneta.
 */
@Singleton
public class OnPremisesRecipeLoader extends RecipeLoader {

    private static final Logger LOG = LoggerFactory.getLogger(OnPremisesRecipeLoader.class);

    private final JpaRecipePermissionsDao permissionsDao;

    @Inject
    public OnPremisesRecipeLoader(@Named(CHE_PREDEFINED_RECIPES) Set<String> predefinedRecipes,
                                  JpaRecipePermissionsDao permissionsDao,
                                  RecipeDao recipeDao,
                                  DBInitializer dbInitializer) {
        super(predefinedRecipes, recipeDao, dbInitializer);
        this.permissionsDao = permissionsDao;
    }

    @Override
    protected void doCreate(RecipeImpl recipe) {
        try {
            try {
                recipeDao.update(recipe);
            } catch (NotFoundException ex) {
                recipeDao.create(recipe);
            }
            permissionsDao.store(new RecipePermissionsImpl("*", recipe.getId(), singletonList("search")));
        } catch (ServerException | ConflictException ex) {
            LOG.error("Failed to store recipe {} cause: {}", recipe.getId(), ex.getLocalizedMessage());
        }
    }

}
