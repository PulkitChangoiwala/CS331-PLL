import java.io.*;
import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.io.File; 
import java.io.FileWriter; 
import java.io.IOException; 

class Account{
	

	private long account_number;
	private long bank_balance;
	private Lock balanceChangeLock;
	private Condition sufficientFundsCondition;

	public Account(long account_number, long bank_balance){
		this.account_number = account_number;
		this.bank_balance = bank_balance;
		balanceChangeLock = new ReentrantLock();
		sufficientFundsCondition = balanceChangeLock.newCondition();
	}

	public void deposit(long amount){

		balanceChangeLock.lock();
		try{
			System.out.print("Depositing" + amount);
			long new_bal = bank_balance + amount;
			System.out.println(", new balance is" + new_bal);
			bank_balance = new_bal;
			sufficientFundsCondition.signalAll();
		}
		finally
		{
			balanceChangeLock.unlock();
		}
	
	}

	public void withdraw(long amount)
		throws InterruptedException
	{

		balanceChangeLock.lock();

		try
		{
			while(bank_balance < amount){
				sufficientFundsCondition.await();
			}

/*			if(bank_balance < amount){
				System.out.println(" !!! Insufficient Balance !!!");
				return;
			}*/

			System.out.print("Withdrawing" + amount);
			long new_bal = bank_balance - amount;
			System.out.println(", new balance is" + new_bal);
			bank_balance = new_bal;
		}


		finally{
			balanceChangeLock.unlock();
		}
	
	}


	public long get_balance(){
		return bank_balance;
	}

	public long get_account_no(){
		return account_number;
	}
}



class LinkedList { 

	Node head;
	int branch;
	long next_ac_no;  
	private Lock branchChangeLock;

	LinkedList(int branch){
		this.branch = branch;
		this.next_ac_no = (long)(branch * (1e9));
		branchChangeLock = new ReentrantLock();
	}


	class Node { 

		Account bank_account;
		Node next; 

		// Constructor 
		Node(long ac_no, long bank_balance) 
		{ 
			bank_account = new Account(ac_no, bank_balance);
			next = null; 
		} 
	} 

	// Method to insert a new node 

	public Node head_ptr(){
		branchChangeLock.lock();
		try{

			return this.head;

		}

		finally {

			branchChangeLock.unlock();

		}

		
	}
	public void insert(long ac_no, long balance) 
	{ 

			Node new_node = new Node(ac_no, balance); 
			new_node.next = null; 

			if (head == null) { 
				head = new_node; 
			} 
			else { 

				Node last = head; 
				while (last.next != null) { 
					last = last.next; 
				} 

				// Insert the new_node at last node 
				last.next = new_node; 
			} 

			// Return the list by head 
			return;
		


	} 

	public void add_account(long amount){

		branchChangeLock.lock();
		
		try{

			this.insert(next_ac_no, amount);
			next_ac_no = next_ac_no+1;

		}

		finally {

			branchChangeLock.unlock();

		} 

	}


	public void delete(long ac_no){

		if(head == null) return;
		if(head.bank_account.get_account_no() == ac_no){
			Node tmp = head;
			head = head.next;
			return;
		}
		LinkedList.Node itr = head;
		while(itr.next!=null && itr.next.bank_account.get_account_no() != ac_no){
			head = head.next;
		}
		Node temp = itr.next;
		itr.next = itr.next.next;
		return;
	}


	public void delete_account(long ac_no){

		branchChangeLock.lock();
		
		try{

			this.delete(ac_no);
			
		}

		finally {

			branchChangeLock.unlock();

		} 

	}

	public long ac_balance(long ac_no) 
	{       

		    branchChangeLock.lock();

		    try{

		    	Node itr = head;
				long balance;

				while(itr!=null && itr.bank_account.get_account_no() != ac_no){
					itr = itr.next;
				}

				// Return the list by head 
				if(itr==null) balance = 0;
				else balance = itr.bank_account.get_balance();
				return balance;

		    }

		    finally {
		    	branchChangeLock.unlock();
		    }

					


	} 
	public void printList(LinkedList list) 
	{ 
		Node currNode = list.head; 

		System.out.print("Branch: "); 

		// Traverse through the LinkedList 
		while (currNode != null) { 
			// Print the data at current node 
			System.out.print("Account Number" + currNode.bank_account.get_account_no() + " "); 
			System.out.println("Account Balance" + currNode.bank_account.get_balance() + " "); 
			// Go to next node 
			currNode = currNode.next; 
		}
	}

 
} 




