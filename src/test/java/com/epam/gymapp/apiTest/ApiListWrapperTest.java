package com.epam.gymapp.apiTest;

import static org.junit.jupiter.api.Assertions.*;

import com.epam.gymapp.utils.ApiListWrapper;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class ApiListWrapperTest {

  @Test
  void shouldCreateWrapperWithValidList() {
    // Arrange
    List<String> testList = List.of("item1", "item2", "item3");

    // Act
    ApiListWrapper<String> wrapper = new ApiListWrapper<>(testList);

    // Assert
    assertNotNull(wrapper);
    assertEquals(testList, wrapper.items());
    assertEquals(3, wrapper.items().size());
  }

  @Test
  void shouldCreateWrapperWithEmptyList() {
    // Arrange
    List<Integer> emptyList = new ArrayList<>();

    // Act
    ApiListWrapper<Integer> wrapper = new ApiListWrapper<>(emptyList);

    // Assert
    assertNotNull(wrapper);
    assertTrue(wrapper.items().isEmpty());
  }

  @Test
  void shouldAcceptNullList() {
    // Act & Assert
    assertDoesNotThrow(() -> new ApiListWrapper<String>(null));
  }

  @Test
  void shouldEqualWrapperWithSameContent() {
    // Arrange
    List<String> list1 = List.of("item1", "item2");
    List<String> list2 = List.of("item1", "item2");

    // Act
    ApiListWrapper<String> wrapper1 = new ApiListWrapper<>(list1);
    ApiListWrapper<String> wrapper2 = new ApiListWrapper<>(list2);

    // Assert
    assertEquals(wrapper1, wrapper2);
    assertEquals(wrapper1.hashCode(), wrapper2.hashCode());
  }
}
