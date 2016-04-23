package io.digitalreactor.core.domain;

/**
 * Created by ingvard on 07.04.16.
 */
public interface SummaryStorage {
    String findBy(String id, String counterId);

    void create(String id, String counterId);

    void addReport(String id, String counterId);
}
