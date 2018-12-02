package com.galvanize.badgevisitor.repository;

import com.galvanize.badgevisitor.entity.Visitor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VisitorRepository extends CrudRepository<Visitor, Long> {
}
