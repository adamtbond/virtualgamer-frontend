package com.edentech.firstserverapi.controller

import spock.lang.Specification

class HelloControllerSpec extends Specification {

    HelloController helloController

    def setup() {
        helloController = new HelloController()
    }

    def "should return greeting message"() {
        when:
            String response = helloController.hello()

        then:
            response == "Hello from my VPS!"
    }

    def "should not return null"() {
        when:
            String response = helloController.hello()

        then:
            response != null
    }

    def "should return expected string"() {
        when:
            String response = helloController.hello()

        then:
            response.contains("Hello")
            response.contains("VPS")
    }
}

