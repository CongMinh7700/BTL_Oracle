package com.test.library.service.impl;

import com.test.library.dto.CartItemDto;
import com.test.library.dto.ProductDto;
import com.test.library.dto.ShoppingCartDto;
import com.test.library.model.CartItem;
import com.test.library.model.Customer;
import com.test.library.model.Product;
import com.test.library.model.ShoppingCart;
import com.test.library.repository.CartItemRepository;
import com.test.library.repository.ShoppingCartRepository;
import com.test.library.service.CustomerService;
import com.test.library.service.ShoppingCartService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository cartRepository;

    private final CartItemRepository itemRepository;

    private final CustomerService customerService;
    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public ShoppingCart addItemToCart(ProductDto productDto, int quantity, String username) {
        Customer customer = customerService.findByUserName(username);
        ShoppingCart shoppingCart = customer.getShoppingCart();

        if (shoppingCart == null) {
            shoppingCart = new ShoppingCart();
        }
        Set<CartItem> cartItemList = shoppingCart.getCartItem();
        CartItem cartItem = find(cartItemList, productDto.getId());
        Product product = transfer(productDto);

        double unitPrice = productDto.getCostPrice();

        int itemQuantity = 0;
        if (cartItemList == null) {
            cartItemList = new HashSet<>();
            if (cartItem == null) {
                cartItem = new CartItem();
                cartItem.setProduct(product);
                cartItem.setCart(shoppingCart);
                cartItem.setQuantity(quantity);
                cartItem.setUnitPrice(unitPrice);
                cartItem.setCart(shoppingCart);
                cartItemList.add(cartItem);
                itemRepository.save(cartItem);
            } else {
                itemQuantity = cartItem.getQuantity() + quantity;
                cartItem.setQuantity(itemQuantity);
                itemRepository.save(cartItem);
            }
        } else {
            if (cartItem == null) {
                cartItem = new CartItem();
                cartItem.setProduct(product);
                cartItem.setCart(shoppingCart);
                cartItem.setQuantity(quantity);
                cartItem.setUnitPrice(unitPrice);
                cartItem.setCart(shoppingCart);
                cartItemList.add(cartItem);
                itemRepository.save(cartItem);
            } else {
                itemQuantity = cartItem.getQuantity() + quantity;
                cartItem.setQuantity(itemQuantity);
                itemRepository.save(cartItem);
            }
        }
        shoppingCart.setCartItem(cartItemList);

        double totalPrice = totalPrice(shoppingCart.getCartItem());
        int totalItem = totalItem(shoppingCart.getCartItem());

        shoppingCart.setTotalPrices(totalPrice);
        shoppingCart.setTotalItems(totalItem);
        shoppingCart.setCustomer(customer);

        return cartRepository.save(shoppingCart);
    }







    @Override
    @Transactional
    public ShoppingCart updateCart(ProductDto productDto, int quantity, String username) {
        Customer customer = customerService.findByUserName(username);
        ShoppingCart shoppingCart = customer.getShoppingCart();
        Set<CartItem> cartItemList = shoppingCart.getCartItem();
        CartItem item = find(cartItemList, productDto.getId());
        int itemQuantity = quantity;


        item.setQuantity(itemQuantity);
        itemRepository.save(item);
        shoppingCart.setCartItem(cartItemList);
        int totalItem = totalItem(cartItemList);
        double totalPrice = totalPrice(cartItemList);
        shoppingCart.setTotalPrices(totalPrice);
        shoppingCart.setTotalItems(totalItem);
        return cartRepository.save(shoppingCart);
    }

    @Override
    public ShoppingCart removeItemFromCart(ProductDto productDto, String username) {
        Customer customer = customerService.findByUserName(username);
        ShoppingCart shoppingCart = customer.getShoppingCart();
        Set<CartItem> cartItemList = shoppingCart.getCartItem();
        CartItem item = find(cartItemList, productDto.getId());
        cartItemList.remove(item);
        itemRepository.delete(item);
        double totalPrice = totalPrice(cartItemList);
        int totalItem = totalItem(cartItemList);
        shoppingCart.setCartItem(cartItemList);
        shoppingCart.setTotalPrices(totalPrice);
        shoppingCart.setTotalItems(totalItem);
        return cartRepository.save(shoppingCart);
    }

