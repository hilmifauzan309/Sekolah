package com.sekolah.masterdata.repository;

import com.sekolah.masterdata.model.CandidateModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CandidateRepository extends JpaRepository<CandidateModel,Integer> {

    CandidateModel findByCreatedBy(String user);

}
