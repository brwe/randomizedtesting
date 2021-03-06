package com.carrotsearch.randomizedtesting;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.fest.assertions.api.Assertions;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

import com.carrotsearch.randomizedtesting.annotations.ParametersFactory;

public class TestParameterizedShufflesOrder extends WithNestedTestClass {
  static StringBuilder buf;

  public static class Base extends RandomizedTest {
    private final int value;

    public Base(int value) {
      this.value = value;
    }

    @Test
    public void testFoo() {
      buf.append(value).append(",");
    }
  }

  public static class NoShuffle extends Base {
    public NoShuffle(int value) {
      super(value);
    }

    @ParametersFactory(shuffle = false)
    public static Iterable<Object[]> parameters() {
      List<Object[]> params = new ArrayList<Object[]>();
      for (int i = 0; i < 10; i++) {
        params.add($(i));
      }
      return params;
    }
  }

  public static class WithShuffle extends Base {
    public WithShuffle(int value) {
      super(value);
    }

    @ParametersFactory()
    public static Iterable<Object[]> parameters() {
      List<Object[]> params = new ArrayList<Object[]>();
      for (int i = 0; i < 10; i++) {
        params.add($(i));
      }
      return params;
    }
  }
  
  @Test
  public void testWithoutShuffle() {
    Set<String> runs = new HashSet<String>();
    for (int i = 0; i < 10; i++) {
      buf = new StringBuilder();
      Result result = JUnitCore.runClasses(NoShuffle.class);
      Assertions.assertThat(result.wasSuccessful()).isTrue();
      runs.add(buf.toString());
    }
    Assertions.assertThat(runs).hasSize(1);
  }
  
  @Test
  public void testWithShuffle() {
    Set<String> runs = new HashSet<String>();
    int iters = 10;
    for (int i = 0; i < iters; i++) {
      buf = new StringBuilder();
      Result result = JUnitCore.runClasses(WithShuffle.class);
      Assertions.assertThat(result.wasSuccessful()).isTrue();
      runs.add(buf.toString());
    }
    Assertions.assertThat(runs).hasSize(iters);
  }  
}
