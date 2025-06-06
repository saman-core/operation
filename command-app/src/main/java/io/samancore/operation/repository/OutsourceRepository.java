package io.samancore.operation.repository;

import io.samancore.operation.entity.OutsourceEntity;

public interface OutsourceRepository {

    OutsourceEntity create(OutsourceEntity entity);
    OutsourceEntity update(OutsourceEntity entity);
    OutsourceEntity getById(Long id);
    void delete(Long id);
}