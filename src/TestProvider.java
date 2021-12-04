public class TestProvider {
    public int placeQuiantity=0;
    public int freeSmallQuiantity=0;
    public int freeBigQuiantity=0;
    public int busyQuantity=0;
    public void dispalyInfo(){
        System.out.println("Free small places:  "+freeSmallQuiantity);
        System.out.println("Free big places:  "+freeBigQuiantity);
        System.out.println("Busy places:  "+busyQuantity);
        int sum=freeSmallQuiantity+freeBigQuiantity+busyQuantity;
        System.out.println("Total quantity:  "+sum);
        System.out.println("Project quantity:  "+placeQuiantity);
        System.out.println("");
    }
}
