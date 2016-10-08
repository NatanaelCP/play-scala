Discussion about asymptotic behavior
------------------------------------
Given that n is the number of transactions:

* **PUT /transactionservice/transaction/$transaction_id**

  - In the worst and best case, this takes constant time and space O(1) with respect to the number of transactions.

* **GET /transactionservice/types/$type**

  - This will iterate through a map containing the key-value pairs of *transaction_id*s and transaction objects containing _type_. When it finds the correct _type_, it will add the *transaction_id* into a list. Worst-case time complexity is O(n). 
  - The space taken by adding *transaction_id*s to the list will also grow linearly so in the worst case space complexity is similarly O(n).
  - As a side note, I choose to sort the resulting list. With radix sort or quick sort, this might take O(n*log*n) time and O(*log*n) space. The time complexity might be dominated by this sorting operation.
  
* **GET /transactionservice/transaction/$transaction_id**

  - This will find a corresponding transaction object in a map by using _transaction_id_ as the key. This is a constant time and space operation, ie., O(1).
  
* **GET /transactionservice/sum/$transaction_id**

  - This will traverse through each key-value in a map and sum the _amount_ quantity of all key-value where _parent_id_ is equal to _transaction_id_. This takes O(n) time and O(1) space in the worst case.

  
  
 
 
 










