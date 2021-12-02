import java.util.ArrayList;

interface ParkingCreationOrder{
    public Size sizeCreator();
}


enum TakedPlace{  // место занято одной машиной или сопряжено с малегьким
    onesmall,
    twosmall,
    big
}

class TakedPlaceParam{
    TakedPlaceParam(TakedPlace p, int num){ //класс нужный для учета мест занятых большой машиной (иначе бы не нужен был)
        placeType=p;
        placeNumber=num;                    //номер занятого места,
    }
    TakedPlace placeType;
    int placeNumber=0;
    private CarModel car=null;

    public void setCar(CarModel car) {
        this.car = car;
    }

    public CarModel getCar() {
        return car;
    }
}

class TempPlaceController{
    PlaceController tpc;
    boolean changed=false;

    public void setTpc(PlaceController tpc) {
        this.tpc = tpc;
    }

    public PlaceController getTpc() {
        return tpc;
    }
}


class PlaceController {  //класс для формирования связанных списков парковочных мест
    PlaceController(ParkingPlace pp) {
        parkingPlace = pp;
    }
    PlaceController() {

    }
    private PlaceController nextFreePlace = null;

    public void setNextFreePlace(PlaceController nextFreePlace) {
        this.nextFreePlace = nextFreePlace;
    }

    public PlaceController getNextFreePlace() {
        return nextFreePlace;
    }

    public PlaceController prevFreePlace = null;

    public void setPrevFreePlace(PlaceController prevFreePlace) {
        this.prevFreePlace = prevFreePlace;
    }

    public PlaceController getPrevFreePlace() {
        return prevFreePlace;
    }

    public ParkingPlace parkingPlace;

    public void clearLinks(){nextFreePlace = null;
        prevFreePlace = null;
    }
}

public class ParkingController { //контроллер парковки




    PlaceController lastSmallFreePlace = null; //последнее свободное маленькое место

    PlaceController lastBigFreePlace = null; //последнее свободное большое место



    private ArrayList<PlaceController> placeList = new ArrayList<>(); //массив в ячейках которого контроллер (скорее модель) парковчного места. Элементы массива образуют два связанных списка

    private ArrayList<TakedPlaceParam> busyPlacesList=new ArrayList<>(); //массив с занятыми парковычными местами


    public void addCar(CarModel car){ //добавление новой машины
       var takedPlaceParam = parkCar(car);
       if (takedPlaceParam!=null){
           takedPlaceParam.setCar(car);
           busyPlacesList.add(takedPlaceParam);
       }

    }

    public void parkingWorking(){ //обход занятых мест
        int y=busyPlacesList.size();
        for (int x=0;x<y;x++){
            if (busyPlacesList.get(x).getCar().isLeaving()){
                System.out.println("Car leaved away");
                relasePlace(busyPlacesList.get(x));
                busyPlacesList.remove(x);
                y--;
            }

        }

    }


    private void relasePlace(TakedPlaceParam tpm){ //освободить парковчное место
        if (tpm.placeType==TakedPlace.big){
           placeList.get(tpm.placeNumber).parkingPlace.busy=Busy.free;
           addBigPlace(tpm.placeNumber); //пихаем в стек
        }
        if (tpm.placeType==TakedPlace.twosmall){
            placeList.get(tpm.placeNumber).parkingPlace.busy=Busy.free;
            placeList.get(tpm.placeNumber+1).parkingPlace.busy=Busy.free;
            addSmallPace(tpm.placeNumber);//пихаем в стек
            addSmallPace(tpm.placeNumber+1);//пихаем в стек
        }
        if (tpm.placeType==TakedPlace.onesmall){
            placeList.get(tpm.placeNumber).parkingPlace.busy=Busy.free;
            addSmallPace(tpm.placeNumber);//пихаем в стек

        }


    }



    private void addBigPlace(int x){ //типа ошибок нет и место точно занято большой машиной. Добавляем большое место в стек с большими местами
            if (lastBigFreePlace == null) { //если стек пустой
                lastBigFreePlace = placeList.get(x);
                lastBigFreePlace.clearLinks();//на всякий случай
                return;
            }
            lastBigFreePlace.setNextFreePlace(placeList.get(x));
            placeList.get(x).setPrevFreePlace(lastBigFreePlace);
            lastBigFreePlace = placeList.get(x);


    }

