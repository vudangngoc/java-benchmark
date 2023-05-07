package org.sample.kotlin

import kotlinx.coroutines.*

public class WithContext {
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
    fun test() : Int {
        return runBlocking {
            var result = job1()
            result += job2()
            result += job3()
            result
        }
    }
    suspend fun job1() : Int{
         return withContext(dispatcher){
             Jobs.job1()
         }
    }
    suspend fun job2() : Int{
        return withContext(dispatcher){
            Jobs.job2()
        }
    }
    suspend fun job3() : Int{
        return withContext(dispatcher){
            Jobs.job3()
        }
    }
}