package com.personal.domain.store.service;

import com.personal.common.code.ResponseCode;
import com.personal.common.entity.AuthUser;
import com.personal.common.exception.custom.NotFoundException;
import com.personal.domain.store.exception.StoreOwnerMismatchException;
import com.personal.domain.store.repository.StoreRepository;
import com.personal.entity.store.Store;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class StoreCommonService {
    private final StoreRepository storeRepository;


    //id가 없고 삭제처리된 가게 예외처리
    public Store getStores(Long storeId) {
        Store store = storeRepository.findById(storeId).orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_STORE));
        if (store.isDeleted()) throw new NotFoundException(ResponseCode.STORE_IS_DELETED);
        return store;
    }

    public void validateUserAccess(AuthUser authUser, Long storeId) {
        Store store = getStores(storeId);
        if (!authUser.getUserId().equals(store.getUser().getId())) {
            throw new StoreOwnerMismatchException(ResponseCode.FORBIDDEN_STORES_USER);
        }
    }
}
