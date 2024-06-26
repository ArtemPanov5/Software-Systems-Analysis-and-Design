package SSAD_Assignment3;

import java.util.*;

public class Assignment3_BankSystem {
    /**
     * Singleton_BankSystem is the class with using Singleton Pattern for ensuring uniqueness of the
     * bunking system object
     */
    static class Singleton_BankSystem {
        /**
         * uniqueness
         */
        private static Singleton_BankSystem instance = null;

        private Singleton_BankSystem getInstance() {
            if (instance == null) {
                instance = new Singleton_BankSystem();
            }
            return instance;
        }

        /**
         * Factory_BankAccounts is the static class in the Singleton_BankSystem class with using
         * Factory Pattern for creating different bank accounts
         */
        static class Factory_BankAccounts {
            public static HashMap<String, Singleton_BankSystem.BusinessAccount> BusinessAccounts1;
            public static HashMap<String, Singleton_BankSystem.CheckingAccount> CheckAccounts1;
            public static HashMap<String, Singleton_BankSystem.SavingsAccount> SaveAccounts1;
            public static HashSet<String> Owners1;

            public Factory_BankAccounts() {
            }
            /** Create - one of main methods*/
            public void Create(String type, String owner, double deposit) {
                Account account = null;
                if (type.equals("Savings")) {
                    account = new SavingsAccount(owner, deposit);
                    SaveAccounts1.put(owner, (SavingsAccount) account);
                    System.out.println("A new Savings account created for " + owner + " with an initial balance of $" + setUp(deposit) + ".");
                    Owners1.add(owner);

                } else if (type.equals("Checking")) {
                    account = new CheckingAccount(owner, deposit);
                    CheckAccounts1.put(owner, (CheckingAccount) account);
                    System.out.println("A new Checking account created for " + owner + " with an initial balance of $" + setUp(deposit) + ".");
                    Owners1.add(owner);
                } else if (type.equals("Business")) {
                    account = new BusinessAccount(owner, deposit);
                    BusinessAccounts1.put(owner, (BusinessAccount) account);
                    System.out.println("A new Business account created for " + owner + " with an initial balance of $" + setUp(deposit) + ".");
                    Owners1.add(owner);
                }
            }

            /** Constructor of subclass of Factory_BankAccounts of Singleton_BankSystem */
            public Factory_BankAccounts(HashMap<String, Singleton_BankSystem.SavingsAccount> SaveAccounts1,
                                        HashMap<String, Singleton_BankSystem.CheckingAccount> CheckAccounts1,
                                        HashMap<String, Singleton_BankSystem.BusinessAccount> BusinessAccounts1,
                                        HashSet<String> Owners) {
                Factory_BankAccounts.CheckAccounts1 = CheckAccounts1;
                Factory_BankAccounts.SaveAccounts1 = SaveAccounts1;
                Factory_BankAccounts.BusinessAccounts1 = BusinessAccounts1;
                Owners1 = Owners;
            }
        }

        /**
         * Using Facade Pattern for control of banking transactions
         */
        interface Account {
            void Deposit(double refill);

            void Withdraw(double withdrawalAmount);

            void Transfer(double transferAmount, String ownerOfTransferAccount);

            void View();

            void Deactivate();

            void Activate();
        }

        /**
         * Abstract general class for accounts
         */
        static abstract class Accounts extends Factory_BankAccounts implements Account {
            public String state = "Active";
            public String owner;
            public double deposit;
            public ArrayList<String> transactionHistory = new ArrayList<>();

            public Accounts(HashMap<String, SavingsAccount> SaveAccounts1, HashMap<String, CheckingAccount> CheckAccounts1, HashMap<String, BusinessAccount> BusinessAccounts1, HashSet<String> Owners) {
                super(SaveAccounts1, CheckAccounts1, BusinessAccounts1, Owners);
            }

            public Accounts() {
                super();
            }
        }

        /**
         * This is one of three type of account. This class inherits class "Accounts" which includes
         * implementations of main methods:
         * Create
         * Deposit
         * Withdraw
         * Transfer
         * View
         * Deactivate
         * Activate
         */
        static class SavingsAccount extends Accounts {
            public void Deactivate() {
                try {
                    if (state.equals("Inactive")) {
                        throw new DeactivateADeactivatedAccount(owner);
                    }
                    state = "Inactive";
                    System.out.println(owner + "'s account is now deactivated.");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    ;
                }
            }

