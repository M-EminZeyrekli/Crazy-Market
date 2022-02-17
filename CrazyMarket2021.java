import java.util.Random;

/**
 * market simulatoru, detaylar icin dokumana basvurunuz
 * @ Muhammet Emin Zeyrekli 20120205050
 */
public class CrazyMarket2021 {
    /** parameters for simulations */
    double lambda;
    /** arrival rate */
    double mu;
    /** service rate */

    /**
     * number of customers to be served. (simulation is done after Nth customer
     * served)
     */
    double N;

    public CrazyMarket2021(double lambda, double mu, int n) {
        // assign lambda mu, etc
        this.lambda = lambda;
        this.mu = mu;
        this.N = n;

    }

    QServer qServer = new QServer();

    QLottery qLottery = new QLottery();
    double Wthreshold = 0.01; // ben rastgele verdim esik degeri
    int totalCustomer = 0; // toplam servis alan musteri

    /* variables for statistics */
    double meanWaitingTime = 0; // mean waiting time of SERVED customers (ortalama)
    double totalWaitingTime = 0; // total waiting time of SERVED customers (toplam)
    double meanServiceTime = 0; // mean service time of SERVED customers
    double totalServiceTime = 0; // total service time of SERVED customers

    /**
     * market simulasyonu
     */
    public void simulateMarket() {

        while (true) {

            // kuyruga musteri ekleyebildigimiz surece musteri gelir
            if (qServer.enqueue(new Customer())) {// arrival event
                System.out.println("arrival event");

                Random random = new Random(); // musteri servis suresi hesabı
                double u = random.nextDouble();
                qServer.queue[(qServer.front) % (qServer.queue.length)].serviceTime = -Math.log(u) / (mu);

                System.out.println("front= " + qServer.front);
                System.out.println("back= " + qServer.back);

                Random random2 = new Random(); // arrival time hesaplanmasi
                double y = random2.nextDouble();
                double interArrivalTime = -Math.log(y) / (lambda); // iki varis suresi arasi fark

                if (qServer.front == 0) {
                    // ilk musteri sira beklemez sadece servis bekler
                    qServer.queue[(qServer.front) % (qServer.queue.length)].waitingTime = qServer.queue[(qServer.front)
                            % (qServer.queue.length)].serviceTime;
                    qServer.queue[(qServer.front) % (qServer.queue.length)].arrivalTime = 0;
                } else {
                    qServer.queue[(qServer.front)
                            % (qServer.queue.length)].arrivalTime = qServer.queue[(qServer.front - 1)
                                    % (qServer.queue.length)].arrivalTime + interArrivalTime;
                }

                qServer.queue[(qServer.front) % (qServer.queue.length)].waitingTime = qServer.queue[(qServer.front)
                        % (qServer.queue.length)].serviceTime
                        + qServer.queue[(qServer.front) % (qServer.queue.length)].arrivalTime;// waiting time hesabi
                qServer.queue[qServer.front].removalTime = qServer.queue[qServer.front].waitingTime
                        + qServer.queue[qServer.front].serviceTime; // removal time hesabi

                System.out.println("interatime= " + interArrivalTime);
                System.out.println("service time= " + qServer.queue[qServer.front].serviceTime);
                System.out.println("waiting time= " + qServer.queue[qServer.front].waitingTime);
                System.out.println("arrival time= " + qServer.queue[qServer.front].arrivalTime);
                System.out.println("removal time= " + qServer.queue[qServer.front].removalTime);

            } else { // departure event
                /*
                 * qServer kuyruğunda eğer sıradaki müşterinin bekleme süresi
                 * Wthreshold değerinden büyük veya eşitse
                 * kuyruğun başındaki müşteri hizmet almaktadır. (müşteri kuyruktan
                 * çıkarılmaktadır)
                 */

                if (qServer.queue[(qServer.front) % (qServer.queue.length)].waitingTime >= Wthreshold) {
                    qServer.dequeue();
                    System.out.println("qlotgirmedi");
                    System.out.println("front= " + qServer.front);
                    System.out.println("back= " + qServer.back);

                } else if (qServer.queue[(qServer.front) % (qServer.queue.length)].waitingTime < Wthreshold) {

                    /*
                     * Küçükse bu müşteri qLottery kuyruğuna eklenmektedir.
                     * Ve rastgele bir sayı üretilerek sıradaki müşteri bu sayı ile bu kuyruktan
                     * seçilmektedir.
                     */
                    qLottery.add(qServer.front + 1);
                    int indexQueueLot = qLottery.lottery();// qlottery deki indexi
                    int indexOfwinner = qLottery.queueLot2.get(indexQueueLot);

                    System.out.println("qlot girdi");
                    System.out.println(
                            "waiting time= " + qServer.queue[(qServer.front + 1) % (qServer.queue.length)].waitingTime);
                    System.out.println("front= " + qServer.front);
                    System.out.println("back= " + qServer.back);
                    System.out.println(indexOfwinner);
                    System.out.println(indexQueueLot);
                    System.out.println(qLottery.queueLot2.get(qLottery.lottery()));
                    System.out.println(qLottery.queueLot2.size());

                    qServer.queue[(indexOfwinner) % (qServer.queue.length)].arrivalTime = 0;
                    qServer.queue[(indexOfwinner) % (qServer.queue.length)].waitingTime = 0;
                    qServer.queue[(indexOfwinner) % (qServer.queue.length)].serviceTime = 0;
                    /* q lotterydeki kazanan q serverdan cıkar hic yokmus gibi kuyruga etki eder. */

                    qLottery.remove(indexQueueLot); // q lotteride cekilisten cikarttik
                    qServer.front = (qServer.front + 1) % (qServer.queue.length);

                }
                System.out.println("departure event");
                totalCustomer++;
            }
            totalServiceTime = totalServiceTime + qServer.queue[qServer.front].serviceTime;
            totalWaitingTime = totalWaitingTime + qServer.queue[qServer.front].waitingTime;
            meanServiceTime = totalServiceTime / totalCustomer;
            meanWaitingTime = totalWaitingTime / totalCustomer;
            System.out.println("total servis time= " + totalServiceTime);
            System.out.println("total waiting time= " + totalWaitingTime);
            System.out.println("mean waiting time= " + meanWaitingTime);
            System.out.println("mean servis time= " + meanServiceTime);
        }

    }

    public void printStatistics() {
        System.out.println("totalWaitingTime:" + totalWaitingTime);
        System.out.println("meanWaitingTime:" + meanWaitingTime);
        System.out.println("totalServiceTime:" + totalServiceTime);
        System.out.println("meanServiceTime:" + meanServiceTime);
    }

    public static void main(String[] args) {
        double lambda = 1;
        double mu = 1;
        int N = 1000;
        CrazyMarket2021 cm = new CrazyMarket2021(lambda, mu, N);
        cm.simulateMarket();
        cm.printStatistics();

    }

}
