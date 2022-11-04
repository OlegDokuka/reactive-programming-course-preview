import java.util.List;

public interface PaymentsHistoryJpaRepository {

	List<Payment> findAllByUserId(String userId);
}
