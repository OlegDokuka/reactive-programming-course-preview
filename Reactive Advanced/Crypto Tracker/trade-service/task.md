1) Provide messages filtering and mapping in the `DefaultTradeService
.filterAndMapTradingEvents` method
2) Provide mapping to database message representation in the `DefaultTradeService
.mapToDomainTrade` method
3) Implement Resilient writing to both databases: 
    * collect elements into batches within a 1 second
    * in case there is disaster with any operation, make sure the elements going
     to be stored in a single batch and will be delivered together even though the
      batch is longer more than a second (e.g. collect longer if something happened
       during storing
      )   
    * write simultaneously
    * provide a timeout for each operation for a 1 second
    * retry up to 100 of times. Apply backoff
