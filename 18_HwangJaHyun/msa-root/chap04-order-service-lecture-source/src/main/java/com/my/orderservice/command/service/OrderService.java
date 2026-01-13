package com.ohgiraffers.orderservice.command.service;

import com.ohgiraffers.orderservice.command.client.UserClient;
import com.ohgiraffers.orderservice.command.dto.OrderDTO;
import com.ohgiraffers.orderservice.command.entity.Order;
import com.ohgiraffers.orderservice.command.repository.OrderRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/*
 * 주문 관련 비즈니스 로직을 처리하는 서비스
 * 주문 생성 시 User Service와 통신하여 사용자 등급 정보를 조회하고, 이에 따른 할인 정책을 적용함
 */
@Service
@RequiredArgsConstructor
public class OrderService {

  private final OrderRepository orderRepository;
  private final UserClient userClient;
  private final ModelMapper modelMapper;

  /*
   * 주문 생성 메서드
   * @CircuitBreaker: User Service 호출 실패 시 fallback 메서드를 실행하여 장애 전파를 방지함
   * name = "userServiceCB": Gateway -> application.yml에 설정된 서킷 브레이커 인스턴스 이름
   */
  @CircuitBreaker(name = "userServiceCB", fallbackMethod = "fallbackGetUserGrade")
  public OrderDTO createOrder(OrderDTO orderDTO, Long userId) {
    // userId를 직접 사용해 주문 생성 전에 사용자의 등급을 알아올 수 있음
    // Feign Client를 통해 User Service 호출
    String userStatus = userClient.getUserGrade(userId).getData();
    System.out.println("userStatus = " + userStatus);
    if (userStatus == null || userStatus.isEmpty()) {
      throw new IllegalArgumentException("유효하지 않은 사용자 등급입니다.");
    }

    // 주문 생성 로직
    // PREMIUM 등급인 경우 10% 할인 적용
    if ("PREMIUM".equals(userStatus)) orderDTO.setPrice(orderDTO.getPrice() * 0.9);
    Order order = modelMapper.map(orderDTO, Order.class);
    order.createOrder(userId, LocalDateTime.now());
    Order savedOrder = orderRepository.save(order);

    orderDTO.setId(savedOrder.getId());
    orderDTO.setOrderDate(savedOrder.getOrderDate());
    return orderDTO;
  }


  // User Service 호출 실패 시 실행되는 Fallback 메서드
  public OrderDTO fallbackGetUserGrade(OrderDTO orderDTO, Long userId, Throwable t) {
    // User Service의 장애가 발생한 경우 기본 등급을 사용하거나 할인 없이 주문 처리 하도록
    // fallback method를 작성한다.
    // 여기서는 할인 없이 주문을 생성하는 로직으로 대체
    Order order = modelMapper.map(orderDTO, Order.class);
    order.createOrder(userId, LocalDateTime.now());
    Order savedOrder = orderRepository.save(order);

    orderDTO.setId(savedOrder.getId());
    orderDTO.setOrderDate(savedOrder.getOrderDate());
    return orderDTO;
  }


}
