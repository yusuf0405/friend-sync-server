# Ktor Social Network Server

## Overview

* This project is a backend server for [a social network application](https://github.com/yusuf0405/kmp-friend-sync), built using Ktor. It provides essential
  functionalities such as authentication, user management, posts, likes, subscriptions, comments, and categories.

## Features

* Authentication: User login and registration with JWT authentication.
* Users: Retrieve and manage user profiles.
* Posts: Create, read, update, and delete posts.
* Likes: Like/unlike posts.
* Subscriptions: Follow/unfollow users.
* Comments: Add and retrieve comments on posts.
* Categories: Categorize posts into different topics.

## Dependencies

### Database:
* PostgreSQL (org.postgresql:postgresql)
* H2 Database (com.h2database:h2)
* Exposed ORM (org.jetbrains.exposed:exposed-core, org.jetbrains.exposed:exposed-jdbc)
* HikariCP for connection pooling (com.zaxxer:HikariCP)

### Ktor Server:

* Netty server (io.ktor:ktor-server-netty-jvm)
* Core Ktor functionalities (io.ktor:ktor-server-core-jvm)
* Serialization (io.ktor:ktor-serialization-kotlinx-json-jvm)
* Content Negotiation (io.ktor:ktor-server-content-negotiation-jvm)

### Authentication:

* Ktor Auth (io.ktor:ktor-server-auth-jvm)
* JWT Authentication (io.ktor:ktor-server-auth-jwt-jvm)

### Logging:
* Logback (ch.qos.logback:logback-classic)

### Dependency Injection:
* Koin (io.insert-koin:koin-ktor)