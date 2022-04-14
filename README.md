# Birthday-Presents-and-Atmospheric-Temperature-Reading-Module
This repository consists of two projects that were required for Homework assignment 3.

## Problem 1: The Birthday Presents Party
### Problem description:
The Minotaur’s birthday party was a success. The Minotaur received a lot of presents from his guests. The next day he decided to sort all of his presents and start writing “Thank you” cards. Every present had a tag with a unique number that was associated with the guest who gave it. Initially all of the presents were thrown into a large bag with no particular order. The Minotaur wanted to take the presents from this unordered bag and create a chain of presents hooked to each other with special links (similar to storing elements in a linked-list). In this chain (linked-list) all of the presents had to be ordered according to their tag numbers in increasing order. The Minotaur asked 4 of his servants to help him with creating the chain of presents and writing the cards to his guests. Each servant would do one of three actions in no particular order:

Take a present from the unordered bag and add it to the chain in the correct location by hooking it to the predecessor’s link. The servant also had to make sure that the newly added present is also linked with the next present in the chain.
Write a “Thank you” card to a guest and remove the present from the chain. To do so, a servant had to unlink the gift from its predecessor and make sure to connect the predecessor’s link with the next gift in the chain.
Per the Minotaur’s request, check whether a gift with a particular tag was present in the chain or not; without adding or removing a new gift, a servant would scan through the chain and check whether a gift with a particular tag is already added to the ordered chain of gifts or not.
As the Minotaur was impatient to get this task done quickly, he instructed his servants not to wait until all of the presents from the unordered bag are placed in the chain of linked and ordered presents. Instead, every servant was asked to alternate adding gifts to the ordered chain and writing “Thank you” cards. The servants were asked not to stop or even take a break until the task of writing cards to all of the Minotaur’s guests was complete.

After spending an entire day on this task the bag of unordered presents and the chain of ordered presents were both finally empty!

Unfortunately, the servants realized at the end of the day that they had more presents than “Thank you” notes. What could have gone wrong?

Can we help the Minotaur and his servants improve their strategy for writing “Thank you” notes?

Design and implement a concurrent linked-list that can help the Minotaur’s 4 servants with this task. In your test, simulate this concurrent “Thank you” card writing scenario by dedicating 1 thread per servant and assuming that the Minotaur received 500,000 presents from his guests.

### Solution compile and run:
To solve this problem I wrote the code file bp.java.

#### To compile and run this program:
- First, navigate to the directory where you have the program saved in your command line.
- Type the following command
```
prompt> javac bpp.java
```
- After that command executes, type the following command:
```
prompt> java bpp
```
### Solution description:
First to address the case of where the servents found that they wrote less thank you cards than they had presents, one potential reason for that would be that they did not deal with contention properly in their approach. 

Here is an example scenario that displays that:

![image](https://user-images.githubusercontent.com/89872696/163303211-837a59b0-b1bf-4721-87b1-a32c42c66647.png)

If one servent is about to remove gift a from the chain and write a thank you card for it while another servent is trying to add gift b, then one outcome depending on the course of the events is that the first servent successfully removes gift a but due to how the chain is connected gift b might never b added.

For the code equivlant version of this explaination is if the first thread applies compareAndSet() to head.next, while the second thread applies compareAndSet() to a.next. The net effect is that a is correctly deleted but b is not added to the list. 

## Problem 2: Atmospheric Temperature Reading Module
### Problem description:
You are tasked with the design of the module responsible for measuring the atmospheric temperature of the next generation Mars Rover, equipped with a multi-core CPU and 8 temperature sensors. The sensors are responsible for collecting temperature readings at regular intervals and storing them in shared memory space. The atmospheric temperature module has to compile a report at the end of every hour, comprising the top 5 highest temperatures recorded for that hour, the top 5 lowest temperatures recorded for that hour, and the 10-minute interval of time when the largest temperature difference was observed. The data storage and retrieval of the shared memory region must be carefully handled, as we do not want to delay a sensor and miss the interval of time when it is supposed to conduct temperature reading. 

Design and implement a solution using 8 threads that will offer a solution for this task. Assume that the temperature readings are taken every 1 minute. In your solution, simulate the operation of the temperature reading sensor by generating a random number from -100F to 70F at every reading. In your report, discuss the efficiency, correctness, and progress guarantee of your program.

### Solution compile and run:
To solve this problem I wrote the code file bp.java.

#### To compile and run this program:
- First, navigate to the directory where you have the program saved in your command line.
- Type the following command
```
prompt> javac ATRM.java
```
- After that command executes, type the following command:
```
prompt> java ATRM
```

- After those two commands, the program will ask you to input a number of hours to simulate, after inputting a certain number of hours simulation will print a report for each of those hours.

### Solution description:
For this solution I created an AtomicInteger counter that will represent every minute where there is a temperature reading, then I also created 8 threads, each of those threads gets the current counter and increments it then generates a temperature for that reading. then compares that temperature to the current top 5 and bottom 5 temperatures and updates them accordingly. after the whole simulation is finished, the program generates and prints out a report for each hour.

This program is efficient with a lock-free progress guarantee, and it guarantees correctness since it uses an AtomicInteger as shared memory, and the efficiency comes from each thread taking the next counter once it finishes processing the current one it holds. 
