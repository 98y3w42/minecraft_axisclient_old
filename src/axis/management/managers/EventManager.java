package axis.management.managers;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import axis.event.Event;
import axis.event.FlexibleArray;
import axis.event.MethodData;
import axis.event.Priority;

public final class EventManager
{
	 private static final Map<Class<? extends Event>, FlexibleArray<MethodData>> REGISTRY_MAP;

	    static {
	        REGISTRY_MAP = new HashMap<Class<? extends Event>, FlexibleArray<MethodData>>();
	    }
public static void register(final Object object) {
    Method[] declaredMethods;
    for (int length = (declaredMethods = object.getClass().getDeclaredMethods()).length, i = 0; i < length; ++i) {
        final Method method = declaredMethods[i];
        if (!isMethodBad(method)) {
            register(method, object);
        }
    }
}

    public static void register(final Object object, final Class<? extends Event> eventClass) {
        Method[] declaredMethods;
        for (int length = (declaredMethods = object.getClass().getDeclaredMethods()).length, i = 0; i < length; ++i) {
            final Method method = declaredMethods[i];
            if (!isMethodBad(method, eventClass)) {
                register(method, object);
            }
        }
    }

    public static void unregister(final Object object) {
        for (final FlexibleArray<MethodData> dataList : EventManager.REGISTRY_MAP.values()) {
            for (final MethodData data : dataList) {
                if (data.source.equals(object)) {
                    dataList.remove(data);
                }
            }
        }
        cleanMap(true);
    }

    public static void unregister(final Object object, final Class<? extends Event> eventClass) {
        if (EventManager.REGISTRY_MAP.containsKey(eventClass)) {
            for (final MethodData data : EventManager.REGISTRY_MAP.get(eventClass)) {
                if (data.source.equals(object)) {
                    EventManager.REGISTRY_MAP.get(eventClass).remove(data);
                }
            }
            cleanMap(true);
        }
    }

    private static void register(final Method method, final Object object) {
        final Class<? extends Event> indexClass = (Class<? extends Event>)method.getParameterTypes()[0];
        final MethodData data = new MethodData(object, method, (byte)0);
        if (!data.target.isAccessible()) {
            data.target.setAccessible(true);
        }
        if (EventManager.REGISTRY_MAP.containsKey(indexClass)) {
            if (!EventManager.REGISTRY_MAP.get(indexClass).contains(data)) {
                EventManager.REGISTRY_MAP.get(indexClass).add(data);
                sortListValue(indexClass);
            }
        }
        else {
            EventManager.REGISTRY_MAP.put(indexClass, new FlexibleArray() {
                private static final long serialVersionUID = 666L;
                {
                    this.add(data);
                }
            });
        }
    }

    public static void removeEntry(final Class<? extends Event> indexClass) {
        final Iterator<Entry<Class<? extends Event>, FlexibleArray<MethodData>>> mapIterator = EventManager.REGISTRY_MAP.entrySet().iterator();
        while (mapIterator.hasNext()) {
            if (mapIterator.next().getKey().equals(indexClass)) {
                mapIterator.remove();
                break;
            }
        }
    }

    public static void cleanMap(final boolean onlyEmptyEntries) {
        final Iterator<Entry<Class<? extends Event>, FlexibleArray<MethodData>>> mapIterator = EventManager.REGISTRY_MAP.entrySet().iterator();
        while (mapIterator.hasNext()) {
            if (!onlyEmptyEntries || mapIterator.next().getValue().isEmpty()) {
                mapIterator.remove();
            }
        }
    }

    private static void sortListValue(final Class<? extends Event> indexClass) {
        final FlexibleArray<MethodData> sortedList = new FlexibleArray<MethodData>();
        byte[] value_ARRAY;
        for (int length = (value_ARRAY = Priority.VALUE_ARRAY).length, i = 0; i < length; ++i) {
            final byte priority = value_ARRAY[i];
            for (final MethodData data : EventManager.REGISTRY_MAP.get(indexClass)) {
                if (data.priority == priority) {
                    sortedList.add(data);
                }
            }
        }
        REGISTRY_MAP.put(indexClass, sortedList);
    }

    private static boolean isMethodBad(final Method method) {
        return method.getParameterTypes().length != 1;
    }

    private static boolean isMethodBad(final Method method, final Class<? extends Event> eventClass) {
        return isMethodBad(method) || !method.getParameterTypes()[0].equals(eventClass);
    }

    public static FlexibleArray<MethodData> get(final Class<? extends Event> clazz) {
        return EventManager.REGISTRY_MAP.get(clazz);
    }
}
