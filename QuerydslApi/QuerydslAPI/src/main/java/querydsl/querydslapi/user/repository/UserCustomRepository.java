package querydsl.querydslapi.user.repository;

import com.querydsl.core.Tuple;
import querydsl.querydslapi.user.domain.User;
import querydsl.querydslapi.user.dto.response.QueryDslDto;

import java.util.List;

public interface UserCustomRepository {

    QueryDslDto getQueryInfo(User reqUser);
}
