package com.transactions;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
     
    @Autowired
    TransactionRepository employeeRepository;
 
    @Autowired
    AccountsRepo accountRepo;
    
    @RequestMapping(method = RequestMethod.POST)
    public Transactions create(@RequestBody Transactions transaction){
    	
    	List<Accounts> allAccounts= accountRepo.findAll();
    	Accounts debitAccount = null;
    	Accounts creditAccount = null;
    	if(allAccounts  != null && allAccounts.size() > 0){
    		for(Accounts account : allAccounts){
    			if(account.getAccountId().equals(transaction.getFromAccountId())){
    				debitAccount = new Accounts();
    				debitAccount.setAccountId(account.getAccountId());
    				debitAccount.setId(account.getId());
    				debitAccount.setInitialBalance(account.getInitialBalance() - transaction.getAmount());
    			}
    			
    			if(account.getAccountId().equals(transaction.getToAccount())){
    				creditAccount = new Accounts();
    				creditAccount.setAccountId(account.getAccountId());
    				creditAccount.setId(account.getId());
    				creditAccount.setInitialBalance(account.getInitialBalance() + transaction.getAmount());
    			}
    		}
    	} 
    	
    	if(debitAccount != null && creditAccount != null)
    	{
    		accountRepo.save(debitAccount);
    		accountRepo.save(creditAccount);
    	}
    	
        Transactions result = employeeRepository.save(transaction);
        return result;
    }
     
    @RequestMapping(method = RequestMethod.GET, value="/all")
    public List<Transactions> get(@PathVariable String employeeId){
        return employeeRepository.findAll();
    }
     
     
}