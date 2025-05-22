package com.epam.gymapp.utilsTest;

import static org.junit.jupiter.api.Assertions.*;

import com.epam.gymapp.utils.ApiListWrapper;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

class ApiListWrapperTest {

  @Test
  void testApiListWrapper_WithValidList() {
    // Given
    List<String> items = Arrays.asList("item1", "item2", "item3");

    // When
    ApiListWrapper<String> wrapper = new ApiListWrapper<>(items);

    // Then
    assertNotNull(wrapper);
    assertEquals(items, wrapper.items());
    assertEquals(3, wrapper.items().size());
    assertTrue(wrapper.items().contains("item1"));
    assertTrue(wrapper.items().contains("item2"));
    assertTrue(wrapper.items().contains("item3"));
  }

  @Test
  void testApiListWrapper_WithEmptyList() {
    // Given
    List<String> items = Arrays.asList();

    // When
    ApiListWrapper<String> wrapper = new ApiListWrapper<>(items);

    // Then
    assertNotNull(wrapper);
    assertEquals(items, wrapper.items());
    assertEquals(0, wrapper.items().size());
    assertTrue(wrapper.items().isEmpty());
  }

  @Test
  void testApiListWrapper_WithNullList() {
    // Given
    List<String> items = null;

    // When
    ApiListWrapper<String> wrapper = new ApiListWrapper<>(items);

    // Then
    assertNotNull(wrapper);
    assertNull(wrapper.items());
  }

  @Test
  void testApiListWrapper_WithDifferentTypes() {
    // Given
    List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);

    // When
    ApiListWrapper<Integer> wrapper = new ApiListWrapper<>(numbers);

    // Then
    assertNotNull(wrapper);
    assertEquals(numbers, wrapper.items());
    assertEquals(5, wrapper.items().size());
    assertTrue(wrapper.items().contains(1));
    assertTrue(wrapper.items().contains(5));
  }

  @Test
  void testApiListWrapper_Equality() {
    // Given
    List<String> items1 = Arrays.asList("a", "b", "c");
    List<String> items2 = Arrays.asList("a", "b", "c");
    List<String> items3 = Arrays.asList("x", "y", "z");

    // When
    ApiListWrapper<String> wrapper1 = new ApiListWrapper<>(items1);
    ApiListWrapper<String> wrapper2 = new ApiListWrapper<>(items2);
    ApiListWrapper<String> wrapper3 = new ApiListWrapper<>(items3);

    // Then
    assertEquals(wrapper1, wrapper2);
    assertNotEquals(wrapper1, wrapper3);
    assertEquals(wrapper1.hashCode(), wrapper2.hashCode());
  }

  @Test
  void testApiListWrapper_ToString() {
    // Given
    List<String> items = Arrays.asList("test");

    // When
    ApiListWrapper<String> wrapper = new ApiListWrapper<>(items);

    // Then
    String toString = wrapper.toString();
    assertNotNull(toString);
    assertTrue(toString.contains("ApiListWrapper"));
    assertTrue(toString.contains("test"));
  }
}