class GNB {

	static LinkedList gnb_branches[]  = new LinkedList[10];
	

	public static void main(String args[]){
		
		for(int i=0;i<10;++i){
			gnb_branches[i] = new LinkedList(i);
		}




		ExecutorService [] updaters;
		updaters = new ExecutorService[10];


		for(int i=0; i<10;++i){
			updaters[i] = Executors.newFixedThreadPool(10);
		}


		//File ac = new File("accounts.txt");
		File am = new File("amount.txt");
		Scanner input, input1;
		

		try {  
			//input = new Scanner(ac); 
			input1 = new Scanner(am); 


			for(int j = 0; j < 10; j++){
			for(int i = 0; i < 100000; i++){
				//String word1 = input.next();
   				String word2 = input1.next();
   				//long ac_no =  Long.parseLong(word1);
   				long amount = Long.parseLong(word2);
   				updaters[j].execute(new add_customer_runnable(amount, gnb_branches[j]));

			}
		}

	}
		catch(Exception ex){}

		
	



		File queries = new File("transactions.txt");
		Scanner query = null;
		ArrayList<ArrayList<Long>> arr = new ArrayList<ArrayList<Long>>();
		try {
			query = new Scanner(queries);

		
			while(query.hasNextLine()){
			String line = query.nextLine();
			String[] splitStr = line.split("\\s+");
			ArrayList<Long> a = new ArrayList<Long>();
			for(String s : splitStr){
				a.add(Long.parseLong(s));
			}
			arr.add(a);
		}
	}
		catch(Exception E){}
		


		for(int i=0; i<arr.size(); ++i){

			int branch = (int)(arr.get(i).get(1)/(1e9));
			// withdraw money
			if(arr.get(i).get(0) == 1){
				long amount = arr.get(i).get(2);
				updaters[branch].execute(new withdraw_runnable(arr.get(i).get(1), amount, gnb_branches[branch].head_ptr()));
			}
			// deposit money
			else if(arr.get(i).get(0) == 2){
				long amount = arr.get(i).get(2);
				updaters[branch].execute(new deposit_runnable(arr.get(i).get(1), amount, gnb_branches[branch].head_ptr()));
			}
			// transfer money
			else if(arr.get(i).get(0) == 3){

				long amount = arr.get(i).get(3);
				long ac_no1 = arr.get(i).get(1);
				long ac_no2 = arr.get(i).get(2);
				int branch2 = (int)(ac_no2/(1e9));
				updaters[branch].execute(new transfer_runnable(ac_no1, ac_no2, amount, gnb_branches[branch].head_ptr(), gnb_branches[branch2].head_ptr() ));


			}
			// add account
			else if(arr.get(i).get(0) == 4){
				long amount =  arr.get(i).get(2);
   				updaters[branch].execute(new add_customer_runnable(amount, gnb_branches[branch]));

			}
			// delete account
			else if(arr.get(i).get(0) == 5){																																					
				updaters[branch].execute(new delete_customer_runnable(arr.get(i).get(1), gnb_branches[branch]));
			}
			// move account
			else if(arr.get(i).get(0) == 6){
				long ac_no1 = arr.get(i).get(1);
				int new_branch = (int)(arr.get(i).get(2)/1);
				updaters[branch].execute(new move_account_runnable(ac_no1, gnb_branches[branch], gnb_branches[new_branch]));
			}
		}



	/*	//gnb_branches[1].insert(1234567890l, 0);
		//gnb_branches[2].insert(2234567890l, 0);

		updaters[1].execute(new add_customer_runnable(0, gnb_branches[1]));
		updaters[2].execute(new add_customer_runnable(0, gnb_branches[2]));


		//For Deposit we need Account Number and Money to Deposit
		long ac_no1 = 1000000000l; 
		long amount = 10000l;

		int branch = (int)(ac_no1/(1e9));
		//System.out.println("Branch1" + branch);
		updaters[branch].execute(new deposit_runnable(ac_no1, amount, gnb_branches[branch].head_ptr()));

		long ac_no2 = 2000000000l; 
		amount = 10000l;

		branch = (int)(ac_no2/(1e9));
		//System.out.println("Branch2" + branch);
		updaters[branch].execute(new deposit_runnable(ac_no2, amount, gnb_branches[branch].head_ptr()));
	


		for(long i=1; i<=100; ++i){
			
			if(i%2==1){
				updaters[1].execute(new transfer_runnable(ac_no1, ac_no2, amount, gnb_branches[1].head_ptr(), gnb_branches[2].head_ptr() ));
			
			}
			else {
				updaters[2].execute(new transfer_runnable(ac_no2, ac_no1, amount, gnb_branches[2].head_ptr(), gnb_branches[1].head_ptr() ));
		
			}

		}
	*/
		//	updaters[1].execute(new move_account_runnable(ac_no1, gnb_branches[1], gnb_branches[2]));
	 	/* updaters[2].execute(new withdraw_runnable(ac_no2, amount, gnb_branches[branch].head_ptr()));
		
	 	 updaters[2].execute(new withdraw_runnable(ac_no2, amount, gnb_branches[branch].head_ptr()));
			
	  	updaters[2].execute(new withdraw_runnable(ac_no2, amount, gnb_branches[branch].head_ptr()));*/
	
		//updaters[1].execute(new transfer_runnable(ac_no1, ac_no2, amount, gnb_branches[1].head, gnb_branches[2].head ));
		//updaters[1].execute(new delete_customer_runnable(ac_no1, gnb_branches[1]));
		
		for(int i=0;i<10;++i){
			updaters[i].shutdown();
		}

		try{																
		
		for(int i=0;i<10;++i){
			updaters[i].awaitTermination(300, TimeUnit.SECONDS);
		
		}
		}

		catch (Exception e){

		}
/*
		if(gnb_branches[1].head!=null){

		System.out.println(gnb_branches[1].head.bank_account.get_account_no());
		System.out.println(gnb_branches[1].head.bank_account.get_balance());

		}

		if(gnb_branches[2].head!=null){
		System.out.println(gnb_branches[2].head.bank_account.get_account_no());		
		System.out.println(gnb_branches[2].head.bank_account.get_balance());
		}*/
/*		gnb_branches[1].printList(gnb_branches[1]);
		System.out.println("Second Branch");
		gnb_branches[2].printList(gnb_branches[2]);*/
	}


}


