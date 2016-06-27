Groovy Scripting Language PoC: Calling Groovy scripts/functions from Java context
=================================================================================
### Goals

  * test Groovy compatibility with java
  * test groovy scripts/methods execution/invoke from Java side 
  * test Groovy runtime scripts reloading, including dependencies
  * investigate different approaches, eg. as implementation of Java template vs. closure callbacks 

### Result: engine script comparison

| groovy executor class |script caching|script reloading|
|---|---|---|
| GroovyShell | yes for script run, not for evaluation| No |
| GroovyClassLoader | Yes | Yes for GroovyClassLoader.loadClassByName only  |
| GroovyScriptEngine | Yes | Yes, including dependencies, has performance impact |

### Result: implementation options

| preference | implementation option | package | Result |
|---|---|---|---|
| 1 | script implementing Java template in groovy | package com.kamenistak.groovy.integrationPoC.pricingAsTemplate | Works, syntax check available |  
| 2 | script using closure delegation | package com.kamenistak.groovy.integrationPoC.pricingAsDelegate | Works, with explicit need of .gdsl as syntax definition. See http://mrhaki.blogspot.com/2013/11/groovy-goodness-set-delegating-class.html |
| 3 | script using simple closure callbacks | package com.kamenistak.groovy.integrationPoC.pricingAsClosure | Works, with explicit need of .gdsl as syntax definition |
