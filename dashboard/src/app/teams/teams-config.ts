/*
 * Copyright (c) [2015] - [2017] Red Hat, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Red Hat, Inc. - initial API and implementation
 */
'use strict';

import {MoreDevsDialogController} from './more-devs-dialog/more-devs-dialog.controller';

/**
 * The configuration of teams, defines controllers, directives and routing.
 *
 * @author Ann Shumilova
 */
export class CodenvyTeamsConfig {

  constructor(register: any) {
    register.controller('MoreDevsDialogController', MoreDevsDialogController);

    let checkPersonalTeam = ($q: ng.IQService, codenvyTeam: che.api.ICheTeam) => {
      var defer = $q.defer();
      codenvyTeam.fetchTeams().then(() => {
        if (codenvyTeam.getPersonalAccount()) {
          defer.resolve();
        } else {
          defer.reject();
        }
      }, (error: any) => {
        if (error.status === 304) {
          if (codenvyTeam.getPersonalAccount()) {
            defer.resolve();
          } else {
            defer.reject();
          }
        }
      });
      return defer.promise;
    };
  }

}
