# Library for common utility in Bukkit
Monorepo containing utilities and general quality of life improvements for Bukkit developers.

## Overview
1. [**bommons-core**](https://github.com/aparx/bommons/tree/master/bommons-core)<br/>
The core contains crucial utilities, acquired by all other modules
2. [**bommons-items**](https://github.com/aparx/bommons/tree/master/bommons-items)<br/>
The items module contains utilities and wrappers to allocate, handle, manage and serialize Bukkit's `ItemStack`s efficiently and easily. Gone are the days of manual ItemStack allocation.
3. [**bommons-ticks**](https://github.com/aparx/bommons/tree/master/bommons-ticks)<br/>
The ticks module houses everything related to ticks (as in TPS). Utilities and helpful wrappers to calculate and deal with ticks.
4. [**bukkit-gui**](https://github.com/aparx/bukkit-gui)<br/>
This is an external module, but worth mentioning. It once used to live among the bommons modules, but was extracted due to it's complexity and independence. It is a very advanced Inventory library, for creating custom, animated GUIs using inventories.

## Installation
```xml
<repositories>
  <repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
  </repository>
</repositories>

<dependencies>
  <dependency>
    <groupId>com.github.aparx.bommons</groupId>
    <artifactId>bommons-core</artifactId>
    <version>1.0.0-SNAPSHOT</version>
  </dependency>
</dependencies>
```

