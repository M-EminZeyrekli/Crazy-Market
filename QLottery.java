import java.util.ArrayList;
import java.util.Random;

/**
 * indeksle cikarma islemenin ve ekleme isleminin verimli oldugu bir
 * implementasyon yapmaniz istenmektedir.
 * @ Muhammet Emin Zeyrekli 20120205050
 */
public class QLottery { // rastgele bir sayi belirleyip onu qserverdan cikar
    ArrayList<Integer> queueLot2 = new ArrayList<Integer>();
    int front = -1, back = -1; // head&tail of queue

    void add(int item) { // ekle

        if (back == -1 && front == -1) { // kuyruk bossa
            front++;
            queueLot2.add(item);

            back++;

        } else if (front == 0 && back == queueLot2.size() - 1) { // kuyruk dolu musteri eklenemez

        } else if (queueLot2.size() == 0) {
            back = (back + 1);
            queueLot2.add(item);
        } else {
            back = (back + 1) % queueLot2.size();
            queueLot2.add(item);
        }
    }

    void remove(int item) {
        queueLot2.remove(item);
        if (queueLot2.size() > 0) {
            front = (front + 1) % queueLot2.size();
        }
    }

    int lottery() { // kazananin dizi indexini verir
        Random random = new Random();
        int winner = -1;
        int size = queueLot2.size();

        winner = random.nextInt(size);
        return winner;

    }

}
