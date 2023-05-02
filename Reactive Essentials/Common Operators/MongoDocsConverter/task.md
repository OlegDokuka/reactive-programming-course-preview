Find all `Document`s in the given collection (use `MongoCollection#find())` and convert them into `User`s entity (using the `convert` method) 
   
<div class="hint">
  Use <code>Flux.from</code> in order to adapt <code>Publisher</code> into <code>Flux</code>
  Use <code>Flux#map</code> to convert elements to <code>String</code>
</div>