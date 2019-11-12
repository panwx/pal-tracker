package io.pivotal.pal.tracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTimeEntryRepository implements TimeEntryRepository{
    private TimeEntry timeEntry;
    private long id = 1L;
    private List list = new ArrayList();

    private Map<Long, TimeEntry> timeEntryMap = new HashMap<>();


    public TimeEntry create(TimeEntry timeEntry) {
        long id = getId();
        timeEntry.setId(id);
        timeEntryMap.put(id, timeEntry);
        list.add(timeEntry);

        id++;
        setId(id);
        return timeEntry;
    }

    public TimeEntry find(long id) {
        TimeEntry timeEntry = getTimeEntryMap().get(id);

        return timeEntry;
    }

    public List list() {
        return list;
    }

    public TimeEntry update(long id, TimeEntry timeEntry) {
        TimeEntry timeEntryGet = timeEntryMap.get(id);

        if(timeEntryGet!=null){
            timeEntry.setId(id);
            getTimeEntryMap().put(id, timeEntry);
            list.remove(timeEntryGet);
            list().add(timeEntry);
            return timeEntry;
        }

        return timeEntryGet;
    }

    public boolean delete(long id) {
        TimeEntry timeEntry = getTimeEntryMap().get(id);

        if(timeEntry != null){
            list.remove(timeEntry);
            timeEntryMap.remove(id);
            return true;
        }

        return false;

    }

    public Map<Long, TimeEntry> getTimeEntryMap() {
        return timeEntryMap;
    }

    public void setTimeEntryMap(Map<Long, TimeEntry> timeEntryMap) {
        this.timeEntryMap = timeEntryMap;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
