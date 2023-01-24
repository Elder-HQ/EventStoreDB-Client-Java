# Changelog
All notable changes to this project will be documented in this file.

## [Unreleased]

### Added
- Add specific exceptions when delete stream operation fails. [EventStoreDB-Client-Java#208](https://github.com/EventStore/EventStoreDB-Client-Java/pull/208)
- Implement human-representation for `ExpectedVersion` types. [EventStoreDB-Client-Java#204](https://github.com/EventStore/EventStoreDB-Client-Java/pull/204)

### Fixed
- Fix server filtering sample code. [EventStoreDB-Client-Java#206](https://github.com/EventStore/EventStoreDB-Client-Java/pull/206)
- Fix `ConnectionSettingsBuilder` when dealing with keep-alive settings. [EventStoreDB-Client-Java#207](https://github.com/EventStore/EventStoreDB-Client-Java/pull/207)
- Fix `tombstoneStream` overload. [EventStoreDB-Client-Java#205](https://github.com/EventStore/EventStoreDB-Client-Java/pull/205)

## [4.0.0] - 2022-11-01

### Fixed
- Fix next expected version when appending to a stream. [EventStoreDB-Client-Java#196](https://github.com/EventStore/EventStoreDB-Client-Java/pull/196)
- Fix condition causing subscribers not to be completed. [EventStoreDB-Client-Java#193](https://github.com/EventStore/EventStoreDB-Client-Java/pull/193)
- Shutdown `GossipClient` after usage. [EventStoreDB-Client-Java#186](https://github.com/EventStore/EventStoreDB-Client-Java/pull/186)
- Fix channel lifecycle behaviour. [EventStoreDB-Client-Java#184](https://github.com/EventStore/EventStoreDB-Client-Java/pull/184)
- Do not shutdown client on leader reconnect attempt. [EventStoreDB-Client-Java#182](https://github.com/EventStore/EventStoreDB-Client-Java/pull/182)
- Fix error signals to the `GrpcClient` based on a `CompletableFuture`. [EventStoreDB-Client-Java#182](https://github.com/EventStore/EventStoreDB-Client-Java/pull/182)

### Changed
- Fix next expected version when appending to a stream. [EventStoreDB-Client-Java#196](https://github.com/EventStore/EventStoreDB-Client-Java/pull/196)
- Add additional logging for connection handling. [EventStoreDB-Client-Java#181](https://github.com/EventStore/EventStoreDB-Client-Java/pull/181)