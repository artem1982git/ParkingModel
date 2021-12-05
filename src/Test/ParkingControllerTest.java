import junit.framework.TestCase;
import org.junit.Assert;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;

//Представленны здесь тесты отражают лишь мое изучение вопроса тестирования кода
//Веротяно представленный здесь подход явялется некрректным такака, одни методы класса мы проверяем с помощью других неоттестированных методов класса
//Так же замечу что реализованный алгоритм является нелинейным и поэтому затрудняет тестировние в части установки ожидаемых состояний и сравнения результатов выполнения методов с
//этими состояниями

public class ParkingControllerTest extends TestCase {

    public void testCreteCustomParkingPlaceNumbersAndSizesOnly() { // плохой тест потому что пришлось урезать equals в PlaceController
        ParkingController parkingController = new ParkingController();
        ParkingCreationOrder mockSize= Mockito.mock(ParkingCreationOrder.class);
        PlaceController[] cList={new PlaceController(new ParkingPlace(0, Size.small)),new PlaceController(new ParkingPlace(1, Size.small)),
                new PlaceController(new ParkingPlace(2, Size.small)),new PlaceController(new ParkingPlace(3, Size.small))};

        ArrayList<PlaceController> expected=new ArrayList<PlaceController>(Arrays.asList(cList));
        Mockito.when(mockSize.sizeCreator()).thenReturn(Size.small);
        parkingController.creteCustomParking(mockSize,4);

        Mockito.when(mockSize.sizeCreator()).thenReturn(Size.big);
        expected.add(new PlaceController(new ParkingPlace(4, Size.big)));
        expected.add(new PlaceController(new ParkingPlace(5, Size.big)));
        parkingController.creteCustomParking(mockSize,2);
        ArrayList<PlaceController> actual=parkingController.getPlacelist();
        Assert.assertEquals(actual,expected);
    }

    public void testCalcPlaces() {
        int totalPlaces=50;
        ParkingController parkingController = new ParkingController();
        parkingController.creteCustomParking(new SizeCreator(), totalPlaces);
        int N=1000;// количество дискрет когда может приехать машина
        for (int x = 0; x < N; x++) {
            var a=Math.random(); // параметр регулирует интенсивность появления машины
            var b=Math.random(); // параметр регулирует размер машины
            if (a>0.1) {
                parkingController.addCar(new RandomCar(b<0.5?Size.big:Size.small)) ;
             }
            parkingController.parkingWorking();
            TestProvider tp=parkingController.calcPlaces();
            Assert.assertEquals(tp.placeQuiantity,totalPlaces);
        }

    }
}