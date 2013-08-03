/*
 *
 * CODENVY CONFIDENTIAL
 * ________________
 *
 * [2012] - [2013] Codenvy, S.A.
 * All Rights Reserved.
 * NOTICE: All information contained herein is, and remains
 * the property of Codenvy S.A. and its suppliers,
 * if any. The intellectual and technical concepts contained
 * herein are proprietary to Codenvy S.A.
 * and its suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Codenvy S.A..
 */


package com.codenvy.analytics.metrics.value;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/** @author <a href="mailto:abazko@codenvy.com">Anatoliy Bazko</a> */
public class StringValueData extends AbstractValueData {

    public static final StringValueData DEFAULT = new StringValueData("");
    private static final long serialVersionUID = 1L;

    private String value;

    public StringValueData() {
    }

    public StringValueData(String value) {
        this.value = value;
    }

    /** {@inheritDoc} */
    @Override
    public String getAsString() {
        return value;
    }

    /** {@inheritDoc} */
    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeUTF(value);
    }

    /** {@inheritDoc} */
    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        value = in.readUTF();
    }

    /** {@inheritDoc} */
    @Override
    protected boolean doEquals(Object object) {
        return value.equals(object);
    }

    /** {@inheritDoc} */
    @Override
    protected int doHashCode() {
        return value.hashCode();
    }
}
