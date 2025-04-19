package com.afric.accounting.service.repository;

import com.afric.accounting.service.model.AccountingJournal;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountingJournalRepository extends MongoRepository<AccountingJournal, String> {

    List<AccountingJournal> findByAccountId(String accountId);
}