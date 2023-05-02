Refactor given imperative to the same reactive one 
   
<div class="hint">
  Use <code>flatMapIterable</code> to convert <code>Mono[List[Endpoint]]</code> to 
  Use <code>concatMap</code> to to reflect sequential calls to remove server
  Use <code>Mono#fromFuture</code> to convert Future to Mono
</div>