package com.vti.vietbank.service.impl;

import com.vti.vietbank.dto.request.CustomerRegistrationRequest;
import com.vti.vietbank.dto.request.CustomerSearchRequest;
import com.vti.vietbank.dto.request.UpdateCustomerRequest;
import com.vti.vietbank.dto.response.ApiResponse;
import com.vti.vietbank.dto.response.CustomerResponse;
import com.vti.vietbank.dto.response.PageResponse;
import com.vti.vietbank.entity.Customer;
import com.vti.vietbank.entity.Role;
import com.vti.vietbank.entity.User;
import com.vti.vietbank.exception.DuplicateResourceException;
import com.vti.vietbank.exception.ResourceNotFoundException;
import com.vti.vietbank.repository.CustomerRepository;
import com.vti.vietbank.repository.RoleRepository;
import com.vti.vietbank.repository.UserRepository;
import com.vti.vietbank.service.CustomerService;
import com.vti.vietbank.service.IRoleService;
import com.vti.vietbank.service.IUserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;
    private final IUserService iUserService;
    private final IRoleService iRoleService;

    @Override
    @Transactional
    public ApiResponse<CustomerResponse> register(CustomerRegistrationRequest request) {
        if (iUserService.existsByPhoneNumber(request.getPhoneNumber()).getData()) {
            throw new DuplicateResourceException("User", "phoneNumber", request.getPhoneNumber());
        }
        if (request.getCitizenId() != null && existsByCitizenId(request.getCitizenId()).getData()) {
            throw new DuplicateResourceException("Customer", "citizenId", request.getCitizenId());
        }

        Role role = iRoleService.getByName("CUSTOMER").getData();

        User user = new User();
        user.setPhoneNumber(request.getPhoneNumber());
        user.setPassword(passwordEncoder.encode((request.getPassword()))); // TODO: encode later when adding security
        user.setRole(role);

        user = userRepository.save(user);

        Customer customer = Customer.builder()
                .user(user)
                .fullName(request.getFullName())
                .email(request.getEmail())
                .dateOfBirth(request.getDateOfBirth())
                .gender(request.getGender())
                .citizenId(request.getCitizenId())
                .address(request.getAddress())
                .build();
        customer = customerRepository.save(customer);

        CustomerResponse response = CustomerResponse.builder()
                .id(customer.getId())
                .phoneNumber(user.getPhoneNumber())
                .fullName(customer.getFullName())
                .email(customer.getEmail())
                .dateOfBirth(customer.getDateOfBirth())
                .gender(customer.getGender())
                .citizenId(customer.getCitizenId())
                .address(customer.getAddress())
                .isDeleted(customer.isDeleted())
                .createdAt(customer.getCreateAt())
                .updatedAt(customer.getUpdateAt())
                .build();
        return ApiResponse.success("Customer registered", response);
    }

    @Override
    public ApiResponse<CustomerResponse> getById(Long id) {
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
    public ApiResponse<CustomerResponse> update(Long id, UpdateCustomerRequest request) {
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
        if (request.isDeleted() != customer.isDeleted()) {
            customer.setDeleted(request.isDeleted());
        }
        customer = customerRepository.save(customer);

        CustomerResponse response = convertToCustomerResponse(customer);
        return ApiResponse.success("Customer updated successfully", response);
    }

    @Override
    @Transactional
    public ApiResponse<Void> delete(Long id) {
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

    @Override
    public ApiResponse<Boolean> existsByCitizenId(String citizenId) {
        return ApiResponse.success(customerRepository.existsByCitizenId(citizenId));
    }

    @Override
    public ApiResponse<CustomerResponse> getCustomerByUserId(Long userId) {
        Customer customer = customerRepository.findByUser_Id(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "userId", userId));
        return ApiResponse.success(convertToCustomerResponse(customer));
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


