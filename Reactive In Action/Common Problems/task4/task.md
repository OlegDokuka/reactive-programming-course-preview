You are working with a service which limits you in the number of invocations per second, and the
content size you can send at once. Given that limitations, you have to transform input flux in that
way, so the required limitations are followed properly as well. Note, the implementation has to be
efficient.

Rules:

1. send data to a server using the given client
2. MAX amount of sent buffers MUST be less or equals to 50 per request
3. frequency of client#send invocation MUST be not oftener than once per 500 Milliseconds
4. delivered results MUST be ordered (assume you are upploading fragmented files onto remote file
   system. Reordering chunks may lead to incorrect future reading of that file)
5. in case if send operation take more than 1 second it MUST be considered as hanged and be
   restarted