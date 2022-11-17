import java.io.*;
import java.util.*;
import java.lang.*;
 
/**
 * @author Mohammad Shaikh
 */


public class r_robin{

    private static Scanner input = new Scanner(System.in);        //scanner that takes input for the standard input.



    public static void main(String[] args){ 
        int n,tQuantum, timer = 0;    //initialize n(number of processes), tQuantum(time quantum/ time slice), and the timer to keep track of the CPU time.
        int latestProcess = 0;  //initialize the number of processes that have been taken into the ready queue, as in the index of the latest process.
        float avgWait = 0;                          //initialize average waiting time variable.
        System.out.print("\nEnter the time quanta : ");         //asks user to enter time quanta / time slice of the scheduler.
        tQuantum = input.nextInt();                                     //take user input, assign it to time quanta.

                                                        
        System.out.print("\nEnter the number of processes : "); 
        n = input.nextInt();


        int arrival[] = new int[n];     //initialize array of arrival times of each process(provided).
        int burst[] = new int[n];       //initialize array of full burst times of each process(provided).
        int wait[] = new int[n];        //initialize array of wait times for each process.
        int exitTimes[] = new int[n];   //initialize array of exit times of each process.
        int readyQueue[] = new int[n];  //initialize the Ready Queue.
        int remBurst[] = new int[n];  //array that stores the remaining burst time of the process.
        boolean complete[] = new boolean[n]; //array that stores, for each process, either true(process completed and terminated) and false(process not yet complete) data.
 

       


        System.out.print("\nEnter the arrival times of the processes in order seperated by spaces (ex: 1 2 3 4) : "); // prompt user to enter arrival times.
        /**
         * this for loops adds arrival times in array one by one.
         */
        for(int i = 0; i < n; i++)
            arrival[i] = input.nextInt();  
 


        System.out.print("\nEnter the burst times of each process sperated by spaces (ex: 5 3 1 2) : ");
        /**
         * this for loop adds burst times of each process.
         * and then duplicates burst times array into a remaining Burst times array which will be later decremented from.
         */
        for(int i = 0; i < n; i++){
            burst[i] = input.nextInt();    
            remBurst[i] = burst[i];   
        }
 

        /**
         * this for loop inititalizes the complete(boolean) array and the readyQueue
         */
        for(int i = 0; i < n; i++){             
            complete[i] = false;
            readyQueue[i] = 0;
        }


        /**
         * this while loop increments the timer untill a process arives
         */
        while(timer < arrival[0])    
            timer++;                  
        readyQueue[0] = 1;        // front of ready queue is set to 1
         




        /**
         * this while loop runs as long as there is a process with remaingin burst time.
         * when there is no reaming process the while loop breaks.
         * and then it is followed by calculations of the wating times.
         */
        while(true){

            boolean check = true;  // boolean value set to true.

            /**
             * this for loop runs through the queue.
             * as soon as itidentifies that there is still a process with remaining burst time, it sets flag as false.
             * Then breaks the loop
             */
            for(int i = 0; i < n; i++){
                if(remBurst[i] != 0){
                    check = false;      
                    break;          
                }
            }

            if(check)
                break; // if no burst time was found (all 0 ie. all processes finished) then the check will remain true and while loop will break.
 


            /**
             * this for loop runs through the ready queue and stops if the ready queue for the specific index is empty
             */
            for(int i = 0; (i < n) && (readyQueue[i] != 0); i++){     // loop starts from 0 and increments untill i is less than number of procceses(essentialyy equal) and readyQueue is not empty.

                int count = 0;                       //initialize counter to keep track of time quantum


            
                while((count < tQuantum) && (remBurst[readyQueue[0]-1] > 0)){ //count checks if time quantum is over or not, remaining burst of readyQueue[0] (which initially is 1 and the -1 since its an array and we assume process 1 to be process 0)
                    remBurst[readyQueue[0]-1] -= 1; // then decrement the remaining burst of this process by one
                    timer += 1;                // incerment the timer (time on CPU)
                    count++;                   // increment counter (which is keeping track of time quantum)
 
                    //Check if a new process has arrived on current time on CPU
                    newProcessCheck(timer, arrival, n, latestProcess, readyQueue); 
                }


        
                /**
                 * this if statement checks if the process in front of the running queue is complete.
                 * first, checks if remaining burst time is 0.
                 * and then checks if the boolean value for complete of the latest running process is false (ie not yet marked complete).
                 */
                if((remBurst[readyQueue[0]-1] == 0) && (complete[readyQueue[0]-1] == false)){ 
                    exitTimes[readyQueue[0]-1] = timer;        //exitTimes currently stores exit CPU times of the finished processes
                    complete[readyQueue[0]-1] = true;          //if complete then changes boolean to true and stores exit time.
                }
                 

                //now once the process is finished I will check if the CPU is currently occupied or not
                boolean isIdle = true;
                if(readyQueue[n-1] == 0){ // if the last index of running queue is empty we check from the start.
                    for(int k = 0; k < n && readyQueue[k] != 0; k++){  // for loop reaches up to each process (if it is in the running queue)
                        if(complete[readyQueue[k]-1] == false){ //checks if that process is compelte or not
                            isIdle = false;  // is it is not complete that means the CPU is not idle and the boolean value of isIdle changes to false.
                        }
                    }
                }
                else  //if it is not empty then clearly the CPU is not idle and we will change the boolean value of isIdle to false
                    isIdle = false;
 
                if(isIdle){//now we we have to check for the next process if the CPU is idle
                    timer++; //increment the time in the CPU
                    newProcessCheck(timer, arrival, n, latestProcess, readyQueue); // check for next available process in the queue
                }
               
                
                rearrangeQueue(readyQueue,n); // keeps the order of the processes in check, everytime the CPU is given up by a process.
            }
        }




        /**
         * Thisfor loop uses basic math to calculate the waiting time for each process and add them to the wait array with respect to each index.
         */
        for(int i = 0; i < n; i++){
            exitTimes[i] = exitTimes[i] - arrival[i];
            wait[i] = exitTimes[i] - burst[i];
        }


        System.out.print("\nProcess ID.\tArrival Time\tBurst Time\tWait Time" + "\n");
        for(int i = 0; i < n; i++){
            System.out.print(i+1+"\t\t"+arrival[i]+"\t\t"+burst[i]
                             +"\t\t"+wait[i]+"\n");
        }

        /**
         * this for loop goes through all the elements of the wait array in to calulcate the average wating time of them all 
         */
        for(int j =0; j< n; j++){
            avgWait += wait[j];

        }


        System.out.print("\n ------ Average wait time : "+(avgWait/n) + "------ \n\n"); // print out the average wating time of the scheduler
    }









