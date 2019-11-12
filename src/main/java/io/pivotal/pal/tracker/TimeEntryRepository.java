package io.pivotal.pal.tracker;

import org.springframework.context.annotation.Bean;

import java.util.List;

public interface TimeEntryRepository {

    public TimeEntry create(TimeEntry any);

    public TimeEntry find(long timeEntryId) ;

    public List list();

    public TimeEntry update(long eq, TimeEntry any);

    public boolean delete(long timeEntryId);

}
