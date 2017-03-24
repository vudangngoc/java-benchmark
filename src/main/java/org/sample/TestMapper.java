package org.sample;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.modelmapper.ModelMapper;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Threads;

public class TestMapper {
  
  @Benchmark
  @BenchmarkMode(Mode.AverageTime)
  @OutputTimeUnit(TimeUnit.NANOSECONDS)
  public void dozer(BenchMarkState state){
    Mapper mapper = new DozerBeanMapper();
    Order order = new Order();
    OrderDTO orderDTO;
    for(int i = 0; i < 10000; i++)
      orderDTO = mapper.map(order, OrderDTO.class);
  }
  @Benchmark
  @BenchmarkMode(Mode.AverageTime)
  @OutputTimeUnit(TimeUnit.NANOSECONDS)
  public void modelmapper(BenchMarkState state){
    ModelMapper modelMapper = new ModelMapper();
    Order order = new Order();
    OrderDTO orderDTO;
    for(int i = 0; i < 10000; i++)
      orderDTO = modelMapper.map(order, OrderDTO.class);
  }
  @State(Scope.Benchmark)
  public static class BenchMarkState {
    @Setup(Level.Trial)
    public void doSetup() {
      atomic.set(0);
      integer = 0;
    }
//    @TearDown(Level.Trial)
//    public void doTearDown() {
//      atomic.set(0);
//      integer = 0;
//    }
    public Object LOCK = new Object();
    public AtomicInteger atomic = new AtomicInteger(0);
    public Integer integer = new Integer(0);
  }
  
  public static class Order {
    Customer customer;
    Address billingAddress;
  }

  public static class Customer {
    Name name;
  }

  public static class Name {
    String firstName = "firstName";
    String lastName = "lastName";
  }

  public static class Address {
    String street = "street";
    String city = "city";
  }
  
  public static class OrderDTO {
    String customerFirstName;
    String customerLastName;
    String billingStreet;
    String billingCity;
  }
}
