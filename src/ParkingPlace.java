public class ParkingPlace {
    public ParkingPlace(int num,Size s){
        number=num; //номер места
        size=s;//размер места
    }

    public int number=-1;
    public Size size= Size.undef;

     Busy busy=Busy.free;


}
