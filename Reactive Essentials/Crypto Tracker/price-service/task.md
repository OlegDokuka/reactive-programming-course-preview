Provide proper integration with `CryptoService`

1) Provide Message Filtering. Check that messages which comes to the `PriceService` is
 a proper price message. Use `MessageMapper` in order to validate messages.
2) Map raw representation of messages from `CryptoService` to `MessageDTO`
3) Provide messages aggregation: 
    * group messages by currency
    * calculate average price for a given ***window*** of time
    * window duration should start with a Default value equal to 30 secs
    * window duration should be configurable by user input. Use `requestedInterval` in
     order to listen and ***switch*** `window` interval dynamically.    