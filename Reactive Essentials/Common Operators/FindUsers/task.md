Find users that matches given country. If non found, propagate [UserNotFoundException](src%2FUserNotFoundException.java) 
   
<div class="hint">
  Use <code>Flux#filter</code>
  use <code>Flux#switchIfEmpty</code> to use another source when the main one is empty
</div>