Modify code and perform validation. Once it is done, resolve user and execute 
insertion operation
   
<div class="hint">
  Use <code>Mono#then(otherMono)</code>
  use <code>Mono#flatMap(user -> insert(user))</code>
</div>