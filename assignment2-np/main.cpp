#include <iostream>
#include <thread>
#include <vector>
#include <list>
#include <atomic>
using namespace std;

class Workers {

private:
    list<function<void()>> tasks;
    vector<thread> threads; // Dynamic array.
    condition_variable cv; // Allows threads to be put into sleep and wait instead of busy-waiting.
    mutex tasks_mutex;
    atomic<bool> should_run = true;
    int numberOfThreads;

public:
    explicit Workers(int numberOfThreads) {
     this -> numberOfThreads = numberOfThreads;
    }

    void start() {
      for (int i = 0; i < numberOfThreads; i++) {
        threads.emplace_back([this] {
            while(true) {

              function<void()> task; {
                unique_lock<mutex> lock(tasks_mutex);

                // Waiting inside a loop to handle spurious wake-ups.
                while(tasks.empty()){
                  if(!should_run){
                    return; // Exit the loop if should_run is false.
                  }
                  // cv only allows threads to proceed when the condition is met.
                  cv.wait(lock);
                }

                // Retrieve the task from the front of the queue.
                task = *tasks.begin(); // Copy task for later use.
                tasks.pop_front(); // Remove task from list.
              }

              // Execute the task outside the critical section to minimize the time the mutex is held,
              // allowing other threads to enter the critical section and process additional tasks.
              if (task) {
                task();
                cv.notify_one(); // Notify one waiting thread that one task is completed.
              }
            }
        });
      }
    }

    void post(const function<void()> task) {
      {
        unique_lock<mutex> lock(tasks_mutex);
        tasks.emplace_back(task);
      }
      cv.notify_one();
    }

    void post_timeout(const function<void()> task, const int timeout) {
      {
        unique_lock<mutex> lock(tasks_mutex); // Lock task list and put new task in.
        tasks.emplace_back([timeout, task]{
            // Thread sleeps for the specified time duration before executing the task.
            this_thread::sleep_for(chrono::milliseconds (timeout));
           task();
        });
      } // Release the lock before notifying, to avoid race condition.
      cv.notify_one(); // Notify one waiting thread that a task is available.
    }

    void stop() {
      should_run = false; // Since the bool is atomic it can be changed without using a lock first.
      cv.notify_all(); // Notify all threads that might be waiting on the cv so that they can exit their execution.
    }

    void join() {
      stop();
      for (auto &thread : threads) { //Join all threads in the thread vector.
        thread.join();
      }
    }
};

mutex write_mutex;

int main() {
  Workers worker_threads(4);
  Workers event_loop(1);

  worker_threads.start();
  event_loop.start();

  for(int i = 0; i < 10; i++) {
    worker_threads.post_timeout([i] {
        {
          // Add lock to prevent race condition when printing in terminal.
          unique_lock<mutex> lock{write_mutex};
          cout << "Worker task: " << i << endl;
        }}, 1000);
  }

  for(int i = 0; i < 3; i++) {
    event_loop.post([i] {
        {
          unique_lock<mutex> lock{write_mutex};
          cout << "Event task " << i << endl;
        }});
  }

  worker_threads.join();
  event_loop.join();

  return 0;
}
