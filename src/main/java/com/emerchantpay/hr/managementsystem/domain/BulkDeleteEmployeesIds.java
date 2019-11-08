package com.emerchantpay.hr.managementsystem.domain;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BulkDeleteEmployeesIds {

    private List<Long> employeesIds;
}
