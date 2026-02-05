package Repositories;

import Entities.Payment;

public interface IPaymentRepository extends IRepository<Payment> {
    void createTable();
}