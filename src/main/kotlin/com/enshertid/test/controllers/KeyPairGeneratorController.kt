package com.enshertid.test.controllers

import com.enshertid.test.controllers.dto.KeyPairDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.KeyPair
import java.security.KeyPairGenerator

@RestController()
@RequestMapping("/keys")
class KeyPairGeneratorController {

    @GetMapping("/pair/generate")
    fun generateKeyPair(): ResponseEntity<KeyPairDto> {

        val generator = KeyPairGenerator.getInstance("RSA")
        generator.initialize(2048)
        val keyPair = generator.generateKeyPair()
        val privateKeyString = keyPair.private.toString()
        val publicKeyString = keyPair.public.toString()

        return ResponseEntity.ok(KeyPairDto(privateKeyString, publicKeyString))
    }

}