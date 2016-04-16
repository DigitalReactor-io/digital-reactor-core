package io.digitalreactor.core.application;

import io.digitalreactor.core.domain.SummaryStorage;

/**
 * Created by ingvard on 07.04.16.
 */
public class MemorySummaryStorage implements SummaryStorage {
    @Override
    public String findBy(String id, String counterId) {
        return null;
    }

    @Override
    public void create(String id, String counterId) {

    }

    @Override
    public void addReport(String id, String counterId) {

    }
}
