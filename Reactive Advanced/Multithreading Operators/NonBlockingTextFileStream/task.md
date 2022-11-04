File reading is a blocking operation even though it is represented as a Stream. Run lines reading on a dedicated thread.
  
<div class="hint">
    Use <code>Flux.subscribeOn</code>
</div>