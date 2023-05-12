package org.sample.kotlin

import kotlinx.coroutines.*
import kotlin.system.*
public class WithContext {
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
    fun test() : Int {
        var result = 0
        runBlocking<Unit> {
            val result1 = async { job1() }
            val result2 = async { job2() }
            val result3 = async { job3() }
            result = result1.await() + result2.await() + result3.await()
        }
        return result
    }
    suspend fun job1() : Int{
         return withContext(dispatcher){
             Jobs.job1()
         }
    }
    suspend fun job2() : Int{
            return Jobs.job2()
    }
    suspend fun job3() : Int{
        return withContext(dispatcher){
            Jobs.job3()
        }
    }
}