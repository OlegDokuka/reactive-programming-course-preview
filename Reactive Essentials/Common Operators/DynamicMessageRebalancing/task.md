Switch on the next target to send messages when the balancer notifies you 
   
<div class="hint">
  Use <code>Flux#concatWith</code> to start with active known target and then listen 
to notifications
  Use <code>Flux#switchMap</code> to replace sending process to new target when the 
exisitin was obsolete
</div>