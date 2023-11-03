package ru.practicum.shareit.request.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, value = "/DBFiller.sql")
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, value = "/DBCleaner.sql")
public class ItemRequestRepositoryTest {
    @Autowired
    private ItemRequestRepository itemRequestRepository;

    @Test
    public void findAllByRequestorIdOrderByCreatedDescTest() {
        List<ItemRequest> requests = itemRequestRepository.findAllByRequestorIdOrderByCreatedDesc(2L);
        assertEquals(2, requests.size());
        assertEquals(2L, requests.get(0).getId());
    }

    @Test
    public void findAllByRequestorIdIsNotOrderByCreatedDescTest() {
        List<ItemRequest> requests = itemRequestRepository.findAllByRequestorIdIsNotOrderByCreatedDesc(2L, PageRequest.of(0, 10));
        assertEquals(1, requests.size());
        assertEquals(3L, requests.get(0).getId());
    }
}
