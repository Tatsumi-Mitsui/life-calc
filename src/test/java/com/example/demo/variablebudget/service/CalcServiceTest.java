package com.example.demo.variablebudget.service;

import org.junit.jupiter.api.Test;

import com.example.demo.feature.variablebudget.service.CalcService;
import com.example.demo.feature.variablebudget.web.model.VariableBudgetForm;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CalcServiceTest {
    
    private final CalcService calc = new CalcService();

    @Test
    void fillTotals_success() {
        var form = new VariableBudgetForm();
        form.setIncome(200_000L);
        form.setFixedCosts(List.of(50_000L,30_000L));

        calc.fillTotals(form);

        assertEquals(80_000L, form.getFixedCostTotal());
        assertEquals(120_000L, form.getResultVariable());
    }
}
