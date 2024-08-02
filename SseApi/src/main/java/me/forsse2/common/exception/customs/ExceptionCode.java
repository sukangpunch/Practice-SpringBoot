package me.forsse2.common.exception.customs;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ExceptionCode {

    // SignIn Exception
    PASSWORD_NOT_EQUAL(HttpStatus.BAD_REQUEST,"잘못된 비밀번호를 입력하셨습니다."),
    // User Exception
    USER_NOT_EXIST(HttpStatus.BAD_REQUEST, "사용자가 존재하지 않습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND,"사용자를 찾을 수 없습니다."),
    PASSWORD_DUPLICATE(HttpStatus.BAD_REQUEST,"이전 비밀번호와 같은 내용으로 비밀번호를 수정 할 수 없습니다."),
    // _Todo Exception
    TODO_NOT_FOUND(HttpStatus.NOT_FOUND,"투두를 찾을 수 없습니다."),
    // Category Exception
    DUPLICATE_CATEGORY_NAME(HttpStatus.BAD_REQUEST,"중복된 카테고리 이름입니다."),
    CATEGORY_NOT_FOUND(HttpStatus.BAD_REQUEST,"카테고리를 찾을 수 없습니다."),
    // Location Exception
    LOCATION_NOT_FOUND(HttpStatus.BAD_REQUEST,"좌표를 찾을 수 없습니다."),
    // Team Exception
    TEAM_NOT_FOUND(HttpStatus.BAD_REQUEST,"그룹을 찾을 수 없습니다."),
    // UserTeam Exception
    USERTEAM_NOT_FOUND(HttpStatus.BAD_REQUEST,"해당 그룹에 가입 된 유저가 존재하지 않습니다."),
    // TEAMTODO Exception
    TEAMTODO_NOT_FOUND(HttpStatus.BAD_REQUEST,"그룹 투두를 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String message;
    ExceptionCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
