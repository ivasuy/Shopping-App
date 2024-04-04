package com.Intern.ShoppingApp.service;

import com.Intern.ShoppingApp.entity.*;
import com.Intern.ShoppingApp.error.CustomException;
import com.Intern.ShoppingApp.model.*;
import com.Intern.ShoppingApp.model.request.CouponRequest;
import com.Intern.ShoppingApp.model.request.UserRequest;
import com.Intern.ShoppingApp.model.response.*;
import com.Intern.ShoppingApp.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    private final InventoryRepository inventoryRepository;
    private final CouponRepository couponRepository;
    private final UserRepository userRepository;
    private final CouponUserRepository couponUserRepository;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final AtomicLong transactionCounter = new AtomicLong(100000);

    @Override
    public ResponseModel getInventoryData() {
        List<Inventory> inventoryItems = inventoryRepository.findAll();
        List<InventoryResponse> inventoryResponse = inventoryItems.stream()
                .map(this::mapToInventoryResponse)
                .collect(Collectors.toList());

        return ResponseModel.builder()
                .statusCode(200L)
                .data(inventoryResponse)
                .build();
    }

    @Override
    public ResponseModel addCoupons(List<CouponRequest> coupons) {
        List<Coupon> couponEntities = coupons.stream()
                .map(couponRequest -> Coupon.builder()
                        .couponName(couponRequest.getCouponName())
                        .discount(couponRequest.getDiscount())
                        .build())
                .collect(Collectors.toList());
        couponRepository.saveAll(couponEntities);

        return ResponseModel.builder()
                .statusCode(200L)
                .data("Coupons Added Successfully!")
                .build();
    }

    @Override
    public ResponseModel fetchCoupons() {
        List<Coupon> coupons = couponRepository.findAll();
        Map<String, Float> couponMap = coupons.stream()
                .collect(Collectors.toMap(Coupon::getCouponName, Coupon::getDiscount));

        return ResponseModel.builder()
                .statusCode(200L)
                .data(couponMap)
                .build();
    }

    @Override
    public ResponseModel createNewUser(UserRequest request) {
        User user = User.builder()
                .userName(request.getUserName())
                .userPassword(request.getUserPassword())
                .userPhoneNumber(request.getUserPhoneNumber())
                .build();
        userRepository.save(user);

        return ResponseModel.builder()
                .statusCode(200L)
                .data("User with id: " + user.getUserId() + " created successfully")
                .build();
    }

    @Override
    public ResponseModel placeOrder(Long userId, Long qty, String coupon) throws CustomException {
        List<Inventory> inventories = inventoryRepository.findAll();
        Inventory inventory = inventories.get(0);
        Long quantity = inventory.getProductAvailable();
        if (qty < 1 || qty > quantity) throw new CustomException("Invalid quantity");

        Coupon savedCoupon = couponRepository.findByCouponName(coupon);
        if (savedCoupon == null) throw new CustomException("Invalid coupon");

        CouponUser savedCouponUser = couponUserRepository.findByUserIdAndCouponId(userId, savedCoupon.getCouponId());
        if (savedCouponUser != null) throw new CustomException("Invalid coupon");

        Float productPrice = inventory.getProductPrice();
        Float amountBeforeDiscount = qty * productPrice;
        Float discountAmount = amountBeforeDiscount * (savedCoupon.getDiscount() / 100);
        Float finalAmount = amountBeforeDiscount - discountAmount;

        User user = getUser(userId);
        CouponUser couponUser = CouponUser.builder()
                .user(user)
                .coupon(savedCoupon)
                .build();
        couponUserRepository.save(couponUser);

        Order order = Order.builder()
                .orderAmount(finalAmount)
                .orderDate(new Date())
                .coupon(savedCoupon)
                .user(user)
                .build();
        orderRepository.save(order);

        Long newQuantity = quantity - qty;
        Long orderedQuantity = inventory.getProductOrdered() + qty;
        inventory.setProductAvailable(newQuantity);
        inventory.setProductOrdered(orderedQuantity);
        inventoryRepository.save(inventory);

        return ResponseModel.builder()
                .statusCode(200L)
                .data(OrderAmountResponse.builder()
                        .orderId(order.getOrderId())
                        .userId(userId)
                        .orderAmount(finalAmount)
                        .quantity(qty)
                        .coupon(savedCoupon.getCouponName())
                        .build())
                .build();
    }

    @Override
    public ResponseModel orderPayment(Long userId, Long orderId, Float amount) throws CustomException {
        Long statusCode = getRandomStatusCode();
        Long transactionSequence = transactionCounter.incrementAndGet();
        String transactionId = "tran010" + transactionSequence;
        String status;
        String description = null;

        if (statusCode == 200) {
            status = "successful";
        } else if (statusCode == 400) {
            status = "failed";
            description = getFailureDescription();
        } else if (statusCode == 504) {
            status = "failed";
            description = "No response from payment server";
        } else {
            status = "failed";
            description = "Order is already paid for";
        }

        Order order = getOrder(orderId);
        Payment payment = Payment.builder()
                .paymentStatus(status)
                .transactionId(transactionId)
                .order(order)
                .build();
        paymentRepository.save(payment);

        if (status.equals("successful")) {
            PaymentSuccessResponse successResponse = new PaymentSuccessResponse();
            successResponse.setUserId(userId);
            successResponse.setOrderId(orderId);
            successResponse.setTransactionId(transactionId);
            successResponse.setPaymentStatus(status);
            return ResponseModel.builder()
                    .statusCode(statusCode)
                    .data(successResponse)
                    .build();
        } else {
            PaymentFailResponse failResponse = new PaymentFailResponse();
            failResponse.setUserId(userId);
            failResponse.setOrderId(orderId);
            failResponse.setTransactionId(transactionId);
            failResponse.setPaymentStatus(status);
            failResponse.setPaymentDescription(description);
            return ResponseModel.builder()
                    .statusCode(statusCode)
                    .data(failResponse)
                    .build();
        }
    }
    @Override
    public ResponseModel getOrdersByUserId(Long userId) throws CustomException {
        List<Order> orders = orderRepository.findByUserId(userId);
        if (orders.isEmpty()) throw new CustomException("NO ORDER EXIST FOR GIVEN USER ID" + userId);
        List<OrderUserResponse> orderResponses = new ArrayList<>();

        for (Order order : orders) {
            OrderUserResponse orderResponse = new OrderUserResponse();
            orderResponse.setOrderId(order.getOrderId());
            orderResponse.setOrderAmount(order.getOrderAmount());
            orderResponse.setOrderDate(order.getOrderDate());
            orderResponse.setCoupon(order.getCoupon().getCouponName());
            orderResponses.add(orderResponse);
        }

        return ResponseModel.builder()
                .statusCode(200L)
                .data(orderResponses)
                .build();
    }

    @Override
    public ResponseModel getOrderByIds(Long userId, Long orderId) throws CustomException {
        List<Order> orders = orderRepository.findByUserId(userId);
        if (orders.isEmpty()) throw new CustomException("NO ORDER EXIST FOR GIVEN USER ID" + userId);

        List<OrderFoundResponse> orderResponses = new ArrayList<>();
        for (Order order : orders) {
            List<Payment> payments = paymentRepository.findByOrderId(order.getOrderId());
            for (Payment payment : payments) {
                OrderFoundResponse orderResponse = getOrderFoundResponse(order, payment);
                orderResponses.add(orderResponse);
            }
        }

        return ResponseModel.builder()
                .statusCode(200L)
                .data(orderResponses)
                .build();
    }

    private OrderFoundResponse getOrderFoundResponse(Order order, Payment payment) {
        OrderFoundResponse orderResponse = new OrderFoundResponse();
        orderResponse.setOrderId(order.getOrderId());
        orderResponse.setOrderAmount(order.getOrderAmount());
        orderResponse.setOrderDate(order.getOrderDate());
        orderResponse.setCoupon(order.getCoupon() != null ? order.getCoupon().getCouponName() : null);
        orderResponse.setTransactionId(payment.getTransactionId());
        orderResponse.setPaymentStatus(payment.getPaymentStatus());
        return orderResponse;
    }

    private InventoryResponse mapToInventoryResponse(Inventory inventory) {
        return InventoryResponse.builder()
                .productOrdered(inventory.getProductOrdered())
                .productPrice(inventory.getProductPrice())
                .productAvailable(inventory.getProductAvailable())
                .build();
    }

    public User getUser(Long userId) throws CustomException {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("USER DOES NOT EXIST FOR GIVEN ID" + userId));
    }

    public Order getOrder(Long orderId) throws CustomException {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException("ORDER DOES NOT EXIST FOR GIVEN ID" + orderId));
    }

    private String getFailureDescription() {
        Long[] failureCodes = {400L, 401L, 402L};
        int randomIndex = (int) (Math.random() * failureCodes.length);
        Long failureCode = failureCodes[randomIndex];

        if (failureCode == 400L) return "Payment Failed as amount is invalid";
        else if (failureCode == 401L) return "Payment Failed from bank";
        else return "Payment Failed due to invalid order id";
    }

    private Long getRandomStatusCode() {
        Long[] statusCodes = {200L, 400L, 504L, 405L};
        int randomIndex = (int) (Math.random() * statusCodes.length);
        return statusCodes[randomIndex];
    }
}
