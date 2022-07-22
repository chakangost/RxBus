# What is RxBus?

RxBus makes it easy to subscriber and publish android observer pattern.
It is reactivex style lib.



## Setup


### Gradle

Edit `root/app/build.gradle` like below.

#### Kotlin Base
```gradle
dependencies {
    implementation 'com.github.chakangost:RxBus:1.0'
}
```

Edit `root/build.gradle` like below.
```gradle
allprojects {
    repositories {
        .....
        maven { url 'https://jitpack.io' }
    }
}
```

If you think this library is useful, please press the star button at the top.


## How to use

### Normal
#### - Subscriber
We will use `subscribe` for receiver data or event handler.

```Kotlin
RxBus.subscribe(1, this) { it: Any
    Log.d(tag : "freddie", lifecycle : it.toString())
}
```


#### - Publish
When an event occurs
```Kotlin
RxBus.publish(subject: 1, message: "hello freddie")

```

#### - Unregister
When lifecycle destory
```Kotlin
RxBus.unregister(this)

```

You must unsubscribe from the subscriber this context.


## License 
 ```code
Copyright 2020 Freddie

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
