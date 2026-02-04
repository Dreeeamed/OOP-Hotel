package Services;

import Entities.Payment;
import Exceptions.PaymentDeclinedException;
import Repositories.IPaymentRepository;

public class PaymentService {
    private final IPaymentRepository paymentRepo;

    public PaymentService(IPaymentRepository paymentRepo) {
        this.paymentRepo = paymentRepo;
    }

    public void processPayment(int resId, double amount, String method) throws PaymentDeclinedException {
        if ("DECLINE".equalsIgnoreCase(method)) {
            throw new PaymentDeclinedException("Bank rejected the transaction.");
        }
        if (amount > 10000) {
            throw new PaymentDeclinedException("Amount exceeds transaction limit.");
        }

        Payment p = new Payment(resId, amount);
        paymentRepo.addPayment(p);
        System.out.println("Payment recorded successfully.");
    }
}