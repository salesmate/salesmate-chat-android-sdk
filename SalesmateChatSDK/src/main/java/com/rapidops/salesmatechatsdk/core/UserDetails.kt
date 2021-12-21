package com.rapidops.salesmatechatsdk.core

class UserDetails {

    companion object {
        fun create(): UserDetails {
            return UserDetails()
        }
    }

    private var email = ""
    private var firstName = ""
    private var lastName = ""

    fun withFirstName(fistName: String): UserDetails {
        this.firstName = fistName
        return this
    }

    fun withLastName(lastName: String): UserDetails {
        this.lastName = lastName
        return this
    }

    fun withEmail(email: String): UserDetails {
        this.email = email
        return this
    }

    fun getFirstName(): String {
        return firstName
    }

    fun getLastName(): String {
        return lastName
    }

    fun getEmail(): String {
        return email
    }
}