package com.example.blyat

import net.corda.client.rpc.CordaRPCClient
import net.corda.core.utilities.NetworkHostAndPort.Companion.parse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import com.expiledger.flows.customer.RegisterCustomerAccount
import java.util.*
import java.util.concurrent.TimeUnit

@RestController
@RequestMapping("/test")
class ControllerTest {

//    private var log = KotlinLogging.logger{}

    @GetMapping("/hello")
    fun helloThereMethod(): Unit {



        val nodeAddress = parse("localhost:8003")
        val username = "user1"
        val password = "test"

        val client = CordaRPCClient(nodeAddress)
        val connection = client.start(username, password)
        val cordaRPCOperations = connection.proxy

        var snapshot = cordaRPCOperations.stateMachinesSnapshot()
        for (stateMachineInfo in snapshot) {
            cordaRPCOperations.killFlow(stateMachineInfo.id)
            print("flow with id: ")
            print(stateMachineInfo.id)
            print(" killed")
        }

//        log.info {  cordaRPCOperations.currentNodeTime().toString() }

        connection.notifyServerAndClose()
    }
}