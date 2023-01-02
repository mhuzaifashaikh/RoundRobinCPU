# RoundRobinCPU

This JAVA program shows you the average waiting time of the processes that are being run on a CPU that uses the Round Robin CPU scheduling algorithm. It asks you for the number of processes running, the arrival times of each, the burst times of each and the time quantum, then the algorithm shows you the wait times of each process and the average wait time, a number that is used mainly to judge the usability of a CPU scheduling system.

Currently it is structured mainly using arrays, I hope to find the time to optimize this algorithm using more effecient data structures. I also hope to add error handling soon.

To Run the program:

-Run it locally using a java compiler 

  -Issue this command to compile the program: javac r_robin.java
  
  -Issue this command to run the program: java r_robin.java
  
  -Follow the steps prompted once the program is run






More on the design of this program-

In this algorithm, I have mainly used the array data structure. To keep track of processes in a Round Robin scheduling system, one of the first data structures to use that I thought of instantly was linked lists. However with a little more thought I chose to go with an algorithm consisting of multiple arrays. Mainly, the luxury of each process having its own index and making it extremely easy to reference that specific process because of these indexes made arrays the slightly better option over linked lists. I made multiple arrays for the key information of the processes such as an array each for arrival times, burst times, wait times, exit times, the ready queue, the remaining burst time of each process after each time quantum passes, and conformation of the completion of each process (true or false, boolean array). Each process is associated with the same index in every array. Next, I created three functions/methods, each with a very specific job.
Firstly, I made a function/method called newProcessCheck that checks if a new process has arrived on the current CPU time. It takes in 5 parameters, and the arguments used for this function in the main method are the timer (current time on CPU), the array containing arrival times of each process, the number of processes, the index of the latest running process and the ready queue. I chose to create this particular function because it is the most efficient and easiest way to traverse through the arrival time array and see if the current CPU time matches with the
     8
 next process on the arrival array. By next I mean the process that has not yet been added to the ready queue.
Design 1-
This entire function/method is an if statement. The if statement checks if the current CPU time (the value passed through the timer parameter) is less than the arrival time of the last most process by comparing it to the value in the last index of the arrival time array. Then if the condition matches it creates a temporary boolean variable that is initially set to false. This boolean variable is an “indicator” of a new process having arrived at given CPU time. Next, a for loop that starts one index after the index of the latest running process is initialized to run up till the number of processes. In this for loop, it checks if the arrival time of index j is less than or equal to the current time on the CPU. If so, then that would mean that this process has obviously arrived and then the index of the latest process is changed to the index of this process because, ofcourse, this is now the latest one to arrive. And finally the temporary boolean variable is turned to “true” indicating that a new process indeed has arrived. And finally, after the for loop has traversed through all the values, another if statements check if this boolean variable is true (if a new process has arrived) and if the condition is met it calls the second function/method below to update the ready queue
The second function/method called appendQueue adds the index of the latest process into the ready queue. It takes in the exact same 5 arguments as the first method, the timer (current time on CPU), the array containing arrival times of each process, the number of processes, the index of the latest running process and the ready queue. This function/method is called by the first function/method if a new process has arrived. If it has arrived the function/method looks for the next available index in the ready queue and adds the newly arrived process in it.
Design 2-
This function/method is called by the first function/method if a new process has arrived. It creates a temporary integer variable and assigns the value -1 to it. Then a for loop starts from 0 up till the number of processes and within the for loop an if statement looks for the first available
  9

0 in the ready queue (0 would mean that that spot is empty in the ready queue. Once it does find this empty spot, it assigns the value of this index to the temporary integer variable created initially and breaks the loop, if it does not find an empty spot (an index with value 0) then it will leave the for loop after traversing through the whole ready queue array. After leaving the for loop, another if statement checks if the value of the temporary integer variable is still -1. If it is, it would mean that no empty spot was found in the ready queue and the function will return without making any changes to the ready queue. However, if it was changed, the function will use this new value of the temporary integer variable that is now the index of the next empty spot in the ready queue and will assign the index of the newest arrived process to this spot.
The third and final function/method simply rearranges the ready queue in a way which makes the most recently run processes (the one that most recently gives up the CPU) go to the end of the queue and the rest to shift up. This function/method takes in 2 parameters, the ready queue itself and the number of processes. Every time the scheduler preempts a process which means if the time quantum is complete or the process is just completed, this function is called to maintain the order of the processes.


Function 3-
This function/method consists entirely of a for loop, the for loop starts at 0 and runs up till the number of processes - 1 (minus one because the number of processes in this case is used as the index for the ready queue, and since I am using the array data structure, it is assumed that process 1 is actually process 0 and 2 is 1 and so on...) or until the next spot in the ready queue is empty (by next spot I mean that since the number of process is used as index number then if the next one has not yet arrived, the for loop will stop). Within the for loop, a basic swap algorithm is used (basic swap algorithm, create a temporary variable, assign current value to it, assign the next value to current value and then assign the temporary value to next value). This algorithm just swaps the foremost process to the one next to it until it is the last one.
