package Configuration;
import Entities.Room;
import Entities.StandardRoom;
import Entities.SuiteRoom;
import Entities.DormRoom;
import java.util.ArrayList;
import java.util.List;

//Room Logic. Next stop Room Service.
//Это Логика чтоб Комнаты были как в реальных отелях типо когда мы говорим 315 комнату в отеле
//Это же не значит что там 315 комнат, а просто 3 этаж и 15 комната, и тут все делается

public class RoomInitialization {
    //Насчет этого "static", я это написал чтобы нам не приходилось создавать e = new Roominitilization
    //А чисто тут он задает и мы его вызываем чтоб все задаeл
    public static List<Room> initializeRooms() {
        //Ну тут мы создаем новый Аррэй подключенный к Room.
        List<Room> AllRooms = new ArrayList<>();
        //Настройка Отеля
        int roomsPerFloor = 12;
        int idCounter = 1;

        //Цикл для того, чтобы создать комнаты
        //Количество Этажов = 3
        for (int floor = 1; floor <= 3; floor++) {
            //Количество Комнат = 12
            for (int i = 1; i <= roomsPerFloor; i++) {
                int roomNumber = (floor * 100) + i;
                //Задаем название для аррэя рум, чтоб потом с ними поработать.
                Room room;
                //Чисто тут логика такая:
                //idcounter % 3, а там же остатки всегда будут 1, 2, 0
                //И с помощью Свитч мы делим комнаты на 3 типа.
                int typeIndex = idCounter % 3;
                switch (typeIndex) {
                    case 1:
                        room = new StandardRoom(roomNumber, floor);
                        break;
                    case 2:
                        room = new SuiteRoom(roomNumber, floor);
                        break;
                    case 0:
                        room = new DormRoom(roomNumber, floor);
                        break;
                    default:
                        throw new IllegalStateException("Something went wrong in Configuration - RoomInitialization");
                }
                AllRooms.add(room);
                idCounter++;
            }
        }
        return AllRooms;
    }
}
