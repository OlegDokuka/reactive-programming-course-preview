public interface RefCounted {

	long refCount();

	void release();
}