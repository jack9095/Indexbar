package stick.head.recycler.test.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

public class ModuleUtil {

    public static LinkedList<City> getData(){
        LinkedList<City> cities;
        //GSON解释出来
        Type listType = new TypeToken<LinkedList<City>>() {
        }.getType();
        Gson gson = new Gson();
        cities = gson.fromJson(DataConstants.cityDataList, listType);
        return cities;
    }

}