//deposit runnable
class deposit_runnable implements Runnable
{
	private static final long Delay  = 1;
	private long ac_no;
	private long amount;
	private LinkedList.Node gnb_branch;

	public deposit_runnable(long ac_no, long amount, LinkedList.Node gnb_branch){
		this.ac_no = ac_no;
		this.amount = amount;
		this.gnb_branch = gnb_branch;
	}

	public void run(){
		try{
			
		//Find Branch
		//Find Account in the linked List of the branch
		//Call Deposit function of that account
		long branch = (long)(ac_no/(1e9));
		LinkedList.Node iterator = gnb_branch;
		while(iterator!= null && (iterator.bank_account.get_account_no() != ac_no)){
				iterator = iterator.next;
			}
			if(iterator == null){
				System.out.println("Customer with account number" + ac_no + "does not exist");
				return;
			}
			Account customer = iterator.bank_account;
			customer.deposit(amount);

			Thread.sleep(1);
		}

		catch( InterruptedException exeption){

		}

		return;
	}

}







//Withdraw Runnable

class withdraw_runnable implements Runnable
{
	private static final long Delay  = 1;
	private long ac_no;
	private long amount;
	private LinkedList.Node gnb_branch;

	public withdraw_runnable(long ac_no, long amount, LinkedList.Node gnb_branch){
		this.ac_no = ac_no;
		this.amount = amount;
		this.gnb_branch = gnb_branch;
	}

	public void run(){
		try{
			
		//Find Branch
		//Find Account in the linked List of the branch
		//Call Deposit function of that account
		long branch = (long)(ac_no/(1e9));
		LinkedList.Node iterator = gnb_branch;
		while(iterator!= null && (iterator.bank_account.get_account_no() != ac_no)){
				iterator = iterator.next;
			}
			if(iterator == null){
				System.out.println("Customer with account number" + ac_no + "does not exist");
				return;
			}
			Account customer = iterator.bank_account;
			customer.withdraw(amount);

			Thread.sleep(1);
		}

		catch( InterruptedException exeption){

		}

		return;
	}

}









