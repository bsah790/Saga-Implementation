package com.demo.learning.saga.UserService.query.api.projection;

import com.demo.learning.saga.CommonService.model.CardDetails;
import com.demo.learning.saga.CommonService.model.User;
import com.demo.learning.saga.CommonService.query.GetUserDetailsQuery;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

@Component
public class UserProjection {
    @QueryHandler
    public User getUserPaymentDetails(GetUserDetailsQuery userDetailsQuery) {
        CardDetails cardDetails = CardDetails
                .builder()
                .name("Amit Kumar")
                .cardNumber("123456784321")
                .validUntilMonth(03)
                .validUntilYear(22)
                .cvv(123)
                .build();
        User user = User.builder()
                .firstName("Amit")
                .lastName("Kumar")
                .userId(userDetailsQuery.getUserId())
                .cardDetails(cardDetails)
                .build();
        return user;
    }
}