            public void Activate() {
                try {
                    if (state.equals("Active")) {
                        throw new ActivateAnActivatedAccount(owner);
                    }
                    state = "Active";
                    System.out.println(owner + "'s account is now activated.");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }

            public void View() {
                System.out.println(owner + "'s " + "Account: Type: Savings, Balance: $" + setUp(deposit) +
                        ", State: " + state + ", Transactions: " + transactionHistory + ".");
            }

            public void Deposit(double refill) {
                deposit += refill;
                System.out.println(owner + " successfully deposited $" + setUp(refill) + ". New Balance: $" + setUp(deposit) + ".");
                transactionHistory.add("Deposit $" + setUp(refill));
            }

            public void Withdraw(double withdrawalAmount) {
                try {
                    if (state.equals("Inactive")) {
                        throw new WithdrawalFromAnInactiveAccount(owner);
                    }
                    if ((deposit - withdrawalAmount) < 0) {
                        throw new InsufficientFundsForTransferAndWithdraw(owner);
                    }
                    double commissionAmount = withdrawalAmount * 0.015;
                    deposit -= withdrawalAmount;
                    System.out.println(owner + " successfully withdrew $" + setUp(withdrawalAmount - commissionAmount) + ". New Balance: $" +
                            setUp(deposit) + ". Transaction Fee: $" + setUp(commissionAmount) + " (1.5%) in the system.");
                    transactionHistory.add("Withdrawal $" + setUp(withdrawalAmount));
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    ;
                }
            }

            public void Transfer(double transferAmount, String ownerOfTransferAccount) {
                try {
                    if (state.equals("Inactive")) {
                        throw new WithdrawalFromAnInactiveAccount(owner);
                    }
                    if ((deposit - transferAmount) < 0) {
                        throw new InsufficientFundsForTransferAndWithdraw(owner);
                    }
                    double commissionAmount = transferAmount * 0.015;
                    double creditAmount = transferAmount - commissionAmount;
                    this.deposit -= transferAmount;
                    boolean a = SaveAccounts1.containsKey(ownerOfTransferAccount);
                    boolean b = CheckAccounts1.containsKey(ownerOfTransferAccount);
                    boolean c = BusinessAccounts1.containsKey(ownerOfTransferAccount);
                    if (a && !b && !c) {
                        SaveAccounts1.get(ownerOfTransferAccount).setDeposit(creditAmount);
                        System.out.println(owner + " successfully transferred $" + setUp(creditAmount) + " to " + ownerOfTransferAccount +
                                ". New Balance: $" + setUp(deposit) + ". Transaction Fee: $" +
                                setUp(commissionAmount) + " (1.5%) in the system.");
                        transactionHistory.add("Transfer $" + setUp(transferAmount));
                    } else if (!a && b && !c) {
                        CheckAccounts1.get(ownerOfTransferAccount).setDeposit(creditAmount);
                        System.out.println(owner + " successfully transferred $" + setUp(creditAmount) + " to " + ownerOfTransferAccount +
                                ". New Balance: $" + setUp(deposit) + ". Transaction Fee: $" +
                                setUp(commissionAmount) + " (1.5%) in the system.");
                        transactionHistory.add("Transfer $" + setUp(transferAmount));
                    } else if (!a && !b && c) {
                        BusinessAccounts1.get(ownerOfTransferAccount).setDeposit(creditAmount);
                        System.out.println(owner + " successfully transferred $" + setUp(creditAmount) + " to " + ownerOfTransferAccount +
                                ". New Balance: $" + setUp(deposit) + ". Transaction Fee: $" +
                                setUp(commissionAmount) + " (1.5%) in the system.");
                        transactionHistory.add("Transfer $" + setUp(transferAmount));
                    } else {
                        throw new AccountDoesNotExist(ownerOfTransferAccount);
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }

            public SavingsAccount(String owner, double deposit) {
                super();
                this.owner = owner;
                this.deposit = deposit;
            }

            public void setDeposit(double creditAmount) {
                deposit += creditAmount;
            }
        }

        /**
         * This is one of three type of account. This class inherits class "Accounts" which includes
         * implementations of main methods. This class inherits class "Accounts"
         */
        static class CheckingAccount extends Accounts {
            public void Deactivate() {
                try {
                    if (state.equals("Inactive")) {
                        throw new DeactivateADeactivatedAccount(owner);
                    }
                    state = "Inactive";
                    System.out.println(owner + "'s account is now deactivated.");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    ;
                }
            }

            public void Activate() {
                try {
                    if (state.equals("Active")) {
                        throw new ActivateAnActivatedAccount(owner);
                    }
                    state = "Active";
                    System.out.println(owner + "'s account is now activated.");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }

            public void View() {
                System.out.println(owner + "'s " + "Account: Type: Checking, Balance: $" + setUp(deposit) +
                        ", State: " + state + ", Transactions: " + transactionHistory + ".");
            }

            public void Deposit(double refill) {
                deposit += refill;
                System.out.println(owner + " successfully deposited $" + setUp(refill) + ". New Balance: $" + setUp(deposit) + ".");
                transactionHistory.add("Deposit $" + setUp(refill));
            }

            public void Withdraw(double withdrawalAmount) {
                try {
                    if (state.equals("Inactive")) {
                        throw new WithdrawalFromAnInactiveAccount(owner);
                    }
                    if ((deposit - withdrawalAmount) < 0) {
                        throw new InsufficientFundsForTransferAndWithdraw(owner);
                    }
                    double commissionAmount = withdrawalAmount * 0.02;
                    deposit -= withdrawalAmount;
                    System.out.println(owner + " successfully withdrew $" + setUp(withdrawalAmount - commissionAmount) + ". New Balance: $" +
                            setUp(deposit) + ". Transaction Fee: $" + setUp(commissionAmount) + " (2.0%) in the system.");
                    transactionHistory.add("Withdrawal $" + setUp(withdrawalAmount));
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }

            public void Transfer(double transferAmount, String ownerOfTransferAccount) {
                try {
                    if (state.equals("Inactive")) {
                        throw new WithdrawalFromAnInactiveAccount(owner);
                    }
                    if ((deposit - transferAmount) < 0) {
                        throw new InsufficientFundsForTransferAndWithdraw(owner);
                    }
                    double commissionAmount = transferAmount * 0.02;
                    double creditAmount = transferAmount - commissionAmount;
                    this.deposit -= transferAmount;
                    boolean a = SaveAccounts1.containsKey(ownerOfTransferAccount);
                    boolean b = CheckAccounts1.containsKey(ownerOfTransferAccount);
                    boolean c = BusinessAccounts1.containsKey(ownerOfTransferAccount);
                    if (a && !b && !c) {
                        SaveAccounts1.get(ownerOfTransferAccount).setDeposit(creditAmount);
                        System.out.println(owner + " successfully transferred $" + setUp(creditAmount) + " to " + ownerOfTransferAccount +
                                ". New Balance: $" + setUp(deposit) + ". Transaction Fee: $" +
                                setUp(commissionAmount) + " (2.0%) in the system.");
                        transactionHistory.add("Transfer $" + setUp(transferAmount));
                    } else if (!a && b && !c) {
                        CheckAccounts1.get(ownerOfTransferAccount).setDeposit(creditAmount);
                        System.out.println(owner + " successfully transferred $" + setUp(creditAmount) + " to " + ownerOfTransferAccount +
                                ". New Balance: $" + setUp(deposit) + ". Transaction Fee: $" +
                                setUp(commissionAmount) + " (2.0%) in the system.");
                        transactionHistory.add("Transfer $" + setUp(transferAmount));
                    } else if (!a && !b && c) {
                        BusinessAccounts1.get(ownerOfTransferAccount).setDeposit(creditAmount);
                        System.out.println(owner + " successfully transferred $" + setUp(creditAmount) + " to " + ownerOfTransferAccount +
                                ". New Balance: $" + setUp(deposit) + ". Transaction Fee: $" +
                                setUp(commissionAmount) + " (2.0%) in the system.");
                        transactionHistory.add("Transfer $" + setUp(transferAmount));
                    } else {
                        throw new AccountDoesNotExist(ownerOfTransferAccount);
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }

            public CheckingAccount(String owner, double deposit) {
                this.owner = owner;
                this.deposit = deposit;
            }

            public void setDeposit(double creditAmount) {
                deposit += creditAmount;
            }
        }

         /**
         * This is one of three type of account. This class inherits class "Accounts" which includes
         * implementations of main methods. This class inherits class "Accounts"
         */
        static class BusinessAccount extends Accounts {
            public void Deactivate() {
                try {
                    if (state.equals("Inactive")) {
                        throw new DeactivateADeactivatedAccount(owner);
                    }
                    state = "Inactive";
                    System.out.println(owner + "'s account is now deactivated.");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }

            public void Activate() {
                try {
                    if (state.equals("Active")) {
                        throw new ActivateAnActivatedAccount(owner);
                    }
                    state = "Active";
                    System.out.println(owner + "'s account is now activated.");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }

            public void View() {
                System.out.println(owner + "'s " + "Account: Type: Business, Balance: $" + setUp(deposit) +
                        ", State: " + state + ", Transactions: " + transactionHistory + ".");
            }

            public void Deposit(double refill) {
                deposit += refill;
                System.out.println(owner + " successfully deposited $" + setUp(refill) + ". New Balance: $" + setUp(deposit) + ".");
                transactionHistory.add("Deposit $" + setUp(refill));
            }

            public void Withdraw(double withdrawalAmount) {
                try {
                    if (state.equals("Inactive")) {
                        throw new WithdrawalFromAnInactiveAccount(owner);
                    }
                    if ((deposit - withdrawalAmount) < 0) {
                        throw new InsufficientFundsForTransferAndWithdraw(owner);
                    }
                    double commissionAmount = withdrawalAmount * 0.025;
                    deposit -= withdrawalAmount;
                    System.out.println(owner + " successfully withdrew $" + setUp(withdrawalAmount - commissionAmount) + ". New Balance: $" +
                            setUp(deposit) + ". Transaction Fee: $" + setUp(commissionAmount) + " (2.5%) in the system.");
                    transactionHistory.add("Withdrawal $" + setUp(withdrawalAmount));
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }

            public void Transfer(double transferAmount, String ownerOfTransferAccount) {
                try {
                    if (state.equals("Inactive")) {
                        throw new WithdrawalFromAnInactiveAccount(owner);
                    }
                    if ((deposit - transferAmount) < 0) {
                        throw new InsufficientFundsForTransferAndWithdraw(owner);
                    }
                    double commissionAmount = transferAmount * 0.025;
                    double creditAmount = transferAmount - commissionAmount;
                    this.deposit -= transferAmount;
                    boolean a = SaveAccounts1.containsKey(ownerOfTransferAccount);
                    boolean b = CheckAccounts1.containsKey(ownerOfTransferAccount);
                    boolean c = BusinessAccounts1.containsKey(ownerOfTransferAccount);
                    if (a && !b && !c) {
                        SaveAccounts1.get(ownerOfTransferAccount).setDeposit(creditAmount);
                        System.out.println(owner + " successfully transferred $" + setUp(creditAmount) + " to " + ownerOfTransferAccount +
                                ". New Balance: $" + setUp(deposit) + ". Transaction Fee: $" +
                                setUp(commissionAmount) + " (2.5%) in the system.");
                        transactionHistory.add("Transfer $" + setUp(transferAmount));
                    } else if (!a && b && !c) {
                        CheckAccounts1.get(ownerOfTransferAccount).setDeposit(creditAmount);
                        System.out.println(owner + " successfully transferred $" + setUp(creditAmount) + " to " + ownerOfTransferAccount +
                                ". New Balance: $" + setUp(deposit) + ". Transaction Fee: $" +
                                setUp(commissionAmount) + " (2.5%) in the system.");
                        transactionHistory.add("Transfer $" + setUp(transferAmount));
                    } else if (!a && !b && c) {
                        BusinessAccounts1.get(ownerOfTransferAccount).setDeposit(creditAmount);
                        System.out.println(owner + " successfully transferred $" + setUp(creditAmount) + " to " + ownerOfTransferAccount +
                                ". New Balance: $" + setUp(deposit) + ". Transaction Fee: $" +
                                setUp(commissionAmount) + " (2.5%) in the system.");
                        transactionHistory.add("Transfer $" + setUp(transferAmount));
                    } else {
                        throw new AccountDoesNotExist(ownerOfTransferAccount);
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }

            public BusinessAccount(String owner, double deposit) {
                this.owner = owner;
                this.deposit = deposit;
            }

            public void setDeposit(double creditAmount) {
                deposit += creditAmount;
            }
        }

        /**
         * Exceptions classes inherits class "Exceptions"
         */
        static class AccountDoesNotExist extends Exception {
            private String Message;

            public AccountDoesNotExist(String name) {
                this.Message = name;
            }

            public String getMessage() {
                return "Error: Account " + Message + " does not exist.";
            }
        }

        static class WithdrawalFromAnInactiveAccount extends Exception {
            private String Message;

            public WithdrawalFromAnInactiveAccount(String name) {
                this.Message = name;
            }

            public String getMessage() {
                return "Error: Account " + Message + " is inactive.";
            }
        }

        static class InsufficientFundsForTransferAndWithdraw extends Exception {
            private String Message;

            public InsufficientFundsForTransferAndWithdraw(String transaction) {
                this.Message = transaction;
            }

            public String getMessage() {
                return "Error: Insufficient funds for " + Message + ".";
            }
        }

        static class ActivateAnActivatedAccount extends Exception {
            private String Message;

            public ActivateAnActivatedAccount(String name) {
                this.Message = name;
            }

            public String getMessage() {
                return "Error: Account " + Message + " is already activated.";
            }
        }

        static class DeactivateADeactivatedAccount extends Exception {
            private String Message;

            public DeactivateADeactivatedAccount(String name) {
                this.Message = name;
            }

            public String getMessage() {
                return "Error: Account " + Message + " is already deactivated.";
            }
        }

        public static String setUp(double value) {
            String result = String.format("%.3f", value);
            result = result.replace(",",".");
            return result;
        }
    }

    public static void main(String[] args) {

        HashMap<String, Singleton_BankSystem.SavingsAccount> SaveAccounts = new HashMap<>();
        HashMap<String, Singleton_BankSystem.CheckingAccount> CheckAccounts = new HashMap<>();
        HashMap<String, Singleton_BankSystem.BusinessAccount> BusinessAccounts = new HashMap<>();
        HashSet<String> Owners = new HashSet<>();
        Scanner inputData = new Scanner(System.in);

        int N = inputData.nextInt();
        inputData.nextLine();

        /** Initialization of parameters*/
        String command = null;
        String type = null;
        String OwnerName = null;
        String Name2 = null;

        Singleton_BankSystem.Factory_BankAccounts Bank_System = new Singleton_BankSystem.Factory_BankAccounts(SaveAccounts, CheckAccounts, BusinessAccounts, Owners);

        /** Parsing of input data and choose according to need with help Swith - case.*/
        for (int i = 0; i < N; i++) {
            command = inputData.next();
            switch (command) {
                case ("Create"):
                    inputData.next();
                    type = inputData.next();
                    OwnerName = inputData.next();
                    double Sum = Double.parseDouble(inputData.next());
                    inputData.nextLine();
                    Bank_System.Create(type, OwnerName, Sum);
                    boolean a = Bank_System.SaveAccounts1.containsKey(OwnerName);
                    boolean b = Bank_System.CheckAccounts1.containsKey(OwnerName);
                    boolean c = Bank_System.BusinessAccounts1.containsKey(OwnerName);
                    if (a && !b && !c) {
                        Bank_System.SaveAccounts1.get(OwnerName).transactionHistory.add("Initial Deposit $" + Singleton_BankSystem.setUp(Sum));
                    } else if (!a && b && !c) {
                        Bank_System.CheckAccounts1.get(OwnerName).transactionHistory.add("Initial Deposit $" + Singleton_BankSystem.setUp(Sum));
                    } else if (!a && !b && c) {
                        Bank_System.BusinessAccounts1.get(OwnerName).transactionHistory.add("Initial Deposit $" + Singleton_BankSystem.setUp(Sum));
                    }
                    continue;
                case ("Deposit"):
                    OwnerName = inputData.next();
                    Sum = Double.parseDouble(inputData.next());
                    inputData.nextLine();
                    if (!Bank_System.Owners1.contains(OwnerName)) {
                        System.out.println("Error: Account " + OwnerName + " does not exist.");
                        continue;
                    } else {
                        a = Bank_System.SaveAccounts1.containsKey(OwnerName);
                        b = Bank_System.CheckAccounts1.containsKey(OwnerName);
                        c = Bank_System.BusinessAccounts1.containsKey(OwnerName);
                        if (a && !b && !c) {
                            Bank_System.SaveAccounts1.get(OwnerName).Deposit(Sum);
                        } else if (!a && b && !c) {
                            Bank_System.CheckAccounts1.get(OwnerName).Deposit(Sum);
                        } else if (!a && !b && c) {
                            Bank_System.BusinessAccounts1.get(OwnerName).Deposit(Sum);
                        }
                    }
                    continue;
                case ("Withdraw"):
                    OwnerName = inputData.next();
                    Sum = Double.parseDouble(inputData.next());
                    inputData.nextLine();
                    if (!Bank_System.Owners1.contains(OwnerName)) {
                        System.out.println("Error: Account " + OwnerName + " does not exist.");
                        continue;
                    } else {
                        a = Bank_System.SaveAccounts1.containsKey(OwnerName);
                        b = Bank_System.CheckAccounts1.containsKey(OwnerName);
                        c = Bank_System.BusinessAccounts1.containsKey(OwnerName);
                        if (a && !b && !c) {
                            Bank_System.SaveAccounts1.get(OwnerName).Withdraw(Sum);
                        } else if (!a && b && !c) {
                            Bank_System.CheckAccounts1.get(OwnerName).Withdraw(Sum);
                        } else if (!a && !b && c) {
                            Bank_System.BusinessAccounts1.get(OwnerName).Withdraw(Sum);
                        }
                    }
                    continue;
                case ("Transfer"):
                    OwnerName = inputData.next();
                    Name2 = inputData.next();
                    Sum = Double.parseDouble(inputData.next());
                    inputData.nextLine();
                    if (!Bank_System.Owners1.contains(OwnerName)) {
                        System.out.println("Error: Account " + OwnerName + " does not exist.");
                        continue;
                    } else if (!Bank_System.Owners1.contains(Name2)) {
                        System.out.println("Error: Account " + Name2 + " does not exist.");
                        continue;
                    } else {
                        a = Bank_System.SaveAccounts1.containsKey(OwnerName);
                        b = Bank_System.CheckAccounts1.containsKey(OwnerName);
                        c = Bank_System.BusinessAccounts1.containsKey(OwnerName);
                        if (a && !b && !c) {
                            Bank_System.SaveAccounts1.get(OwnerName).Transfer(Sum, Name2);
                        } else if (!a && b && !c) {
                            Bank_System.CheckAccounts1.get(OwnerName).Transfer(Sum, Name2);
                        } else if (!a && !b && c) {
                            Bank_System.BusinessAccounts1.get(OwnerName).Transfer(Sum, Name2);
                        }
                    }
                    continue;
                case ("View"):
                    OwnerName = inputData.next();
                    inputData.nextLine();
                    if (!Bank_System.Owners1.contains(OwnerName)) {
                        System.out.println("Error: Account " + OwnerName + " does not exist.");
                        continue;
                    } else {
                        a = Bank_System.SaveAccounts1.containsKey(OwnerName);
                        b = Bank_System.CheckAccounts1.containsKey(OwnerName);
                        c = Bank_System.BusinessAccounts1.containsKey(OwnerName);
                        if (a && !b && !c) {
                            Bank_System.SaveAccounts1.get(OwnerName).View();
                        } else if (!a && b && !c) {
                            Bank_System.CheckAccounts1.get(OwnerName).View();
                        } else if (!a && !b && c) {
                            Bank_System.BusinessAccounts1.get(OwnerName).View();
                        }
                    }
                    continue;
                case ("Deactivate"):
                    OwnerName = inputData.next();
                    inputData.nextLine();
                    if (!Bank_System.Owners1.contains(OwnerName)) {
                        System.out.println("Error: Account " + OwnerName + " does not exist.");
                        continue;
                    } else {
                        a = Bank_System.SaveAccounts1.containsKey(OwnerName);
                        b = Bank_System.CheckAccounts1.containsKey(OwnerName);
                        c = Bank_System.BusinessAccounts1.containsKey(OwnerName);
                        if (a && !b && !c) {
                            Bank_System.SaveAccounts1.get(OwnerName).Deactivate();
                        } else if (!a && b && !c) {
                            Bank_System.CheckAccounts1.get(OwnerName).Deactivate();
                        } else if (!a && !b && c) {
                            Bank_System.BusinessAccounts1.get(OwnerName).Deactivate();
                        }
                    }
                    continue;
                case ("Activate"):
                    OwnerName = inputData.next();
                    inputData.nextLine();
                    if (!Bank_System.Owners1.contains(OwnerName)) {
                        System.out.println("Error: Account " + OwnerName + " does not exist.");
                        continue;
                    } else {
                        a = Bank_System.SaveAccounts1.containsKey(OwnerName);
                        b = Bank_System.CheckAccounts1.containsKey(OwnerName);
                        c = Bank_System.BusinessAccounts1.containsKey(OwnerName);
                        if (a && !b && !c) {
                            Bank_System.SaveAccounts1.get(OwnerName).Activate();
                        } else if (!a && b && !c) {
                            Bank_System.CheckAccounts1.get(OwnerName).Activate();
                        } else if (!a && !b && c) {
                            Bank_System.BusinessAccounts1.get(OwnerName).Activate();
                        }
                    }
            }
        }
    }
}
