package com.example.demo.variablebudget.service;

import com.example.demo.variablebudget.web.model.VariableBudgetForm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class HistoryFlowTest {
    
    @Autowired
    HistoryAppService service;

    @Test
    void save_fetch_delete() {
        // userId が null/0L → セッション保存（メモリ）
        var sessionForm = new VariableBudgetForm();
        sessionForm.setIncome(200_000L);
        sessionForm.setFixedCosts(List.of(50_000L, 30_00L));
        var sid = service.saveSnapshot(sessionForm);
        assertNotNull(sid);
        assertFalse(service.recentByUser(0L, 10).isEmpty());

        // userId が 1L → JPA保存（DB/H2）
        var dbForm = new VariableBudgetForm();
        dbForm.setUserId(1L);
        dbForm.setIncome(200_000L);
        dbForm.setFixedCosts(List.of(50_000L, 30_000L));
        var id = service.saveSnapshot(dbForm);
        assertNotNull(id);

        var list = service.recentByUser(1L, 10);
        assertTrue(list.stream().anyMatch(h -> id.equals(h.getId())));
        
        assertTrue(service.deleteById(1L, id));
    }
}
