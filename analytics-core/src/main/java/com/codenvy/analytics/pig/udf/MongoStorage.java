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
package com.codenvy.analytics.pig.udf;

import com.mongodb.*;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapreduce.*;
import org.apache.pig.StoreFunc;
import org.apache.pig.data.Tuple;

import java.io.IOException;

/** @author <a href="mailto:abazko@codenvy.com">Anatoliy Bazko</a> */
public class MongoStorage extends StoreFunc {

    public static final String SERVER_URL_PARAM = "server.url";

    private RecordWriter writer;

    /** {@link MongoStorage} constructor. */
    public MongoStorage() {
    }

    /** {@inheritDoc) */
    @Override
    public OutputFormat getOutputFormat() throws IOException {
        return new MongoOutputFormat();
    }

    /** {@inheritDoc) */
    @Override
    public void setStoreLocation(String location, Job job) throws IOException {
        job.getConfiguration().set(SERVER_URL_PARAM, location);
    }

    /** {@inheritDoc) */
    @Override
    public void prepareToWrite(RecordWriter writer) throws IOException {
        this.writer = writer;
    }

    /** {@inheritDoc) */
    @Override
    public void putNext(Tuple t) throws IOException {
        try {
            writer.write(null, t);
        } catch (InterruptedException e) {
            throw new IOException(e);
        }
    }

    /**
     *
     */
    public static class MongoWriter extends RecordWriter<WritableComparable, Tuple> {

        /** Collection to write data in. */
        protected DBCollection dbCollection;

        /** Mongo client. Have to be closed. */
        protected MongoClient mongoClient;

        /** MongoWriter constructor. */
        public MongoWriter(String serverUrl) throws IOException {
            MongoClientURI uri = new MongoClientURI(serverUrl);
            mongoClient = new MongoClient(uri);

            DB db = mongoClient.getDB(uri.getDatabase());
            if (uri.getUsername() != null && !uri.getUsername().isEmpty()) {
                db.authenticate(uri.getUsername(), uri.getPassword());
            }

            db.setWriteConcern(WriteConcern.ACKNOWLEDGED);

            this.dbCollection = db.getCollection(uri.getCollection());
        }

        /** {@inheritDoc) */
        @Override
        public void write(WritableComparable key, Tuple value) throws IOException, InterruptedException {
            DBObject dbObject = new BasicDBObject();

            dbObject.put("_id", value.get(0));
            for (int i = 1; i < value.size(); i++) {
                Tuple tuple = (Tuple)value.get(i);
                dbObject.put(tuple.get(0).toString(), tuple.get(1));
            }

            dbCollection.save(dbObject);
        }

        /** {@inheritDoc) */
        @Override
        public void close(TaskAttemptContext context) throws IOException, InterruptedException {
            mongoClient.close();
        }
    }

    /** @author <a href="mailto:abazko@codenvy.com">Anatoliy Bazko</a> */
    public static class MongoOutputFormat extends OutputFormat<WritableComparable, Tuple> {

        /** {@inheritDoc} */
        @Override
        public RecordWriter<WritableComparable, Tuple> getRecordWriter(TaskAttemptContext context) throws IOException,
                                                                                                          InterruptedException {
            String serverUrl = context.getConfiguration().get(SERVER_URL_PARAM);
            return new MongoWriter(serverUrl);
        }

        /** {@inheritDoc} */
        @Override
        public void checkOutputSpecs(JobContext context) throws IOException, InterruptedException {
        }

        /** {@inheritDoc} */
        @Override
        public OutputCommitter getOutputCommitter(TaskAttemptContext context) throws IOException, InterruptedException {
            return new MongoCommitter();
        }
    }

    /** @author <a href="mailto:abazko@codenvy.com">Anatoliy Bazko</a> */
    public static class MongoCommitter extends OutputCommitter {

        /** {@inheritDoc) */
        @Override
        public void setupJob(JobContext jobContext) throws IOException {
        }

        /** {@inheritDoc) */
        @Override
        public void cleanupJob(JobContext jobContext) throws IOException {
        }

        /** {@inheritDoc) */
        @Override
        public void setupTask(TaskAttemptContext taskContext) throws IOException {
        }

        /** {@inheritDoc) */
        @Override
        public boolean needsTaskCommit(TaskAttemptContext taskContext) throws IOException {
            return false;
        }

        /** {@inheritDoc) */
        @Override
        public void commitTask(TaskAttemptContext taskContext) throws IOException {
        }

        /** {@inheritDoc) */
        @Override
        public void abortTask(TaskAttemptContext taskContext) throws IOException {
        }
    }
}

