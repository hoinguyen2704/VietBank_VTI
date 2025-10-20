package com.vti.vietbank2.service.impl;

import com.vti.vietbank2.dto.request.CustomerRegistrationRequest;
import com.vti.vietbank2.dto.request.CustomerSearchRequest;
import com.vti.vietbank2.dto.request.UpdateCustomerRequest;
import com.vti.vietbank2.dto.response.ApiResponse;
import com.vti.vietbank2.dto.response.CustomerResponse;
import com.vti.vietbank2.dto.response.PageResponse;
import com.vti.vietbank2.entity.Customer;
import com.vti.vietbank2.entity.Role;
import com.vti.vietbank2.entity.User;
import com.vti.vietbank2.exception.DuplicateResourceException;
import com.vti.vietbank2.exception.ResourceNotFoundException;
import com.vti.vietbank2.repository.CustomerRepository;
import com.vti.vietbank2.repository.RoleRepository;
import com.vti.vietbank2.repository.UserRepository;
import com.vti.vietbank2.service.CustomerService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CustomerRepository customerRepository;

    @Override
    @Transactional
    public ApiResponse<CustomerResponse> register(CustomerRegistrationRequest request) {
        if (userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new DuplicateResourceException("User", "phoneNumber", request.getPhoneNumber());
        }
        if (request.getCitizenId() != null && customerRepository.existsByCitizenId(request.getCitizenId())) {
            throw new DuplicateResourceException("Customer", "citizenId", request.getCitizenId());
        }

        Role role = roleRepository.findByName("CUSTOMER")
                .orElseThrow(() -> new ResourceNotFoundException("Role", "name", "CUSTOMER"));

        User user = new User();
        user.setPhoneNumber(request.getPhoneNumber());
        user.setPassword(request.getPassword()); // TODO: encode later when adding security
        user.setRole(role);
        user = userRepository.save(user);

        Customer customer = new Customer();
        customer.setId(user.getId()); // keep same id if desired, or let auto
        customer.setUser(user);
        customer.setFullName(request.getFullName());
        customer.setEmail(request.getEmail());
        customer.setDateOfBirth(request.getDateOfBirth());
        customer.setGender(request.getGender());
        customer.setCitizenId(request.getCitizenId());
        customer.setAddress(request.getAddress());
        customer = customerRepository.save(customer);

        CustomerResponse response = new CustomerResponse(
                customer.getId(),
                user.getPhoneNumber(),
                customer.getFullName(),
                customer.getEmail(),
                customer.getDateOfBirth(),
                customer.getGender(),
                customer.getCitizenId(),
                customer.getAddress(),
                customer.isDeleted(),
                customer.getCreateAt(),
                customer.getUpdateAt()
        );
        return ApiResponse.success("Customer registered", response);
    }

    @Override
    public ApiResponse<CustomerResponse> getById(Integer id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", id));
        CustomerResponse response = new CustomerResponse(
                customer.getId(),
                customer.getUser().getPhoneNumber(),
                customer.getFullName(),
                customer.getEmail(),
                customer.getDateOfBirth(),
                customer.getGender(),
                customer.getCitizenId(),
                customer.getAddress(),
                customer.isDeleted(),
                customer.getCreateAt(),
                customer.getUpdateAt()
        );
        return ApiResponse.success(response);
    }

    @Override
    public ApiResponse<PageResponse<CustomerResponse>> getAll(CustomerSearchRequest searchRequest) {
        // Tạo Specification cho filtering
        Specification<Customer> spec = createCustomerSpecification(searchRequest);
        
        // Tạo Pageable cho pagination và sorting
        Sort sort = Sort.by(
            searchRequest.getSortDirection().equalsIgnoreCase("desc") ? 
            Sort.Direction.DESC : Sort.Direction.ASC, 
            searchRequest.getSortBy()
        );
        Pageable pageable = PageRequest.of(searchRequest.getPage(), searchRequest.getSize(), sort);
        
        // Thực hiện query với pagination
        Page<Customer> customerPage = customerRepository.findAll(spec, pageable);
        
        // Convert to DTO
        List<CustomerResponse> customerResponses = customerPage.getContent().stream()
            .map(this::convertToCustomerResponse)
            .toList();
        
        // Tạo PageResponse
        PageResponse<CustomerResponse> pageResponse = new PageResponse<>(
            customerResponses,
            customerPage.getNumber(),
            customerPage.getSize(),
            customerPage.getTotalElements(),
            customerPage.getTotalPages(),
            customerPage.isFirst(),
            customerPage.isLast()
        );
        
        return ApiResponse.success(pageResponse);
    }
    
    @Override
    @Transactional
    public ApiResponse<CustomerResponse> update(Integer id, UpdateCustomerRequest request) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", id));
        
        // Check for duplicate citizen ID if provided
        if (request.getCitizenId() != null && !request.getCitizenId().equals(customer.getCitizenId())) {
            if (customerRepository.existsByCitizenId(request.getCitizenId())) {
                throw new DuplicateResourceException("Customer", "citizenId", request.getCitizenId());
            }
        }
        
        // Update customer information
        if (request.getFullName() != null) {
            customer.setFullName(request.getFullName());
        }
        if (request.getEmail() != null) {
            customer.setEmail(request.getEmail());
        }
        if (request.getDateOfBirth() != null) {
            customer.setDateOfBirth(request.getDateOfBirth());
        }
        if (request.getGender() != null) {
            customer.setGender(request.getGender());
        }
        if (request.getCitizenId() != null) {
            customer.setCitizenId(request.getCitizenId());
        }
        if (request.getAddress() != null) {
            customer.setAddress(request.getAddress());
        }
        if(request.isDeleted() != customer.isDeleted()){
            customer.setDeleted(request.isDeleted());
        }
        customer = customerRepository.save(customer);
        
        CustomerResponse response = convertToCustomerResponse(customer);
        return ApiResponse.success("Customer updated successfully", response);
    }
    
    @Override
    @Transactional
    public ApiResponse<Void> delete(Integer id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", id));
        
        if (customer.isDeleted()) {
            return new ApiResponse<>(true, "Customer is already deleted", null, LocalDateTime.now());
        }
        
        customer.setDeleted(true);
        customerRepository.save(customer);
        
        return new ApiResponse<>(true, "Customer deleted successfully", null, LocalDateTime.now());
    }
    
    @Override
    public ApiResponse<List<CustomerResponse>> search(CustomerSearchRequest searchRequest) {
        Specification<Customer> spec = createCustomerSpecification(searchRequest);
        List<Customer> customers = customerRepository.findAll(spec);
        
        List<CustomerResponse> responses = customers.stream()
                .map(this::convertToCustomerResponse)
                .toList();
        
        return ApiResponse.success(responses);
    }
    
    private Specification<Customer> createCustomerSpecification(CustomerSearchRequest searchRequest) {
        return (root, query, criteriaBuilder) -> {
            List<jakarta.persistence.criteria.Predicate> predicates = new ArrayList<>();
            
            if (searchRequest.getFullName() != null && !searchRequest.getFullName().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("fullName")), 
                    "%" + searchRequest.getFullName().toLowerCase() + "%"
                ));
            }
            
            if (searchRequest.getPhoneNumber() != null && !searchRequest.getPhoneNumber().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("user").get("phoneNumber")), 
                    "%" + searchRequest.getPhoneNumber().toLowerCase() + "%"
                ));
            }
            
            if (searchRequest.getCitizenId() != null && !searchRequest.getCitizenId().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("citizenId")), 
                    "%" + searchRequest.getCitizenId().toLowerCase() + "%"
                ));
            }
            
            if (searchRequest.getEmail() != null && !searchRequest.getEmail().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("email")), 
                    "%" + searchRequest.getEmail().toLowerCase() + "%"
                ));
            }
            
            // Exclude deleted customers
            predicates.add(criteriaBuilder.equal(root.get("isDeleted"), false));
            
            return criteriaBuilder.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        };
    }
    
    private CustomerResponse convertToCustomerResponse(Customer customer) {
        return new CustomerResponse(
                customer.getId(),
                customer.getUser().getPhoneNumber(),
                customer.getFullName(),
                customer.getEmail(),
                customer.getDateOfBirth(),
                customer.getGender(),
                customer.getCitizenId(),
                customer.getAddress(),
                customer.isDeleted(),
                customer.getCreateAt(),
                customer.getUpdateAt()
        );
    }
}


