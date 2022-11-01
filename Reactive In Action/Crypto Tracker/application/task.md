Provide implementations for a missing methods:

1) Implement `ApplicationRunner.handleRequestedAveragePriceIntervalValue`.
    Provide Mapping of inbound messages from WebSocket to a stream of Interval.
    Make sure interval is within (0, 60] range and can ***continue*** working in case
     of malformed strings   
1) Implement `ApplicationRunner.handleOutgoingStreamBackpressure` general backpressure
 handling using a ***strategy***