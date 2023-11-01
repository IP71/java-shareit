package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, value = "/DBFiller.sql")
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, value = "/DBCleaner.sql")
public class ItemRepositoryTest {
    @Autowired
    private ItemRepository itemRepository;
    private PageRequest pageRequest = PageRequest.of(0, 10);

    @Test
    public void findAllByOwnerTest() {
        List<Item> result = itemRepository.findAllByOwner(1L, pageRequest);
        assertEquals(2, result.size());
    }

    @Test
    public void searchTest() {
        List<Item> result = itemRepository.search("item3", pageRequest);
        assertEquals(1, result.size());
    }

    @Test
    public void findAllByRequestIdInTest() {
        List<Item> result = itemRepository.findAllByRequestIdIn(List.of(1L));
        assertEquals(1, result.size());
        assertEquals(2L, result.get(0).getId());
    }
}
