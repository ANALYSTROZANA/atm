package atm;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
class UserAccount{
    private int accountNumber;
    private int PIN;
    private double availableBalance;
    
    public UserAccount(int accountNumber,int PIN, double availableBalance){
      this.accountNumber=accountNumber;
      this.PIN=PIN;
      this.availableBalance=availableBalance;
    }
    public int getAccountNumber(){
     return accountNumber;   
    }
    public int getPIN(){
        return PIN;
    }
    public double getAvailableBalance(){
        return availableBalance;
    }
    public boolean validatePIN(String inputPIN){
        return Integer.toString(PIN).equals(inputPIN);
    }
    public void setAvailableBalance(double balance){
        availableBalance=balance;
    }
}
class ATMBankDatabase{
    private UserAccount[] accounts;
    public ATMBankDatabase(){
        accounts=new UserAccount[1];
        accounts[0]=new UserAccount(12345,1234,1000.0);
    }
    public UserAccount getAccount(int accountNumber){
        for(UserAccount account:accounts){
            if(account.getAccountNumber()==accountNumber){
               return account; 
            }
        }
        return null;
    }
}
class ATMKeyPad{
    public String getInput(){
     return JOptionPane.showInputDialog(null, "Enter data:");   
    }
}
class ATMCashDispenser{
    public void dispenseCash(double amount){
        JOptionPane.showMessageDialog(null,"Dispensing$" +amount,"Cash Dispensed",JOptionPane.INFORMATION_MESSAGE);
    }
}
class Transaction{
int accountNumber;
ATMBankDatabase bankDatabase;

public Transaction( int accountNumber,ATMBankDatabase bankDatabase){
this.accountNumber =accountNumber;
this.bankDatabase=bankDatabase;
}
public int getAccountNumber(){
    return accountNumber;
}
public ATMBankDatabase getBankDatabase(){
    return bankDatabase;
}
public void execute(){
    
}
}
class Withdraw extends Transaction{
  private double amount;
  
public Withdraw(int accountNumber, ATMBankDatabase bankDatabase,double amount){
  super(accountNumber, bankDatabase); 
  this.amount=amount;
} 
public void execute(){
    UserAccount account=getBankDatabase().getAccount(getAccountNumber());
    if(account!=null&& account.getAvailableBalance()>=amount){
        account.setAvailableBalance(account.getAvailableBalance()-amount);
        ATMCashDispenser cashDispenser=new ATMCashDispenser();
        cashDispenser.dispenseCash(amount);
        JOptionPane.showMessageDialog(null,"Withdrawal of $"+amount+"successful.","success",JOptionPane.INFORMATION_MESSAGE);
    }
    else{
JOptionPane.showMessageDialog(null,"Withdrawal failed.insufficient funds or invalid account.","Withdrawal Error",JOptionPane.ERROR_MESSAGE);
}
} 
}
class ATMGUI extends JFrame{
    private ATMBankDatabase bankDatabase;
    
    private JTextField accountField;
    private JPasswordField pinField;
    private JTextField amountField;
    private JButton loginButton;
    private JButton WithdrawButton;
    
    public ATMGUI(ATMBankDatabase bankDatabase){
        this.bankDatabase=bankDatabase;
        setTitle("ATM Login");
        setSize(400,250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        
        JLabel accountLabel=new JLabel("AccountNumber:");
        accountLabel.setBounds(20,20,200,20);
        add(accountLabel);
        
        accountField=new JTextField();
        accountField.setBounds(220,20,150,30);
        add(accountField);
        
        JLabel pinLabel=new JLabel("PIN:");
        pinLabel.setBounds(20,60,200,20);
        add(pinLabel);
        
        pinField=new JPasswordField();
        pinField.setBounds(220,60,150,30);
        add(pinField);
        
        loginButton=new JButton("Login");
        loginButton.setBounds(20,100,100,30);
        add(loginButton);
        
        JLabel amountLabel=new JLabel("Enter Withdrawal amount:");
        amountLabel.setBounds(20,150,200,20);
        add(amountLabel);
        
        amountField=new JTextField();
        amountField.setBounds(220,150,150,30);
        add(amountField);
        
        WithdrawButton=new JButton("Withdraw");
        WithdrawButton.setBounds(20,190,100,30);
        add(WithdrawButton);
        
        loginButton.addActionListener(new ActionListener(){
             public void actionPerformed(ActionEvent e){
                 int accountNumber=Integer.parseInt(accountField.getText());
                 char[] pinChars=pinField.getPassword();
                 String pin=new String(pinChars);
                 
                 UserAccount userAccount=bankDatabase.getAccount(accountNumber);
                
                 if(userAccount!=null){
                   try{
                       if(userAccount.validatePIN(pin)){
                           JOptionPane.showMessageDialog(null,"Login successiful.","success",JOptionPane.INFORMATION_MESSAGE );
                       }
                       else{
                           throw new InvalidPINException("Invalid PIN");
                       }
                   }
                   catch(InvalidPINException ex){
                       JOptionPane.showMessageDialog(null,ex.getMessage(),"Login Error",JOptionPane.ERROR_MESSAGE);
                   }
                   
                 }
                 else{
                   JOptionPane.showMessageDialog(null,"Account not found.","Login Error",JOptionPane.ERROR_MESSAGE);  
                 }
             }
        }
        );
       WithdrawButton.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent e){
            int accountNumber=Integer.parseInt(accountField.getText());
            double withdrawalAmount=Double.parseDouble(amountField.getText());
            Transaction withdrawalTransaction=new Withdraw(accountNumber,bankDatabase,withdrawalAmount);
           withdrawalTransaction.execute();
        }   
       });
                
        
    }
}
class InvalidPINException extends Exception{
    public InvalidPINException(String message){
        super(message);
    }
}
/**JOPtionpane.showMessageDialog

 *
 * @author ROZANA
 */
public class ATM {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
      ATMBankDatabase bankDatabase=new ATMBankDatabase(); 
      SwingUtilities.invokeLater(new Runnable(){
          public void run(){
              new ATMGUI(bankDatabase).setVisible(true);
          }
      });// TODO code application logic here
    }
    
}
