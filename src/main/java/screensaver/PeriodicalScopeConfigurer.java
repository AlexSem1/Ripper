package screensaver;


import javafx.util.Pair;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class PeriodicalScopeConfigurer implements Scope {

    Map<String, Pair<LocalTime, Object>> map = new HashMap<>();

    public Object get(String name, ObjectFactory<?> objectFactory) {
        if (map.containsKey(name)){
            Pair<LocalTime, Object> pair = map.get(name);
            int secondsSinceLastRequest = LocalTime.now().getSecond() - pair.getKey().getSecond();
            if (secondsSinceLastRequest > 3) {
                map.put(name, new Pair<>(LocalTime.now(), objectFactory.getObject()));
            }
        } else {
            map.put(name, new Pair<>(LocalTime.now(), objectFactory.getObject()));
        }

        return map.get(name).getValue();
    }

    public Object remove(String s) {
        return null;
    }

    public void registerDestructionCallback(String s, Runnable runnable) {

    }

    public Object resolveContextualObject(String s) {
        return null;
    }

    public String getConversationId() {
        return null;
    }
}
