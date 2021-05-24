package com.example.sendmail1.emailsrv

class EmailContext {
    var from: String = ""
    var to: String = ""
    var subject: String = ""
    var email: String = ""
//    private val attachment: String? = null
    var fromDisplayName: String = ""
    var templateLocation: String = ""
    var context: Map<String, Any>? = null
}