public interface Deserializer<In, Out> {
    Out deserialize(In input);
}
