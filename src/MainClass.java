/*
Принцип работы программы:
Есть паркова class ParkingController в состав которого входят:
массив с парковочными местамиб
массив в ячейках которого контроллеры каждого парковочного места. Контроллеры здесь конечно спутаны частично с моделями.
При формировании (создании массива) парковочных мест из ячеек массива контроллеа формируется два связанных списка: список
со свободными маленькми парковчными местами и список со ссвободными большими местами.
Когда появляется свободное или занимается больше парковочное место работа с со списком больших парковочных мест осуществляется как со стеком (
последний зашел-первым вышел). Аналогично с маленькими местами (когда их занимает маельнкая машина).
Когда нет свободных больших парковочных мест (стек пуст) алгоритм пробегает по связанному списку свободных маленьких мест и на каждой итерации смотрит есть ли свободное место
чсправа и слева. Свободные места смотрятся не у следующего  предыдущего элемента списка а через обращение к массиву парковочных мест через индекс.
если место есть справи или слева то эти два элемента изымаются из связанного списка. Когда освобождается два маленьких места под большой машиной, эти места просто заносятся в стек.

 */




public class MainClass {
    public static void main(String[] args) {
        int P=50;
        ParkingController parkingController = new ParkingController();
        parkingController.creteCustomParking(new SizeCreator(), P);
        int N=10000000;// количество дискрет когда может приехать машина
        for (int x = 0; x < N; x++) {
            var a=Math.random(); // параметр регулирует интенсивность появления машины
            var b=Math.random(); // параметр регулирует размер машины
            if (a>0.1) {
                parkingController.addCar(new RandomCar(b<0.5?Size.big:Size.small)) ;
                if (b<0.5)
                    System.out.println("Big CAR");
                else
                    System.out.println("Small CAR");
            }else{
                System.out.println("No Car arrived");
            }
           parkingController.parkingWorking();
           parkingController.calcPlaces().dispalyInfo();
            TestProvider tp=parkingController.calcPlaces();
            assert(tp.placeQuiantity==P);
           // System.out.println("ok");
        }
    }
}

class RandomCar extends CarModel{ // машина которая будет уезжать по равномерному случайному закону
    RandomCar(Size s){
        super(s);
      //  System.out.println(s.toString()+"  CAR");
    };

    @Override
    public boolean isLeaving() {
        var a=Math.random();
        return a>=0.99?true:false;

    }
}


class SizeCreator implements ParkingCreationOrder{  //определяет закон формирорвания размера парковочного места
    @Override
    public Size sizeCreator(){
        var a=Math.random();
        return a<0.5?Size.big:Size.small;

    }
}