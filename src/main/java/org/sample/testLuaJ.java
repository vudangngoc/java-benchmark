package org.sample;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.JsePlatform;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

public class testLuaJ {

	@Benchmark
	@BenchmarkMode(Mode.AverageTime)
	@OutputTimeUnit(TimeUnit.MILLISECONDS)
	public void testLuaJAPI(BenchMarkState state) {
		for (int i = 0; i < state.size; i++) {
			LuaValue chunk = state.globals.load(String.format(state.apiFormular,state.testData.get(i)));
			chunk.call();
		}
	}

	@Benchmark
	@BenchmarkMode(Mode.AverageTime)
	@OutputTimeUnit(TimeUnit.MILLISECONDS)
	public  void testScriptManager(BenchMarkState state) {
		for (int i = 0; i < state.size; i++) {
			try {
				state.e.put("x", state.testData.get(i));
				state.e.eval(state.seFormular);
			} catch (ScriptException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Benchmark
	@BenchmarkMode(Mode.AverageTime)
	@OutputTimeUnit(TimeUnit.MILLISECONDS)
	public  void testJava(BenchMarkState state) {
		for (int i = 0; i < state.size; i++) {
			Math.sqrt(state.testData.get(i));
		}
	}
//	Benchmark                   Mode  Cnt   Score    Error  Units
//	testLuaJ.testJava           avgt  200   0.003 ▒  0.001  ms/op
//	testLuaJ.testLuaJAPI        avgt  200  20.236 ▒  0.148  ms/op
//	testLuaJ.testScriptManager  avgt  200  16.920 ▒  0.123  ms/op
	@State(Scope.Benchmark)
	public static class BenchMarkState {
		//JSE-223 pluggable scripting language interface
		ScriptEngineManager sem = new ScriptEngineManager();
		ScriptEngine e = sem.getEngineByName("luaj");
		String seFormular = "y = math.sqrt(x)";

		String apiFormular = "return math.sqrt(%d)";
		Globals globals = JsePlatform.standardGlobals();
		public int size = 5000;
		public List<Integer> testData = new ArrayList<>();
		@Setup(Level.Trial)
		public void doSetup() {
			for(int i = 0; i < size; i++)
				testData.add(Integer.valueOf(i));
		}
	}
}