    private void addSmallPace(int x){ // аналогично addBigPlace

            if (lastSmallFreePlace == null) {
                lastSmallFreePlace = placeList.get(x);
                lastSmallFreePlace.clearLinks();//на всякий случай
                 return;
            }
            lastSmallFreePlace.setNextFreePlace(placeList.get(x));
            placeList.get(x).setPrevFreePlace(lastSmallFreePlace);
            lastSmallFreePlace = placeList.get(x);

        }





    public void creteCustomParking(ParkingCreationOrder creator, int addSize) { // позволяет наращивать парковку кусками по закону creator
        // boolean firstCreation=placeList.size()==0?true:false; //первое заполнение списка или нет
        if (addSize < 1) {
            return;
        }
        int size=placeList.size();
        for (int x = size; x < size + addSize; x++) {
            Size placeSize = creator.sizeCreator();

            placeList.add(new PlaceController(new ParkingPlace(x,placeSize)));

            if (placeSize == Size.big) { //создаем связанный список больших мест с которым будем потом работать как со стеком
                System.out.println("PLACE BIIIIIIIIIG "+ x+"\n");

                addBigPlace(x);
                //continue;
            }
            if (placeSize == Size.small) { //создаем связанный список малых мест
                System.out.println("PLACE SMAAAAAAAAAAAAAL "+ x+"\n");
                addSmallPace(x);
            }
        }

    }

    private int getBigPlace() { //достаем последний эелемент из стека
        if (lastBigFreePlace == null) {
            return -1; //все места заняты
        }
        int freeNumber = lastBigFreePlace.parkingPlace.number;
        if (lastBigFreePlace.prevFreePlace==null && lastBigFreePlace.getNextFreePlace()==null) { //осталось одно место
            lastBigFreePlace.clearLinks();
            lastBigFreePlace = null;
            return freeNumber;
        }

        lastBigFreePlace = lastBigFreePlace.getPrevFreePlace();
        lastBigFreePlace.getNextFreePlace().clearLinks();
        lastBigFreePlace.setNextFreePlace(null);
        return freeNumber;
    }


    private int getSmallPlace() { //аналогично
        if (lastSmallFreePlace==null ) {
            return -1; //все места заняты
        }
        int freeNumber = lastSmallFreePlace.parkingPlace.number;
        if (lastSmallFreePlace.getPrevFreePlace() == null && lastSmallFreePlace.getNextFreePlace()==null) { //осталось одно место
            lastSmallFreePlace.clearLinks();
            lastSmallFreePlace = null;
            return freeNumber; //все места заняты
        }

        lastSmallFreePlace = lastSmallFreePlace.getPrevFreePlace();
        lastSmallFreePlace.getNextFreePlace().clearLinks();
        lastSmallFreePlace.setNextFreePlace(null);
        System.out.println(freeNumber+"\n");
        return freeNumber;
    }


