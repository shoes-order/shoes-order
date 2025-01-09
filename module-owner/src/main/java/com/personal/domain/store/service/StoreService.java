package com.personal.domain.store.service;

import com.personal.common.code.ResponseCode;
import com.personal.common.entity.AuthUser;
import com.personal.domain.owner.service.OwnerCommonService;
import com.personal.domain.store.dto.StoreRequest;
import com.personal.domain.store.dto.StoreResponse;
import com.personal.domain.store.exception.StoreOwnerMismatchException;
import com.personal.domain.store.repository.StoreRepository;
import com.personal.entity.store.Store;
import com.personal.entity.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional(readOnly = true)
public class StoreService {
    private final StoreRepository storeRepository;
    private final OwnerCommonService ownerCommonService;
    private final StoreCommonService storeCommonService;


    @Transactional
    public void saveStores(StoreRequest.SaveStores saveStores, AuthUser authUser) {
        User user = ownerCommonService.getUserById(authUser.getUserId());
        Store store = Store.builder()
                .user(user)
                .name(saveStores.name())
                .tel(saveStores.tel())
                .zip(saveStores.zip())
                .address(saveStores.address())
                .addressDetail(saveStores.addressDetail())
                .description(saveStores.description())
                .build();
        storeRepository.save(store);
    }

    public Page<StoreResponse.GetStores> getStores(AuthUser authUser, StoreRequest.GetStores getStores) {
        User user = ownerCommonService.getUserById(authUser.getUserId());
        Pageable pageable = PageRequest.of(getStores.page() - 1, getStores.size());

        Page<Store> storeList = storeRepository.findAllByUser(pageable, user);
        return storeList.map(store -> new StoreResponse.GetStores
                (
                        store.getName(),
                        store.getTel(),
                        store.getZip(),
                        store.getAddress(),
                        store.getAddressDetail()
                ));
    }

    public StoreResponse.GetStores getStore(AuthUser authUser, Long storeId) {
        Long userId = authUser.getUserId();
        Store store = storeCommonService.getStores(storeId);
        if (!userId.equals(store.getUser().getId())) {
            throw new StoreOwnerMismatchException(ResponseCode.FORBIDDEN_STORES_USER);
        }

        return new StoreResponse.GetStores(
                store.getName(),
                store.getTel(),
                store.getZip(),
                store.getAddress(),
                store.getAddressDetail()
        );
    }

    @Transactional
    public void updateStores(AuthUser authUser, Long storeId, StoreRequest.UpdateStores updateStores) {
        Store store = storeCommonService.getStores(storeId);
        if (!authUser.getUserId().equals(store.getUser().getId())) {
            throw new StoreOwnerMismatchException(ResponseCode.FORBIDDEN_STORES_UPDATE);
        }
        store.updateInfos(
                updateStores.name(),
                updateStores.tel(),
                updateStores.zip(),
                updateStores.address(),
                updateStores.addressDetail(),
                updateStores.description());
    }

    @Transactional
    public void deleteStores(Long storeId, AuthUser authUser) {
        Store store = storeCommonService.getStores(storeId);
        if (!authUser.getUserId().equals(store.getUser().getId())) {
            throw new StoreOwnerMismatchException(ResponseCode.FORBIDDEN_STORES_DELETE);
        }

        store.updateIsDeleted(true);
    }


}
