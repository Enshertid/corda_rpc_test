package com.enshertid.test.controllers

import net.corda.client.rpc.CordaRPCClient
import net.corda.core.utilities.NetworkHostAndPort.Companion.parse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import net.corda.core.identity.Party
import net.corda.core.messaging.CordaRPCOps
import net.corda.core.messaging.startFlow
import net.corda.samples.dollartohousetoken.flows.FiatCurrencyIssueFlow
import org.springframework.http.ResponseEntity
import java.util.*
import kotlin.collections.ArrayList

@RestController
@RequestMapping("/corda/rpc")
class CordaRPCClientController {

    @GetMapping("/tokens/performance/test")
    fun helloThereMethod(): ResponseEntity<List<List<String>>> {

        val nodeAddress = parse("localhost:10006")
        val username = "user1"
        val password = "test"

        val client = CordaRPCClient(nodeAddress)
        val connection = client.start(username, password)
        val cordaRPCOperations = connection.proxy

        val recipients = cordaRPCOperations.partiesFromName("PartyC", true)

        var recipient: Party? = null
        recipients.forEach { party -> recipient = party }

        val finalResult: ArrayList<ArrayList<String>> = ArrayList<ArrayList<String>>()

        var iter: Int = 0
        while (iter < 5) {
            val result = ArrayList<String>()

            result.add(String.format("%10s %30s %10s", "", "--- WITH FUTURE IS DONE ---", ""))
            result.add(test(cordaRPCOperations, recipient, 5, true))
            result.add(test(cordaRPCOperations, recipient, 10, true))
            result.add(test(cordaRPCOperations, recipient, 20, true))
            result.add(test(cordaRPCOperations, recipient, 40, true))
            result.add(test(cordaRPCOperations, recipient, 80, true))
            result.add(test(cordaRPCOperations, recipient, 160, true))
            result.add(test(cordaRPCOperations, recipient, 320, true))

            result.add("")

            result.add(String.format("%10s %30s %10s", "", "--- WITHOUT FUTURE IS DONE ---", ""))
            result.add(test(cordaRPCOperations, recipient, 5, false))
            result.add(test(cordaRPCOperations, recipient, 10, false))
            result.add(test(cordaRPCOperations, recipient, 20, false))
            result.add(test(cordaRPCOperations, recipient, 40, false))
            result.add(test(cordaRPCOperations, recipient, 80, false))
            result.add(test(cordaRPCOperations, recipient, 160, false))
            result.add(test(cordaRPCOperations, recipient, 320, false))

            finalResult.add(result)
            iter++
            Thread.sleep(10000)
        }
        iter = 0
        while (iter < 5) {
            val result = ArrayList<String>()
            finalResult.add(result)
            iter++
        }


        connection.notifyServerAndClose()
        return ResponseEntity.ok(finalResult)
    }

    private fun test(cordaRPCOperations: CordaRPCOps, recipient: Party?, operationNumber: Int, withCheck: Boolean): String {
        var iter: Int = 0

        val startTime = System.currentTimeMillis()

        while (iter < operationNumber) {
            var aa = cordaRPCOperations.startFlow(::FiatCurrencyIssueFlow, "USD", 1000000L, recipient)
            if (withCheck)
            while (!aa.returnValue.isDone) {}
            iter++
        }

        val endTime = System.currentTimeMillis()
        if (!withCheck) {
            return String.format(
                "Test condition: %-3d operations | Test result time is: %-5d ms",
                operationNumber,
                (endTime - startTime)
            )
        }

        return String.format(
            "Test condition: %-3d operations | Test result time is: %-5d ms",
            operationNumber,
            (endTime - startTime)
        )
    }
}