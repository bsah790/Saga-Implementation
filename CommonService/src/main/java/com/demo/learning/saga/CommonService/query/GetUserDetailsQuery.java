package com.demo.learning.saga.CommonService.query;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetUserDetailsQuery {
    private String userId;
}