//Transfer Runnable

class transfer_runnable implements Runnable
{
	private static final long Delay  = 1;
	private long src_ac_no;
	private long dest_ac_no;
	private long amount;
	private LinkedList.Node src_gnb_branch;
	private LinkedList.Node dest_gnb_branch;

	public transfer_runnable(long src_ac_no, long dest_ac_no, long amount, LinkedList.Node src_gnb_branch, LinkedList.Node dest_gnb_branch){
		this.src_ac_no = src_ac_no;
		this.dest_ac_no = dest_ac_no;
		this.amount = amount;
		this.src_gnb_branch = src_gnb_branch;
		this.dest_gnb_branch = dest_gnb_branch;
	}

	public void run(){
		try{
			
		//Find Branch
		//Find Account in the linked List of the branch
		//Call Deposit function of that account
		long src_branch = (long)(src_ac_no/(1e9));
		long dest_branch = (long)(dest_ac_no/(1e9));
		//LinkedList.Node iterator = gnb_branch.head;
		LinkedList.Node src_itr = src_gnb_branch;
		LinkedList.Node dest_itr = dest_gnb_branch;

		while(src_itr!= null  && (src_itr.bank_account.get_account_no() != src_ac_no)){
				src_itr = src_itr.next;
			}
		while(dest_itr!= null  && (dest_itr.bank_account.get_account_no() != dest_ac_no)){
				dest_itr = dest_itr.next;
			}

		if(src_itr == null){
			System.out.println("Customer with account number" + src_ac_no + "does not exist");
			return;
		}

		if(dest_itr == null){
			System.out.println("Customer with account number" + dest_ac_no + "does not exist");
			return;
		}
		
		if(src_ac_no < dest_ac_no)
		{

			Account src_customer = src_itr.bank_account;
			src_customer.withdraw(amount);

			Account dest_customer = dest_itr.bank_account;
			dest_customer.deposit(amount);


		}

		else 
		{
			
			Account dest_customer = dest_itr.bank_account;
			dest_customer.deposit(amount);

			Account src_customer = src_itr.bank_account;
			src_customer.withdraw(amount);


		}	


		Thread.sleep(1);
		
		}

		catch( InterruptedException exeption){

		}

		return;
	}

}






//Add Customer Runnable

class add_customer_runnable implements Runnable
{
	private static final long Delay  = 1;
	//private long ac_no;
	private long amount;
	private LinkedList gnb_branch;

	public add_customer_runnable(long amount, LinkedList gnb_branch){
		
		this.amount = amount;
		this.gnb_branch = gnb_branch;
	}

	public void run(){
		try{
			
		gnb_branch.add_account(amount);

		Thread.sleep(1);

		}

		catch( InterruptedException exeption){

		}

		return;
	}

}



//Add Customer Runnable

class delete_customer_runnable implements Runnable
{
	private static final long Delay  = 1;
	//private long ac_no;
	private long ac_no;
	private LinkedList gnb_branch;

	public delete_customer_runnable(long ac_no,   LinkedList gnb_branch){
		
		this.ac_no = ac_no;
		this.gnb_branch = gnb_branch;
	}

	public void run(){
		try{
			
		gnb_branch.delete_account(ac_no);

		Thread.sleep(1);

		}

		catch( InterruptedException exeption){

		}

		return;
	}

}




//Move from one branch to another

class move_account_runnable implements Runnable
{
	private static final long Delay  = 1;
	//private long ac_no;
	private long ac_no;
	private LinkedList src_gnb_branch;
	private LinkedList dest_gnb_branch;

	public move_account_runnable(long ac_no,   LinkedList src_gnb_branch, LinkedList dest_gnb_branch){
		
		this.ac_no = ac_no;
		this.src_gnb_branch = src_gnb_branch;
		this.dest_gnb_branch = dest_gnb_branch;
	}

	public void run(){
		try{
			
		long amount = src_gnb_branch.ac_balance(ac_no);
		src_gnb_branch.delete_account(ac_no);
		dest_gnb_branch.add_account(amount);
		Thread.sleep(1);

		}

		catch( InterruptedException exeption){

		}

		return;
	}

}