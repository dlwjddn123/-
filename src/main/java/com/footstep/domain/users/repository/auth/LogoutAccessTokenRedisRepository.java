package com.footstep.domain.users.repository.auth;

import com.footstep.domain.users.domain.auth.LogoutAccessToken;
import org.springframework.data.repository.CrudRepository;

public interface LogoutAccessTokenRedisRepository extends CrudRepository<LogoutAccessToken, String> {
}
