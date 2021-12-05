public class ParkingPlace {
    public ParkingPlace(int num,Size s){
        number=num; //номер места
        size=s;//размер места
        busy=Busy.free;
    }

    public int number=-1;
    public Size size= Size.undef;
    Busy busy;
    @Override
   public boolean equals(Object obj){
        if (obj==null|| obj.getClass() != this.getClass())
            return false;
        ParkingPlace pp = (ParkingPlace) obj;
        if (pp.number!= this.number)
            return false;
        if (pp.size!=this.size)
            return false;
        if (pp.busy!=this.busy)
            return false;
        return true;



    }


}
