public interface Payment {
    boolean processPayment(double amount);
}

class CashPayment implements Payment {
    @Override
    public boolean processPayment(double amount) {
        System.out.println("Processing cash payment of RM" + String.format("%.2f", amount));
        return true;
    }
}

class OnlineBankingPayment implements Payment {
    @Override
    public boolean processPayment(double amount) {
        System.out.println("Processing online banking payment of RM" + String.format("%.2f", amount));
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return true;
    }
}