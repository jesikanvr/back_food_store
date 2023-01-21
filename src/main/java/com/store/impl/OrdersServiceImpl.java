package com.store.impl;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.store.dto.EmailValuesDTO;
import com.store.dto.OrderDTO;
import com.store.dto.OrdersResponse;
import com.store.dto.UserDTO;
import com.store.entitys.Address;
import com.store.entitys.Order;
import com.store.entitys.Product;
import com.store.entitys.User;
import com.store.exceptions.ResourceNotFoundException;
import com.store.repository.AddressRepository;
import com.store.repository.OrdersRepository;
import com.store.repository.ProductsRepository;
import com.store.repository.UsersRepository;
import com.store.service.EmailService;
import com.store.service.OrdersService;
import com.store.service.WSService;

@Service
public class OrdersServiceImpl implements OrdersService {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private OrdersRepository repository;

    @Autowired
    private UsersRepository repository2;

    @Autowired
    private AddressRepository repository3;

    @Autowired
    private ProductsRepository repository4;

    @Autowired
    private WSService service;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UsersRepository repository5;

    @Override
    public Boolean receiveOrder(Long userId, OrderDTO orderDTO) {
        Collection<Order> orders = createOrders(userId, orderDTO);
        Collection<Order> newOrders = repository.saveAll(orders);
        newOrders.forEach((order) -> {
            orderDTO.getProducts().forEach((key, value) -> {
                repository4.insertAmount(value, order.getId(), key);
                Product product = repository4.findById(key)
                        .orElseThrow(() -> new ResourceNotFoundException("Product", "id", key));
                Double net_price = product.getPrice() * value;
                repository4.insertNetPrice(net_price, order.getId(), key);
            });
            service.notifyFrontend(mapOrderDTO(order));
        });
        return true;
    }

    @Override
    public void setStatus(Long id, int status) {
        Order order = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));
        order.setState(status);
        repository.save(order);
        if (status == 2) {
            Long addressId = order.getAddress().getId();
            Address address = repository3.findById(addressId)
                    .orElseThrow(() -> new ResourceNotFoundException("Address", "id", addressId));
            User user = repository2.findByAddress(address.getId());

            String content = "El pedido realizado el " + order.getDate() + " ha sido rechazado"
                    +
                    ". La dirección " + address.getFormatted() + " adjunta al pedido no es válida. "
                    +
                    "Por favor, rectifique la dirección y vuelva a realizar el pedido. Muchas Gracias.";

            EmailValuesDTO dto = new EmailValuesDTO(null, user.getEmail(), "Pedido rechazado", content, user.getUser());
            emailService.sendMail(dto);
            System.out.println("Correo enviadoooooooooooooooooooooooooooooooooo a " + user.getEmail());
        }
    }

    private Collection<Order> createOrders(Long userId, OrderDTO orderDTO) {

        Collection<Order> orders = new HashSet<>();
        User user = repository2.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        Collection<Product> products = new ArrayList<>();
        orderDTO.getProducts().forEach((key, value) -> {
            Product product = repository4.findById(key)
                    .orElseThrow(() -> new ResourceNotFoundException("Product", "id", key));
            products.add(product);
        });
        orderDTO.getAddresses().forEach((addr) -> {
            Address address = repository3.findById(addr)
                    .orElseThrow(() -> new ResourceNotFoundException("Address", "id", addr));
            /**
             * Aqí debo validar cada dirección para ver si puede existir o no.
             * Por ahora voy a tomarlas todas como validas
             */
            List<User> recepcionists = repository5.findByMunicipalities(address.getMunicipalities());
            /**
             * Aqui se selcciona el recepcionista que menos pedidos haya atendido
             * para enviarle la notificación para que atienda el pedido
             */
            User recepcionist = selectRecepcionist(recepcionists);
            /**
             * Envio una notificación global con el nombre del recepcionista
             * del municipio de la dirección
             */
            int state = address.getIsValid() ? 1 : 0;
            Order newOrder = new Order(state, address, user);
            newOrder.setDate(Date.valueOf(LocalDate.now()));
            newOrder.setRecepcionist(recepcionist);
            newOrder.setProducts(products);
            orders.add(newOrder);
        });
        return orders;
    }

    private User selectRecepcionist(List<User> recepcionists) {
        User recepcionist = null;
        int pivot = 10000000;
        for (User recep : recepcionists) {
            List<Order> orders = repository.findByRecepcionist(recep);
            if (orders.size() < pivot) {
                pivot = orders.size();
                recepcionist = recep;
            }
        }
        return recepcionist;
    }

    public Date getMinDate() {
        return repository.getMinDateSale();
    }

    @Override
    @Transactional(readOnly = true)
    public OrdersResponse getAllOrders(Long recepcId, int pageNum, int pageSize, String sortBy, String sortDir,
            String filter) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);
        User user = repository2.findById(recepcId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", recepcId));
        Order order = new Order();
        order.setRecepcionist(user);
        Example<Order> example = Example.of(order,
                ExampleMatcher.matchingAll().withStringMatcher(StringMatcher.CONTAINING).withIgnoreCase()
                        .withIgnorePaths("id", "secondPhone", "state", "date", "products", "address"));
        Page<Order> ordesPage = repository.findAll(example, pageable);
        List<Order> lisOrders = ordesPage.getContent();
        List<OrderDTO> content = lisOrders.stream().map(orde -> mapOrderDTO(orde)).collect(Collectors.toList());
        OrdersResponse response = new OrdersResponse();
        response.setContent(content);
        response.setPageNo(ordesPage.getNumber());
        response.setPageSize(ordesPage.getSize());
        response.setTotalPage(ordesPage.getTotalPages());
        response.setTotalProducts(ordesPage.getTotalElements());
        response.setLastPage(ordesPage.isLast());
        return response;
    }

    private OrderDTO mapOrderDTO(Order order) {
        OrderDTO dto = mapper.map(order, OrderDTO.class);

        List<Long> productsId = repository4.getProductsByOrder(order.getId());
        Map<Long, Integer> mapProducts = new HashMap<>();
        productsId.forEach((productId) -> {
            int amount = repository4.getAmount(order.getId(), productId);
            mapProducts.put(productId, amount);
        });
        dto.setProducts(mapProducts);
        User user = repository2.findByAddress(order.getAddress().getId());
        dto.setClient(mapper.map(user, UserDTO.class));
        return dto;
    }

    private Order mapOrderEntity(OrderDTO orderDTO) {
        return mapper.map(orderDTO, Order.class);
    }

    @Override
    public void init() {
        /**
         * No funciona por ahora
         */
        // repository.crearAmountColumn();
        // repository.crearNetPriceColumn();
    }

}
