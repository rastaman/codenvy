#!/bin/bash
# Copyright (c) 2012-2016 Codenvy, S.A.
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html
#
# Contributors:
#   Tyler Jewell - Initial Implementation
#

cmd_rmi() {
  info "rmi" "Checking registry for version '$CODENVY_VERSION' images"
  if ! has_version_registry $CODENVY_VERSION; then
    version_error $CODENVY_VERSION
    return 1;
  fi

  WARNING="rmi !!! Removing images disables codenvy and forces a pull !!!"
  if ! confirm_operation "${WARNING}" "$@"; then
    return;
  fi

  IMAGE_LIST=$(cat "$CODENVY_MANIFEST_DIR"/$CODENVY_VERSION/images)
  IFS=$'\n'
  info "rmi" "Removing ${CHE_MINI_PRODUCT_NAME} Docker images..."

  for SINGLE_IMAGE in $IMAGE_LIST; do
    VALUE_IMAGE=$(echo $SINGLE_IMAGE | cut -d'=' -f2)
    info "rmi" "Removing $VALUE_IMAGE..."
    log "docker rmi -f ${VALUE_IMAGE} >> \"${LOGS}\" 2>&1 || true"
    docker rmi -f $VALUE_IMAGE >> "${LOGS}" 2>&1 || true
  done
}
