import java.util.ArrayList;


public abstract  class CarModel {

    CarModel(Size s){ carSize=s; };


    private Size carSize;
    public Size getSize(){return carSize;}
   // public abstract boolean isComing();
    public abstract boolean isLeaving(); //определяет поведение машины по отъезду от парковки

}
