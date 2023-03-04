package org.sample;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class TestStreamAggregationPerformance {

	@Benchmark
	@BenchmarkMode(Mode.AverageTime)
	@OutputTimeUnit(TimeUnit.MILLISECONDS)
	public void testReduce(BenchMarkState state, Blackhole blackhole) {
		Map<String, OrderSummary> summaryMap = state.testData.stream()
				.collect(Collectors.groupingBy(
						order -> order.getCustomerId() + "-" + order.getProductId(),
						Collectors.reducing(
								new OrderSummary(0, 0, 0),
								order -> new OrderSummary(order.getQuantity(),              // value mapper function
										order.getPrice(),
										order.getPrice() / order.getQuantity()),
								(summary1, summary2) -> new OrderSummary(                      // merge function
										summary1.getQuantity() + summary2.getQuantity(),
										summary1.getTotalPrice() + summary2.getTotalPrice(),
										(summary1.getAveragePrice() + summary2.getAveragePrice()) / 2))));
		blackhole.consume(summaryMap);

	}
    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void testMap(BenchMarkState state, Blackhole blackhole) {
        Map<String, OrderSummary> map = state.testData.stream()
                .collect(Collectors.toMap(
                        order -> order.getCustomerId() + "-" + order.getProductId(),  // key mapper function
                        order -> new OrderSummary(order.getQuantity(),              // value mapper function
                                order.getPrice(),
                                order.getPrice() / order.getQuantity()),
                        (summary1, summary2) -> new OrderSummary(                      // merge function
                                summary1.getQuantity() + summary2.getQuantity(),
                                summary1.getTotalPrice() + summary2.getTotalPrice(),
                                (summary1.getAveragePrice() + summary2.getAveragePrice()) / 2
                        )
                ));
		blackhole.consume(map);
    }

	@Benchmark
	@BenchmarkMode(Mode.AverageTime)
	@OutputTimeUnit(TimeUnit.MILLISECONDS)
	public void testForLoop(BenchMarkState state, Blackhole blackhole) {
		Map<String, OrderSummary> map = new HashMap<>(10000);
		for(int i = 0; i < state.testData.size();i++){
			Order order = state.testData.get(i);
			String key = order.getCustomerId() + "-" + order.getProductId();
			OrderSummary value = map.get(key);
			if(value == null){
				value = new OrderSummary(0, 0, 0);
				map.put(key,value);
			}
			value.quantity += order.quantity;
			value.totalPrice += order.price;
			value.averagePrice = (value.averagePrice + order.getPrice() / order.getQuantity()) /2;
		}
		blackhole.consume(map);
	}

//    Benchmark                                     Mode  Cnt    Score     Error  Units
//    TestStreamAggregationPerformance.testForLoop  avgt  200  215.010 ▒   2.412  ms/op
//    TestStreamAggregationPerformance.testMap      avgt  200  638.792 ▒ 114.651  ms/op
//    TestStreamAggregationPerformance.testReduce   avgt  200  879.825 ▒ 139.587  ms/op

    @State(Scope.Benchmark)
    public static class BenchMarkState {
        @Setup(Level.Trial)
        public void doSetup() {
            Random random = new Random();
            for (int i = 0; i < 5000000; i++)
                testData.add(new Order(random.nextInt(100), random.nextInt(100), random.nextInt(), random.nextDouble()));
        }

        @TearDown(Level.Trial)
        public void doTearDown() {
            testData = new ArrayList<>(5000000);
        }

        public List<Order> testData = new ArrayList<>();

    }


    public static class Order {
        private int customerId;
        private int productId;
        private int quantity;
        private double price;

        public Order(int customerId, int productId, int quantity, double price) {
            this.customerId = customerId;
            this.productId = productId;
            this.quantity = quantity;
            this.price = price;
        }

        public int getCustomerId() {
            return customerId;
        }

        public int getProductId() {
            return productId;
        }

        public int getQuantity() {
            return quantity;
        }

        public double getPrice() {
            return price;
        }
    }

    public class OrderSummary {
        private int quantity;
        private double totalPrice;
        private double averagePrice;

        public OrderSummary(int quantity, double totalPrice, double averagePrice) {
            this.quantity = quantity;
            this.totalPrice = totalPrice;
            this.averagePrice = averagePrice;
        }

        public int getQuantity() {
            return quantity;
        }

        public double getTotalPrice() {
            return totalPrice;
        }

        public double getAveragePrice() {
            return averagePrice;
        }

        @Override
        public String toString() {
            return "OrderSummary{" +
                    "quantity=" + quantity +
                    ", totalPrice=" + totalPrice +
                    ", averagePrice=" + averagePrice +
                    '}';
        }
    }


}

