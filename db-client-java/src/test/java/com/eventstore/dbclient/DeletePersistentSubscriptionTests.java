package com.eventstore.dbclient;

import org.junit.jupiter.api.Test;
import testcontainers.module.ESDBTests;
import testcontainers.module.EventStoreDB;

import java.util.concurrent.ExecutionException;

public class DeletePersistentSubscriptionTests extends ESDBTests {
    @Test
    public void testDeletePersistentSub() throws Throwable {
        EventStoreDBPersistentSubscriptionsClient client = getEmptyServer().getPersistentSubscriptionsClient();
        String streamName = generateName();
        String groupName = generateName();

        client.create(streamName, groupName)
                .get();

        client.delete(streamName, groupName)
                .get();
    }

    @Test
    public void testDeletePersistentSubToAll() throws Throwable {
        EventStoreDBPersistentSubscriptionsClient client = getEmptyServer().getPersistentSubscriptionsClient();
        String groupName = generateName();

        try {
            client.createToAll(groupName)
                    .get();

            client.deleteToAll(groupName)
                    .get();
        } catch (ExecutionException e) {
            if (e.getCause() instanceof UnsupportedFeature && !EventStoreDB.isTestedAgainstVersion20()) {
                throw e;
            }
        }
    }
}
