package com.personal.common.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResponseCode {
    // 공통
    SUCCESS("정상 처리되었습니다."),


    // User Module Error
    NOT_FOUND_USER("유저를 찾을 수 없습니다."),
    EMAIL_ALREADY_REGISTERED("이미 등록된 이메일입니다."),
    PASSWORD_NOT_MATCHED("비밀번호가 일치하지 않습니다."),
    NOT_USE_USER("사용 불가 아이디입니다."),
    INVALID_PASSWORD_FORMAT("비밀번호는 대소문자 포함 영문, 숫자, 특수문자를 최소 1글자씩 포함하며, 최소 8글자 이상이어야 합니다."),
    NOT_FOUND_USER_ADDRESS("등록되어 있는 유저 주소가 없습니다."),
    FORBIDDEN_ADDRESS_UPDATE("해당 주소 수정이 허용되지 않습니다."),
    FORBIDDEN_ADDRESS_DELETE("해당 주소 삭제가 허용되지 않습니다."),

    NOT_FOUND_STORE("매장를 찾을 수 없습니다."),

    NOT_FOUND_CART("장바구니 정보를 찾을 수 없습니다."),

    NOT_FOUND_ORDERS("주문 정보를 찾을 수 없습니다."),

    INVALID_ORDER_ACCESS("잘못된 주문 접근입니다."),

    NOT_FOUND_REVIEW("리뷰 정보를 찾을 수 없습니다."),
    INVALID_REVIEW_ACCESS("잘못된 리뷰 접근입니다."),
    ALREADY_REGISTERED_REVIEW("이미 등록된 리뷰가 있습니다."),

    //Owner Module Error

    //403
    FORBIDDEN_PRODUCTS_ADD("해당 상품 등록이 허용되지 않습니다."),
    FORBIDDEN_PRODUCTS_OUTPUT("원자재 상품은 출고할 수 없습니다."),
    FORBIDDEN_PRODUCTS_UPDATE("해당 상품 수정이 허용되지 않습니다."),
    FORBIDDEN_PRODUCTS_DELETE("해당 상품 삭제가 허용되지 않습니다."),
    FORBIDDEN_STORES_UPDATE("해당 가게 수정이 허용되지 않습니다."),
    FORBIDDEN_STORES_USER("해당 가게 오너가 아닙니다."),
    FORBIDDEN_STORES_DELETE("해당 가게 삭제가 허용되지 않습니다."),

    //400
    INVALID_STOCK_QUNTITY("추가,감소할 수량은 0보다 커야 합니다."),
    INVALID_STOCK("재고가 부족하여 차감할 수 없습니다."),

    //404
    NOT_FOUND_PRODUCT("상품을 찾을 수 없습니다."),
    NOT_FOUND_BASE("해당 완제품을 찾을 수 없습니다."),
    NOT_FOUND_MATERIAL("해당 원자재를 찾을 수 없습니다."),
    NOT_FOUND_PRODUCTBOM("상품 BOM을 찾을 수 없습니다."),
    NOT_FOUND_STOCK("재고를 찾을 수 없습니다."),
    NOT_FOUND_ORDER("주문을 찾을 수 없습니다."),
    STORE_IS_DELETED("해당 매점은 삭제되었습니다."),
    NOT_FOUND_SHIPPING("배송현황이 없습니다."),

    //500
    ILLEGAL_LOT("lot가 중복됩니다.");


    private final String message;
}