//    @Override
//    @Transactional
//    public void deleteCartById(Long id) {
//        Optional<ShoppingCart> optionalShoppingCart = cartRepository.findById(id);
//
//        if (optionalShoppingCart.isPresent()) {
//            ShoppingCart shoppingCart = optionalShoppingCart.get();
//
//            // Kiểm tra và xóa tất cả các CartItem
//            Set<CartItem> cartItems = shoppingCart.getCartItem();
//            if (!CollectionUtils.isEmpty(cartItems)) {
//                itemRepository.deleteAll(cartItems);
//                cartItems.clear();
//            }
//
//            // Cập nhật thông tin ShoppingCart
//            shoppingCart.setTotalPrices(0);
//            shoppingCart.setTotalItems(0);
//
//            // Clear session
//            entityManager.clear();
//
//            // Lưu lại ShoppingCart
//            cartRepository.save(shoppingCart);
//        } else {
//            // Handle the case when ShoppingCart is not found
//            // For example, throw an exception or log an error
//            throw new EntityNotFoundException("ShoppingCart with id " + id + " not found");
//        }
//    }
@Override
public void deleteCartById(Long id) {
    ShoppingCart shoppingCart = cartRepository.getById(id);
    for (CartItem cartItem : shoppingCart.getCartItem()) {
        itemRepository.deleteById(cartItem.getId());
    }
    shoppingCart.setCustomer(null);
    shoppingCart.getCartItem().clear();
    shoppingCart.setTotalPrices(0);
    shoppingCart.setTotalItems(0);
    cartRepository.save(shoppingCart);
}

    @Override
    public ShoppingCartDto addItemToCartSession(ShoppingCartDto cartDto, ProductDto productDto, int quantity) {
        CartItemDto cartItem = findInDTO(cartDto, productDto.getId());
        if (cartDto == null) {
            cartDto = new ShoppingCartDto();
        }
        Set<CartItemDto> cartItemList = cartDto.getCartItems();
        double unitPrice = productDto.getCostPrice();
        int itemQuantity = 0;
        if (cartItemList == null) {
            cartItemList = new HashSet<>();
            if (cartItem == null) {
                cartItem = new CartItemDto();
                cartItem.setProduct(productDto);
                cartItem.setCart(cartDto);
                cartItem.setQuantity(quantity);
                cartItem.setUnitPrice(unitPrice);
                cartItemList.add(cartItem);
                System.out.println("add");
            } else {
                itemQuantity = cartItem.getQuantity() + quantity;
                cartItem.setQuantity(itemQuantity);
            }
        } else {
            if (cartItem == null) {
                cartItem = new CartItemDto();
                cartItem.setProduct(productDto);
                cartItem.setCart(cartDto);
                cartItem.setQuantity(quantity);
                cartItem.setUnitPrice(unitPrice);
                cartItemList.add(cartItem);
                System.out.println("add");
            } else {
                itemQuantity = cartItem.getQuantity() + quantity;
                cartItem.setQuantity(itemQuantity);
            }
        }
        System.out.println("here");
        cartDto.setCartItems(cartItemList);
        double totalPrice = totalPriceDto(cartItemList);
        int totalItem = totalItemDto(cartItemList);
        cartDto.setTotalPrice(totalPrice);
        cartDto.setTotalItems(totalItem);
        System.out.println(cartDto.getTotalItems());
        System.out.println(cartDto.getTotalPrice());
        System.out.println("success");
        return cartDto;
    }

    @Override
    public ShoppingCartDto updateCartSession(ShoppingCartDto cartDto, ProductDto productDto, int quantity) {
        Set<CartItemDto> cartItemList = cartDto.getCartItems();
        CartItemDto item = findInDTO(cartDto, productDto.getId());
        int itemQuantity = item.getQuantity() + quantity;
        int totalItem = totalItemDto(cartItemList);
        double totalPrice = totalPriceDto(cartItemList);
        item.setQuantity(itemQuantity);
        cartDto.setCartItems(cartItemList);
        cartDto.setTotalPrice(totalPrice);
        cartDto.setTotalItems(totalItem);
        System.out.println(cartDto.getTotalItems());
        return cartDto;
    }

    @Override
    public ShoppingCartDto removeItemFromCartSession(ShoppingCartDto cartDto, ProductDto productDto, int quantity) {
        Set<CartItemDto> cartItemList = cartDto.getCartItems();
        CartItemDto item = findInDTO(cartDto, productDto.getId());
        cartItemList.remove(item);
        double totalPrice = totalPriceDto(cartItemList);
        int totalItem = totalItemDto(cartItemList);
        cartDto.setCartItems(cartItemList);
        cartDto.setTotalPrice(totalPrice);
        cartDto.setTotalItems(totalItem);
        System.out.println(cartDto.getTotalItems());
        return cartDto;
    }

    @Override
    public ShoppingCart combineCart(ShoppingCartDto cartDto, ShoppingCart cart) {
        if (cart == null) {
            cart = new ShoppingCart();
        }
        Set<CartItem> cartItems = cart.getCartItem();
        if (cartItems == null) {
            cartItems = new HashSet<>();
        }
        Set<CartItem> cartItemsDto = convertCartItem(cartDto.getCartItems(), cart);
        for (CartItem cartItem : cartItemsDto) {
            cartItems.add(cartItem);
        }
        double totalPrice = totalPrice(cartItems);
        int totalItems = totalItem(cartItems);
        cart.setTotalItems(totalItems);
        cart.setCartItem(cartItems);
        cart.setTotalPrices(totalPrice);
        return cart;
    }

    @Override
    public ShoppingCart getCart(String username) {
        Customer customer = customerService.findByUserName(username);
        ShoppingCart cart = customer.getShoppingCart();
        return cart;
    }
    private CartItemDto findInDTO(ShoppingCartDto shoppingCart, long productId) {
        if (shoppingCart == null) {
            return null;
        }
        CartItemDto cartItem = null;
        for (CartItemDto item : shoppingCart.getCartItems()) {
            if (item.getProduct().getId() == productId) {
                cartItem = item;
            }
        }
        return cartItem;
    }
    private CartItem findCartItem(Set<CartItem> cartItems, Long productId) {
        if(cartItems == null){
            return null;
        }
        CartItem cartItem = null;
        for(CartItem item : cartItems){
            if(item.getProduct().getId() == productId){
                cartItem = item;
            }
        }
        return cartItem;
    }

    private Product transfer(ProductDto productDto) {
        Product product = new Product();
        product.setId(productDto.getId());
        product.setName(productDto.getName());
        product.setCurrentQuantity(productDto.getCurrentQuantity());
        product.setCostPrice(productDto.getCostPrice());
        product.setSalePrice(productDto.getSalePrice());
        product.setDescription(productDto.getDescription());
        product.setImage(productDto.getImage());
        product.set_activated(productDto.isActivated());
        product.set_deleted(productDto.isDeleted());
        product.setCategory(productDto.getCategory());
        return product;
    }



    private CartItem find(Set<CartItem> cartItems, long productId) {
        if (cartItems == null) {
            return null;
        }
        CartItem cartItem = null;
        for (CartItem item : cartItems) {
            if (item.getProduct().getId() == productId) {
                cartItem = item;
            }
        }
        return cartItem;
    }
    private int totalItemDto(Set<CartItemDto> cartItemList) {
        int totalItem = 0;
        for (CartItemDto item : cartItemList) {
            totalItem += item.getQuantity();
        }
        return totalItem;
    }

    private double totalPriceDto(Set<CartItemDto> cartItemList) {
        double totalPrice = 0;
        for (CartItemDto item : cartItemList) {
            totalPrice += item.getUnitPrice() * item.getQuantity();
        }
        return totalPrice;
    }
    private int totalItem(Set<CartItem> cartItemList) {
        int totalItem = 0;
        for (CartItem item : cartItemList) {
            totalItem += item.getQuantity();
        }
        return totalItem;
    }

    private double totalPrice(Set<CartItem> cartItemList) {
        double totalPrice = 0.0;
        for (CartItem item : cartItemList) {
            totalPrice += item.getUnitPrice() * item.getQuantity();
        }
        return totalPrice;
    }
    private Set<CartItem> convertCartItem(Set<CartItemDto> cartItemDtos, ShoppingCart cart) {
        Set<CartItem> cartItems = new HashSet<>();
        for (CartItemDto cartItemDto : cartItemDtos) {
            CartItem cartItem = new CartItem();
            cartItem.setQuantity(cartItemDto.getQuantity());
            cartItem.setProduct(transfer(cartItemDto.getProduct()));
            cartItem.setUnitPrice(cartItemDto.getUnitPrice());
            cartItem.setId(cartItemDto.getId());
            cartItem.setCart(cart);
            cartItems.add(cartItem);
        }
        return cartItems;
    }

}