    private int getPairSmallPlaces() { // ищем пару свободных мест.

        if (lastSmallFreePlace == null) { // нет мест
            return -1;
        }

        if (lastSmallFreePlace.getPrevFreePlace() == null) { // одно место
            return -1;
        }

        if (lastSmallFreePlace.getPrevFreePlace().getPrevFreePlace() == null) { // всего два свободных места
           int fNumber = lastSmallFreePlace.parkingPlace.number;
           int lNumber = lastSmallFreePlace.getPrevFreePlace().parkingPlace.number;
           if (Math.abs(fNumber-lNumber)==1){//если  они соседи
               lastSmallFreePlace.getPrevFreePlace().clearLinks();
               lastSmallFreePlace.clearLinks();
               lastSmallFreePlace=null;
                if (fNumber<lNumber) {  //контролируем чтобы нумерация была слева направо (либо нужно создать отдельную переменную для контроля )
                   return fNumber;
                }
                   return lNumber;
           } else {
               return -1;
           }

        }

        PlaceController tmpPlace = lastSmallFreePlace;

        while (tmpPlace.getPrevFreePlace() != null) {
            int tmpNumber = tmpPlace.parkingPlace.number;
            System.out.println(tmpNumber+"\n");
            if (tmpNumber < placeList.size() - 1 && placeList.get(tmpNumber + 1).parkingPlace.size == Size.small && placeList.get(tmpNumber + 1).parkingPlace.busy == Busy.free) { //соседнее место справа свободно
                  TempPlaceController tempPlaceController=new TempPlaceController();
                  removeFromList( tempPlaceController, tmpPlace) ;
                  removeFromList(tempPlaceController, placeList.get(tmpNumber + 1));
                  if(tempPlaceController.changed) {
                      lastSmallFreePlace = tempPlaceController.getTpc();
                  }



              return tmpNumber;
            }

            if (tmpNumber > 0 && placeList.get(tmpNumber - 1).parkingPlace.size == Size.small && placeList.get(tmpNumber - 1).parkingPlace.busy == Busy.free) { //соседнее место слево свободно
                TempPlaceController tempPlaceController=new TempPlaceController();
                removeFromList( tempPlaceController, tmpPlace);
                removeFromList(tempPlaceController, placeList.get(tmpNumber - 1));
                if(tempPlaceController.changed) {
                    lastSmallFreePlace = tempPlaceController.getTpc();
                }
            
                return tmpNumber - 1;
            }
            tmpPlace=tmpPlace.getPrevFreePlace();
        }
        return -1;
    }

    private TakedPlaceParam parkCar(CarModel car) {//паркует машину
        if (car.getSize() == Size.big) {
            int num = getBigPlace();
            if (num >= 0) {
                placeList.get(num).parkingPlace.busy=Busy.busyWithBig;
                System.out.println("Busy  Big ok\n");
                return new TakedPlaceParam(TakedPlace.big, num);
            }
            System.out.println("No big place\n");
            num = getPairSmallPlaces();
            if (num >= 0) {
                placeList.get(num).parkingPlace.busy=Busy.busyWithBig;
                placeList.get(num+1).parkingPlace.busy=Busy.busyWithBig;
                System.out.println("Busy  two small ok\n");
                return new TakedPlaceParam(TakedPlace.twosmall, num);
            }
            System.out.println("No two small place\n");
            return null;
        }
        if (car.getSize() == Size.small) {
            int num = getSmallPlace();
            if (num >= 0) {
                System.out.println("Busy  one small ok   "+ num+"       \n");
                placeList.get(num).parkingPlace.busy=Busy.busyWithSmall;
                return new TakedPlaceParam(TakedPlace.onesmall, num);
            }
        }
        System.out.println("No place\n");
        return null;
    }






//функция для выкидывания элемена из списка
    private void removeFromList( TempPlaceController tempPlaceController,PlaceController actualPlace){
        if (actualPlace.getPrevFreePlace()==null && actualPlace.getNextFreePlace()!=null){ //начало списка
            actualPlace.getNextFreePlace().setPrevFreePlace(null);
            tempPlaceController.setTpc(actualPlace.getNextFreePlace());
            tempPlaceController.changed=true;
            actualPlace.clearLinks();
            return ;
        }
        if (actualPlace.getNextFreePlace()==null && actualPlace.getPrevFreePlace()!=null) {
            //конец списка
            actualPlace.getPrevFreePlace().setNextFreePlace(null);
            tempPlaceController.setTpc(actualPlace.getPrevFreePlace());
            tempPlaceController.changed=true;
            actualPlace.clearLinks();
            return ;
        }
        if (actualPlace.getNextFreePlace()!=null && actualPlace.getPrevFreePlace()!=null){
            actualPlace.getPrevFreePlace().setNextFreePlace(actualPlace.getNextFreePlace());
            actualPlace.getNextFreePlace().setPrevFreePlace(actualPlace.getPrevFreePlace());
            actualPlace.clearLinks();
            return ;

        }
        if (actualPlace.getNextFreePlace()==null && actualPlace.getPrevFreePlace()==null){
            tempPlaceController.setTpc(null);
            tempPlaceController.changed=true;
            return ;
        }


    }

















}


