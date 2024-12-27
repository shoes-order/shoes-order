package com.personal.domain.order.service;

import com.personal.common.code.ResponseCode;
import com.personal.common.entity.AuthUser;
import com.personal.common.exception.custom.NotFoundException;
import com.personal.domain.cart.dto.CartList;
import com.personal.domain.cart.dto.CartResponse;
import com.personal.domain.cart.dto.TotalResponse;
import com.personal.domain.cart.service.CartCommonService;
import com.personal.domain.order.dto.OrderDetail;
import com.personal.domain.order.dto.OrderRequest;
import com.personal.domain.order.dto.OrderResponse;
import com.personal.domain.order.exception.OrderException;
import com.personal.domain.order.repository.OrdersRepository;
import com.personal.domain.product.service.ProductCommonService;
import com.personal.domain.store.service.StoreCommonService;
import com.personal.domain.user.service.UserAddressCommonService;
import com.personal.domain.user.service.UserCommonService;
import com.personal.entity.order.OrderStatus;
import com.personal.entity.order.Orders;
import com.personal.entity.order.OrdersDetail;
import com.personal.entity.product.Product;
import com.personal.entity.store.Store;
import com.personal.entity.user.User;
import com.personal.entity.user.UserAddress;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class OrdersService {

    private final static String PREFIX_ORDER_ID = "ORDER_NO";

    private final UserCommonService userCommonService;
    private final UserAddressCommonService userAddressCommonService;
    private final StoreCommonService storeCommonService;
    private final ProductCommonService productCommonService;
    private final CartCommonService cartCommonService;
    private final OrdersCommonService ordersCommonService;
    private final OrderDetailService orderDetailService;
    private final OrdersRepository ordersRepository;

    @Transactional
    public void orders(AuthUser authUser , OrderRequest.Order order) {
        // 장바구니 조회(현 시점 가격 포함)
        CartResponse.GetCart carts = cartCommonService.getCarts(authUser.getUserId());

        List<CartList.Cart> cartList = carts.list();
        TotalResponse.TotalAmt totalAmt = carts.total();

        // 장바구니가 비어있을 때
        if (cartList.isEmpty()) {
            throw new OrderException(ResponseCode.INVALID_ORDER_ACCESS);
        }

        UserAddress userAddress = userAddressCommonService.getRepUserAddress(authUser.getUserId());

        // 등록되어있는 대표주소가 없을 때!
        if (!userAddress.isRepYN()) {
            throw new NotFoundException(ResponseCode.NOT_FOUND_USER_ADDRESS);
        }

        User user = userCommonService.getUserById(authUser.getUserId());
        Store store = storeCommonService.getStoreById(cartList.get(0).storeId());

        // OrderNO 채번
        String orderNo = PREFIX_ORDER_ID + UUID.randomUUID().toString().replaceAll("-" , "");

        // OrderDate 오늘 날짜
        LocalDate orderDate = LocalDate.now();

        // Orders에 저장
        Orders orders = Orders.builder()
                .orderNo(orderNo)
                .orderDate(orderDate)
                .recipi(user.getName())
                .tel(order.tel())
                .request(order.request())
                .orderStatus(OrderStatus.PENDING)
                .paymentMethod("일반 결제")
                .totalAmt(totalAmt.totalAmt())
                .totalTax(totalAmt.totalTax())
                .zip(userAddress.getZip())
                .address(userAddress.getAddress())
                .addressDetail(userAddress.getAddressDetail())
                .user(user)
                .store(store)
                .build();
        Orders saveOrders = ordersRepository.save(orders);

        // OrdersDetail에 저장
        ArrayList<OrdersDetail> list = new ArrayList<>();
        for (CartList.Cart cart : cartList) {
            Product product = productCommonService.getProductByIdAndStoreIdAndTypeProduct(cart.productId() , cart.storeId());
            Long sumAmt = cart.customyn() ? cart.basePrice() + cart.customPrice() : cart.basePrice();
            OrdersDetail ordersDetail = OrdersDetail.builder()
                    .productName(cart.productName())
                    .length(cart.length())
                    .width(cart.width())
                    .qty(cart.qty())
                    .customYN(cart.customyn())
                    .customPrice(cart.customPrice())
                    .basePrice(cart.basePrice())
                    .amt(sumAmt)
                    .tax(0L)
                    .orders(saveOrders)
                    .product(product)
                    .build();
            list.add(ordersDetail);
        }
        orderDetailService.saveAll(list);
    }

    public Page<OrderResponse.Infos> getOrders(AuthUser authUser , OrderRequest.GetOrder getOrder) {
        Pageable pageable = PageRequest.of(getOrder.page() - 1, getOrder.size());
        return ordersRepository.getOrders(authUser.getUserId(), getOrder, pageable);
    }

    public OrderResponse.Info getOrder(AuthUser authUser , Long orderId) {
        Orders orders = ordersCommonService.getOrderByOrdersIdAndUserId(orderId, authUser.getUserId());
        List<OrderDetail.Detail> ordersDetails = orderDetailService.getOrderDetail(orderId);
        return new OrderResponse.Info(
                orders.getId(),
                orders.getOrderNo(),
                orders.getOrderDate(),
                orders.getRecipi(),
                orders.getTel(),
                orders.getRequest(),
                orders.getZip(),
                orders.getAddress(),
                orders.getAddressDetail(),
                orders.getOrderStatus(),
                orders.getTotalAmt(),
                orders.getTotalTax(),
                ordersDetails);
    }
}
