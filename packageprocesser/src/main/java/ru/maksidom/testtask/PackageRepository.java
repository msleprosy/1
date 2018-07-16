package ru.maksidom.testtask;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PackageRepository extends CrudRepository<PackageEntity, Long> {

    List<PackageEntity> findByIdGreaterThan(long id);

}
