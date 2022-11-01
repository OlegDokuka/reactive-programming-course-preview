Create `Flux` that ***generates*** a [Fibonacci sequence](https://en.wikipedia.org/wiki/Fibonacci_number) with 20 iterations depth
   
<div class="hint">
  Use <code>Flux.generate(stateSupplier, (state, sink) -> state)</code>.
  Consider to use и <code>Flux#startWith</code> in order to supply the very first element of the Fibonacci sequence
</div>