package com.remember.core.repositories;

import com.remember.core.domains.Platform;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlatformsRepository extends JpaRepository<Platform, Long> {

}
