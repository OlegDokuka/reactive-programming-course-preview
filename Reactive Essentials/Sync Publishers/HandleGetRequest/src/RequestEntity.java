public class RequestEntity<T> {

   final HttpMethod method;
   final T data;

   public RequestEntity(HttpMethod method, T data) {
      this.method = method;
      this.data = data;
   }
}
