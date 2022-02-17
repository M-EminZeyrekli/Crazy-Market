
/**
 * maintains a customer queue in a circular array
 * @ Muhammet Emin Zeyrekli 20120205050
 */
public class QServer {

    Customer[] queue = new Customer[20]; // kuyruga boyut belirle
    int front = -1, back = -1; // head&tail of queue

    /**
     * enqueue a customer to queue
     */
    boolean enqueue(Customer customer) {
        if (back == -1 && front == -1) { // kuyruk bossa
            back++;
            front++;
            queue[front] = customer;
            return true;
        } else if (isFull()) { // kuyruk dolu musteri eklenemez
            return false;
        } else {
            back = (back + 1) % queue.length;
            queue[back] = customer;
            return true;
        }
    }

    /**
     * dequeue a customer from queue
     * 
     */
    Customer dequeue() {
        if (isEmpty()) { // kuyruk bos
            return null;

        } else {

            queue[front] = null;
            front = (front + 1) % queue.length;
        }
        return null;
    }

    /**
     * peek a customer in queue
     * 
     */
    Customer peek() { // siranin basindaki musteri

        return queue[front];
    }

    /** kuyruktaki toplam eleman sayisi */
    int size() {
        if (back >= front) {
            return back - front + 1;

        } else {

            return back + queue.length - front + 1;
        }

    }

    boolean isEmpty() { // kuyruk bos mu kontrol eder
        if (size() == 0) {
            return true;
        } else if (front == -1) {
            return true;
        }
        return false;

    }

    boolean isFull() {
        if (front == 0 && back == queue.length - 1) {
            return true;
        }
        if (size() == 20) { // "front > back && front - back == 1"da kullanılabilir
            return true;
        }
        return false;
    }
}