    //-----------------FUNCTIONS/METHODS----------------------------








    /**
     * this function/method checks if a new process has arrived.
     * 
     * @param timer this is the current time on the CPU
     * @param arrival this is an array consisting of arrival times of all the processes
     * @param n number of processes
     * @param latestProcess the index of the latest process in the queue
     * @param queue the ready queue of all processes
     */
    public static void newProcessCheck(int timer, int arrival[], int n, int latestProcess,int queue[]){

        if(timer <= arrival[n-1]){        //if the current time on CPU is less than the arrival time of the last process.
            boolean newProcess = false;   // set newProcess as false, indicating no new arrival has been detected yet on current time on CPU.
            for(int j = (latestProcess+1); j < n; j++){ //set j as the index of the next process we are looking for and incerement it up untill the number of processes.
                if(arrival[j] <= timer){     //if the arrival time of the next available process is less than the current time on CPU (ie. it has already arrived).
                    if(latestProcess < j){   //then we check if the index of next process is less than the actual index.
                        latestProcess = j;   //if it is then we know that actual value of the index of newwest process is j.
                        newProcess = true;   //and we also know that we have a new process so we set the boolean to true.
                    } // if no new process is found then for loop will exit without changing boolean value of newProcess.
                }
            } 

            if(newProcess)                                         // if a new process is found. 
                appendQueue(queue,timer,arrival,n, latestProcess); //update the queue with the index of the new process.
        }
    }





    /**
     * this function/method adds the index of the latest process into the queue.
     * 
     * @param queue the ready queue
     * @param timer this is the current time on the CPU
     * @param arrival this is an array consisting of arrival times of all the processes
     * @param n number of processes
     * @param latestProcess the index of the latest process in the queue
     */
    public static void appendQueue(int queue[],int timer,int arrival[],int n, int latestProcess){
        int tempIndex = -1;         //set a temporary index as a negative number, because the index of a process cannot be negative so it will not clash with this value
        for(int i = 0; i < n; i++){ //for loop, starts at 0 ends at n (number of processes)
            if(queue[i] == 0){      //checks the next available index in the queue (next index with value of 0)
                tempIndex = i;      //if found.... and assigns this index to the temporary index instead of -1
                break;              //exit the for loop
            }
        }       // if no availble index found in queue then for loop will exit without chaning the value of the temporary index.
        if(tempIndex == -1)
        return; //if the temporary index value is unchanged then function/method will return without doing anything.

        
        queue[tempIndex] = latestProcess + 1; //other wise the available index in the queue will change its element to index of NEXT/NEWs latest process.
    }




    /**
     * this function/method re-arranges to make the process that just gave up the CPU (after the time quantum) to go last and the rest to shift up
     * 
     * @param queue the ready queue of all processes
     * @param n the number of processes
     */
    public static void rearrangeQueue(int rqueue[], int n){
 
        for(int i = 0; (i < n-1) && (rqueue[i+1] != 0) ; i++){  //this for loop starts at 0 and stops when it reaches the second last index of the queue or when then next element in the queue is 0
            int temp = rqueue[i];                               //swap all indexes at first at the glance 
            rqueue[i] = rqueue[i+1];
            rqueue[i+1] = temp;
        }
    }
}


