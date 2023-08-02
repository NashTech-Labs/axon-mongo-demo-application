package org.knoldus.axon.state.machine.query.repository;

import org.knoldus.axon.state.machine.query.entity.PaymentEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(collectionResourceRel = "payments", path = "payments")
public interface PaymentRepository extends MongoRepository<PaymentEntity, String> {

    @Override
    <S extends PaymentEntity> S save(S entity);

    @Override
    Optional<PaymentEntity> findById(String s);
}