package project.Ecommerce.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
  USER_ALREADY_EXIST("이미 존재하는 회원입니다"),
  USER_NOT_FOUND("회원가입이 안된 이메일입니다"),
  USER_PASSWORD_INCORRECT("비밀번호가 일치하지 않습니다"),
  REDIS_ERROR("redis 연결에 실패하였습니다."),
  INVALID_JWT("지원되지 않거나 잘못된 토큰 입니다."),
  NOT_EXIST_REFRESH_JWT("존재하지 않거나 만료된 Refresh 토큰입니다. 다시 로그인해주세요."),
  INVALID_USER_PW("유저의 비밀번호가 타당하지 않습니다."),
  INVALID_USER_EMAIL("이메일이 타당하지 않습니다."),
  INVALID_TOKEN("토큰이 타당하지 않습니다."),

  FAIL_UPLOAD_IMAGE("이미지 업로드에 실패하였습니다."),
  PRODUCT_NOT_FOUND("존재하지 않는 상품입니다."),

  ALREADY_ADD_WATCHLIST("이미 관심목록에 추가한 삼품입니다."),
  NOT_FOUND_WATCHLIST("관심목록에 없는 삼품입니다."),

  CHATROOM_ALREADY_EXIST("이미 존재하는 채팅방입니다."),
  CHATROOM_NOT_EXIST("이미 존재하는 채팅방입니다."),
  UNAUTHORIZED_USER_IN_CHATROOM("이 채팅방에 허용된 사용자가 아닙니다."),
  CANNOT_SEND_CHAT_YOURSELF("본인에게 채팅을 보낼 수 없습니다."),
  CANNOT_SEND_MESSAGE_YOURSELF("본인에게 메세지를 보낼 수 없습니다."),
  
  WALLET_NOT_FOUND("지갑을 불러올 수 없습니다."),
  LESS_MONEY_WALLET("물건을 사기에 돈이 부족합니다."),
  WALLET_ALREADY_EXISTS("지갑이 이미 존재합니다."),
  PRODUCT_NOT_ONSAIL("판매중인 상품이 아닙니다."),

  SELLER_DIFFERENT("상품 판매자가 다름니다."),
  DELETE_IMAGE_FAILED("이미지 삭제를 실패하였습니다."),
  ;

  private final String description;
}
