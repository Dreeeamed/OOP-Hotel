package Repositories;

import Entities.Payment;

import java.util.List;

public interface IPaymentRepository {
    void addPayment(Payment payment);
    List<Payment> getAllPayments();
}