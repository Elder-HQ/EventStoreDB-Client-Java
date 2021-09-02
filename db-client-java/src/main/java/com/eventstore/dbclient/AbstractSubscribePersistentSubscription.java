package com.eventstore.dbclient;

import com.eventstore.dbclient.proto.persistentsubscriptions.Persistent;
import com.eventstore.dbclient.proto.persistentsubscriptions.PersistentSubscriptionsGrpc;
import com.eventstore.dbclient.proto.shared.Shared;
import io.grpc.Metadata;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.ClientCallStreamObserver;
import io.grpc.stub.ClientResponseObserver;
import io.grpc.stub.MetadataUtils;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.CompletableFuture;

public abstract class AbstractSubscribePersistentSubscription {
    protected static final Persistent.ReadReq.Options.Builder defaultReadOptions;
    private final GrpcClient connection;
    private final String group;
    private final PersistentSubscriptionListener listener;
    private final SubscribePersistentSubscriptionOptions options;

    static {
        defaultReadOptions = Persistent.ReadReq.Options.newBuilder()
                .setUuidOption(Persistent.ReadReq.Options.UUIDOption.newBuilder()
                        .setStructured(Shared.Empty.getDefaultInstance()));
    }

    public AbstractSubscribePersistentSubscription(GrpcClient connection, String group,
                                                   SubscribePersistentSubscriptionOptions options,
                                                   PersistentSubscriptionListener listener) {
        this.connection = connection;
        this.group = group;
        this.options = options;
        this.listener = listener;
    }

    protected abstract Persistent.ReadReq.Options.Builder createOptions();

    public CompletableFuture<PersistentSubscription> execute() {
        return this.connection.run(channel -> {
            Metadata headers = this.options.getMetadata();
            PersistentSubscriptionsGrpc.PersistentSubscriptionsStub client = MetadataUtils
                    .attachHeaders(PersistentSubscriptionsGrpc.newStub(channel), headers);

            final CompletableFuture<PersistentSubscription> result = new CompletableFuture<>();

            int bufferSize = this.options.getBufferSize();

            Persistent.ReadReq req = Persistent.ReadReq.newBuilder()
                    .setOptions(createOptions()
                            .setBufferSize(bufferSize)
                            .setGroupName(group))
                    .build();

            ClientResponseObserver<Persistent.ReadReq, Persistent.ReadResp> observer = new ClientResponseObserver<Persistent.ReadReq, Persistent.ReadResp>() {
                private boolean _confirmed;
                private PersistentSubscription _subscription;
                private ClientCallStreamObserver<Persistent.ReadReq> _requestStream;

                @Override
                public void beforeStart(ClientCallStreamObserver<Persistent.ReadReq> requestStream) {
                    this._requestStream = requestStream;
                }

                @Override
                public void onNext(Persistent.ReadResp readResp) {
                    if (!_confirmed && readResp.hasSubscriptionConfirmation()) {
                        this._confirmed = true;
                        this._subscription = new PersistentSubscription(this._requestStream,
                                readResp.getSubscriptionConfirmation().getSubscriptionId());
                        result.complete(this._subscription);
                        return;
                    }

                    if (!_confirmed && readResp.hasEvent()) {
                        onError(new IllegalStateException("Unconfirmed persistent subscription received event"));
                        return;
                    }

                    if (_confirmed && !readResp.hasEvent()) {
                        onError(new IllegalStateException(
                                String.format("Confirmed persistent subscription %s received non-{event,checkpoint} variant",
                                        _subscription.getSubscriptionId())));
                        return;
                    }

                    listener.onEvent(this._subscription, ResolvedEvent.fromWire(readResp.getEvent()));
                }

                @Override
                public void onError(Throwable t) {
                    if (t instanceof StatusRuntimeException) {
                        Status s = ((StatusRuntimeException) t).getStatus();
                        if (s.getCode() == Status.Code.CANCELLED) {
                            listener.onCancelled(this._subscription);
                            return;
                        }
                    }

                    listener.onError(this._subscription, t);
                }

                @Override
                public void onCompleted() {
                    // Subscriptions should only complete on error.
                }
            };

            StreamObserver<Persistent.ReadReq> wireStream = client.read(observer);
            wireStream.onNext(req);

            return result;
        });
    }
}