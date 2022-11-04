You are given with the content that has to be stored in 2 datastores. Though it sounds good, in
reality both operations are independent so any of them can failure. Therefore, as an engineer you
have to make sure that the whole operation is resilient and ensures atomicity and consistency in
both stores.

1) Ensure Transaction is rolled back in case of failure
2) Ensure All transactions are rolled back in case any of written operations has failed
3) Ensure Transaction lasts less than 1 sec
4) Retry connection opening if it has failed (connection opening is retryable operation)